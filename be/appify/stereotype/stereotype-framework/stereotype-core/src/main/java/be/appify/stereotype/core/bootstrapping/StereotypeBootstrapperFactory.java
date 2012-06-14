package be.appify.stereotype.core.bootstrapping;

public interface StereotypeBootstrapperFactory {
	StereotypeBootstrapper createBootstrapper(String... packageNames);
}
