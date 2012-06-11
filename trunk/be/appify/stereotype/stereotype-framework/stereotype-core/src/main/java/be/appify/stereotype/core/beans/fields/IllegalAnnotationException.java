package be.appify.stereotype.core.beans.fields;

@SuppressWarnings("serial")
public class IllegalAnnotationException extends RuntimeException {

	public IllegalAnnotationException(String message, Throwable cause) {
		super(message, cause);
	}

	public IllegalAnnotationException(String message) {
		super(message);
	}

}
