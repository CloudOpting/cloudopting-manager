package eu.cloudopting.events.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Daniel P.
 *         Exceptia este prinsa automat de Spring, si se returneaza pe request, un HTTP 404 (Not Found)
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends RuntimeException {
    /**
     * Instantiates a new EntityNotFoundException exception.
     */
    public EntityNotFoundException() {
        super();
    }

    /**
     * Instantiates a new EntityNotFoundException exception.
     *
     * @param message the message
     */
    public EntityNotFoundException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new EntityNotFoundException exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public EntityNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new EntityNotFoundException exception.
     *
     * @param cause the cause
     */
    public EntityNotFoundException(final Throwable cause) {
        super(cause);
    }

    /**
     * Instantiates a new EntityNotFoundException exception.
     *
     * @param message            the message
     * @param cause              the cause
     * @param enableSuppression  the enable suppression
     * @param writableStackTrace the writable stack trace
     */
    protected EntityNotFoundException(final String message, final Throwable cause, final boolean enableSuppression,
                                      final boolean writableStackTrace) {
        super(message, cause, enableSuppression,
                writableStackTrace);
    }
}
