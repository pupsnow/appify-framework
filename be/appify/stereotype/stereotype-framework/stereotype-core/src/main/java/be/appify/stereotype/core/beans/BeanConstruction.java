package be.appify.stereotype.core.beans;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.Map;

import be.appify.stereotype.core.beans.fields.FieldAccessor;
import be.appify.stereotype.core.functional.Procedure;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class BeanConstruction<T> {

	private final Class<T> type;
	private final Map<FieldAccessor<T, ?>, Object> fields;
	private final Map<Integer, Object> constructorParameters;
	private final Collection<Procedure<T>> setterProcedures;
	private T bean;

	public BeanConstruction(Class<T> type) {
		this.type = type;
		fields = Maps.newHashMap();
		constructorParameters = Maps.newHashMap();
		setterProcedures = Sets.newHashSet();
	}

	public void addField(FieldAccessor<T, ?> fieldAccessor, Object value) {
		fields.put(fieldAccessor, value);
	}

	public T create() {
		for (FieldAccessor<T, ?> fieldAccessor : fields.keySet()) {
			initializeField(fieldAccessor);
		}
		Constructor<?> constructor = findSuitableConstructor();
		createBean(constructor);
		setFields();
		return bean;
	}

	@SuppressWarnings("unchecked")
	private <F> void initializeField(FieldAccessor<T, F> fieldAccessor) {
		F value = (F) fields.get(fieldAccessor);
		fieldAccessor.initialize(this, value);
	}

	private void setFields() {
		for (Procedure<T> procedure : setterProcedures) {
			procedure.execute(bean);
		}
	}

	private void createBean(Constructor<?> constructor) {
		Object[] parameters = new Object[constructorParameters.size()];
		for (Integer index : constructorParameters.keySet()) {
			parameters[index] = constructorParameters.get(index);
		}
		try {
			bean = type.cast(constructor.newInstance(parameters));
		} catch (ReflectiveOperationException e) {
			throw new BeanConstructionException("Failed to invoke constructor " + constructor + " with parameters <"
					+ parameters + ">.", e);
		}
	}

	private Constructor<?> findSuitableConstructor() {
		Constructor<?> result = null;
		for (Constructor<?> constructor : type.getConstructors()) {
			if (constructor.getParameterTypes().length == constructorParameters.size()) {
				boolean matches = true;
				int index = 0;
				for (Class<?> parameterType : constructor.getParameterTypes()) {
					if (!constructorParameters.containsKey(index)) {
						throw new IllegalStateException("Missing constructor parameter " + index + " on " + constructor
								+ ".");
					}
					Object argument = constructorParameters.get(index);
					if (argument != null && !parameterType.isAssignableFrom(argument.getClass())) {
						matches = false;
						break;
					}
				}
				if (matches) {
					result = constructor;
					break;
				}
			}
		}
		if (result == null) {
			throw new IllegalStateException("No matching constructor found on " + type + " for parameters <"
					+ constructorParameters + ">.");
		}
		return result;
	}

	public void addConstructorParameter(int index, Object value) {
		constructorParameters.put(index, value);
	}

	public void addSetterProcedure(Procedure<T> setterProcedure) {
		setterProcedures.add(setterProcedure);
	}
}
