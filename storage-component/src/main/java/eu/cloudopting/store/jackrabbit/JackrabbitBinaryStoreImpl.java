package eu.cloudopting.store.jackrabbit;

import eu.cloudopting.exceptions.StorageGeneralException;
import eu.cloudopting.util.MimeTypeUtils;
import org.apache.jackrabbit.ocm.manager.ObjectContentManager;

import javax.inject.Inject;
import javax.jcr.*;
import java.io.InputStream;
import java.util.Date;

/**
 * Store only binary data
 */
public class JackrabbitBinaryStoreImpl extends AbstractJackrabbitStore implements JackrabbitStore{

    @Inject
    Repository repository;
    @Inject
    Session session;
    @Inject
    ObjectContentManager ocm;

    @Inject
    JackrabbitStore jackrabbitOcmStore;

    @Override
    public JackrabbitStoreResult store(JackrabbitStoreRequest req) {
        InputStream stream = req.getContent();
        String mimeType = "application/octet-stream";
        Node folder;
        Node file;
        JackrabbitStoreResult<JackrabbitStoreRequest> result = new JackrabbitStoreResult<>();
        try {
            folder = session.getRootNode();
            Node binaryFolder;
            try {
                binaryFolder  = folder.getNode("binary");
            }catch (PathNotFoundException pne){
                binaryFolder=  folder.addNode("binary");
            }
             file = binaryFolder.addNode(req.getPath()+"."+req.getExtension(), JackrabbitConstants.NT_FILE);
            Node content = file.addNode(JackrabbitConstants.JCR_CONTENT, JackrabbitConstants.NT_RESOURCE);
            Binary binary = session.getValueFactory().createBinary(stream);
            content.setProperty(JackrabbitConstants.JCR_DATA, binary);
            content.setProperty(JackrabbitConstants.JCR_MIMETYPE, mimeType);
            session.save();
            result.setStoredContent(req);
            result.setBinaryStoredPaht(file.getPath());
        } catch (RepositoryException e) {
            throw new StorageGeneralException(e);
        }

        return result;
    }

    @Override
    public JackrabbitStoreResult retrieve(String path) {
        Node file = null;
        InputStream stream;
        try {
            file = session.getRootNode().getNode("binary/"+path + "/" + JackrabbitConstants.JCR_CONTENT);
            final Binary in = file.getProperty(JackrabbitConstants.JCR_DATA).getBinary();
            stream = in.getStream();
//            String mimeGuess = MimeTypeUtils.mimeUtilDetectMimeType(stream);
        } catch (RepositoryException e) {
            throw new StorageGeneralException(e);
        }
        JackrabbitStoreResult<InputStream> storeResult = new JackrabbitStoreResult<>();
        storeResult.setStoredContent(stream);
        return storeResult;
    }

    @Override
    public JackrabbitStoreResult remove(String path) {
        Node file = null;
        InputStream stream;
        try {
            session.getRootNode().getNode("binary/"+path).remove();
            session.save();
        } catch (RepositoryException e) {
            throw new StorageGeneralException(e);
        }
        JackrabbitStoreResult<InputStream> storeResult = new JackrabbitStoreResult<>();
        storeResult.setStored(true);//set the flag that the operation has succeeded (it is strage that we mark a successful deletion as stored)
        return storeResult;
    }

    @Override
    public JackrabbitStore getBinaryStore() {
        return this;
    }

    @Override
    public JackrabbitStore getOcmStore() {
        return jackrabbitOcmStore;
    }
}
