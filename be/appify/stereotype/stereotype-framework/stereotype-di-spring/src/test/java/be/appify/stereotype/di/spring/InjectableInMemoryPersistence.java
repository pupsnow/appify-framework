package be.appify.stereotype.di.spring;

import javax.inject.Named;

import be.appify.stereotype.core.persistence.inmemory.InMemoryPersistence;

@Named
public class InjectableInMemoryPersistence extends InMemoryPersistence {

}
