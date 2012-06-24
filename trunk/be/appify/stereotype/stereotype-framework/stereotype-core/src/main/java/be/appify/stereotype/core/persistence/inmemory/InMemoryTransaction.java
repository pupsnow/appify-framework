package be.appify.stereotype.core.persistence.inmemory;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import be.appify.stereotype.core.beans.Bean;
import be.appify.stereotype.core.beans.BeanModel;
import be.appify.stereotype.core.beans.BeanModelRegistry;
import be.appify.stereotype.core.beans.fields.FieldAccessor;
import be.appify.stereotype.core.beans.fields.FieldModel;
import be.appify.stereotype.core.persistence.Transaction;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class InMemoryTransaction implements Transaction {
	private final Map<UUID, Bean> beansInTransaction;
	private final InMemoryPersistence persistence;
	private final boolean active;
	private final BeanModelRegistry beanModelRegistry;

	public InMemoryTransaction(InMemoryPersistence persistence, BeanModelRegistry beanModelRegistry) {
		this.beansInTransaction = Maps.newHashMap();
		for (Bean bean : persistence.getBeans()) {
			this.beansInTransaction.put(bean.getID(), bean);
		}
		this.persistence = persistence;
		this.beanModelRegistry = beanModelRegistry;
		active = true;
	}

	@Override
	public <T extends Bean> void save(T bean) {
		checkActive();
		beansInTransaction.put(bean.getID(), bean);
	}

	@Override
	public <T extends Bean> T findByID(Class<T> entityClass, UUID id) {
		checkActive();
		Bean bean = beansInTransaction.get(id);
		if (!entityClass.isInstance(bean)) {
			return null;
		}
		return entityClass.cast(bean);
	}

	@Override
	public void commit() {
		checkActive();
		persistence.update(beansInTransaction.values());
	}

	@Override
	public void rollback() {
		checkActive();
		beansInTransaction.clear();
	}

	private void checkActive() {
		if (!active) {
			throw new IllegalStateException("Transaction is inactive");
		}
	}

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public <T extends Bean> List<T> findAll(Class<T> entityClass) {
		List<T> beans = Lists.newArrayList();
		for (Bean bean : beansInTransaction.values()) {
			if (entityClass.isInstance(bean)) {
				beans.add(entityClass.cast(bean));
			}
		}
		return beans;
	}

	@Override
	public <T extends Bean> List<T> findAllContaining(Class<T> entityClass, String propertyName, String search) {
		List<T> beans = Lists.newArrayList();
		BeanModel<T> beanModel = beanModelRegistry.getBeanModel(entityClass);
		FieldModel<T, ?> fieldModel = beanModel.getField(propertyName);
		FieldAccessor<T, ?> accessor = fieldModel.getAccessor();
		for (Bean bean : beansInTransaction.values()) {
			if (entityClass.isInstance(bean) &&
					accessor.get(entityClass.cast(bean)).toString().toLowerCase().contains(search.toLowerCase())) {
				beans.add(entityClass.cast(bean));
			}
		}
		return beans;
	}

	@Override
	public <T extends Bean> void deleteAll(Class<T> entityClass) {
		// TODO Auto-generated method stub

	}

}
