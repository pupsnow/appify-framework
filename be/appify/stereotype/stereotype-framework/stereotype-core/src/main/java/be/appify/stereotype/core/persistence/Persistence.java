package be.appify.stereotype.core.persistence;

import java.util.UUID;

public interface Persistence {
	<T> void save(T bean);
	
	<T> T findByID(Class<T> entityClass, UUID id);
	
	<T> UUID getID(T bean);
}