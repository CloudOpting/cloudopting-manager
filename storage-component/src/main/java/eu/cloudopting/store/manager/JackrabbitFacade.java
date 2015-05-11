package eu.cloudopting.store.manager;

import eu.cloudopting.store.jackrabbit.JackRabbitStoreRequest;
import eu.cloudopting.store.jackrabbit.JackrabbitStoreResult;

/**
 * The bean to manage Jackrabbit operations
 */
public interface JackrabbitFacade {

    public JackrabbitStoreResult store(JackRabbitStoreRequest jackRabbitStoreRequest);

}
