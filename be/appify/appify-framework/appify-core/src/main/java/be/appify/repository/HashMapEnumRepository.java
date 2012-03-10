package be.appify.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Singleton;

import org.apache.commons.lang.Validate;

import be.appify.view.jaxrs.TypeView;

@Singleton
public class HashMapEnumRepository implements EnumRepository {
    private final Map<String, Class<? extends Enum<?>>> enumTypes = new ConcurrentHashMap<String, Class<? extends Enum<?>>>();

    @Override
    public void store(Class<? extends Enum<?>> enumType) {
        TypeView typeView = enumType.getAnnotation(TypeView.class);
        Validate.notNull(typeView, "Missing TypeView annotation on enum type " + enumType.getName());
        String key = typeView.namespace() + "." + typeView.type();
        enumTypes.put(key, enumType);
    }

    @Override
    public Class<? extends Enum<?>> find(String namespace, String type) {
        return enumTypes.get(namespace + "." + type);
    }

}
