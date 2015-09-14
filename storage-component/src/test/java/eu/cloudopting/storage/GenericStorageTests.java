package eu.cloudopting.storage;

import eu.cloudopting.store.jackrabbit.JackrabbitStoreRequest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;

/**
 * Base class for storage tests.
 * @author Daniel P.
 */
public class GenericStorageTests {

    JackrabbitStoreRequest createRequest() {
        JackrabbitStoreRequest storeRequest = new JackrabbitStoreRequest();
        JackrabbitStoreRequest request = new JackrabbitStoreRequest();
        try {
            request.setContent(new FileInputStream("d:/tmp/test.zip"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Long fileName = new Date().getTime();
        System.out.println(fileName);
        request.setPath(String.valueOf(fileName));
        request.setPubDate(new Date());
        request.setTitle("My Title Document 1234");
        request.setExtension("zip");
        return request;
    }

}
