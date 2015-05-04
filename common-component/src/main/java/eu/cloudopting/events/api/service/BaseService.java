package eu.cloudopting.events.api.service;

import org.apache.commons.lang3.tuple.Triple;
import org.springframework.data.domain.Page;
import eu.cloudopting.events.api.constants.ClientOperation;
import eu.cloudopting.events.api.entity.BaseEntity;

import java.util.List;

/**
 * Interfata pentru un serviciu de baza.
 *
 * @param <T> tipul entitatii
 * @author Daniel P.
 */
public interface BaseService<T extends BaseEntity> extends DefaultOperations<T> {
    // search

    /**
     * Search all.
     *
     * @param queryString the query string
     * @return the list
     */
    List<T> searchAll(final String queryString);

    /**
     * Search paginated.
     *
     * @param queryString the query string
     * @param page        the page
     * @param size        the size
     * @return the list
     */
    List<T> searchPaginated(final String queryString, final int page, final int size);

    /**
     * Search paginated.
     *
     * @param page        the page
     * @param size        the size
     * @param constraints the constraints
     * @return the page
     */
    Page<T> searchPaginated(final int page, final int size,
                            final Triple<String, ClientOperation, String>... constraints);

    /**
     * Find all paginated and sorted raw.
     *
     * @param page      the page
     * @param size      the size
     * @param sortBy    the sort by
     * @param sortOrder the sort order
     * @return the page
     */
    Page<T> findAllPaginatedAndSortedRaw(final int page, final int size, final String sortBy, final String sortOrder);

    /**
     * Find all paginated and sorted raw.
     *
     * @param page      the page
     * @param size      the size
     * @param sortBy    the sort by
     * @param sortOrder the sort order
     * @param filterObject the filtering object
     * @return the page
     */
    Page<T> findAllPaginatedAndSortedRawWithFilter(final int page, final int size, final String sortBy,
                                                   final String sortOrder, final String filterObject);


}
