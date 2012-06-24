package be.appify.stereotype.core.persistence;

import java.util.List;
import java.util.UUID;

import be.appify.stereotype.core.beans.Bean;

public interface Transaction {

	<T extends Bean> void save(T bean);

	<T extends Bean> T findByID(Class<T> entityClass, UUID id);

	<T extends Bean> List<T> findAll(Class<T> entityClass);

	<T extends Bean> List<T> findAllContaining(Class<T> entityClass, String propertyName, String search);

	<T extends Bean> void deleteAll(Class<T> entityClass);

	void commit();

	void rollback();

	boolean isActive();
}
