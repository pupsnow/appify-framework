package be.appify.stereotype.core.beans.fields;

@SuppressWarnings("serial")
public class BeanAccessException extends RuntimeException {

	public BeanAccessException(String message, Throwable cause) {
		super(message, cause);
	}

	public BeanAccessException(String message) {
		super(message);
	}

}
