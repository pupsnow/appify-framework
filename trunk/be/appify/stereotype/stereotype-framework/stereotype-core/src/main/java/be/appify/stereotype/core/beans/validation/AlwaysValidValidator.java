package be.appify.stereotype.core.beans.validation;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashSet;

import be.appify.stereotype.core.i18n.Message;

import com.google.common.collect.Sets;

public enum AlwaysValidValidator implements Validator<Object> {
	INSTANCE;

	private static final HashSet<Class<?>> SUPPORTED_TYPES = Sets.<Class<?>> newHashSet(Object.class);

	public void validate(Object value, Message<?> fieldName) throws ValidationException {
	}

	public boolean supportsAnnotation(Annotation annotation) {
		return false;
	}

	public boolean supportsType(Class<?> type) {
		return true;
	}

	public Validator<Object> createNew(Annotation annotation) {
		return this;
	}

	public Collection<Class<?>> getSupportedTypes() {
		return SUPPORTED_TYPES;
	}

}
