package be.appify.stereotype.core.beans;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import be.appify.stereotype.core.beans.fields.CompositeFieldAccessor;
import be.appify.stereotype.core.beans.fields.ConstructorAccessor;
import be.appify.stereotype.core.beans.fields.Field;
import be.appify.stereotype.core.beans.fields.FieldAccess;
import be.appify.stereotype.core.beans.fields.FieldAccessor;
import be.appify.stereotype.core.beans.fields.FieldModel;
import be.appify.stereotype.core.beans.fields.FieldType;
import be.appify.stereotype.core.beans.fields.GetterAccessor;
import be.appify.stereotype.core.beans.fields.IllegalAnnotationException;
import be.appify.stereotype.core.beans.fields.Order;
import be.appify.stereotype.core.beans.fields.SetterAccessor;
import be.appify.stereotype.core.beans.validation.AlwaysValidValidator;
import be.appify.stereotype.core.beans.validation.CompositeValidator;
import be.appify.stereotype.core.beans.validation.Validator;
import be.appify.stereotype.core.beans.validation.ValidatorFactory;
import be.appify.stereotype.core.i18n.Message;
import be.appify.stereotype.core.operation.GenericOperation;
import be.appify.stereotype.core.operation.Operation;
import be.appify.stereotype.core.operation.OperationFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

class BeanAnalyzer {
	private final ValidatorFactory validatorFactory;
	private final OperationFactory operationFactory;

	public BeanAnalyzer(ValidatorFactory validatorFactory, OperationFactory operationFactory) {
		this.validatorFactory = validatorFactory;
		this.operationFactory = operationFactory;
	}

	public <T> BeanModel<T> analyze(Class<T> beanClass) {
		return new BeanModel.Builder<T>(beanClass)
				.fields(getFields(beanClass))
				.operations(getOperations(beanClass))
				.build();
	}

	private <T> Collection<GenericOperation<?>> getOperations(Class<T> beanClass) {
		Collection<GenericOperation<?>> operations = Sets.newHashSet();
		for (Annotation annotation : beanClass.getAnnotations()) {
			Class<? extends Annotation> annotationType = annotation.annotationType();
			if (annotationType.isAnnotationPresent(Operation.class)) {
				GenericOperation<?> operation = operationFactory.getOperation(annotationType.getAnnotation(
						Operation.class).value());
				if (operation != null) {
					operations.add(operation);
				}
			}
		}
		return operations;
	}

	private <T> Collection<FieldModel<T, ?>> getFields(Class<T> beanClass) {
		Map<String, FieldModel<T, ?>> fields = Maps.newHashMap();
		Constructor<?> preferredConstructor = null;
		for (Constructor<?> constructor : beanClass.getConstructors()) {
			int index = 0;
			if (preferredConstructor == null && constructor.getParameterTypes().length == 0) {
				preferredConstructor = constructor;
			}
			boolean annotatedConstructor = false;
			for (Annotation[] annotations : constructor.getParameterAnnotations()) {
				Annotation fieldAnnotation = getFieldAnnotation(annotations, constructor.toString() + "[" + index + "]");
				if ((fieldAnnotation != null && index > 0 && !annotatedConstructor)
						|| (fieldAnnotation == null && annotatedConstructor)) {
					throw new IllegalAnnotationException("Not all constructor parameters are field-annotated on "
							+ constructor + ".");
				}
				if (fieldAnnotation != null) {
					Validator<?> validator = createValidator(constructor.getParameterTypes()[index], fieldAnnotation,
							annotations);
					FieldModel<T, ?> fieldModel = createFieldModel(constructor, index, fieldAnnotation, validator);
					fields.put(fieldModel.getName(), fieldModel);
					annotatedConstructor = true;
				}
				index++;
			}
			if (annotatedConstructor) {
				preferredConstructor = constructor;
			}
		}
		if (preferredConstructor == null) {
			throw new IllegalAnnotationException(
					"Missing no-arg constructor or constructor with field-annotated parameters on " + beanClass + ".");
		}
		Collection<Method> toRevaluate = Sets.newHashSet();
		for (Method method : beanClass.getMethods()) {
			String propertyName = getPropertyName(method);
			FieldModel<T, ?> existingField = fields.get(propertyName);
			Annotation fieldAnnotation = getFieldAnnotation(method.getAnnotations(), method.toString());
			if (fieldAnnotation != null) {
				if (existingField != null) {
					throw new IllegalAnnotationException("Conflicting annotations found for property <" + propertyName
							+ "> on " + beanClass + ". Field annotations found on constructor and on " + method + ".");
				}
				FieldModel<T, ?> fieldModel = createFieldModel(method, fieldAnnotation, beanClass);
				fields.put(fieldModel.getName(), fieldModel);
			} else if (propertyName != null) {
				if (existingField != null) {
					fields.put(existingField.getName(), updateExistingField(existingField, method, beanClass));
				} else {
					toRevaluate.add(method);
				}
			}
		}
		for (Method method : toRevaluate) {
			String propertyName = getPropertyName(method);
			FieldModel<T, ?> existingField = fields.get(propertyName);
			if (existingField != null) {
				fields.put(existingField.getName(), updateExistingField(existingField, method, beanClass));
			}
		}
		return fields.values();
	}

