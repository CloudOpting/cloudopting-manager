package eu.cloudopting.store.jackrabbit;

import eu.cloudopting.storagecomponent.StorageComponent;
import eu.cloudopting.store.jackrabbit.JackrabbitStoreRequest;
import eu.cloudopting.store.jackrabbit.JackrabbitStoreResult;

/**
 * The bean to manage Jackrabbit operations
 */
public interface JackrabbitStore extends StorageComponent<JackrabbitStoreResult,JackrabbitStoreRequest> {

    public JackrabbitStoreResult storeOcmAndBinary(JackrabbitStoreRequest req);



}
