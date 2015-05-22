package eu.cloudopting.storage;

import eu.cloudopting.config.jcr.JcrConfig;
import eu.cloudopting.store.config.StorageConfig;
import eu.cloudopting.store.jackrabbit.JackrabbitStore;
import eu.cloudopting.store.jackrabbit.JackrabbitStoreRequest;
import eu.cloudopting.store.jackrabbit.JackrabbitStoreResult;
import eu.cloudopting.util.MimeTypeUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.DependsOn;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;

/**
 * Tests for storage different implementations.
 */
@ContextConfiguration(classes = {StorageConfig.class, JcrConfig.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class JackrabbitStoreTests extends GenericStorageTests {

    @Inject
    JackrabbitStore jackrabbitOcmStore;

    @Inject
    JackrabbitStore jackrabbitBinaryStore;

    String path;

    @Test
    public void testStoreOcm() {
        JackrabbitStoreRequest req = createRequest();
        path = req.getPath();
        JackrabbitStoreResult result = jackrabbitOcmStore.store(req);
        Assert.assertTrue(result.isStored());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testRetrieveOcm() {
        testStoreOcm();
        JackrabbitStoreResult<JackrabbitStoreRequest> retrieve = jackrabbitOcmStore.retrieve("/ocm_"+path);
        Assert.assertTrue(retrieve.getStoredContent().getPath().equals("/ocm_"+path));
    }

    @Test
    public void testBinaryStore() {
        JackrabbitStoreRequest req = createRequest();
        JackrabbitStoreResult result = jackrabbitBinaryStore.store(req);
        path = req.getPath();
        System.out.println(">>>> path >>>>> : " + path);
        Assert.assertTrue(result.isStored());
    }

    @Test
    public void testBinaryOcmStore() {
        JackrabbitStoreRequest req = createRequest();
        JackrabbitStoreResult result = jackrabbitBinaryStore.storeOcmAndBinary(req);
        path = req.getPath();
        System.out.println(">>>> path >>>>> : " + path);
        Assert.assertTrue(result.isStored());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testBinatyRetrieve() throws IOException {
//        testBinaryStore();
        JackrabbitStoreResult<InputStream> retrieve = jackrabbitBinaryStore.retrieve("1432287780536.zip");
        Assert.assertTrue(retrieve.getStoredContent().available() > 0);
    }

    @Test
    public void testMimeDetection() {
        JackrabbitStoreRequest request = createRequest();
        String mimeType = MimeTypeUtils.detectMymeTypeCustom(request.getContent());
        System.out.println(mimeType);
    }
}
