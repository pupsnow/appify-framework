package be.appify.stereotype.core.beans;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import be.appify.stereotype.core.Advice;
import be.appify.stereotype.core.beans.fields.FieldModel;
import be.appify.stereotype.core.beans.validation.MaxLengthValidator;
import be.appify.stereotype.core.beans.validation.MinLengthValidator;
import be.appify.stereotype.core.beans.validation.RequiredValidator;
import be.appify.stereotype.core.beans.validation.ValidationException;
import be.appify.stereotype.core.beans.validation.Validator;
import be.appify.stereotype.core.beans.validation.ValidatorFactory;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class BeanModelTest {
	private BeanModel<Advice> beanModel;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void before() {
		beanModel = new BeanAnalyzer(new ValidatorFactory(Sets.<Validator<?>> newHashSet(
				new MaxLengthValidator(),
				new MinLengthValidator(),
				new RequiredValidator()))).analyze(Advice.class);
	}

	@Test
	public void getFieldsShouldReturnFieldsInOrder() {
		List<FieldModel<Advice, ?>> fields = beanModel.getFields();
		assertEquals(4, fields.size());
		assertEquals("name", fields.get(0).getName());
		assertEquals("description", fields.get(1).getName());
		assertEquals("timesImplemented", fields.get(2).getName());
		assertEquals("inaccessible", fields.get(3).getName());
	}

	@Test
	public void createShouldReturnNewlyCreatedObject() {
		Map<String, Object> fieldValues = Maps.newHashMap();
		fieldValues.put("name", "The Name");
		fieldValues.put("description", "The Description");
		Advice advice = beanModel.create(fieldValues);

		assertNotNull(advice);
		assertEquals("The Name", advice.getName());
		assertEquals("The Description", advice.getDescription());
	}

	@Test
	public void createShouldThrowExceptionForMissingProperty() {
		thrown.expect(IllegalStateException.class);
		thrown.expectMessage("No matching constructor found on " + Advice.class + " for parameters <{}>.");

		Map<String, Object> fieldValues = Maps.newHashMap();
		fieldValues.put("description", "The Description");
		beanModel.create(fieldValues);
	}

	@Test
	public void createShouldThrowExceptionForUnknownProperty() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("No property <descraption> defined on " + Advice.class + ".");

		Map<String, Object> fieldValues = Maps.newHashMap();
		fieldValues.put("name", "The Name");
		fieldValues.put("descraption", "The Description");
		beanModel.create(fieldValues);
	}

	@Test
	public void createShouldThrowExceptionWhenValidationFails() {
		thrown.expect(ValidationException.class);
		thrown.expectMessage("The value for \"name\" exceeds maximum length of 255.");

		Map<String, Object> fieldValues = Maps.newHashMap();
		fieldValues
				.put("name", "This value is so long that it cannot get past validation." +
						" Well, it has to be longer than this. 255 characters really is a lot." +
						" How much more crap do I have to come up with." +
						" Oh look, they're betting on the soccer results here." +
						" The funny thing is, the guy who knows the least about soccer will win.");
		fieldValues.put("description", "The Description");
		beanModel.create(fieldValues);
	}

	@Test
	public void getValueShouldReturnValueForProperty() {
		Map<String, Object> fieldValues = Maps.newHashMap();
		fieldValues.put("name", "The Name");
		fieldValues.put("description", "The Description");
		Advice advice = beanModel.create(fieldValues);

		assertEquals("The Name", beanModel.getValue(advice, "name"));
		assertEquals("The Description", beanModel.getValue(advice, "description"));
	}

	@Test
	public void getValueShouldThrowExceptionForUnknownProperty() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("No property <nome> defined on " + Advice.class + ".");

		Map<String, Object> fieldValues = Maps.newHashMap();
		fieldValues.put("name", "The Name");
		fieldValues.put("description", "The Description");
		Advice advice = beanModel.create(fieldValues);

		beanModel.getValue(advice, "nome");
	}

	@Test
	public void getValueShouldThrowExceptionForInaccessibleProperty() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Property <inaccessible> on " + Advice.class + " is not readable.");

		Map<String, Object> fieldValues = Maps.newHashMap();
		fieldValues.put("name", "The Name");
		fieldValues.put("description", "The Description");
		Advice advice = beanModel.create(fieldValues);

		beanModel.getValue(advice, "inaccessible");
	}

	@Test
	public void setValueShouldSetValueForProperty() {
		Map<String, Object> fieldValues = Maps.newHashMap();
		fieldValues.put("name", "The Name");
		fieldValues.put("description", "The Description");
		Advice advice = beanModel.create(fieldValues);

		beanModel.setValue(advice, "description", "Another Description");
		assertEquals("Another Description", advice.getDescription());
	}

	@Test
	public void setValueShouldThrowExceptionWhenValidationFails() {
		thrown.expect(ValidationException.class);
		thrown.expectMessage("The value for \"description\" is too short for minimum length of 10.");

		Map<String, Object> fieldValues = Maps.newHashMap();
		fieldValues.put("name", "The Name");
		fieldValues.put("description", "The Description");
		Advice advice = beanModel.create(fieldValues);

		beanModel.setValue(advice, "description", "Too short");
	}

	@Test
	public void setValueShouldThrowExceptionForUnknownProperty() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("No property <descraption> defined on " + Advice.class + ".");

		Map<String, Object> fieldValues = Maps.newHashMap();
		fieldValues.put("name", "The Name");
		fieldValues.put("description", "The Description");
		Advice advice = beanModel.create(fieldValues);

		beanModel.setValue(advice, "descraption", "Another Description");
	}

	@Test
	public void setValueShouldThrowExceptionForInaccessibleProperty() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Property <name> on " + Advice.class + " is not writable.");

		Map<String, Object> fieldValues = Maps.newHashMap();
		fieldValues.put("name", "The Name");
		fieldValues.put("description", "The Description");
		Advice advice = beanModel.create(fieldValues);

		beanModel.setValue(advice, "name", "Another Name");
	}
}
