package be.appify.stereotype.core.beans.fields;

import org.apache.commons.lang.builder.EqualsBuilder;

public enum FieldAccess {
	READ_ONLY(true, true, false),
	WRITE_ONLY(false, true, true),
	READ_WRITE(true, true, true),
	WRITE_ONCE(false, true, false),
	DERIVED(true, false, false);

	private boolean readable;
	private boolean writable;
	private boolean updatable;

	private FieldAccess(boolean readable, boolean writable, boolean updatable) {
		this.readable = readable;
		this.writable = writable;
		this.updatable = updatable;
	}

	public FieldAccess or(FieldAccess other) {
		return valueOf(this.readable || other.readable,
				this.writable || other.writable,
				this.updatable || other.updatable);
	}

	public static FieldAccess valueOf(boolean readable, boolean writable, boolean updatable) {
		for (FieldAccess fieldAccess : values()) {
			if (new EqualsBuilder().append(fieldAccess.readable, readable).append(fieldAccess.isWritable(), writable)
					.append(fieldAccess.isUpdatable(), updatable).isEquals()) {
				return fieldAccess;
			}
		}
		return null;
	}

	public boolean isReadable() {
		return readable;
	}

	public boolean isWritable() {
		return writable;
	}

	public boolean isUpdatable() {
		return updatable;
	}

}
