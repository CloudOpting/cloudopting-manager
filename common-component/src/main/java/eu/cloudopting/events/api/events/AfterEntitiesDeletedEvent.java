package eu.cloudopting.events.api.events;

import com.google.common.base.Preconditions;
import org.springframework.context.ApplicationEvent;
import eu.cloudopting.events.api.entity.BaseEntity;

/**
 * The type After entities deleted event.
 *
 * @param <T> tipul entitatii
 * @author Daniel P.
 */
public final class AfterEntitiesDeletedEvent<T extends BaseEntity> extends ApplicationEvent {
    /** Clasa. */
    private final Class<T> clazz;

    /**
     * Instantiates a new After entities deleted event.
     *
     * @param sourceToSet the source to set
     * @param clazzToSet  the clazz to set
     */
    public AfterEntitiesDeletedEvent(final Object sourceToSet, final Class<T> clazzToSet) {
        super(sourceToSet);

        Preconditions.checkNotNull(clazzToSet);
        clazz = clazzToSet;
    }

    // API

    /**
     * Gets clazz.
     *
     * @return the clazz
     */
    public Class<T> getClazz() {
        return clazz;
    }

}
