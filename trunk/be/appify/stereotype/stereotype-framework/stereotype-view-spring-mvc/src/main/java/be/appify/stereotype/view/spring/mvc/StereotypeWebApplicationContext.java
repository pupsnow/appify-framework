package be.appify.stereotype.view.spring.mvc;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

public class StereotypeWebApplicationContext extends AnnotationConfigWebApplicationContext {
	private static final String APPLICATION_CONTEXT = "stereotype-context.xml";

	public StereotypeWebApplicationContext() {
		super();
		Resource resource = new ClassPathResource(APPLICATION_CONTEXT);
		if (resource.exists()) {
			ClassPathXmlApplicationContext parentContext = new ClassPathXmlApplicationContext(APPLICATION_CONTEXT);
			setParent(parentContext);
		}
	}
}
