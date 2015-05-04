package eu.cloudopting.events.api.events;

import org.springframework.context.ApplicationEvent;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

/**
 * Event that is fired when a paginated search is performed.
 * This event object contains all the information needed to create the URL for the paginated results
 *
 * @param <T> Type of the result that is being handled (commonly Entities).
 */
public final class PaginatedResultsRetrievedEvent<T extends Serializable> extends ApplicationEvent {
    private final UriComponentsBuilder uriBuilder;
    private final HttpServletResponse response;
    private final int page;
    private final int totalPages;
    private final int pageSize;

    public PaginatedResultsRetrievedEvent(final Class<T> clazz, final UriComponentsBuilder uriBuilderToSet,
                                          final HttpServletResponse responseToSet, final int pageToSet,
                                          final int totalPagesToSet, final int pageSizeToSet) {
        super(clazz);

        uriBuilder = uriBuilderToSet;
        response = responseToSet;
        page = pageToSet;
        totalPages = totalPagesToSet;
        pageSize = pageSizeToSet;
    }

    // API

    public UriComponentsBuilder getUriBuilder() {
        return uriBuilder;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public int getPage() {
        return page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getPageSize() {
        return pageSize;
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

