package be.appify.stereotype.core.beans.validation;

import java.lang.annotation.Annotation;
import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Named;

import com.google.common.collect.Sets;

// TODO: invert validator dependency from annotation to validator
@Named
public class ValidatorFactory {
	private final Collection<Validator<?>> validators;

	@Inject
	public ValidatorFactory(Collection<Validator<?>> validators) {
		this.validators = Sets.newHashSet(validators);
	}

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
