package be.appify.stereotype.core.beans.fields;

import java.util.Collection;
import java.util.Date;

import com.google.common.collect.Sets;

public enum FieldType {
	TEXT(String.class),
	BOOLEAN(boolean.class, Boolean.class),
	NUMBER(int.class, long.class, float.class, double.class, Number.class),
	DATE(Date.class);

	private Collection<Class<?>> supportedTypes;

	private FieldType(Class<?>... supportedTypes) {
		this.supportedTypes = Sets.newHashSet(supportedTypes);
	}

	public boolean supports(Class<?> type) {
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
}
