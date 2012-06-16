package be.appify.stereotype.core.beans;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import be.appify.stereotype.core.beans.fields.FieldAccessor;
import be.appify.stereotype.core.beans.fields.FieldModel;
import be.appify.stereotype.core.i18n.Message;
import be.appify.stereotype.core.operation.GenericOperation;
import be.appify.stereotype.core.operation.SpawningOperation;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public final class BeanModel<T> {
	private Map<String, FieldModel<T, ?>> fields;
	private Map<Class<?>, GenericOperation<?>> operations;
	private List<FieldModel<T, ?>> orderedFields;
	public Class<T> type;

	public static final class Builder<T> {
		private Collection<FieldModel<T, ?>> fields;
		private Set<GenericOperation<?>> operations;
		private final Class<T> type;

		public Builder(Class<T> type) {
			Preconditions.checkNotNull(type, "type cannot be null");
			this.type = type;
		}

		public Builder<T> fields(Collection<FieldModel<T, ?>> fields) {
			this.fields = Sets.newHashSet(fields);
			return this;
		}

		public Builder<T> operations(Collection<GenericOperation<?>> operations) {
			this.operations = Sets.newHashSet(operations);
			return this;
		}

		public BeanModel<T> build() {
			Preconditions.checkNotNull(fields, "fields cannot be null");
			Preconditions.checkNotNull(operations, "operations cannot be null");
			BeanModel<T> instance = new BeanModel<T>();
			instance.fields = Maps.newHashMap();
			for (FieldModel<T, ?> field : fields) {
				instance.fields.put(field.getName(), field);
			}
			instance.orderedFields = Lists.newArrayList(instance.fields.values());
			Collections.sort(instance.orderedFields);
			instance.type = type;
			instance.operations = Maps.newHashMap();
			for (GenericOperation<?> operation : this.operations) {
				instance.operations.put(operation.getClass(), operation);
			}
			return instance;
		}
	}

	private BeanModel() {
	}

	public List<FieldModel<T, ?>> getFields() {
		return Collections.unmodifiableList(orderedFields);
	}

	public T create(Map<String, Object> fieldValues) {
		BeanConstruction<T> beanConstruction = new BeanConstruction<T>(type);
		for (String propertyName : fieldValues.keySet()) {
			if (!fields.containsKey(propertyName)) {
				throw new IllegalArgumentException("No property <" + propertyName + "> defined on " + type + ".");
			}
			FieldModel<T, ?> fieldModel = fields.get(propertyName);
			FieldAccessor<T, ?> fieldAccessor = fieldModel.getAccessor();
			beanConstruction.addField(fieldAccessor, fieldValues.get(propertyName));
		}
		return beanConstruction.create();
	}

	public Object getValue(T bean, String propertyName) {
		if (!fields.containsKey(propertyName)) {
			throw new IllegalArgumentException("No property <" + propertyName + "> defined on " + type + ".");
		}
		FieldModel<T, ?> fieldModel = fields.get(propertyName);
		FieldAccessor<T, ?> accessor = fieldModel.getAccessor();
		if (!accessor.canGet()) {
			throw new IllegalArgumentException("Property <" + propertyName + "> on " + type + " is not readable.");
		}
		return fieldModel.getAccessor().get(bean);
	}

	public <F> void setValue(T bean, String propertyName, F value) {
		if (!fields.containsKey(propertyName)) {
			throw new IllegalArgumentException("No property <" + propertyName + "> defined on " + type + ".");
		}
		@SuppressWarnings("unchecked")
		FieldModel<T, F> fieldModel = (FieldModel<T, F>) fields.get(propertyName);
		FieldAccessor<T, F> accessor = fieldModel.getAccessor();
		if (!accessor.canUpdate()) {
			throw new IllegalArgumentException("Property <" + propertyName + "> on " + type + " is not writable.");
		}
		fieldModel.getAccessor().update(bean, value);
	}

	public <F> void validate(T bean, String propertyName, F value) {
		if (!fields.containsKey(propertyName)) {
			throw new IllegalArgumentException("No property <" + propertyName + "> defined on " + type + ".");
		}
		@SuppressWarnings("unchecked")
		FieldModel<T, F> fieldModel = (FieldModel<T, F>) fields.get(propertyName);
		FieldAccessor<T, F> accessor = fieldModel.getAccessor();
		if (!accessor.canUpdate()) {
			throw new IllegalArgumentException("Property <" + propertyName + "> on " + type + " is not writable.");
		}
		Message<String> fieldName = Message.create(bean.getClass(), propertyName);
		fieldModel.getAccessor().getValidator().validate(value, fieldName);
	}

	public <O extends SpawningOperation<?>> SpawningOperation<T> newOperation(Class<O> operationClass) {
		GenericOperation<?> operation = operations.get(operationClass);
		return (SpawningOperation<T>) operation.createNew(this);
	}

	public Class<T> getType() {
		return type;
	}
}
