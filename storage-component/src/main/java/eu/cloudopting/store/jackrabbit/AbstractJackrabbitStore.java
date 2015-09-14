package eu.cloudopting.store.jackrabbit;

import eu.cloudopting.util.StreamUtil;
import eu.cloudopting.util.WontCloseBufferedInputStream;
import org.apache.jackrabbit.ocm.manager.ObjectContentManager;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by danielpo on 15/05/2015.
 */
public abstract class AbstractJackrabbitStore implements JackrabbitStore {


    @Override
    public JackrabbitStoreResult storeOcmAndBinary(JackrabbitStoreRequest req) {
        WontCloseBufferedInputStream wontCloseBufferedInputStream = new WontCloseBufferedInputStream(req.getContent());
        req.setContent(wontCloseBufferedInputStream);
        JackrabbitStoreResult resultBinary = getBinaryStore().store(req);
        JackrabbitStoreResult resultOcm = getOcmStore().store(req);
        wontCloseBufferedInputStream.reallyClose();
        JackrabbitStoreResult result = new JackrabbitStoreResult();
        result.setStored(true);
        result.setSentRequest(req);
        return result;
    }


    public abstract JackrabbitStore getBinaryStore();

    public abstract JackrabbitStore getOcmStore();

}
