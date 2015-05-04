package eu.cloudopting.events.api.events;

import com.google.common.base.Preconditions;
import org.springframework.context.ApplicationEvent;

import java.io.Serializable;

/**
 * The type After entity create event.
 *
 * @param <T> tipul entitatii
 * @author Daniel P.
 */
public final class AfterEntityCreateEvent<T extends Serializable> extends ApplicationEvent {
    private final Class<T> clazz;
    private final T entity;

    /**
     * Instantiates a new After entity create event.
     *
     * @param sourceToSet the source to set
     * @param clazzToSet  the clazz to set
     * @param entityToSet the entity to set
     */
    public AfterEntityCreateEvent(final Object sourceToSet, final Class<T> clazzToSet, final T entityToSet) {
        super(sourceToSet);

        Preconditions.checkNotNull(clazzToSet);
        clazz = clazzToSet;

        Preconditions.checkNotNull(entityToSet);
        entity = entityToSet;
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

    /**
     * Gets entity.
     *
     * @return the entity
     */
    public T getEntity() {
        return Preconditions.checkNotNull(entity);
    }

}
