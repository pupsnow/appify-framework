package be.appify.stereotype.core.beans;

import be.appify.stereotype.core.beans.fields.stereotypes.NumberType;
import be.appify.stereotype.core.beans.fields.stereotypes.LongTextType;
import be.appify.stereotype.core.beans.fields.stereotypes.ShortTextType;
import be.appify.stereotype.core.beans.validation.Required;

public class AdviceWithMissingPropertyNameOnConstructor {
	private final String name;
	private String description;
	private int timesImplemented;

	public AdviceWithMissingPropertyNameOnConstructor(@ShortTextType @Required String name) {
		this.name = name;
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
