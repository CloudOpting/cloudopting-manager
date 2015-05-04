package eu.cloudopting.events.api.events;

import com.google.common.base.Preconditions;
import org.springframework.context.ApplicationEvent;
import eu.cloudopting.events.api.entity.BaseEntity;

/**
 * @param <T> tipul entitatii
 * @author Daniel P.
 */
public final class BeforeEntityDeleteEvent<T extends BaseEntity> extends ApplicationEvent {

    private final Class<T> clazz;
    private final T entity;

    public BeforeEntityDeleteEvent(final Object sourceToSet, final Class<T> clazzToSet, final T entityToSet) {
        super(sourceToSet);

        Preconditions.checkState(clazzToSet != null);
        clazz = clazzToSet;

        Preconditions.checkState(entityToSet != null);
        entity = entityToSet;
    }

    // API

    public Class<T> getClazz() {
        return clazz;
    }

    public T getEntity() {
        return entity;
    }

}