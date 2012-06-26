package be.appify.stereotype.demo.model;

import javax.persistence.Entity;

import be.appify.stereotype.core.beans.Bean;
import be.appify.stereotype.core.beans.fields.DisplayField;
import be.appify.stereotype.core.beans.fields.Order;
import be.appify.stereotype.core.beans.fields.stereotypes.LongTextType;
import be.appify.stereotype.core.beans.fields.stereotypes.NumberType;
import be.appify.stereotype.core.beans.fields.stereotypes.ReferenceType;
import be.appify.stereotype.core.beans.fields.stereotypes.ShortTextType;
import be.appify.stereotype.core.beans.validation.Required;
import be.appify.stereotype.core.operation.Create;
import be.appify.stereotype.core.operation.FindAllContaining;
import be.appify.stereotype.core.operation.FindByID;
import be.appify.stereotype.core.operation.Update;

@Entity
@Create
@Update
@FindAllContaining
@FindByID
public class Fact extends Bean {
	private String name;
	private FactType type;
	private Long validity;
	private String comment;

	public Fact(
			@ShortTextType("name") @Required @DisplayField @Order(1) String name,
			@ReferenceType("type") @Required @Order(2) FactType type) {
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public FactType getType() {
		return type;
	}

	public Long getValidity() {
		return validity;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(FactType type) {
		this.type = type;
	}

	@NumberType("validity")
	@Required
	@Order(3)
	public void setValidity(Long validity) {
		this.validity = validity;
	}

	public String getComment() {
		return comment;
	}

	@LongTextType
	@Order(4)
	public void setComment(String comment) {
		this.comment = comment;
	}

}
