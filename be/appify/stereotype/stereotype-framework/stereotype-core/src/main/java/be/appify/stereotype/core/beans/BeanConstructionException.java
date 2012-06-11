package be.appify.stereotype.core.beans;

@SuppressWarnings("serial")
public class BeanConstructionException extends RuntimeException {

	public BeanConstructionException(String message, Throwable cause) {
		super(message, cause);
	}

	public BeanConstructionException(String message) {
		super(message);
	}

}
