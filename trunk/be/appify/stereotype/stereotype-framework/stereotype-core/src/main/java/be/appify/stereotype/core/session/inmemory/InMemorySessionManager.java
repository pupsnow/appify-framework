package be.appify.stereotype.core.session.inmemory;

import be.appify.stereotype.core.beans.BeanModelRegistry;
import be.appify.stereotype.core.persistence.Persistence;
import be.appify.stereotype.core.persistence.inmemory.InMemoryPersistence;
import be.appify.stereotype.core.session.SimpleSession;
import be.appify.stereotype.core.session.Session;
import be.appify.stereotype.core.session.SessionManager;

public class InMemorySessionManager implements SessionManager {
	private BeanModelRegistry beanModelRegistry;
	private Persistence persistence;
	
	public InMemorySessionManager(BeanModelRegistry beanModelRegistry) {
		this.beanModelRegistry = beanModelRegistry;
		this.persistence = new InMemoryPersistence(beanModelRegistry);
	}

	@Override
	public Session newSession() {
		return new SimpleSession(persistence, beanModelRegistry);
	}

}
