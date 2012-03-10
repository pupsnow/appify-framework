package be.appify.repository;

public interface EnumRepository {
    void store(Class<? extends Enum<?>> enumType);

    Class<? extends Enum<?>> find(String namespace, String type);
}
