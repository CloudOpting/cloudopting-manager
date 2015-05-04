package eu.cloudopting.events.api.preconditions;

import eu.cloudopting.events.api.exceptions.BadRequestException;
import eu.cloudopting.events.api.exceptions.EntityNotFoundException;

/** @author Daniel P. */
public final class ServicePreconditions {

    private ServicePreconditions() {
        throw new AssertionError();
    }

    // API

    /**
     * Ensures that the entity reference passed as a parameter to the calling method is not null.
     *
     * @param <T>    entity type
     * @param entity an object reference
     * @return the non-null reference that was validated
     * @throws eu.cloudopting.events.api.exceptions.EntityNotFoundException if {@code entity} is null
     */
    public static <T> T checkEntityExists(final T entity) {
        if (entity == null) {
            throw new EntityNotFoundException();
        }
        return entity;
    }

    // TODO: eliminat? nu stiu daca e nevoie de ele
    public static void checkEntityExists(final boolean entityExists) {
        if (!entityExists) {
            throw new EntityNotFoundException();
        }
    }

    public static void checkOKArgument(final boolean okArgument) {
        if (!okArgument) {
            throw new BadRequestException();
        }
    }

}

