package be.appify.stereotype.core.i18n;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class Message<K> {
	private final K key;
	private final List<Object> arguments;
	private final Class<?> declaringClass;

	public static <E extends Enum<E>> Message<E> create(E key, Object... arguments) {
		Preconditions.checkNotNull(key, "Key cannot be null");
		return new Message<E>(key.getClass(), key, arguments);
	}

	public static Message<String> create(Class<?> declaringClass, String key, Object... arguments) {
		Preconditions.checkNotNull(declaringClass, "Declaring class cannot be null");
		Preconditions.checkNotNull(key, "Key cannot be null");
		return new Message<String>(declaringClass, key, arguments);
	}

	private Message(Class<?> declaringClass, K key, Object... arguments) {
		this.key = key;
		this.declaringClass = declaringClass;
		this.arguments = Lists.newArrayList(arguments);
	}

	public K getKey() {
		return key;
	}

	public List<Object> getArguments() {
		return Collections.unmodifiableList(arguments);
	}

	@Override
	public String toString() {
		Dictionary dictionary = DictionaryFactory.INSTANCE.getDictionary();
		return dictionary.translate(this, Locale.ENGLISH);
	}

	public Class<?> getDeclaringClass() {
		return declaringClass;
	}
}
