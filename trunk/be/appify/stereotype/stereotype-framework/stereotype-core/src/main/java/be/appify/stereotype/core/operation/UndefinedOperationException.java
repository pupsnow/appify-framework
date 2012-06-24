package be.appify.stereotype.core.operation;

@SuppressWarnings("serial")
public class UndefinedOperationException extends RuntimeException {

	public UndefinedOperationException(String message) {
		super(message);
	}

}