	private <T, F> FieldModel<T, F> updateExistingField(FieldModel<T, F> existingField, Method method,
			Class<T> beanClass) {
		boolean readable = false;
		boolean updatable = false;
		if (isSetter(method)) {
			updatable = true;
		} else if (isGetter(method)) {
			readable = true;
		}
		FieldAccessor<T, F> fieldAccessor = getFieldAccessor(existingField.getName(), method, beanClass,
				AlwaysValidValidator.INSTANCE);
		return existingField.extend(FieldAccess.valueOf(readable, updatable, updatable), fieldAccessor);
	}

	private String getPropertyName(Method method) {
		String propertyName = null;
		if (isGetter(method) || isSetter(method)) {
			if (method.getName().startsWith("set") || method.getName().startsWith("get")) {
				propertyName = StringUtils.uncapitalize(method.getName().substring(3));
			} else if (method.getName().startsWith("is")) {
				propertyName = StringUtils.uncapitalize(method.getName().substring(2));
			}
		}
		return propertyName;
	}

	private Annotation getFieldAnnotation(Annotation[] annotations, String subject) {
		Annotation fieldAnnotation = null;
		for (Annotation annotation : annotations) {
			if (annotation.annotationType().isAnnotationPresent(Field.class)) {
				if (fieldAnnotation != null) {
					throw new IllegalAnnotationException("Multiple field annotations found on " + subject + ": <@"
							+ fieldAnnotation.annotationType().getSimpleName() + ", @"
							+ annotation.annotationType().getSimpleName() + ">.");
				}
				fieldAnnotation = annotation;
			}
		}
		return fieldAnnotation;
	}

	// TODO: work on FieldModel.Builder instead of passing every parameter around
	@SuppressWarnings("unchecked")
	private <T, F> FieldModel<T, F> createFieldModel(Method method, Annotation fieldAnnotation, Class<T> beanClass) {
		String propertyName = getPropertyName(method);
		if (propertyName == null) {
			throw new IllegalAnnotationException("Method " + method
					+ " does not qualify as a field since it does not have a correct getter or setter signature.");
		}
		boolean readable = false;
		boolean updatable = false;
		Class<F> fieldClass = null;
		if (isSetter(method)) {
			updatable = true;
			fieldClass = (Class<F>) method.getParameterTypes()[0];
		} else if (isGetter(method)) {
			readable = true;
			fieldClass = (Class<F>) method.getReturnType();
		}
		FieldAccess fieldAccess = FieldAccess.valueOf(readable, updatable, updatable);
		Annotation[] annotations = method.getAnnotations();
		Validator<? super F> validator = createValidator(fieldClass, fieldAnnotation, annotations);
		FieldAccessor<T, F> fieldAccessor = getFieldAccessor(propertyName, method, beanClass, validator);
		int order = Integer.MAX_VALUE;
		Order orderAnnotation = method.getAnnotation(Order.class);
		if (orderAnnotation != null) {
			order = orderAnnotation.value();
		}
		return createFieldModel(fieldAnnotation, propertyName, fieldClass, fieldAccess, fieldAccessor,
				annotations, order);
	}

	private <T, F> FieldAccessor<T, F> getFieldAccessor(String propertyName, Method method, Class<T> declaringClass,
			Validator<? super F> validator) {
		Collection<FieldAccessor<T, F>> accessors = Lists.newArrayList();
		Message<String> message = Message.create(declaringClass, propertyName);
		if (isSetter(method)) {
			accessors.add(SetterAccessor.<T, F> create(method, validator, message));
		} else if (isGetter(method)) {
			accessors.add(GetterAccessor.<T, F> create(method, validator, message));
		}
		return CompositeFieldAccessor.create(accessors);
	}

