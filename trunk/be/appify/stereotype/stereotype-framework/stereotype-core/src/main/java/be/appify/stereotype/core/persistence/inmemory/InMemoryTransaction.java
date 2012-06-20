package be.appify.stereotype.core.persistence.inmemory;

import java.util.Map;
import java.util.UUID;

import be.appify.stereotype.core.beans.AbstractBean;
import be.appify.stereotype.core.persistence.Transaction;

import com.google.common.collect.Maps;

public class InMemoryTransaction implements Transaction {
	private final Map<UUID, AbstractBean> beansInTransaction;
	private final InMemoryPersistence persistence;
	private final boolean active;

	public InMemoryTransaction(InMemoryPersistence persistence) {
		this.beansInTransaction = Maps.newHashMap();
		for (AbstractBean bean : persistence.getBeans()) {
			this.beansInTransaction.put(bean.getID(), bean);
		}
		this.persistence = persistence;
		active = true;
	}

	@Override
	public <T extends AbstractBean> void save(T bean) {
		checkActive();
		beansInTransaction.put(bean.getID(), bean);
	}

	@Override
	public <T extends AbstractBean> T findByID(Class<T> entityClass, UUID id) {
		checkActive();
		AbstractBean bean = beansInTransaction.get(id);
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

}
