package be.appify.stereotype.core.beans.validation;

import be.appify.stereotype.core.i18n.Message;

@SuppressWarnings("serial")
public class ValidationException extends RuntimeException {
	private final Message<?> clientMessage;

	public <E extends Enum<E>> ValidationException(E messageKey, Object... arguments) {
		this.clientMessage = Message.create(messageKey, arguments);
	}

	@Override
	public String getMessage() {
		return clientMessage.toString();
	}

	public Message<?> getClientMessage() {
		return clientMessage;
	}

}
