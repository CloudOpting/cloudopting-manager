package eu.cloudopting.events.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The type Resource not found exception.
 *
 * @author Daniel P.
 *         Date: 8/8/13
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    /**
     * Instantiates a new Resource not found exception.
     */
    public ResourceNotFoundException() {
        super();
    }

    /**
     * Instantiates a new Resource not found exception.
     *
     * @param message the message
     */
    public ResourceNotFoundException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new Resource not found exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public ResourceNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new Resource not found exception.
     *
     * @param cause the cause
     */
    public ResourceNotFoundException(final Throwable cause) {
        super(cause);
    }

    /**
     * Instantiates a new Resource not found exception.
     *
     * @param message            the message
     * @param cause              the cause
     * @param enableSuppression  the enable suppression
     * @param writableStackTrace the writable stack trace
     */
    protected ResourceNotFoundException(final String message, final Throwable cause, final boolean enableSuppression,
                                        final boolean writableStackTrace) {
        super(message, cause, enableSuppression,
                writableStackTrace);
    }
}
