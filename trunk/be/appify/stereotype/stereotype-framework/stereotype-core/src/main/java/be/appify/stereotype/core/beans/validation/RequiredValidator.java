package be.appify.stereotype.core.beans.validation;

import javax.inject.Named;

import org.apache.commons.lang.StringUtils;

import be.appify.stereotype.core.i18n.Message;

@Named
public class RequiredValidator extends AbstractValidator<Required, Object> {
	public RequiredValidator() {
		super(Required.class, Object.class);
	}

	public void validate(Object value, Message<?> fieldName) {
		if (value == null || (value instanceof String &&
				StringUtils.isBlank((String) value))) {
			throw new ValidationException(ValidationMessage.REQUIRED, fieldName);
		}
	}

	@Override
	protected Validator<Object> createNewInternal(Required annotation) {
		return this;
	}

}
