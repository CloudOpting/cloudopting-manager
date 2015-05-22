package eu.cloudopting.storage;

import eu.cloudopting.config.jcr.JcrConfig;
import eu.cloudopting.storagecomponent.StorageComponent;
import eu.cloudopting.store.config.StorageConfig;
import eu.cloudopting.store.jackrabbit.JackrabbitStoreRequest;
import eu.cloudopting.store.jackrabbit.JackrabbitStoreResult;
import eu.cloudopting.util.MimeTypeUtils;
import org.apache.jackrabbit.ocm.manager.ObjectContentManager;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import javax.jcr.*;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Tests for jackrabbit store.
 */
@ContextConfiguration(classes = {StorageConfig.class, JcrConfig.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class JackrabbitStorageTests {
    @Inject
    Repository repository;
    @Inject
    Session session;
    @Inject
    ObjectContentManager ocm;

    /*@Inject
    StorageComponent<JackrabbitStoreResult,JackrabbitStoreRequest> jackrabbitManager;*/


    File file = new File("d:/tmp/test.zip");




 /*   @Before
    public void setUp() throws Exception {
        List<Class> classes = new ArrayList<Class>();
        classes.add(JackRabbitStoreRequest.class); // Call this method for each persistent class

        Mapper mapper = new AnnotationMapperImpl(classes);
        repository = JcrUtils.getRepository("http://cloudopting1.cloudapp.net:8082/server");
        session = repository.login(new SimpleCredentials("admin", "admin".toCharArray()));
        ocm = new ObjectContentManagerImpl(session, mapper);

    }*/

    @Test
    public void testStoreFile() throws FileNotFoundException, RepositoryException {
        InputStream stream = new BufferedInputStream(new FileInputStream(file));
        String mimeType = MimeTypeUtils.mimeUtilDetectMimeType(stream);
        Node folder = session.getRootNode();
        Node file = folder.addNode(String.valueOf(new Date().getTime()) + ".pdf", "nt:file");
        Node content = file.addNode("jcr:content", "nt:resource");
        Binary binary = session.getValueFactory().createBinary(stream);
        content.setProperty("jcr:data", binary);
        content.setProperty("jcr:mimeType", mimeType);
        session.save();
    }


    @Test
    public void testMymeTyperetrieve() throws RepositoryException, IOException {
        Node file = session.getRootNode().getNode("1431431601575/jcr:content");
        final Binary in = file.getProperty("jcr:data").getBinary();
        InputStream stream = in.getStream();
        String mimeGuess = MimeTypeUtils.mimeUtilDetectMimeType(stream);
        System.out.println(mimeGuess);
    }


    @Test
    public void testWriteFile() throws RepositoryException, IOException {
        Node file = session.getRootNode().getNode("Article.pdf/jcr:content");
        final Binary in = file.getProperty("jcr:data").getBinary();
        File f = new File("d:/tmp/from.pdf");
        InputStream stream = in.getStream();
        Files.copy(stream, f.toPath());
        assertTrue(f.exists());
    }

    @Test
    public void testStore() throws FileNotFoundException {
        JackrabbitStoreRequest request = new JackrabbitStoreRequest();
        request.setContent(new FileInputStream("d:/tmp/test.pdf"));
        request.setPath("/mycontent");
        request.setPubDate(new Date());
        request.setTitle("My Title Document");
        ocm.insert(request);
        ocm.save();

           /* Node node = session.getRootNode();
            Node result = JcrUtils.putFile(node.addNode("default"), "My test document", "pdf", new FileInputStream("d:/tmp/test.pdf"));
            System.out.println(result.getName());*/
        System.out.println("saved");
    }

    @Test
    public void testRetrieveDocument() {
        JackrabbitStoreRequest request = (JackrabbitStoreRequest) ocm.getObject("/mycontent");
        assertTrue(request != null);
        System.out.println(request);
    }

    private final Logger log = LoggerFactory.getLogger(JackrabbitStorageTests.class);

    @Test
    public void testLogDebug() {
        log.debug("my debug");
    }

    @Test
    public void testDeleteObject() {
        JackrabbitStoreRequest request = (JackrabbitStoreRequest) ocm.getObject("/mycontent");
        ocm.remove(request);
        ocm.save();
        System.out.println("Removed");
    }

    /*@Test
    public void testJackrabbitManager() throws FileNotFoundException {
        jackrabbitManager.store(createJackrabbitStoreRequest());
    }*/

    private JackrabbitStoreRequest createJackrabbitStoreRequest() throws FileNotFoundException {
        return new JackrabbitStoreRequest("/mypath","mytitle",new Date(),"zip",new BufferedInputStream(new FileInputStream(file)));
    }
}
