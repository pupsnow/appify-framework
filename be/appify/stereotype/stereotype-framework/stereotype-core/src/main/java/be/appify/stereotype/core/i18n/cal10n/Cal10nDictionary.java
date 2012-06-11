package be.appify.stereotype.core.i18n.cal10n;

import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.appify.stereotype.core.i18n.Dictionary;
import be.appify.stereotype.core.i18n.Message;
import ch.qos.cal10n.IMessageConveyor;
import ch.qos.cal10n.MessageConveyor;
import ch.qos.cal10n.MessageConveyorException;

import com.google.common.collect.Maps;

public class Cal10nDictionary implements Dictionary {
	private static final Logger LOGGER = LoggerFactory.getLogger(Cal10nDictionary.class);
	private final Map<Locale, IMessageConveyor> enumConveyorCache;
	private final Map<Locale, StringMessageConveyor> stringConveyorCache;

	public Cal10nDictionary() {
		enumConveyorCache = Maps.newHashMap();
		stringConveyorCache = Maps.newHashMap();
	}

	public String translate(Message<?> message, Locale locale) {
		try {
			Object key = message.getKey();
			if (key instanceof Enum) {
				IMessageConveyor messageConveyor = getEnumConveyor(locale);
				return messageConveyor.getMessage((Enum<?>) message.getKey(), message.getArguments().toArray());
			} else {
				StringMessageConveyor messageConveyor = getStringConveyor(locale);
				return messageConveyor.getMessage(message.getDeclaringClass(), (String) message.getKey(),
						message.getArguments().toArray());
			}
		} catch (MessageConveyorException e) {
			String keyString = message.getDeclaringClass().getName() + "." + message.getKey();
			LOGGER.error("Failed to find translation for message with key <" + keyString + ">.");
			return "???" + keyString + "???";
		}
	}

	private StringMessageConveyor getStringConveyor(Locale locale) {
		StringMessageConveyor messageConveyor;
		synchronized (stringConveyorCache) {
			messageConveyor = stringConveyorCache.get(locale);
			if (messageConveyor == null) {
				messageConveyor = new Cal10nStringMessageConveyor(locale);
				stringConveyorCache.put(locale, messageConveyor);
			}
		}
		return messageConveyor;
	}

	private IMessageConveyor getEnumConveyor(Locale locale) {
		IMessageConveyor messageConveyor;
		synchronized (enumConveyorCache) {
			messageConveyor = enumConveyorCache.get(locale);
			if (messageConveyor == null) {
				messageConveyor = new MessageConveyor(locale);
				enumConveyorCache.put(locale, messageConveyor);
			}
		}
		return messageConveyor;
	}
}
