package be.appify.i18n;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.enterprise.context.RequestScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequestScoped
public class ResourceBundleInternationalizationProvider implements InternationalizationProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceBundleInternationalizationProvider.class);
    private static final String BUNDLE_NAME = "messages";

    private final ResourceBundle resourceBundle;
    private final Locale locale;

    public ResourceBundleInternationalizationProvider(Locale locale) {
        this.resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME, locale);
        this.locale = locale;
    }

    @Override
    public String getMessage(String key, Object... arguments) {
        String message = null;
        try {
            message = getMessageFromBundle(key, arguments);
        } catch (NoSuchMessageException e) {
            LOGGER.warn("No resource found for key " + key);
            message = "???" + key + "???";
        }
        return message;
    }

    @Override
    public String getMessage(String key, boolean mustExist, Object... arguments) throws NoSuchMessageException {
        return mustExist ? getMessageFromBundle(key, arguments) : getMessage(key, arguments);
    }

    private String getMessageFromBundle(String key, Object... arguments) throws NoSuchMessageException {
        String message;
        try {
            message = resourceBundle.getString(key);
        } catch (MissingResourceException e) {
            throw new NoSuchMessageException(key, e);
        }
        return String.format(locale, message, arguments);
    }

    @Override
    public String toString() {
        return "ResourceBundleInternationalizationProvider[" + BUNDLE_NAME + ", " + locale + "]";
    }
}
