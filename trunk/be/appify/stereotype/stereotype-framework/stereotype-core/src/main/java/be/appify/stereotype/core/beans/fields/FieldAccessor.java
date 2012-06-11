package be.appify.stereotype.core.beans.fields;

import be.appify.stereotype.core.beans.BeanConstruction;
import be.appify.stereotype.core.beans.validation.Validator;
import be.appify.stereotype.core.i18n.Message;

public interface FieldAccessor<T, F> {
	void initialize(BeanConstruction<T> beanConstruction, F value);

	void update(T bean, F value);

	F get(T bean);

	boolean canInitialize();

	boolean canUpdate();

	boolean canGet();

	Validator<? super F> getValidator();

	Message<?> getFieldName();
}
