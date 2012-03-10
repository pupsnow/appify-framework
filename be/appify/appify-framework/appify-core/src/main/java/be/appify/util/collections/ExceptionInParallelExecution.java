package be.appify.util.collections;

public class ExceptionInParallelExecution extends RuntimeException {

    private static final long serialVersionUID = -4542673131222201032L;

    public ExceptionInParallelExecution(String message, Throwable cause) {
        super(message, cause);
    }

}
