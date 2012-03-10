package be.appify.util.collections;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.commons.lang.Validate;

public class TypeMap<S> {
    private final HashMap<Class<S>, S> map = new HashMap<Class<S>, S>();

    @SuppressWarnings("unchecked")
    public void put(S object) {
        Validate.notNull(object);
        putType(object, (Class<S>) object.getClass());
    }

    private void putType(S object, Class<S> type) {
        map.put(type, object);
        putSuper(object, type);
    }

    @SuppressWarnings("unchecked")
    private void putSuper(S object, Class<S> type) {
        if (type.getSuperclass() != Object.class && !map.containsKey(type.getSuperclass())) {
            putType(object, (Class<S>) type.getSuperclass());
        }
    }

    public <T extends S> T get(Class<T> type) {
        return type.cast(map.get(type));
    }

    public Collection<S> values() {
        return new HashSet<S>(map.values());
    }
}
