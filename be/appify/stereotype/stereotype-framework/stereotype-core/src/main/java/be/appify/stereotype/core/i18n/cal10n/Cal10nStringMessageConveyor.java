package be.appify.stereotype.core.i18n.cal10n;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Cal10nConstants;
import ch.qos.cal10n.MessageConveyorException;
import ch.qos.cal10n.util.AnnotationExtractor;
import ch.qos.cal10n.util.CAL10NResourceBundle;
import ch.qos.cal10n.util.CAL10NResourceBundleFinder;

public class Cal10nStringMessageConveyor implements StringMessageConveyor {
	private final Locale locale;
	final Map<String, CAL10NResourceBundle> cache = new ConcurrentHashMap<String, CAL10NResourceBundle>();

	public Cal10nStringMessageConveyor(Locale locale) {
		this.locale = locale;
	}

	public String getMessage(Class<?> declaringClass, String key, Object... arguments) {
		String declararingClassName = declaringClass.getName();
		CAL10NResourceBundle rb = cache.get(declararingClassName);
		if (rb == null || rb.hasChanged()) {
			rb = lookup(declaringClass, key);
			cache.put(declararingClassName, rb);
		}

		String keyAsStr = key.toString();
		String value = rb.getString(keyAsStr);
		if (value == null) {
			return "No key found for " + keyAsStr;
		} else {
			if (arguments == null || arguments.length == 0) {
				return value;
			} else {
				return MessageFormat.format(value, arguments);
			}
		}
	}

	private <E extends Enum<?>> CAL10NResourceBundle lookup(Class<?> declaringClass, String key)
			throws MessageConveyorException {

		String baseName = getBaseName(declaringClass);
		if (baseName == null) {
			throw new MessageConveyorException(
					"Missing @BaseName annotation in enum type ["
							+ key.getClass().getName() + "]. See also "
							+ Cal10nConstants.MISSING_BN_ANNOTATION_URL);
		}

		String charset = AnnotationExtractor.getCharset(declaringClass, locale);
		CAL10NResourceBundle rb = CAL10NResourceBundleFinder.getBundle(
				declaringClass.getClassLoader(), baseName, locale, charset);

		if (rb == null) {
			throw new MessageConveyorException(
					"Failed to locate resource bundle [" + baseName
							+ "] for locale [" + locale + "] for enum type ["
							+ declaringClass.getName() + "]");
		}
		return rb;
	}

	private String getBaseName(Class<?> declaringClass) {
		BaseName baseName = declaringClass.getAnnotation(BaseName.class);
		if (baseName == null) {
			return declaringClass.getName();
		}
		return baseName.value();
	}

}
