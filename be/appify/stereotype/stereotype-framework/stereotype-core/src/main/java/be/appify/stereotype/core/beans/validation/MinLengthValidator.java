package be.appify.stereotype.core.beans.validation;

import javax.inject.Named;

import be.appify.stereotype.core.i18n.Message;

@Named
public class MinLengthValidator extends AbstractValidator<MinLength, String> {
	private final int length;

	public MinLengthValidator() {
		this(0);
	}

	public MinLengthValidator(int length) {
		super(MinLength.class, String.class);
		this.length = length;
	}

	public void validate(String value, Message<?> fieldName) {
		if (value != null && value.length() < length) {
			throw new ValidationException(ValidationMessage.MIN_LENGTH, fieldName, length);
		}
	}

	@Override
	protected Validator<String> createNewInternal(MinLength annotation) {
		return new MinLengthValidator(annotation.value());
	}

}
