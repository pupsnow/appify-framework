package be.appify.stereotype.persistence.jpa;

import javax.persistence.Entity;

import be.appify.stereotype.core.beans.AbstractBean;
import be.appify.stereotype.core.beans.fields.DisplayField;
import be.appify.stereotype.core.beans.fields.Order;
import be.appify.stereotype.core.beans.fields.stereotypes.LongTextType;
import be.appify.stereotype.core.beans.fields.stereotypes.ShortTextType;
import be.appify.stereotype.core.beans.validation.MinLength;
import be.appify.stereotype.core.beans.validation.Required;
import be.appify.stereotype.core.operation.Create;
import be.appify.stereotype.core.operation.FindByID;
import be.appify.stereotype.core.operation.Update;

@Entity
@Create
@FindByID
@Update
public class Advice extends AbstractBean {
	private String name;
	private String description;

	Advice() {
	}

	public Advice(@ShortTextType("name") @Required @DisplayField @Order(1) String name) {
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
}
