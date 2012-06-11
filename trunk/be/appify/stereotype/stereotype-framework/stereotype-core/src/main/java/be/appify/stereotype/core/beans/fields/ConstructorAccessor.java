package be.appify.stereotype.core.beans.fields;

import be.appify.stereotype.core.beans.BeanConstruction;
import be.appify.stereotype.core.beans.validation.Validator;
import be.appify.stereotype.core.i18n.Message;

public class ConstructorAccessor<T, F> implements FieldAccessor<T, F> {

	private final int index;
	private final Validator<? super F> validator;
	private final Message<?> fieldName;

	private ConstructorAccessor(int index, Validator<? super F> validator, Message<?> fieldName) {
		this.index = index;
		this.validator = validator;
		this.fieldName = fieldName;
	}

	public void initialize(BeanConstruction<T> beanConstruction, F value) {
		validator.validate(value, fieldName);
		beanConstruction.addConstructorParameter(index, value);
	}

	public void update(T bean, F value) {
		throw new UnsupportedOperationException();
	}

	public F get(T bean) {
		throw new UnsupportedOperationException();
	}

	public boolean canInitialize() {
		return true;
	}

	public boolean canUpdate() {
		return false;
	}

	public boolean canGet() {
		return false;
	}

	public static <T, F> FieldAccessor<T, F> create(int index, Validator<? super F> validator, Message<?> fieldName) {
		return new ConstructorAccessor<T, F>(index, validator, fieldName);
	}

	public Validator<? super F> getValidator() {
		return validator;
	}

	public Message<?> getFieldName() {
		return fieldName;
	}

}
