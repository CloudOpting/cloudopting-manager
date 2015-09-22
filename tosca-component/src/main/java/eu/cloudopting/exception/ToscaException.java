package eu.cloudopting.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
public class ToscaException extends RuntimeException {
	
	private static final long serialVersionUID = 9144532704007000645L;

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
