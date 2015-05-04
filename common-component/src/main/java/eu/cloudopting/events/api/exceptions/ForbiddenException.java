package eu.cloudopting.events.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The type Forbidden exception.
 *
 * @author Daniel P.
 *         Date: 8/8/13
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException {
    /**
     * Instantiates a new Forbidden exception.
     */
    public ForbiddenException() {
        super();
    }

    /**
     * Instantiates a new Forbidden exception.
     *
     * @param message the message
     */
    public ForbiddenException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new Forbidden exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public ForbiddenException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new Forbidden exception.
     *
     * @param cause the cause
     */
    public ForbiddenException(final Throwable cause) {
        super(cause);
    }

    /**
     * Instantiates a new Forbidden exception.
     *
     * @param message            the message
     * @param cause              the cause
     * @param enableSuppression  the enable suppression
     * @param writableStackTrace the writable stack trace
     */
    protected ForbiddenException(final String message, final Throwable cause, final boolean enableSuppression,
                                 final boolean writableStackTrace) {
        super(message, cause, enableSuppression,
                writableStackTrace);
    }
}
