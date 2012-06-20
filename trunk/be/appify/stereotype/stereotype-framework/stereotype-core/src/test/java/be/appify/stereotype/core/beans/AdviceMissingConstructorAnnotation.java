package be.appify.stereotype.core.beans;

import be.appify.stereotype.core.beans.fields.stereotypes.LongTextType;
import be.appify.stereotype.core.beans.fields.stereotypes.NumberType;

public class AdviceMissingConstructorAnnotation extends AbstractBean {
	private final String name;
	private String description;
	private int timesImplemented;

	public AdviceMissingConstructorAnnotation(String name) {
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
