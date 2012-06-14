package be.appify.stereotype.di.spring;

import org.springframework.context.ApplicationContext;

import be.appify.stereotype.core.bootstrapping.Context;

public class SpringContext implements Context {

	private final ApplicationContext applicationContext;

	public SpringContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public <T> T getUniqueBean(Class<T> type) {
		return applicationContext.getBean(type);
	}

}
