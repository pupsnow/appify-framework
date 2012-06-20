package be.appify.stereotype.persistence.jpa;

import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import be.appify.stereotype.core.beans.AbstractBean;
import be.appify.stereotype.core.persistence.Transaction;

public class JPATransaction implements Transaction {

	private final EntityManager entityManager;
	private final EntityTransaction transaction;

	public JPATransaction(EntityManager entityManager) {
		this.entityManager = entityManager;
		this.transaction = entityManager.getTransaction();
		transaction.begin();
	}

	@Override
	public <T extends AbstractBean> void save(T bean) {
		entityManager.merge(bean);
	}

	@Override
	public <T extends AbstractBean> T findByID(Class<T> entityClass, UUID id) {
		return entityManager.find(entityClass, id);
	}

	@Override
	public void commit() {
		transaction.commit();
	}

	@Override
	public void rollback() {
		transaction.rollback();
	}

	@Override
	public boolean isActive() {
		return transaction.isActive();
	}

}
