package be.appify.stereotype.core.beans.validation;

import be.appify.stereotype.core.i18n.Message;

public class MaxLengthValidator extends AbstractValidator<MaxLength, String> {
	private final int length;

	public MaxLengthValidator(int length) {
		super(MaxLength.class, String.class);
		this.length = length;
	}

	public void validate(String value, Message<?> fieldName) {
		if (value != null && value.length() > length) {
			throw new ValidationException(ValidationMessage.MAX_LENGTH, fieldName, length);
		}
	}

	@Override
	protected Validator<String> createNewInternal(MaxLength annotation) {
		return new MaxLengthValidator(annotation.value());
	}

}
