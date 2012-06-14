package be.appify.stereotype.di.spring;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.junit.Before;
import org.junit.Test;

import be.appify.stereotype.core.beans.validation.AbstractValidator;
import be.appify.stereotype.core.beans.validation.Required;
import be.appify.stereotype.core.beans.validation.RequiredValidator;
import be.appify.stereotype.core.beans.validation.ValidationException;
import be.appify.stereotype.core.beans.validation.Validator;
import be.appify.stereotype.core.beans.validation.ValidatorClass;
import be.appify.stereotype.core.beans.validation.ValidatorFactory;
import be.appify.stereotype.core.bootstrapping.Context;
import be.appify.stereotype.core.bootstrapping.StereotypeBootstrapper;
import be.appify.stereotype.core.i18n.Message;

public class SpringStereotypeBootstrapperTest {

	@Before
	public void before() {
		System.clearProperty("be.appify.stereotype.di.spring.context");
	}

	@Test
	public void shouldInitializeWithoutXMLContext() throws ReflectiveOperationException {
		StereotypeBootstrapper bootstrapper = StereotypeBootstrapper.createBootstrapper();

		Context context = bootstrapper.getContext();
		assertNotNull(context);

		ValidatorFactory validatorFactory = context.getUniqueBean(ValidatorFactory.class);
		assertNotNull(validatorFactory);
		Validator<?> validator = validatorFactory.createValidator(RequiredStub.of(Required.class));
		assertNotNull(validator);
		assertTrue(validator instanceof RequiredValidator);
	}

	@Test
	public void shouldInitializeWithXMLContext() throws ReflectiveOperationException {
		System.setProperty("be.appify.stereotype.di.spring.context", "test-context.xml");
		StereotypeBootstrapper bootstrapper = StereotypeBootstrapper.createBootstrapper();

		Context context = bootstrapper.getContext();
		assertNotNull(context);

		ValidatorFactory validatorFactory = context.getUniqueBean(ValidatorFactory.class);
		assertNotNull(validatorFactory);
		Validator<?> validator = validatorFactory.createValidator(TestAnnotationStub.of(TestAnnotation.class));
		assertNotNull(validator);
		assertTrue(validator instanceof TestValidator);
	}

	static class RequiredStub implements InvocationHandler {
		@SuppressWarnings("unchecked")
		public static <A extends Annotation> A of(Class<A> annotation) {
			return (A) Proxy.newProxyInstance(annotation.getClassLoader(),
					new Class[] { annotation }, new RequiredStub());
		}

		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			return Required.class;
		}

	}

	static class TestAnnotationStub implements InvocationHandler {
		@SuppressWarnings("unchecked")
		public static <A extends Annotation> A of(Class<A> annotation) {
			return (A) Proxy.newProxyInstance(annotation.getClassLoader(),
					new Class[] { annotation }, new TestAnnotationStub());
		}

		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			return TestAnnotation.class;
		}

	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.PARAMETER })
	@ValidatorClass(TestValidator.class)
	static @interface TestAnnotation {
	}

	static class TestValidator extends AbstractValidator<TestAnnotation, Object> {

		public TestValidator() {
			super(TestAnnotation.class, Object.class);
		}

		public void validate(Object value, Message<?> fieldName) throws ValidationException {
		}

		@Override
		protected Validator<Object> createNewInternal(TestAnnotation annotation) {
			return this;
		}

	}

}
