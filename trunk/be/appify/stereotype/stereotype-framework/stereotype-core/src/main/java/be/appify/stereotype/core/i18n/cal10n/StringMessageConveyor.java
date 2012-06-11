package be.appify.stereotype.core.i18n.cal10n;

public interface StringMessageConveyor {
	String getMessage(Class<?> declaringClass, String key, Object... arguments);
}
