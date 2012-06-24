package be.appify.stereotype.persistence.jpa;

import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import be.appify.stereotype.core.beans.Bean;
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
	public <T extends Bean> void save(T bean) {
		entityManager.merge(bean);
	}

	@Override
	public <T extends Bean> T findByID(Class<T> entityClass, UUID id) {
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

	@Override
	public <T extends Bean> List<T> findAll(Class<T> entityClass) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<T> query = criteriaBuilder.createQuery(entityClass);
		Root<T> root = query.from(entityClass);
		query.select(root);
		return entityManager.createQuery(query).getResultList();
	}

	@Override
	public <T extends Bean> List<T> findAllContaining(Class<T> entityClass, String propertyName, String search) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<T> query = criteriaBuilder.createQuery(entityClass);
		Root<T> root = query.from(entityClass);
		query.select(root)
				.where(criteriaBuilder.like(criteriaBuilder.lower(root.<String> get(propertyName)),
						"%" + search.toLowerCase() + "%"));
		return entityManager.createQuery(query).getResultList();
	}

	@Override
	public <T extends Bean> void deleteAll(Class<T> entityClass) {
		for (T bean : findAll(entityClass)) {
			entityManager.remove(bean);
		}
	}

}
