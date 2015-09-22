package eu.cloudopting.exception;

public class ToscaException extends RuntimeException {
	   public ToscaException() {
	        super();
	    }

	    public ToscaException(String message) {
	        super(message);
	    }

	    public ToscaException(String message, Throwable cause) {
	        super(message, cause);
	    }

	    public ToscaException(Throwable cause) {
	        super(cause);
	    }

	    protected ToscaException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
	        super(message, cause, enableSuppression, writableStackTrace);
	    }
}
