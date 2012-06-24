package be.appify.stereotype.core.beans.fields;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class FieldModel<T, F> implements Comparable<FieldModel<T, F>> {

	private String name;
	private FieldAccess fieldAccess;
	private Class<F> fieldClass;
	private FieldType fieldType;
	private FieldAccessor<T, F> fieldAccessor;
	private int order;
	private boolean displayField;

	public static class Builder<T, F> {
		private String name;
		private FieldAccess fieldAccess;
		private Class<F> fieldClass;
		private FieldType fieldType;
		private FieldAccessor<T, F> fieldAccessor;
		private int order = Integer.MAX_VALUE;
		private boolean displayField = false;

		public Builder<T, F> name(String name) {
			this.name = name;
			return this;
		}

		public Builder<T, F> fieldAccess(FieldAccess fieldAccess) {
			this.fieldAccess = fieldAccess;
			return this;
		}

		public Builder<T, F> fieldClass(Class<F> fieldClass) {
			this.fieldClass = fieldClass;
			return this;
		}

		public Builder<T, F> fieldType(FieldType fieldType) {
			this.fieldType = fieldType;
			return this;
		}

		public Builder<T, F> fieldAccessor(FieldAccessor<T, F> fieldAccessor) {
			this.fieldAccessor = fieldAccessor;
			return this;
		}

		public Builder<T, F> order(int order) {
			this.order = order;
			return this;
		}

		public Builder<T, F> displayField(boolean displayField) {
			this.displayField = displayField;
			return this;
		}

		public FieldModel<T, F> build() {
			Preconditions.checkNotNull(name, "name cannot be null");
			Preconditions.checkNotNull(fieldAccess, "fieldAccess cannot be null");
			Preconditions.checkNotNull(fieldClass, "fieldClass cannot be null");
			Preconditions.checkNotNull(fieldType, "fieldType cannot be null");
			FieldModel<T, F> instance = new FieldModel<T, F>();
			instance.name = name;
			instance.fieldAccess = fieldAccess;
			instance.fieldClass = fieldClass;
			instance.fieldType = fieldType;
			instance.fieldAccessor = fieldAccessor;
			instance.order = order;
			instance.displayField = displayField;
			return instance;
		}
	}

	private FieldModel() {
	}

	public FieldModel<T, F> extend(FieldAccess additionalAccess, FieldAccessor<T, F> fieldAccessor) {
		FieldModel<T, F> extended = new FieldModel<T, F>();
		extended.name = this.name;
		extended.fieldClass = this.fieldClass;
		extended.fieldType = this.fieldType;
		extended.fieldAccess = this.fieldAccess.or(additionalAccess);
		List<FieldAccessor<T, F>> fieldAccessors = Lists.newArrayList();
		fieldAccessors.add(this.fieldAccessor);
		fieldAccessors.add(fieldAccessor);
		extended.fieldAccessor = CompositeFieldAccessor.create(fieldAccessors);
		extended.order = this.order;
		extended.displayField = this.displayField;
		return extended;
	}

	public String getName() {
		return name;
	}

	public FieldAccess getFieldAccess() {
		return fieldAccess;
	}

	public Class<?> getFieldClass() {
		return fieldClass;
	}

	public FieldType getFieldType() {
		return fieldType;
	}

	public FieldAccessor<T, F> getAccessor() {
		return fieldAccessor;
	}

	@Override
	public int compareTo(FieldModel<T, F> other) {
		return Integer.compare(this.order, other.order);
	}

	public boolean isDisplayField() {
		return displayField;
	}

}
