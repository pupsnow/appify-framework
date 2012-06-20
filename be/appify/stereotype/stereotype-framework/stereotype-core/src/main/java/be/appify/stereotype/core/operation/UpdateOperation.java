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
public class UpdateOperation<B extends AbstractBean> implements ManipulatingOperation<B> {
	private final BeanModel<B> beanModel;
	private final Persistence persistence;

	private UpdateOperation(Persistence persistence, BeanModel<B> beanModel) {
		this.beanModel = beanModel;
		this.persistence = persistence;
	}

	@Inject
	public UpdateOperation(Persistence persistence) {
		this(persistence, null);
	}

	@Override
	public <N extends AbstractBean> UpdateOperation<N> createNew(BeanModel<N> beanModel) {
		return new UpdateOperation<N>(persistence, beanModel);
	}

	@Override
	public void execute(Map<String, Object> namedParameters) {
		Transaction transaction = persistence.startTransaction();
		B bean = transaction.findByID(beanModel.getType(), (UUID) namedParameters.get("id"));
		for (String fieldName : namedParameters.keySet()) {
			if (!"id".equals(fieldName)) {
				beanModel.setValue(bean, fieldName, namedParameters.get(fieldName));
			}
		}
		transaction.save(bean);
		transaction.commit();
	}

}
