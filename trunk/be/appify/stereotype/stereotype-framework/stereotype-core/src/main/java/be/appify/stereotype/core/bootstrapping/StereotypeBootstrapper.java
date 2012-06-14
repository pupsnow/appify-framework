package be.appify.stereotype.core.bootstrapping;

import java.util.List;

import com.google.common.collect.Lists;

public abstract class StereotypeBootstrapper {
	private static final String STEREOTYPE_BOOTSTRAPPER_FACTORY = "be.appify.stereotype.di.bootstrapping.ConcreteStereotypeBootstrapperFactory";
	private static final String STEREOTYPE_PACKAGE = "be.appify.stereotype";

	public static StereotypeBootstrapper createBootstrapper(String... packageNames) {
		try {
			Class<?> bootstrapperClass = Class.forName(STEREOTYPE_BOOTSTRAPPER_FACTORY);
			StereotypeBootstrapperFactory factory = (StereotypeBootstrapperFactory) bootstrapperClass.newInstance();
			List<String> packages = Lists.newArrayList(packageNames);
			packages.add(STEREOTYPE_PACKAGE);
			return factory.createBootstrapper(packages.toArray(new String[packages.size()]));
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(
					"Failed to instantiate "
							+ STEREOTYPE_BOOTSTRAPPER_FACTORY
							+ ". Please add stereotype-di-spring, stereotype-di-weld, stereotype-di-guice or another dependency injection implementation.",
					e);
		}
	}

	public abstract Context getContext();
}
