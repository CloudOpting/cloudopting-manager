package eu.cloudopting.events.api.controller;

import org.springframework.web.util.UriComponentsBuilder;
import eu.cloudopting.events.api.entity.BaseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Interfata specializata pentru operatii de sortare.
 *
 * @param <T> entitatea pentru se efectueaza operatiil
 */
public interface SortingController<T extends BaseEntity> {
    /**
     * Find all paginated and sorted.
     *
     * @param page       the page
     * @param size       the size
     * @param sortBy     the sort by
     * @param sortOrder  the sort order
     * @param uriBuilder the uri builder
     * @param response   the response
     * @return the list
     */
    List<T> findAllPaginatedAndSorted(final int page, final int size, final String sortBy, final String sortOrder,
                                      final UriComponentsBuilder uriBuilder, final HttpServletResponse response);

    /**
     * Find all paginated.
     *
     * @param page       the page
     * @param size       the size
     * @param uriBuilder the uri builder
     * @param response   the response
     * @return the list
     */
    List<T> findAllPaginated(final int page, final int size, final UriComponentsBuilder uriBuilder,
                             final HttpServletResponse response);

    /**
     * Find all sorted.
     *
     * @param sortBy    the sort by
     * @param sortOrder the sort order
     * @return the list
     */
    List<T> findAllSorted(final String sortBy, final String sortOrder);

    /**
     * Find all.
     *
     * @param request    the request
     * @param uriBuilder the uri builder
     * @param response   the response
     * @return the list
     */
    List<T> findAll(final HttpServletRequest request, final UriComponentsBuilder uriBuilder,
                    final HttpServletResponse response);
}
