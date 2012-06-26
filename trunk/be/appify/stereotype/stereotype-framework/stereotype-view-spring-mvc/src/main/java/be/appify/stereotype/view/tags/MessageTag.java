package be.appify.stereotype.view.tags;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Locale;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import be.appify.stereotype.core.i18n.Dictionary;
import be.appify.stereotype.core.i18n.DictionaryFactory;
import be.appify.stereotype.core.i18n.Message;

@SuppressWarnings("serial")
public class MessageTag extends TagSupport {
	private Locale locale;
	private Message<?> message;
	private boolean capitalize;

	public void setLocale(String locale) {
		this.locale = new Locale(locale);
	}

	public void setMessage(Message<?> message) {
		this.message = message;
	}

	public void setCapitalize(boolean capitalize) {
		this.capitalize = capitalize;
	}

	@Override
	public int doStartTag() throws JspException {
		Dictionary dictionary = DictionaryFactory.INSTANCE.getDictionary();
		String translation = null;
		if (locale == null) {
			@SuppressWarnings("unchecked")
			Enumeration<Locale> locales = pageContext.getRequest().getLocales();
			while (locales.hasMoreElements()) {
				Locale locale = locales.nextElement();
				if (dictionary.hasTranslation(message, locale)) {
					translation = dictionary.translate(message, locale);
					break;
				}
			}
		} else {
			translation = dictionary.translate(message, locale);
		}
		if (translation == null) {
			translation = "???" + message.getDeclaringClass().getName() + "." + message.getKey() + "???";
		}
		if (capitalize) {
			translation = StringUtils.capitalize(translation);
		}
		try {
			pageContext.getOut().write(StringEscapeUtils.escapeHtml(translation));
		} catch (IOException e) {
			throw new JspException(e);
		}
		return EVAL_BODY_INCLUDE;
	}
}
