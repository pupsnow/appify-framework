package be.appify.stereotype.core;

import be.appify.stereotype.core.beans.fields.Order;
import be.appify.stereotype.core.beans.fields.stereotypes.BooleanType;
import be.appify.stereotype.core.beans.fields.stereotypes.LongTextType;
import be.appify.stereotype.core.beans.fields.stereotypes.NumberType;
import be.appify.stereotype.core.beans.fields.stereotypes.ShortTextType;
import be.appify.stereotype.core.beans.validation.MinLength;
import be.appify.stereotype.core.beans.validation.Required;

public class Advice {
	private final String name;
	private String description;
	private int timesImplemented;

	public Advice(@ShortTextType("name") @Required @Order(1) String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@LongTextType
	@MinLength(10)
	@Order(2)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@NumberType
	@Order(3)
	public int getTimesImplemented() {
		return timesImplemented;
	}

	public void implement() {
		this.timesImplemented++;
	}

	@BooleanType
	@Order(4)
	public void setInaccessible(Boolean inaccessible) {

	}
}
