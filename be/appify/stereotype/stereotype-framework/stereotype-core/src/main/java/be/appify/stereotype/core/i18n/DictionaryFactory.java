package be.appify.stereotype.core.i18n;

import be.appify.stereotype.core.i18n.cal10n.Cal10nDictionary;

public enum DictionaryFactory {
	INSTANCE;

	private Dictionary dictionary;

	private DictionaryFactory() {
		dictionary = new Cal10nDictionary();
	}

	public Dictionary getDictionary() {
		return dictionary;
	}
}
