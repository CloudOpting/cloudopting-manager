package eu.cloudopting.store.jackrabbit;

import org.apache.jackrabbit.ocm.manager.ObjectContentManager;

import java.io.InputStream;

/**
 * Created by danielpo on 15/05/2015.
 */
public abstract class AbstractJackrabbitStore implements JackrabbitStore{




    @Override
    public JackrabbitStoreResult storeOcmAndBinary(JackrabbitStoreRequest req) {
        getBinaryStore().store(req);
        getOcmStore().store(req);
        return new JackrabbitStoreResult();
    }

    public abstract JackrabbitStore getBinaryStore();
    public abstract JackrabbitStore getOcmStore();

}
