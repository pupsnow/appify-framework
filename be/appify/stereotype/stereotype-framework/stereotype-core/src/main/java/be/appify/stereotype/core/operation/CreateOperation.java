package be.appify.stereotype.core.operation;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import be.appify.stereotype.core.beans.BeanModel;
import be.appify.stereotype.core.persistence.Persistence;

@Named
public class CreateOperation<B> implements SpawningOperation<B> {

	private final BeanModel<B> beanModel;
	private Persistence persistence;

	private CreateOperation(Persistence persistence, BeanModel<B> beanModel) {
		this.beanModel = beanModel;
		this.persistence = persistence;
	}

	@Inject
	public CreateOperation(Persistence persistence) {
		this(persistence, null);
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
