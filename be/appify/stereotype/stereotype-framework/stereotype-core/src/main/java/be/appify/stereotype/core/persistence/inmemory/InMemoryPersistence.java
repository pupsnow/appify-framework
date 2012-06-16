package be.appify.stereotype.core.persistence.inmemory;

import java.util.Map;
import java.util.UUID;

import be.appify.stereotype.core.beans.BeanModel;
import be.appify.stereotype.core.beans.BeanModelRegistry;
import be.appify.stereotype.core.persistence.Persistence;

import com.google.common.collect.Maps;

public class InMemoryPersistence implements Persistence {

	private BeanModelRegistry beanModelRegistry;
	private final Map<Class<?>, Map<UUID, Object>> beansByUUID;
	private final Map<Object, UUID> uuidsByBean;

	public InMemoryPersistence() {
		this(null);
	}

	public InMemoryPersistence(BeanModelRegistry beanModelRegistry) {
		setBeanModelRegistry(beanModelRegistry);
		this.beansByUUID = Maps.newHashMap();
		this.uuidsByBean = Maps.newHashMap();
	}

	public void setBeanModelRegistry(BeanModelRegistry beanModelRegistry) {
		this.beanModelRegistry = beanModelRegistry;
	}

	@Override
	public <T> void save(T bean) {
		@SuppressWarnings("unchecked")
		BeanModel<T> beanModel = (BeanModel<T>) beanModelRegistry.getBeanModel(bean.getClass());
		add(beanModel.getType(), bean);
	}

	private <T> void add(Class<T> beanClass, T bean) {
		Map<UUID, Object> beansForClass = getBeansFor(beanClass);
		UUID uuid = UUID.randomUUID();
		beansForClass.put(uuid, bean);
		uuidsByBean.put(bean, uuid);
	}

	private Map<UUID, Object> getBeansFor(Class<?> beanClass) {
		Map<UUID, Object> beansForClass = beansByUUID.get(beanClass);
		if (beansForClass == null) {
			beansForClass = Maps.newHashMap();
			beansByUUID.put(beanClass, beansForClass);
		}
		return beansForClass;
	}

	@Override
	public <T> T findByID(Class<T> entityClass, UUID id) {
		Map<UUID, Object> beansForClass = getBeansFor(entityClass);
		return entityClass.cast(beansForClass.get(id));
	}

	@Override
	public <T> UUID getID(T bean) {
		return uuidsByBean.get(bean);
	}

}
