package eu.cloudopting.events.api.service;

import org.apache.commons.lang3.tuple.Triple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

import eu.cloudopting.events.api.constants.ClientOperation;

import java.util.List;

/**
 * Operatii default.
 *
 * @param <T> entitatea pe care se opereaza actiunile
 */

public interface DefaultOperations<T> {
    // find - one

    /**
     * Find one.
     *
     * @param id the id
     * @return the t
     */
    T findOne(final long id);

    /**
     * contract: if nothing is found, an empty list will be returned to the calling client <br>.
     *
     * @return the list
     */
    List<T> findAll();

    /**
     * contract: if nothing is found, an empty list will be returned to the calling client <br>.
     *
     * @return the list
     */
    Page<T> findAll(Pageable pageable);

    /**
     * contract: if nothing is found, an empty list will be returned to the calling client <br>.
     *
     * @param sortBy    the sort by
     * @param sortOrder the sort order
     * @return the list
     */
    List<T> findAllSorted(final String sortBy, final String sortOrder);

    /**
     * contract: if nothing is found, an empty list will be returned to the calling client <br>.
     *
     * @param page the page
     * @param size the size
     * @return the list
     */
    List<T> findAllPaginated(final int page, final int size);

    /**
     * contract: if nothing is found, an empty list will be returned to the calling client <br>.
     *
     * @param page      the page
     * @param size      the size
     * @param sortBy    the sort by
     * @param sortOrder the sort order
     * @return the list
     */
    List<T> findAllPaginatedAndSorted(final int page, final int size, final String sortBy, final String sortOrder);

    // create

    /**
     * Create t.
     *
     * @param resource the resource
     * @return the t
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    T create(final T resource);

    // update

    /**
     * Update void.
     *
     * @param resource the resource
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    void update(final T resource);

    // delete

    /**
     * Delete void.
     *
     * @param id the id
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    void delete(final long id);

    /**
     * Delete all.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    void deleteAll();

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    void delete(Iterable<T> entities);

    // count

    /**
     * Count long.
     *
     * @return the long
     */
    long count();

    // search

    /**
     * Search all.
     *
     * @param constraints the constraints
     * @return the list
     */

    List<T> searchAll(final Triple<String, ClientOperation, String>... constraints);

    /**
     * Search one.
     *
     * @param constraints the constraints
     * @return the t
     */
    T searchOne(final Triple<String, ClientOperation, String>... constraints);
}
