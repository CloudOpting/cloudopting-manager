package eu.cloudopting.events.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The type Conflict exception.
 *
 * @author Daniel P.
 *         Date: 8/8/13
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class ConflictException extends RuntimeException {
    /**
     * Instantiates a new Conflict exception.
     */
    public ConflictException() {
        super();
    }

    /**
     * Instantiates a new Conflict exception.
     *
     * @param message the message
     */
    public ConflictException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new Conflict exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public ConflictException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new Conflict exception.
     *
     * @param cause the cause
     */
    public ConflictException(final Throwable cause) {
        super(cause);
    }

    /**
     * Instantiates a new Conflict exception.
     *
     * @param message            the message
     * @param cause              the cause
     * @param enableSuppression  the enable suppression
     * @param writableStackTrace the writable stack trace
     */
    protected ConflictException(final String message, final Throwable cause, final boolean enableSuppression,
                                final boolean writableStackTrace) {
        super(message, cause, enableSuppression,
                writableStackTrace);
    }
}
