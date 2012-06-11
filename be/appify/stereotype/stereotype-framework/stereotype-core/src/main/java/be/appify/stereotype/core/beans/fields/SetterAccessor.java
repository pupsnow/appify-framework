package be.appify.stereotype.core.beans.fields;

import java.lang.reflect.Method;

import be.appify.stereotype.core.beans.BeanConstruction;
import be.appify.stereotype.core.beans.validation.Validator;
import be.appify.stereotype.core.functional.Procedure;
import be.appify.stereotype.core.i18n.Message;

public class SetterAccessor<T, F> implements FieldAccessor<T, F> {

	private final Method method;
	private final Validator<? super F> validator;
	private final Message<?> fieldName;

	private SetterAccessor(Method method, Validator<? super F> validator, Message<?> fieldName) {
		this.method = method;
		this.validator = validator;
		this.fieldName = fieldName;
	}

	public static <T, F> FieldAccessor<T, F> create(Method method, Validator<? super F> validator, Message<?> fieldName) {
		return new SetterAccessor<T, F>(method, validator, fieldName);
	}

	public void initialize(BeanConstruction<T> beanConstruction, final F value) {
		addSetterProcedure(beanConstruction, value);
	}

	private void addSetterProcedure(BeanConstruction<T> beanConstruction, final F value) {
		beanConstruction.addSetterProcedure(new Procedure<T>() {

			public void execute(T bean) {
				invokeSetter(bean, value);
			}

		});
	}

	private void invokeSetter(T bean, final F value) {
		validator.validate(value, fieldName);
		try {
			method.invoke(bean, value);
		} catch (ReflectiveOperationException e) {
			throw new BeanAccessException("Failed to call setter " + method + " with argument " + value + ".",
					e);
		}
	}

	public void update(T bean, F value) {
		invokeSetter(bean, value);
	}

	public F get(T bean) {
		throw new UnsupportedOperationException();
	}

	public boolean canInitialize() {
		return true;
	}

	public boolean canUpdate() {
		return true;
	}

	public boolean canGet() {
		return false;
	}

	public Validator<? super F> getValidator() {
		return validator;
	}

	public Message<?> getFieldName() {
		return fieldName;
	}

}
