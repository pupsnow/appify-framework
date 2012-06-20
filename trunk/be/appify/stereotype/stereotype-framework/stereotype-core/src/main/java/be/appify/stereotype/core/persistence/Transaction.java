package be.appify.stereotype.core.persistence;

import java.util.UUID;

import be.appify.stereotype.core.beans.AbstractBean;

public interface Transaction {

	<T extends AbstractBean> void save(T bean);

	<T extends AbstractBean> T findByID(Class<T> entityClass, UUID id);

	void commit();

	void rollback();

	boolean isActive();
}
