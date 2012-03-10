package be.appify.i18n;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import junit.framework.Assert;

import org.junit.Test;

import be.appify.i18n.InternationalizationProvider;
import be.appify.i18n.ResourceBundleInternationalizationProvider;

public class ResourceBundleInternationalizationProviderTest {
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private final InternationalizationProvider internationalizationProvider = new ResourceBundleInternationalizationProvider(Locale.ENGLISH);

    @Test
    public void shouldReturnTranslation() {
        Assert.assertEquals("Message without arguments", internationalizationProvider.getMessage("message.without.arguments"));
    }

    @Test
    public void argumentsShouldBeFilledIn() throws ParseException {
        Assert.assertEquals("Message with a Date (17-11-2010) and a BigDecimal (15.23)",
                internationalizationProvider.getMessage("message.with.2.arguments", DATE_FORMAT.parse("2010-11-17"), new BigDecimal("15.23")));
    }

    @Test
    public void shouldShowQuestionMarksWhenNotFound() {
        Assert.assertEquals("???not.found???", internationalizationProvider.getMessage("not.found"));
    }
}
