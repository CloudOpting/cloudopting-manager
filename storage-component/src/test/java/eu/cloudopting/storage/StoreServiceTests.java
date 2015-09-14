package eu.cloudopting.storage;

import eu.cloudopting.config.jcr.JcrConfig;
import eu.cloudopting.store.StoreService;
import eu.cloudopting.store.config.StorageConfig;
import eu.cloudopting.store.jackrabbit.JackrabbitStore;
import eu.cloudopting.store.jackrabbit.JackrabbitStoreRequest;
import eu.cloudopting.store.jackrabbit.JackrabbitStoreResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

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
        JackrabbitStoreResult res = storeService.storeBinaryAndOcm(req);
        System.out.println("Am finalizat");
    }
}
