package eu.cloudopting.store.jackrabbit;

import eu.cloudopting.storagecomponent.StoreResult;

/**
 * result object for jackrabbit results.
 *
 * @author Daniel P.
 */
public class JackrabbitStoreResult<T> implements StoreResult {

    JackrabbitStoreRequest sentRequest;

    public T  storedContent;

    boolean stored = true;

    String binaryStoredPaht;
    String ocmStoredPath;


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

    public JackrabbitStoreRequest getSentRequest() {
        return sentRequest;
    }

    public void setSentRequest(JackrabbitStoreRequest sentRequest) {
        this.sentRequest = sentRequest;
    }

    public String getBinaryStoredPaht() {
        return binaryStoredPaht;
    }

    public void setBinaryStoredPaht(String binaryStoredPaht) {
        this.binaryStoredPaht = binaryStoredPaht;
    }

    public String getOcmStoredPath() {
        return ocmStoredPath;
    }

    public void setOcmStoredPath(String ocmStoredPath) {
        this.ocmStoredPath = ocmStoredPath;
    }
}
