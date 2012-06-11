package be.appify.stereotype.core.beans.validation;

import java.lang.annotation.Annotation;
import java.util.Collection;

import com.google.common.collect.Sets;

public class ValidatorFactory {
	private final Collection<Validator<?>> validators = Sets.<Validator<?>> newHashSet(
			new MaxLengthValidator(0),
			new MinLengthValidator(0),
			RequiredValidator.INSTANCE);

	@SuppressWarnings("unchecked")
	public <T> Validator<T> createValidator(Annotation validatorAnnotation) {
		Validator<T> result = null;
		for (Validator<?> validator : validators) {
			if (validator.supportsAnnotation(validatorAnnotation)) {
				result = ((Validator<T>) validator).createNew(validatorAnnotation);
				break;
			}
		}
		return result;
	}
}
