package eu.cloudopting.store.jackrabbit;

import eu.cloudopting.exceptions.StorageGeneralException;
import eu.cloudopting.util.MimeTypeUtils;
import org.apache.jackrabbit.ocm.manager.ObjectContentManager;

import javax.inject.Inject;
import javax.jcr.*;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;

/**
 * Store OCM data
 */
public class JackrabbitOcmStoreImpl extends AbstractJackrabbitStore implements JackrabbitStore {

    @Inject
    Repository repository;
    @Inject
    Session session;
    @Inject
    ObjectContentManager ocm;

    @Inject
    JackrabbitStore jackrabbitBinaryStor;
    @Override
    public JackrabbitStoreResult store(JackrabbitStoreRequest req) {
        JackrabbitStoreRequest request = new JackrabbitStoreRequest();
        request.setContent(req.getContent());
        request.setPath("/ocm_"+req.getPath());
        request.setPubDate(req.getPubDate());
        request.setTitle(req.getTitle());
        ocm.insert(request);
        ocm.save();
        JackrabbitStoreResult<JackrabbitStoreRequest> result = new JackrabbitStoreResult<>();
        result.setStoredContent(req);
        return result;
    }

    @Override
    public JackrabbitStoreResult retrieve(String path) {
        JackrabbitStoreResult<JackrabbitStoreRequest> storeResult = new JackrabbitStoreResult<>();
        storeResult.setStoredContent((JackrabbitStoreRequest) ocm.getObject(path));
        return storeResult;
    }


    @Override
    public JackrabbitStore getBinaryStore() {
        return jackrabbitBinaryStor;
    }

    @Override
    public JackrabbitStore getOcmStore() {
        return this;
    }
}
