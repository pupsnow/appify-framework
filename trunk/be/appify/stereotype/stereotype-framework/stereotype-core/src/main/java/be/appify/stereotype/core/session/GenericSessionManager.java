package be.appify.stereotype.core.session;

import be.appify.stereotype.core.beans.BeanModelRegistry;
import be.appify.stereotype.core.persistence.Persistence;

public class GenericSessionManager implements SessionManager {
	private final BeanModelRegistry beanModelRegistry;
	private final Persistence persistence;

	public GenericSessionManager(BeanModelRegistry beanModelRegistry, Persistence persistence) {
		this.beanModelRegistry = beanModelRegistry;
		this.persistence = persistence;
	}

	@Override
	public Session newSession() {
		return new SimpleSession(persistence, beanModelRegistry);
	}

}
