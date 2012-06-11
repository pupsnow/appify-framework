package be.appify.stereotype.core.beans.validation;

import java.lang.annotation.Annotation;
import java.util.Collection;

import be.appify.stereotype.core.i18n.Message;

public interface Validator<T> {
	void validate(T value, Message<?> fieldName) throws ValidationException;

	boolean supportsAnnotation(Annotation annotation);

	boolean supportsType(Class<?> type);

	Validator<T> createNew(Annotation annotation);

	Collection<Class<?>> getSupportedTypes();
}
