package be.appify.stereotype.core.i18n;

import java.util.Locale;

public interface Dictionary {
	String translate(Message<?> message, Locale locale);

	boolean hasTranslation(Message<?> message, Locale locale);
}
