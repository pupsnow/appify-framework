package be.appify.stereotype.core.operation;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import be.appify.stereotype.core.beans.Bean;
import be.appify.stereotype.core.beans.BeanModel;
import be.appify.stereotype.core.beans.fields.FieldModel;
import be.appify.stereotype.core.persistence.Persistence;
import be.appify.stereotype.core.persistence.Transaction;

@Named
public class FindAllContainingOperation<B extends Bean> implements ListingOperation<B> {
	private Persistence persistence;
	private BeanModel<B> beanModel;

	private FindAllContainingOperation(Persistence persistence, BeanModel<B> beanModel) {
		this.persistence = persistence;
		this.beanModel = beanModel;
	}

	@Inject
	public FindAllContainingOperation(Persistence persistence) {
		this(persistence, null);
	}

	@Override
	public <N extends Bean> GenericOperation<N> createNew(BeanModel<N> beanModel) {
		return new FindAllContainingOperation<N>(persistence, beanModel);
	}

	@Override
	public List<B> execute(Map<String, Object> namedParameters) {
		Transaction transaction = persistence.startTransaction();
		FieldModel<B, ?> displayField = beanModel.getDisplayField();
		List<B> beans = transaction.findAllContaining(beanModel.getType(), displayField.getName(),
				(String) namedParameters.get("search"));
		transaction.rollback();
		return beans;
	}

}
