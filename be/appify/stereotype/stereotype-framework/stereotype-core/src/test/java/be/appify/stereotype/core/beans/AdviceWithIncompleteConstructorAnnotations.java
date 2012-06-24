package be.appify.stereotype.core.beans;

import be.appify.stereotype.core.beans.fields.stereotypes.LongTextType;
import be.appify.stereotype.core.beans.fields.stereotypes.NumberType;
import be.appify.stereotype.core.beans.fields.stereotypes.ShortTextType;
import be.appify.stereotype.core.beans.validation.Required;

public class AdviceWithIncompleteConstructorAnnotations extends Bean {
	private final String name;
	private String description;
	private int timesImplemented;

	public AdviceWithIncompleteConstructorAnnotations(@ShortTextType("name") @Required String name,
			String description) {
		this.name = name;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	@LongTextType
	public void setDescription(String description) {
		this.description = description;
	}

	@NumberType
	public int getTimesImplemented() {
		return timesImplemented;
	}

	public void implement() {
		this.timesImplemented++;
	}
}
