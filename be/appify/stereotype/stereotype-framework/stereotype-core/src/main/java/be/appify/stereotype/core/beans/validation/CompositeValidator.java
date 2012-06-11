package be.appify.stereotype.core.beans.validation;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import be.appify.stereotype.core.i18n.Message;

import com.google.common.collect.Sets;

public class CompositeValidator<F> implements Validator<F> {

	private final HashSet<Validator<? super F>> validators;

	public CompositeValidator(Collection<Validator<? super F>> validators) {
		this.validators = Sets.newHashSet(validators);
	}

	public void validate(F value, Message<?> fieldName) throws ValidationException {
		for (Validator<? super F> validator : validators) {
			validator.validate(value, fieldName);
		}
	}

	public boolean supportsAnnotation(Annotation annotation) {
		return false;
	}

	public boolean supportsType(Class<?> type) {
		return true;
	}

	public Validator<F> createNew(Annotation annotation) {
		return this;
	}

	public Collection<Class<?>> getSupportedTypes() {
		return Collections.emptySet();
	}

	public static <F> CompositeValidator<? super F> create(Collection<Validator<? super F>> validators) {
		return new CompositeValidator<F>(validators);
	}

}
