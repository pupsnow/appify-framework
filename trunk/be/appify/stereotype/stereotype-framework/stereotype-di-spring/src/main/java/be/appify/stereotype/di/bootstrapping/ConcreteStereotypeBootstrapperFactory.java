package be.appify.stereotype.di.bootstrapping;

import be.appify.stereotype.core.bootstrapping.StereotypeBootstrapper;
import be.appify.stereotype.core.bootstrapping.StereotypeBootstrapperFactory;
import be.appify.stereotype.di.spring.SpringStereotypeBootstrapper;

public class ConcreteStereotypeBootstrapperFactory implements StereotypeBootstrapperFactory {

	public StereotypeBootstrapper createBootstrapper(String... packageNames) {
		return new SpringStereotypeBootstrapper(packageNames);
	}

}
