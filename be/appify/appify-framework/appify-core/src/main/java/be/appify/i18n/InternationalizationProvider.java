package be.appify.i18n;


public interface InternationalizationProvider {
    String getMessage(String key, Object... arguments);

    String getMessage(String key, boolean mustExist, Object... arguments) throws NoSuchMessageException;
}
