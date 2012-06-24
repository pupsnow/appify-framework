package be.appify.stereotype.core.operation;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import be.appify.stereotype.core.beans.Bean;
import be.appify.stereotype.core.beans.BeanModel;
import be.appify.stereotype.core.persistence.Persistence;
import be.appify.stereotype.core.persistence.Transaction;

@Named
public class FindAllOperation<B extends Bean> implements ListingOperation<B> {

	private Persistence persistence;
	private BeanModel<B> beanModel;

	@Inject
	public FindAllOperation(Persistence persistence) {
		this(persistence, null);
	}

	private FindAllOperation(Persistence persistence, BeanModel<B> beanModel) {
		this.persistence = persistence;
		this.beanModel = beanModel;
	}

	@Override
	public <N extends Bean> GenericOperation<N> createNew(BeanModel<N> beanModel) {
		return new FindAllOperation<N>(persistence, beanModel);
	}

	@Override
	public List<B> execute(Map<String, Object> namedParameters) {
		Transaction transaction = persistence.startTransaction();
		List<B> beans = transaction.findAll(beanModel.getType());
		transaction.rollback();
		return beans;
	}

}
