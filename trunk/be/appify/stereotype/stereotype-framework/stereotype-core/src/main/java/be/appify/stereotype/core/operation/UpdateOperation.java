package be.appify.stereotype.core.operation;

import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;

import be.appify.stereotype.core.beans.BeanModel;
import be.appify.stereotype.core.persistence.Persistence;

@Named
public class UpdateOperation<B> implements ManipulatingOperation<B> {
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
	public <N> UpdateOperation<N> createNew(BeanModel<N> beanModel) {
		return new UpdateOperation<N>(persistence, beanModel);
	}

	@Override
	public void execute(Map<String, Object> namedParameters) {
		B bean = persistence.findByID(beanModel.getType(), (UUID) namedParameters.get("id"));
		for (String fieldName : namedParameters.keySet()) {
			if (!"id".equals(fieldName)) {
				beanModel.setValue(bean, fieldName, namedParameters.get(fieldName));
			}
		}
		persistence.save(bean);
	}

}
