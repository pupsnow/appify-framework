package be.appify.stereotype.core.operation;

import java.util.Map;

import be.appify.stereotype.core.beans.BeanModel;
import be.appify.stereotype.core.persistence.Persistence;

public class CreateOperation<B> implements SpawningOperation<B> {

	private final BeanModel<B> beanModel;
	private Persistence persistence;

	public CreateOperation(Persistence persistence, BeanModel<B> beanModel) {
		this.beanModel = beanModel;
		this.persistence = persistence;
	}

	public CreateOperation(Persistence persistence) {
		this(persistence, null);
	}

	@Override
	public B execute(Object... parameters) {
		// TODO
		return null;
	}

	@Override
	public B execute(Map<String, Object> namedParameters) {
		B bean = beanModel.create(namedParameters);
		persistence.save(bean);
		return bean;
	}

	@Override
	public <N> SpawningOperation<N> createNew(BeanModel<N> beanModel) {
		return new CreateOperation<N>(persistence, beanModel);
	}

}
