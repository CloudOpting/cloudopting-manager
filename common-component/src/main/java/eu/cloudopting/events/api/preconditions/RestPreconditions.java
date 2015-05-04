package eu.cloudopting.events.api.preconditions;

import eu.cloudopting.events.api.exceptions.ConflictException;
import eu.cloudopting.events.api.exceptions.ForbiddenException;
import eu.cloudopting.events.api.exceptions.ResourceNotFoundException;

/**
 * @author Daniel P.
 *         Date: 8/8/13
 */
public final class RestPreconditions {
    /** Private contructor. */
    private RestPreconditions() {
        throw new AssertionError();
    }

    // API

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * @param <T>       the object reference type
     * @param reference an object reference
     * @return the non-null reference that was validated
     * @throws eu.cloudopting.events.api.exceptions.ResourceNotFoundException if {@code reference} is null
     */
    public static <T> T checkNotNull(final T reference) {
        if (reference == null) {
            throw new ResourceNotFoundException();
        }
        return reference;
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * @param <T>       the object reference type
     * @param reference an object reference
     * @return the non-null reference that was validated
     * @throws eu.cloudopting.events.api.exceptions.ConflictException if {@code reference} is null
     */
    public static <T> T checkRequestElementNotNull(final T reference) {
        if (reference == null) {
            throw new ConflictException();
        }
        return reference;
    }

    /**
     * Ensures the truth of an expression.
     *
     * @param expression a boolean expression
     */
    public static void checkRequestState(final boolean expression) {
        if (!expression) {
            throw new ConflictException();
        }
    }

    /**
     * Check if some value was found, otherwise throw exception.
     *
     * @param expression has value true if found, otherwise false
     * @throws eu.cloudopting.events.api.exceptions.ResourceNotFoundException if expression is false, means value not found.
     */
    public static void checkFound(final boolean expression) {
        if (!expression) {
            throw new ResourceNotFoundException();
        }
    }

    /**
     * Check if some value was found, otherwise throw exception.
     * has value true if found, otherwise false
     *
     * @param <T>      the resource type
     * @param resource the evaluated value
     * @return the validated resource
     * @throws eu.cloudopting.events.api.exceptions.ResourceNotFoundException if expression is false, means value not found.
     */
    public static <T> T checkFound(final T resource) {
        if (resource == null) {
            throw new ResourceNotFoundException();
        }

        return resource;
    }

    /**
     * Check if some value was found, otherwise throw exception.
     *
     * @param expression has value true if found, otherwise false
     * @throws eu.cloudopting.events.api.exceptions.ForbiddenException if expression is false, means operation not allowed.
     */
    public static void checkAllowed(final boolean expression) {
        if (!expression) {
            throw new ForbiddenException();
        }
    }
}
