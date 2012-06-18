package be.appify.stereotype.core.operation;

import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;

import be.appify.stereotype.core.beans.BeanModel;
import be.appify.stereotype.core.persistence.Persistence;

@Named
public class FindByIDOperation<B> implements SpawningOperation<B> {

	private BeanModel<B> beanModel;
	private Persistence persistence;

	private FindByIDOperation(Persistence persistence, BeanModel<B> beanModel) {
		this.persistence = persistence;
		this.beanModel = beanModel;
	}

	@Inject
	public FindByIDOperation(Persistence persistence) {
		this(persistence, null);
	}

	@Override
	public B execute(Map<String, Object> namedParameters) {
		return persistence.findByID(beanModel.getType(), (UUID) namedParameters.get("id"));
	}

	@Override
	public <N> SpawningOperation<N> createNew(BeanModel<N> beanModel) {
		return new FindByIDOperation<N>(persistence, beanModel);
	}

}
