package be.appify.i18n;

public class NoSuchMessageException extends RuntimeException {

    private static final long serialVersionUID = 8607635115135335694L;

    public NoSuchMessageException(String key) {
        super(format(key));
    }

    private static String format(String key) {
        return String.format("No message found for key " + key);
    }

    public NoSuchMessageException(String key, Throwable cause) {
        super(format(key), cause);
    }

}
