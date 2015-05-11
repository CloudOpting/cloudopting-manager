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
public class JackRabbitStoreRequest implements StoreRequest {

    @Field(path = true)
    String path;

    @Field
    String title;

    @Field
    Date pubDate;

    @Field(jcrName = "jcr:data")
    InputStream content;

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
}
