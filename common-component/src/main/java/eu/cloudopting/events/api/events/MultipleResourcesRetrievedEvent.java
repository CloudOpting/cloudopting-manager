package eu.cloudopting.events.api.events;

import org.springframework.context.ApplicationEvent;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

/**
 * Event that is fired when a single resource was retrieved.
 * This event object contains all the information needed to create the URL for direct access to the resource
 *
 * @param <T> Type of the result that is being handled (commonly Entities).
 */
public final class MultipleResourcesRetrievedEvent<T extends Serializable> extends ApplicationEvent {
    private final UriComponentsBuilder uriBuilder;
    private final HttpServletResponse response;

    public MultipleResourcesRetrievedEvent(final Class<T> clazz, final UriComponentsBuilder uriBuilderToSet,
                                           final HttpServletResponse responseToSet) {
        super(clazz);

        uriBuilder = uriBuilderToSet;
        response = responseToSet;
    }

    //

    public UriComponentsBuilder getUriBuilder() {
        return uriBuilder;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    /**
     * The object on which the Event initially occurred.
     *
     * @return The object on which the Event initially occurred.
     */
    @SuppressWarnings("unchecked")
    public Class<T> getClazz() {
        return (Class<T>) getSource();
    }

}

