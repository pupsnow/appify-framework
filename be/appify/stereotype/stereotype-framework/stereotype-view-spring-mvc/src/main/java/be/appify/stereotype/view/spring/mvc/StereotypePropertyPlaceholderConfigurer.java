package be.appify.stereotype.view.spring.mvc;

import javax.inject.Named;

import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.context.support.StandardServletEnvironment;

@Named
public class StereotypePropertyPlaceholderConfigurer extends PropertySourcesPlaceholderConfigurer {

	public StereotypePropertyPlaceholderConfigurer() {
		setEnvironment(new StandardServletEnvironment());
	}

}
