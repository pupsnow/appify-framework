package be.appify.stereotype.core.beans.fields;

import java.util.Collection;
import java.util.List;

import be.appify.stereotype.core.beans.BeanConstruction;
import be.appify.stereotype.core.beans.validation.CompositeValidator;
import be.appify.stereotype.core.beans.validation.Validator;
import be.appify.stereotype.core.functional.Specification;
import be.appify.stereotype.core.i18n.Message;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class CompositeFieldAccessor<T, F> implements FieldAccessor<T, F> {

	private final List<FieldAccessor<T, F>> accessors;
	private final Validator<? super F> validator;
	private final Specification<FieldAccessor<T, F>> canInitialize;
	private final Specification<FieldAccessor<T, F>> canUpdate;
	private final Specification<FieldAccessor<T, F>> canGet;
	private final Message<?> fieldName;

	private CompositeFieldAccessor(Collection<FieldAccessor<T, F>> accessors) {
		Preconditions.checkNotNull(accessors, "Accessors cannot be null");
		if (accessors.isEmpty()) {
			throw new IllegalArgumentException("Accessors cannot be empty");
		}
		this.accessors = Lists.newArrayList(accessors);
		canInitialize = new Specification<FieldAccessor<T, F>>() {

			public boolean isSatisfiedBy(FieldAccessor<T, F> object) {
				return object.canInitialize();
			}
		};
		canUpdate = new Specification<FieldAccessor<T, F>>() {

			public boolean isSatisfiedBy(FieldAccessor<T, F> object) {
				return object.canUpdate();
			}
		};
		canGet = new Specification<FieldAccessor<T, F>>() {

			public boolean isSatisfiedBy(FieldAccessor<T, F> object) {
				return object.canGet();
			}
		};
		Collection<Validator<? super F>> validators = Sets.newHashSet();
		for (FieldAccessor<T, F> accessor : accessors) {
			validators.add(accessor.getValidator());
		}
		validator = CompositeValidator.create(validators);
		fieldName = accessors.iterator().next().getFieldName();
	}

	public void initialize(BeanConstruction<T> beanConstruction, F value) {
		validator.validate(value, fieldName);
		FieldAccessor<T, F> executingAccessor = null;
		for (FieldAccessor<T, F> accessor : accessors) {
			if (canInitialize.isSatisfiedBy(accessor)) {
				executingAccessor = accessor;
				break;
			}
		}
		if (executingAccessor == null) {
			throw new BeanAccessException("No FieldAccessor found that supports initialize");
		}
		executingAccessor.initialize(beanConstruction, value);
	}

	public void update(T bean, F value) {
		validator.validate(value, fieldName);
		FieldAccessor<T, F> executingAccessor = null;
		for (FieldAccessor<T, F> accessor : accessors) {
			if (canUpdate.isSatisfiedBy(accessor)) {
				executingAccessor = accessor;
				break;
			}
		}
		if (executingAccessor == null) {
			throw new BeanAccessException("No FieldAccessor found that supports update");
		}
		executingAccessor.update(bean, value);
	}

	public F get(T bean) {
		FieldAccessor<T, F> executingAccessor = null;
		for (FieldAccessor<T, F> accessor : accessors) {
			if (canGet.isSatisfiedBy(accessor)) {
				executingAccessor = accessor;
				break;
			}
		}
		if (executingAccessor == null) {
			throw new BeanAccessException("No FieldAccessor found that supports get");
		}
		return executingAccessor.get(bean);
	}

	public static <T, F> FieldAccessor<T, F> create(Collection<FieldAccessor<T, F>> accessors) {
		return new CompositeFieldAccessor<T, F>(accessors);
	}

	public boolean canInitialize() {
		return isSatisfiedByOne(canInitialize);
	}

	public boolean canUpdate() {
		return isSatisfiedByOne(canUpdate);
	}

	public boolean canGet() {
		return isSatisfiedByOne(canGet);
	}

	private boolean isSatisfiedByOne(Specification<FieldAccessor<T, F>> specification) {
		boolean result = false;
		for (FieldAccessor<T, F> accessor : accessors) {
			if (specification.isSatisfiedBy(accessor)) {
				result = true;
				break;
			}
		}
		return result;
	}

	public Validator<? super F> getValidator() {
		return validator;
	}

	public Message<?> getFieldName() {
		return fieldName;
	}

}
