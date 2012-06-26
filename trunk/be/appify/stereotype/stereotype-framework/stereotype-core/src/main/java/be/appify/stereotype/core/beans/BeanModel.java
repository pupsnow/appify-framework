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
import be.appify.stereotype.core.operation.UndefinedOperationException;
import be.appify.stereotype.core.util.LazyInitializer;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public final class BeanModel<B extends Bean> {
	private Map<String, FieldModel<B, ?>> fields;
	private Map<Class<?>, GenericOperation<?>> operations;
	private List<FieldModel<B, ?>> orderedFields;
	public Class<B> type;
	private final LazyInitializer<FieldModel<B, ?>> displayField = new LazyInitializer<FieldModel<B, ?>>() {
		@Override
		protected FieldModel<B, ?> initialize() {
			for (FieldModel<B, ?> fieldModel : fields.values()) {
				if (fieldModel.isDisplayField()) {
					return fieldModel;
				}
			}
			return null;
		}
	};
	private Message<?> name;
	private Message<?> plural;

	public static final class Builder<T extends Bean> {
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
			instance.name = Message.create(type, "name");
			instance.plural = Message.create(type, "plural");
			instance.operations = Maps.newHashMap();
			for (GenericOperation<?> operation : this.operations) {
				instance.operations.put(operation.getClass(), operation);
			}
			return instance;
		}
	}

	private BeanModel() {
	}

	public List<FieldModel<B, ?>> getFields() {
		return Collections.unmodifiableList(orderedFields);
	}

	public B create(Map<String, Object> fieldValues) {
		BeanConstruction<B> beanConstruction = new BeanConstruction<B>(type);
		for (String propertyName : fieldValues.keySet()) {
			if (!fields.containsKey(propertyName)) {
				throw new IllegalArgumentException("No property <" + propertyName + "> defined on " + type + ".");
			}
			FieldModel<B, ?> fieldModel = fields.get(propertyName);
			FieldAccessor<B, ?> fieldAccessor = fieldModel.getAccessor();
			beanConstruction.addField(fieldAccessor, fieldValues.get(propertyName));
		}
		return beanConstruction.create();
	}

	public Object getValue(B bean, String propertyName) {
		if (!fields.containsKey(propertyName)) {
			throw new IllegalArgumentException("No property <" + propertyName + "> defined on " + type + ".");
		}
		FieldModel<B, ?> fieldModel = fields.get(propertyName);
		FieldAccessor<B, ?> accessor = fieldModel.getAccessor();
		if (!accessor.canGet()) {
			throw new IllegalArgumentException("Property <" + propertyName + "> on " + type + " is not readable.");
		}
		return fieldModel.getAccessor().get(bean);
	}

	public <F> void setValue(B bean, String propertyName, F value) {
		if (!fields.containsKey(propertyName)) {
			throw new IllegalArgumentException("No property <" + propertyName + "> defined on " + type + ".");
		}
		@SuppressWarnings("unchecked")
		FieldModel<B, F> fieldModel = (FieldModel<B, F>) fields.get(propertyName);
		FieldAccessor<B, F> accessor = fieldModel.getAccessor();
		if (!accessor.canUpdate()) {
			throw new IllegalArgumentException("Property <" + propertyName + "> on " + type + " is not writable.");
		}
		fieldModel.getAccessor().update(bean, value);
	}

	public <F> void validate(B bean, String propertyName, F value) {
		if (!fields.containsKey(propertyName)) {
			throw new IllegalArgumentException("No property <" + propertyName + "> defined on " + type + ".");
		}
		@SuppressWarnings("unchecked")
		FieldModel<B, F> fieldModel = (FieldModel<B, F>) fields.get(propertyName);
		FieldAccessor<B, F> accessor = fieldModel.getAccessor();
		if (!accessor.canUpdate()) {
			throw new IllegalArgumentException("Property <" + propertyName + "> on " + type + " is not writable.");
		}
		Message<String> fieldName = Message.create(bean.getClass(), "field." + propertyName);
		fieldModel.getAccessor().getValidator().validate(value, fieldName);
	}

	public <O extends GenericOperation<?>> GenericOperation<B> newOperation(Class<O> operationClass) {
		GenericOperation<?> operation = operations.get(operationClass);
		if (operation == null) {
			throw new UndefinedOperationException("Operation " + operationClass.getSimpleName() + " not defined for "
					+ type.getName());
		}
		return operation.createNew(this);
	}

	public Class<B> getType() {
		return type;
	}

	public Message<?> getName() {
		return name;
	}

	public Message<?> getPlural() {
		return plural;
	}

	public FieldModel<B, ?> getDisplayField() {
		return displayField.get();
	}

	public FieldModel<B, ?> getField(String fieldName) {
		return fields.get(fieldName);
	}
}
