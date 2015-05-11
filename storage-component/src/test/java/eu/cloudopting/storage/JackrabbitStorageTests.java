package eu.cloudopting.storage;

import eu.cloudopting.store.jackrabbit.JackRabbitStoreRequest;
import org.apache.jackrabbit.commons.JcrUtils;
import org.apache.jackrabbit.ocm.manager.ObjectContentManager;
import org.apache.jackrabbit.ocm.manager.impl.ObjectContentManagerImpl;
import org.apache.jackrabbit.ocm.mapper.Mapper;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.AnnotationMapperImpl;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;
import org.junit.Before;
import org.junit.Test;

import javax.activation.MimeTypeParameterList;
import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests for jackrabbit store.
 */

public class JackrabbitStorageTests {
    Repository repository;
    Session session;
    ObjectContentManager ocm;

    @Before
    public void setUp() throws Exception {
        List<Class> classes = new ArrayList<Class>();
        classes.add(JackRabbitStoreRequest.class); // Call this method for each persistent class

        Mapper mapper = new AnnotationMapperImpl(classes);
        repository = JcrUtils.getRepository("http://cloudopting1.cloudapp.net:8082/server");
        session = repository.login(new SimpleCredentials("admin", "admin".toCharArray()));
        ocm =  new ObjectContentManagerImpl(session, mapper);

    }

    @Test
    public void testStore() throws FileNotFoundException {
        JackRabbitStoreRequest request = new JackRabbitStoreRequest();
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
    public void testRetrieveDocument(){
        JackRabbitStoreRequest request = (JackRabbitStoreRequest) ocm.getObject("/mycontent");
        assertTrue(request != null);
        System.out.println(request);
    }

    @Test
    public void testDeleteObject(){
        JackRabbitStoreRequest request = (JackRabbitStoreRequest) ocm.getObject("/mycontent");
        ocm.remove(request);
        ocm.save();
        System.out.println("Removed");
    }

    @Node(jcrMixinTypes = "mix:versionable")
    public class File extends Content {
        @Field
        protected byte[] content;

        public byte[] getContent() {
            return content;
        }

        public void setContent(byte[] content) {
            this.content = content;
        }
    }

    @Node(jcrMixinTypes = "mix:versionable")
    public class Content {
        @Field(uuid=true)
        protected String id;
        @Field(path=true)
        protected String path;
        @Field
        protected String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }


}
