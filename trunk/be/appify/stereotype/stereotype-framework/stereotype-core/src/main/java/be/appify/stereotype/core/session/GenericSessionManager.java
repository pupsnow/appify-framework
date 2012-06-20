package be.appify.stereotype.core.session;

import javax.inject.Inject;
import javax.inject.Named;

import be.appify.stereotype.core.beans.BeanModelRegistry;

@Named
public class GenericSessionManager implements SessionManager {
	private final BeanModelRegistry beanModelRegistry;

	@Inject
	public GenericSessionManager(BeanModelRegistry beanModelRegistry) {
		this.beanModelRegistry = beanModelRegistry;
	}

	@Override
	public Session newSession() {
		return new SimpleSession(beanModelRegistry);
	}

}
