package be.appify.stereotype.core.operation;

import java.util.Map;

import be.appify.stereotype.core.beans.BeanModel;

public class FindByIDOperation<B> implements SpawningOperation<B> {

	private static final SpawningOperation<?> PROTOTYPE = new FindByIDOperation<Object>(null);
	private BeanModel<B> beanModel;
	
	public FindByIDOperation(BeanModel<B> beanModel) {
		this.beanModel = beanModel;
	}

	@Override
	public B execute(Object... parameters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public B execute(Map<String, Object> namedParameters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <N> SpawningOperation<N> createNew(BeanModel<N> beanModel) {
		return new FindByIDOperation<N>(beanModel);
	}

	public static SpawningOperation<?> prototype() {
		return PROTOTYPE;
	}

}
