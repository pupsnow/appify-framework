package be.appify.stereotype.core.beans.validation;

import java.lang.annotation.Annotation;
import java.util.Collection;

import com.google.common.collect.Sets;

public abstract class AbstractValidator<A extends Annotation, T> implements Validator<T> {
	private final Class<A> annotationType;
	private final Collection<Class<?>> supportedTypes;

	public AbstractValidator(Class<A> annotationType, Class<?>... supportedTypes) {
		this.annotationType = annotationType;
		this.supportedTypes = Sets.newHashSet(supportedTypes);
	}

	public boolean supportsAnnotation(Annotation annotation) {
		return annotationType.isInstance(annotation);
	}

	public boolean supportsType(Class<?> type) {
		for (Class<?> supportedType : supportedTypes) {
			if (supportedType.isAssignableFrom(type)) {
				return true;
			}
		}
		return false;
	}

	public Collection<Class<?>> getSupportedTypes() {
		return Sets.newHashSet(supportedTypes);
	}

	public Validator<T> createNew(Annotation annotation) {
		if (!supportsAnnotation(annotation)) {
			throw new IllegalArgumentException(annotation + " is not supported by this Validator.");
		}
		return createNewInternal(annotationType.cast(annotation));
	}

	protected abstract Validator<T> createNewInternal(A annotation);

}
