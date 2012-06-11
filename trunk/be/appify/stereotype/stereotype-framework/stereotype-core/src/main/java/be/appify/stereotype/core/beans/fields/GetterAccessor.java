package be.appify.stereotype.core.beans.fields;

import java.lang.reflect.Method;

import be.appify.stereotype.core.beans.BeanConstruction;
import be.appify.stereotype.core.beans.validation.Validator;
import be.appify.stereotype.core.i18n.Message;

public class GetterAccessor<T, F> implements FieldAccessor<T, F> {
	private final Method method;
	private final Validator<? super F> validator;
	private final Message<?> fieldName;

	private GetterAccessor(Method method, Validator<? super F> validator, Message<?> fieldName) {
		this.method = method;
		this.validator = validator;
		this.fieldName = fieldName;
	}

	public static <T, F> FieldAccessor<T, F> create(Method method, Validator<? super F> validator, Message<?> fieldName) {
		return new GetterAccessor<T, F>(method, validator, fieldName);
	}

	public void initialize(BeanConstruction<T> beanConstruction, F value) {
		throw new UnsupportedOperationException();
	}

	public void update(T bean, F value) {
		throw new UnsupportedOperationException();
	}

	@SuppressWarnings("unchecked")
	public F get(T bean) {
		try {
			return (F) method.invoke(bean);
		} catch (ReflectiveOperationException e) {
			throw new BeanAccessException("Failed to call getter " + method + ".", e);
		}
	}

	public boolean canInitialize() {
		return false;
	}

	public boolean canUpdate() {
		return false;
	}

	public boolean canGet() {
		return true;
	}

	public Validator<? super F> getValidator() {
		return validator;
	}

	public Message<?> getFieldName() {
		return fieldName;
	}

}
