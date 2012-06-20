package be.appify.stereotype.core.operation;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import be.appify.stereotype.core.beans.AbstractBean;
import be.appify.stereotype.core.beans.BeanModel;
import be.appify.stereotype.core.persistence.Persistence;
import be.appify.stereotype.core.persistence.Transaction;

@Named
public class CreateOperation<B extends AbstractBean> implements SpawningOperation<B> {

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
		Transaction transaction = persistence.startTransaction();
		B bean = beanModel.create(namedParameters);
		transaction.save(bean);
		transaction.commit();
		return bean;
	}

	@Override
	public <N extends AbstractBean> SpawningOperation<N> createNew(BeanModel<N> beanModel) {
		return new CreateOperation<N>(persistence, beanModel);
	}

}
