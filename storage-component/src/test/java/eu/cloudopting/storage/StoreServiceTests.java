package eu.cloudopting.storage;

import java.io.File;
import java.io.InputStream;
import java.util.regex.Pattern;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import eu.cloudopting.config.jcr.JcrConfig;
import eu.cloudopting.store.StoreService;
import eu.cloudopting.store.config.StorageConfig;
import eu.cloudopting.store.jackrabbit.JackrabbitStoreRequest;
import eu.cloudopting.store.jackrabbit.JackrabbitStoreResult;

/**
 * Created by danielpo on 11/09/2015.
 */
@ContextConfiguration(classes = {StorageConfig.class, JcrConfig.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class StoreServiceTests extends GenericStorageTests{

    @Inject
    StoreService storeService;

    @Test
    public void testJackrabbitBinaryStore() throws Exception {
        JackrabbitStoreRequest req = createRequest();
        JackrabbitStoreResult res = storeService.storeBinary(req);
        System.out.println(res.toString());
    }

    @Test
    public void testRetrieve() throws Exception {
        JackrabbitStoreResult<InputStream> is = storeService.retrieve("1442231820882.zip");
        Assert.assertTrue(is.getStoredContent().available() > 0);
    }
    
    @Test
    public void testSplitPath() throws Exception {
        String path = "a/b/c/d/";
        String splitRegex = Pattern.quote(File.separator);
		String[] splittedFileName = path.split(splitRegex);
		Assert.assertTrue(splittedFileName.length==4);
    }
}
