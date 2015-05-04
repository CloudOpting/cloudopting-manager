package eu.cloudopting.events.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Daniel P.
 *         Exceptia este prinsa automat de Spring, si se returneaza pe request, un HTTP 400 (Bad Request)
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
    /**
     * Instantiates a new BadRequestException exception.
     */
    public BadRequestException() {
        super();
    }

    /**
     * Instantiates a new BadRequestException exception.
     *
     * @param message the message
     */
    public BadRequestException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new BadRequestException exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public BadRequestException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new BadRequestException exception.
     *
     * @param cause the cause
     */
    public BadRequestException(final Throwable cause) {
        super(cause);
    }

    /**
     * Instantiates a new BadRequestException exception.
     *
     * @param message            the message
     * @param cause              the cause
     * @param enableSuppression  the enable suppression
     * @param writableStackTrace the writable stack trace
     */
    protected BadRequestException(final String message, final Throwable cause, final boolean enableSuppression,
                                  final boolean writableStackTrace) {
        super(message, cause, enableSuppression,
                writableStackTrace);
    }
}
