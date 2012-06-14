package be.appify.stereotype.core.beans;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import be.appify.stereotype.core.Advice;
import be.appify.stereotype.core.beans.fields.FieldAccess;
import be.appify.stereotype.core.beans.fields.FieldModel;
import be.appify.stereotype.core.beans.fields.FieldType;
import be.appify.stereotype.core.beans.fields.IllegalAnnotationException;
import be.appify.stereotype.core.beans.validation.MaxLengthValidator;
import be.appify.stereotype.core.beans.validation.MinLengthValidator;
import be.appify.stereotype.core.beans.validation.RequiredValidator;
import be.appify.stereotype.core.beans.validation.Validator;
import be.appify.stereotype.core.beans.validation.ValidatorFactory;

import com.google.common.collect.Sets;

public class BeanAnalyzerTest {
	private BeanAnalyzer beanAnalyzer;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void before() {
		beanAnalyzer = new BeanAnalyzer(new ValidatorFactory(Sets.<Validator<?>> newHashSet(
				new MaxLengthValidator(),
				new MinLengthValidator(),
				new RequiredValidator())));
	}

	@Test
	public void analyzeShouldReturnBeanModelForSimpleBean() {
		BeanModel<Advice> model = beanAnalyzer.analyze(Advice.class);

		Collection<FieldModel<Advice, ?>> fields = model.getFields();
		assertEquals(4, fields.size());

		for (FieldModel<Advice, ?> field : fields) {
			if ("name".equals(field.getName())) {
				assertEquals(FieldAccess.READ_ONLY, field.getFieldAccess());
				assertEquals(String.class, field.getFieldClass());
				assertEquals(FieldType.TEXT, field.getFieldType());
			} else if ("description".equals(field.getName())) {
				assertEquals(FieldAccess.READ_WRITE, field.getFieldAccess());
				assertEquals(String.class, field.getFieldClass());
				assertEquals(FieldType.TEXT, field.getFieldType());
			} else if ("timesImplemented".equals(field.getName())) {
				assertEquals(FieldAccess.DERIVED, field.getFieldAccess());
				assertEquals(int.class, field.getFieldClass());
				assertEquals(FieldType.NUMBER, field.getFieldType());
			} else if ("inaccessible".equals(field.getName())) {
				assertEquals(FieldAccess.WRITE_ONLY, field.getFieldAccess());
				assertEquals(Boolean.class, field.getFieldClass());
				assertEquals(FieldType.BOOLEAN, field.getFieldType());
			}
		}
	}

	@Test
	public void analyzeShouldThrowExceptionForMissingConstructorAnnotation() {
		thrown.expect(IllegalAnnotationException.class);
		thrown.expectMessage("Missing no-arg constructor or constructor with field-annotated parameters on "
				+ AdviceMissingConstructorAnnotation.class + ".");

		beanAnalyzer.analyze(AdviceMissingConstructorAnnotation.class);
	}

	@Test
	public void analyzeShouldThrowExceptionForIncompleteConstructorAnnotations() {
		thrown.expect(IllegalAnnotationException.class);
		thrown.expectMessage("Not all constructor parameters are field-annotated on public "
				+ AdviceWithIncompleteConstructorAnnotations.class.getName() + "(java.lang.String,java.lang.String).");

		beanAnalyzer.analyze(AdviceWithIncompleteConstructorAnnotations.class);
	}

	@Test
	public void analyzeShouldThrowExceptionForConflictingAnnotations() {
		thrown.expect(IllegalAnnotationException.class);
		thrown.expectMessage("Conflicting annotations found for property <name> on "
				+ AdviceWithConflictingAnnotations.class +
				". Field annotations found on constructor and on public java.lang.String " +
				AdviceWithConflictingAnnotations.class.getName() + ".getName().");

		beanAnalyzer.analyze(AdviceWithConflictingAnnotations.class);
	}

	@Test
	public void analyzeShouldThrowExceptionForMultipleFieldAnnotations() {
		thrown.expect(IllegalAnnotationException.class);
		thrown.expectMessage("Multiple field annotations found on public void "
				+ AdviceWithMultipleFieldAnnotations.class.getName()
				+ ".setDescription(java.lang.String): <@ShortTextType, @LongTextType>.");

		beanAnalyzer.analyze(AdviceWithMultipleFieldAnnotations.class);
	}

	@Test
	public void analyzeShouldThrowExceptionForAnnotationOnNonProperty() {
		thrown.expect(IllegalAnnotationException.class);
		thrown.expectMessage("Method public java.lang.String "
				+ AdviceWithAnnotationOnNonProperty.class.getName()
				+ ".nonProperty() does not qualify as a field since it does not have a correct getter or setter signature.");

		beanAnalyzer.analyze(AdviceWithAnnotationOnNonProperty.class);
	}

	@Test
	public void analyzeShouldThrowExceptionForInvalidFieldType() {
		thrown.expect(IllegalAnnotationException.class);
		thrown.expectMessage("Annotation @ShortTextType does not support int. Supported types are: <[class java.lang.String]>.");

		beanAnalyzer.analyze(AdviceWithInvalidFieldType.class);
	}

	@Test
	public void analyzeShouldThrowExceptionForInvalidValidator() {
		thrown.expect(IllegalAnnotationException.class);
		thrown.expectMessage("Annotation @MaxLength does not support int. Supported types are: <[class java.lang.String]>.");

		beanAnalyzer.analyze(AdviceWithInvalidValidator.class);
	}

	@Test
	public void analyzeShouldThrowExceptionForMissingPropertyNameOnConstructor() {
		thrown.expect(IllegalAnnotationException.class);
		thrown.expectMessage("Missing field name value of @ShortTextType on public "
				+ AdviceWithMissingPropertyNameOnConstructor.class.getName() + "(java.lang.String)[0].");

		beanAnalyzer.analyze(AdviceWithMissingPropertyNameOnConstructor.class);
	}
}
