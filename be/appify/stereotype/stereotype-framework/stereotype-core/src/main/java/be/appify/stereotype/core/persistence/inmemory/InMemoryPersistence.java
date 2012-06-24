package be.appify.stereotype.core.persistence.inmemory;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import be.appify.stereotype.core.beans.Bean;
import be.appify.stereotype.core.beans.BeanModelRegistry;
import be.appify.stereotype.core.persistence.Persistence;
import be.appify.stereotype.core.persistence.Transaction;

import com.google.common.collect.Sets;

public class InMemoryPersistence implements Persistence {
	private final Set<Bean> beans;
	private BeanModelRegistry beanModelRegistry;

	public InMemoryPersistence() {
		this.beans = Sets.newHashSet();
	}

	public void setBeanModelRegistry(BeanModelRegistry beanModelRegistry) {
		this.beanModelRegistry = beanModelRegistry;
	}

	@Override
	public Transaction startTransaction() {
		return new InMemoryTransaction(this, beanModelRegistry);
	}

	public Set<Bean> getBeans() {
		return Collections.unmodifiableSet(beans);
	}

	public void update(Collection<Bean> values) {
		beans.clear();
		beans.addAll(values);
	}

}
