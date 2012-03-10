package be.appify.i18n;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FallbackResourceBundleInternationalizationProvider implements InternationalizationProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceBundleInternationalizationProvider.class);

    private final List<InternationalizationProvider> providers = new ArrayList<InternationalizationProvider>();

    public FallbackResourceBundleInternationalizationProvider(Locale... locales) {
        List<Locale> l = Arrays.asList(locales);
        if (l == null || l.isEmpty()) {
            l = Arrays.asList(Locale.ENGLISH);
        }
        for (Locale locale : l) {
            providers.add(new ResourceBundleInternationalizationProvider(locale));
        }
    }

    @Override
    public String getMessage(String key, Object... arguments) {
        return getMessage(key, false, arguments);
    }

    @Override
    public String getMessage(String key, boolean mustExist, Object... arguments) throws NoSuchMessageException {
        String message = null;
        for (InternationalizationProvider provider : providers) {
            try {
                message = provider.getMessage(key, true, arguments);
                break;
            } catch (NoSuchMessageException e) {
                LOGGER.debug("No resource found for key " + key + " on provider " + provider);
            }
        }
        if (message == null) {
            if (mustExist) {
                throw new NoSuchMessageException(key);
            }
            LOGGER.warn("No resource found for key " + key);
            message = "???" + key + "???";
        }
        return message;
    }

    @Override
    public String toString() {
        return "FallbackResourceBundleInternationalizationProvider" + providers;
    }
}
