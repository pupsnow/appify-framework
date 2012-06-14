package be.appify.stereotype.di.spring;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import be.appify.stereotype.core.bootstrapping.Context;
import be.appify.stereotype.core.bootstrapping.StereotypeBootstrapper;
import be.appify.stereotype.core.util.LazyInitializer;

public class SpringStereotypeBootstrapper extends StereotypeBootstrapper {

	private static final String DEFAULT_APPLICATION_CONTEXT = "stereotype-context.xml";
	private static final String APPLICATION_CONTEXT_PROPERTY = "be.appify.stereotype.di.spring.context";
	private final LazyInitializer<Context> context;

	public SpringStereotypeBootstrapper(final String... packageNames) {
		context = new LazyInitializer<Context>() {

			@Override
			protected Context initialize() {
				String configLocation = DEFAULT_APPLICATION_CONTEXT;
				if (System.getProperty(APPLICATION_CONTEXT_PROPERTY) != null) {
					configLocation = System.getProperty(APPLICATION_CONTEXT_PROPERTY);
				}
				ConfigurableApplicationContext applicationContext = new AnnotationConfigApplicationContext();
				((AnnotationConfigApplicationContext) applicationContext).scan(packageNames);
				Resource resource = new ClassPathResource(configLocation);
				if (resource.exists()) {
					ClassPathXmlApplicationContext parentContext = new ClassPathXmlApplicationContext(configLocation);
					applicationContext.setParent(parentContext);
				}
				applicationContext.refresh();
				return new SpringContext(applicationContext);
			}
		};
	}

	@Override
	public Context getContext() {
		return context.get();
	}

}
