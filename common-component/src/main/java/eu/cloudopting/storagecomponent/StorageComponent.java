package eu.cloudopting.storagecomponent;

/**
 * This interface will be implemented by all classes that use some kind of storage.
 * @author Daniel P.
 */
public interface StorageComponent<Resp extends StoreResult, Req extends StoreRequest> {

    public Resp store(Req context);

    public  Resp retrieve(String path);

}
