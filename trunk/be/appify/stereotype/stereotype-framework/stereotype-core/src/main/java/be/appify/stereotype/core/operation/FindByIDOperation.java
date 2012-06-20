package be.appify.stereotype.core.operation;

import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;

import be.appify.stereotype.core.beans.AbstractBean;
import be.appify.stereotype.core.beans.BeanModel;
import be.appify.stereotype.core.persistence.Persistence;
import be.appify.stereotype.core.persistence.Transaction;

@Named
public class FindByIDOperation<B extends AbstractBean> implements SpawningOperation<B> {

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
		Transaction transaction = persistence.startTransaction();
		B bean = transaction.findByID(beanModel.getType(), (UUID) namedParameters.get("id"));
		transaction.rollback();
		return bean;
	}

	@Override
	public <N extends AbstractBean> SpawningOperation<N> createNew(BeanModel<N> beanModel) {
		return new FindByIDOperation<N>(persistence, beanModel);
	}

}
