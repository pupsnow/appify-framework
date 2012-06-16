package be.appify.stereotype.core.operation;

import java.util.Map;

import be.appify.stereotype.core.beans.BeanModel;

public class CreateOperation<B> implements SpawningOperation<B> {

	private static final CreateOperation<Object> PROTOTYPE = new CreateOperation<Object>(null);
	private BeanModel<B> beanModel;

	public CreateOperation(BeanModel<B> beanModel) {
		this.beanModel = beanModel;
	}

	@Override
	public B execute(Object... parameters) {
		// TODO
		return null;
	}

	@Override
	public B execute(Map<String, Object> namedParameters) {
		return beanModel.create(namedParameters);
	}

	public static SpawningOperation<?> prototype() {
		return PROTOTYPE;
	}

	@Override
	public <N> SpawningOperation<N> createNew(BeanModel<N> beanModel) {
		return new CreateOperation<N>(beanModel);
	}
	
}
