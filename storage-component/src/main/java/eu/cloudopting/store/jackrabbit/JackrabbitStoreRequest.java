package eu.cloudopting.store.jackrabbit;

import eu.cloudopting.storagecomponent.StoreRequest;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;

import java.io.InputStream;
import java.util.Date;

/**
 * request object for jackrabbit store
 * @author Daniel P.
 */
@Node
public class JackrabbitStoreRequest implements StoreRequest {

    public JackrabbitStoreRequest() {
    }

    public JackrabbitStoreRequest(String path, String title, Date pubDate, String extension, InputStream content) {
        this.path = path;
        this.title = title;
        this.pubDate = pubDate;
        this.extension = extension;
        this.content = content;
    }

    @Field(path = true)
    String path;

    @Field
    String title;

    @Field
    Date pubDate;

    @Field
    String extension;

    @Field(jcrName = "jcr:data")
    InputStream content;


    boolean storeOcm = true;
    boolean storeBinary = true;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    public InputStream getContent() {
        return content;
    }

    public void setContent(InputStream content) {
        this.content = content;
    }

    public boolean isStoreOcm() {
        return storeOcm;
    }

    public void setStoreOcm(boolean storeOcm) {
        this.storeOcm = storeOcm;
    }

    public boolean isStoreBinary() {
        return storeBinary;
    }

    public void setStoreBinary(boolean storeBinary) {
        this.storeBinary = storeBinary;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}
