package eu.cloudopting.store.jackrabbit;

import eu.cloudopting.storagecomponent.StoreResult;

/**
 * result object for jackrabbit results.
 *
 * @author Daniel P.
 */
public class JackrabbitStoreResult<T> implements StoreResult {

    public T  storedContent;

    boolean stored = true;


    public T getStoredContent() {
        return storedContent;
    }

    public void setStoredContent(T storedContent) {
        this.storedContent = storedContent;
    }

    @Override
    public boolean isStored() {
        return stored;
    }

    @Override
    public void setStored(boolean stored) {
        this.stored = stored;
    }
}
