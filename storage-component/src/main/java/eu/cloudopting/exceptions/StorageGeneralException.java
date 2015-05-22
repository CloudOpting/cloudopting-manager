package eu.cloudopting.exceptions;

/**
 * General exceptions thrown by storage component
 */
public class StorageGeneralException extends RuntimeException{
    public StorageGeneralException() {
        super();
    }

    public StorageGeneralException(String message) {
        super(message);
    }

    public StorageGeneralException(String message, Throwable cause) {
        super(message, cause);
    }

    public StorageGeneralException(Throwable cause) {
        super(cause);
    }

    protected StorageGeneralException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
