package eu.cloudopting.storage;

import eu.cloudopting.config.jcr.JcrConfig;
import eu.cloudopting.store.StoreService;
import eu.cloudopting.store.config.StorageConfig;
import eu.cloudopting.store.jackrabbit.JackrabbitStore;
import eu.cloudopting.store.jackrabbit.JackrabbitStoreRequest;
import eu.cloudopting.store.jackrabbit.JackrabbitStoreResult;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import java.io.InputStream;

/**
 * Created by danielpo on 11/09/2015.
 */
@ContextConfiguration(classes = {StorageConfig.class, JcrConfig.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class StoreServiceTests extends GenericStorageTests{

    @Inject
    StoreService storeService;

    @Test
    public void testJackrabbitOcmStore() throws Exception {
        JackrabbitStoreRequest req = createRequest();
        JackrabbitStoreResult res = storeService.storeBinary(req);
        System.out.println(res.toString());
    }

    @Test
    public void testRetrieve() throws Exception {
        JackrabbitStoreResult<InputStream> is = storeService.retrieve("1442231820882.zip");
        Assert.assertTrue(is.getStoredContent().available() > 0);
    }
}
