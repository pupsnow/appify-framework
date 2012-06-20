package be.appify.stereotype.core.persistence.inmemory;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import be.appify.stereotype.core.beans.AbstractBean;
import be.appify.stereotype.core.persistence.Persistence;
import be.appify.stereotype.core.persistence.Transaction;

import com.google.common.collect.Sets;

public class InMemoryPersistence implements Persistence {
	private final Set<AbstractBean> beans;

	public InMemoryPersistence() {
		this.beans = Sets.newHashSet();
	}

	@Override
	public Transaction startTransaction() {
		return new InMemoryTransaction(this);
	}

	public Set<AbstractBean> getBeans() {
		return Collections.unmodifiableSet(beans);
	}

	public void update(Collection<AbstractBean> values) {
		beans.clear();
		beans.addAll(values);
	}

}
