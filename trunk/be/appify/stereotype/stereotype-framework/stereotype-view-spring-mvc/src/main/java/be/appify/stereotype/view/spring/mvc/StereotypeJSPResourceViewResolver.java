package be.appify.stereotype.view.spring.mvc;

import javax.inject.Named;

import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Named("viewResolver")
public class StereotypeJSPResourceViewResolver extends InternalResourceViewResolver {
	public StereotypeJSPResourceViewResolver() {
		super();
		setPrefix("/WEB-INF/jsp/");
		setSuffix(".jsp");
	}

}
