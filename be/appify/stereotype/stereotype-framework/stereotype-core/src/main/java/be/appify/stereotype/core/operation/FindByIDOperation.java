package be.appify.stereotype.core.operation;

import java.util.Map;
import java.util.UUID;

import be.appify.stereotype.core.beans.BeanModel;
import be.appify.stereotype.core.persistence.Persistence;

public class FindByIDOperation<B> implements SpawningOperation<B> {

	private BeanModel<B> beanModel;
	private Persistence persistence;

	private FindByIDOperation(Persistence persistence, BeanModel<B> beanModel) {
		this.persistence = persistence;
		this.beanModel = beanModel;
	}

	public FindByIDOperation(Persistence persistence) {
		this(persistence, null);
	}

	@Override
	public B execute(Object... parameters) {
		return persistence.findByID(beanModel.getType(), (UUID) parameters[0]);
	}

	@Override
	public B execute(Map<String, Object> namedParameters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <N> SpawningOperation<N> createNew(BeanModel<N> beanModel) {
		return new FindByIDOperation<N>(persistence, beanModel);
	}

}
