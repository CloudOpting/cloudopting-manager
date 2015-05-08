package eu.cloudopting.events.api.entity;

import java.io.Serializable;

/**
 * The interface Base entity.
 * @author Daniel P.
 *         Date: 8/8/13
 */
public interface BaseEntity extends Serializable {

    /**
     * Gets id.
     *
     * @return the id
     */
    Long getId();

    /**
     * Sets id.
     *
     * @param id the id
     */
    void setId(final Long id);
}
