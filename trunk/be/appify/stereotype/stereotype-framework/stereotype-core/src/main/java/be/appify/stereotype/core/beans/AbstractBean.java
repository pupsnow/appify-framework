package be.appify.stereotype.core.beans;

import java.util.Objects;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public abstract class AbstractBean {
	@Id
	private final UUID id;

	public AbstractBean() {
		id = UUID.randomUUID();
	}

	public final UUID getID() {
		return id;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null || other.getClass() != this.getClass()) {
			return false;
		}
		return Objects.equals(id, ((AbstractBean) other).id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return this.getClass() + "[" + id + "]";
	}

}