	private <T, F> FieldModel<T, F> createFieldModel(Annotation fieldAnnotation, String propertyName,
			Class<F> fieldClass, FieldAccess fieldAccess, FieldAccessor<T, F> fieldAccessor, Annotation[] annotations,
			int order) {
		FieldType fieldType = fieldAnnotation.annotationType().getAnnotation(Field.class).value();
		if (!fieldType.supports(fieldClass)) {
			throw new IllegalAnnotationException("Annotation @" + fieldAnnotation.annotationType().getSimpleName()
					+ " does not support " + fieldClass + ". Supported types are: <" + fieldType.getSupportedTypes()
					+ ">.");
		}
		return new FieldModel.Builder<T, F>()
				.name(propertyName)
				.fieldAccess(fieldAccess)
				.fieldClass(fieldClass)
				.fieldType(fieldType)
				.fieldAccessor(fieldAccessor)
				.order(order)
				.build();
	}

	private <F> Validator<? super F> createValidator(Class<F> fieldClass, Annotation fieldAnnotation,
			Annotation[] annotations) {
		Collection<Validator<? super F>> validators = Sets.newHashSet();
		for (Annotation annotation : annotations) {
			addValidator(fieldClass, validators, annotation, annotation.annotationType().getSimpleName());
		}
		for (Annotation annotation : fieldAnnotation.annotationType().getAnnotations()) {
			addValidator(fieldClass, validators, annotation, fieldAnnotation.annotationType().getSimpleName());
		}
		return CompositeValidator.create(validators);
	}

	private <F> void addValidator(Class<F> fieldClass, Collection<Validator<? super F>> validators,
			Annotation annotation,
			String source) {
		Validator<? super F> validator = validatorFactory.createValidator(annotation);
		if (validator != null) {
			if (!validator.supportsType(fieldClass)) {
				throw new IllegalAnnotationException("Annotation @" + source
						+ " does not support " + fieldClass + ". Supported types are: <"
						+ validator.getSupportedTypes() + ">.");
			}
			validators.add(validator);
		}
	}

	private boolean isGetter(Method method) {
		return (method.getName().startsWith("get") || method.getName().startsWith("is"))
				&& method.getParameterTypes().length == 0 && method.getReturnType() != Void.TYPE;
	}

	private boolean isSetter(Method method) {
		return method.getName().startsWith("set") && method.getParameterTypes().length == 1;
	}

	@SuppressWarnings("unchecked")
	private <T, F> FieldModel<T, F> createFieldModel(Constructor<?> constructor, int index, Annotation fieldAnnotation,
			Validator<F> validator) {
		String propertyName = getParameter(fieldAnnotation, "value", String.class);
		if (StringUtils.isBlank(propertyName)) {
			throw new IllegalAnnotationException("Missing field name value of @"
					+ fieldAnnotation.annotationType().getSimpleName() + " on " + constructor.toString() + "[" + index
					+ "].");
		}
		Class<F> fieldClass = (Class<F>) constructor.getParameterTypes()[index];
		FieldAccess fieldAccess = FieldAccess.WRITE_ONCE;
		Annotation[] annotations = constructor.getParameterAnnotations()[index];
		Message<String> message = Message.create(constructor.getDeclaringClass(), propertyName);
		FieldAccessor<T, F> fieldAccessor = ConstructorAccessor.create(index, validator, message);
		int order = Integer.MAX_VALUE;
		Order orderAnnotation = getAnnotation(annotations, Order.class);
		if (orderAnnotation != null) {
			order = orderAnnotation.value();
		}
		return createFieldModel(fieldAnnotation, propertyName, fieldClass, fieldAccess, fieldAccessor, annotations,
				order);
	}

	private <A extends Annotation> A getAnnotation(Annotation[] annotations, Class<A> annotationType) {
		A annotation = null;
		for (Annotation a : annotations) {
			if (annotationType.isInstance(a)) {
				annotation = annotationType.cast(a);
				break;
			}
		}
		return annotation;
	}

	private <T> T getParameter(Annotation annotation, String parameterName, Class<T> expectedType) {
		try {
			Method method = annotation.annotationType().getMethod(parameterName);
			return expectedType.cast(method.invoke(annotation));
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}
}
