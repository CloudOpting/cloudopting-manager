package eu.cloudopting.events.api.service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;
import eu.cloudopting.events.api.SearchCommonUtil;
import eu.cloudopting.events.api.constants.ClientOperation;
import eu.cloudopting.events.api.entity.BaseEntity;
import eu.cloudopting.events.api.events.*;
import eu.cloudopting.events.api.exceptions.BadRequestException;
import eu.cloudopting.events.api.exceptions.ConflictException;
import eu.cloudopting.events.api.preconditions.ServicePreconditions;

import java.util.List;

/**
 * @param <T> base entity, modelul pe care lucram
 * @author Daniel P.
 *         Date: 8/8/13
 */
public abstract class AbstractDefaultService<T extends BaseEntity> implements BaseService<T> {

    /**
     * logger.
     */
    private final Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * Class<BaseEntity>.
     */
    private Class<T> clazz;
    /**
     * Event Publisherul injectat. Acesta este folosit pentru a ridica evenimente si a notifica finalizarea operatiilor,
     * astfel evenimentele putand fi prinse de listeneri si putandu-se realiza diferite operatii precum introducerea
     * butoanelor de next,prev la paginare.
     */
    @Autowired
    protected ApplicationEventPublisher eventPublisher;

    /**
     * Constructor.
     *
     * @param clazzToSet clasa care trebuie setata
     */
    public AbstractDefaultService(final Class<T> clazzToSet) {
        super();
        clazz = clazzToSet;
    }

    // API

    // search

    @SuppressWarnings("all")
    @Override
    public final List<T> searchAll(final String queryString) {
        Preconditions.checkNotNull(queryString);
        List<Triple<String, ClientOperation, String>> parsedQuery;
        try {
            parsedQuery = SearchCommonUtil.parseQueryString(queryString);
        } catch (final IllegalStateException illState) {
            logger.error("IllegalStateException on find operation");
            logger.warn("IllegalStateException on find operation", illState);
            throw new BadRequestException(illState);
        }

        return searchAll(parsedQuery.toArray(new ImmutableTriple[parsedQuery.size()]));
    }

    @SuppressWarnings({"all"})
    @Override
    public final List<T> searchPaginated(final String queryString, final int page, final int size) {
        List<Triple<String, ClientOperation, String>> parsedQuery;
        try {
            parsedQuery = SearchCommonUtil.parseQueryString(queryString);
        } catch (final IllegalStateException illState) {
            logger.error("IllegalStateException on find operation");
            logger.warn("IllegalStateException on find operation", illState);
            throw new ConflictException(illState);
        }

        final Page<T> resultPage = searchPaginated(page, size, parsedQuery
                .toArray(new ImmutableTriple[parsedQuery.size()]));
        return Lists.newArrayList(resultPage.getContent());
    }

    @SuppressWarnings("all")
    @Override
    public final List<T> searchAll(final Triple<String, ClientOperation, String>... constraints) {
        Preconditions.checkState(constraints != null);
        Preconditions.checkState(constraints.length > 0);
        final Specification<T> firstSpec = resolveConstraint(constraints[0]);
        Specifications<T> specifications = Specifications.where(firstSpec);
        for (int i = 1; i < constraints.length; i++) {
            specifications = specifications.and(resolveConstraint(constraints[i]));
        }
        if (firstSpec == null) {
            return Lists.newArrayList();
        }

        return getSpecificationExecutor().findAll(specifications);
    }

    @SuppressWarnings(value = {"all"})
    @Override
    public final T searchOne(final Triple<String, ClientOperation, String>... constraints) {
        Preconditions.checkState(constraints != null);
        Preconditions.checkState(constraints.length > 0);
        final Specification<T> firstSpec = resolveConstraint(constraints[0]);
        Specifications<T> specifications = Specifications.where(firstSpec);
        for (int i = 1; i < constraints.length; i++) {
            specifications = specifications.and(resolveConstraint(constraints[i]));
        }
        if (firstSpec == null) {
            return null;
        }

        return getSpecificationExecutor().findOne(specifications);
    }

    @Override
    @SuppressWarnings("unchecked")
    public final Page<T> searchPaginated(final int page, final int size,
                                         final Triple<String, ClientOperation, String>... constraints) {
        final Specification<T> firstSpec = resolveConstraint(constraints[0]);
        Preconditions.checkState(firstSpec != null);
        Specifications<T> specifications = Specifications.where(firstSpec);
        for (int i = 1; i < constraints.length; i++) {
            specifications = specifications.and(resolveConstraint(constraints[i]));
        }

        return getSpecificationExecutor().findAll(specifications, new PageRequest(page, size, null));
    }

    // find - one
    @Override
    @Transactional(readOnly = true)
    public T findOne(final long id) {
        return getDao().findOne(id);
    }

    // find - all

    @Override
    @Transactional(readOnly = true)
    public final List<T> findAll() {
        return Lists.newArrayList(getDao().findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<T> findAll(Pageable pageable) {
        return  getDao().findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public final Page<T> findAllPaginatedAndSortedRaw(final int page, final int size, final String sortBy,
                                                      final String sortOrder) {
        final Sort sortInfo = constructSort(sortBy, sortOrder);
        return getDao().findAll(new PageRequest(page, size, sortInfo));
    }

    @Override
    @Transactional(readOnly = true)
    public final List<T> findAllPaginatedAndSorted(final int page, final int size, final String sortBy,
                                                   final String sortOrder) {
        final Sort sortInfo = constructSort(sortBy, sortOrder);
        final List<T> content = getDao().findAll(new PageRequest(page, size, sortInfo)).getContent();
        if (content == null) {
            return Lists.newArrayList();
        }
        return content;
    }

    @Override
    @Transactional(readOnly = true)
    public final List<T> findAllPaginated(final int page, final int size) {
        final List<T> content = getDao().findAll(new PageRequest(page, size, null)).getContent();
        if (content == null) {
            return Lists.newArrayList();
        }
        return content;
    }

    @Override
    @Transactional(readOnly = true)
    public final List<T> findAllSorted(final String sortBy, final String sortOrder) {
        final Sort sortInfo = constructSort(sortBy, sortOrder);
        return Lists.newArrayList(getDao().findAll(sortInfo));
    }

    // save/create/persist
    @Override
    public T create(final T entity) {
        Preconditions.checkNotNull(entity);

        eventPublisher.publishEvent(new BeforeEntityCreateEvent<>(this, clazz, entity));
        final T persistedEntity = getDao().save(entity);
        eventPublisher.publishEvent(new AfterEntityCreateEvent<>(this, clazz, persistedEntity));

        return persistedEntity;
    }

    // update/merge
    @Override
    public void update(final T entity) {
        ServicePreconditions.checkEntityExists(entity);

        eventPublisher.publishEvent(new BeforeEntityUpdateEvent<>(this, clazz, entity));
        getDao().save(entity);
        eventPublisher.publishEvent(new AfterEntityUpdateEvent<>(this, clazz, entity));
    }

    // delete

    @Override
    public final void deleteAll() {
        getDao().deleteAll();
        eventPublisher.publishEvent(new AfterEntitiesDeletedEvent<>(this, clazz));
    }

    @Override
    public void delete(final long id) {
        final T entity = getDao().findOne(id);
        ServicePreconditions.checkEntityExists(entity);

        eventPublisher.publishEvent(new BeforeEntityDeleteEvent<>(this, clazz, entity));
        getDao().delete(entity);
        eventPublisher.publishEvent(new AfterEntityDeleteEvent<>(this, clazz, entity));
    }

    @Override
    public void delete(Iterable<T> entities) {
        for (T entity : entities) {
            ServicePreconditions.checkEntityExists(entity);

            eventPublisher.publishEvent(new BeforeEntityDeleteEvent<>(this, clazz, entity));
            getDao().delete(entity);
            eventPublisher.publishEvent(new AfterEntityDeleteEvent<>(this, clazz, entity));
        }
    }

    // count
    @Override
    public final long count() {
        return getDao().count();
    }

    /**
     * Metoda returneaza instanta de repository setata in interiorul unui service.
     *
     * @return o instanta de PagingAndSortingRepository
     */
    protected abstract PagingAndSortingRepository<T, Long> getDao();


    /**
     * Metoda returneaza un SpecificationExecutor. Specification este folosit pentru crearea de queryuri avansate.
     *
     * @return JpaSpecificationExecutor
     */
    protected abstract JpaSpecificationExecutor<T> getSpecificationExecutor();

    /**
     * Metoda rezolva constrangerile de pe obiectul nostru.
     *
     * @param constraint constrangerile
     * @return un Specification
     */
    @SuppressWarnings({"static-method", "unused"})
    public final Specification<T> resolveConstraint(final Triple<String, ClientOperation, String> constraint) {
        throw new UnsupportedOperationException();
    }

    /**
     * Metoda builduieste operatiile de sort.
     *
     * @param sortBy    parametrul in functie de care se face sortarea.
     * @param sortOrder ordinea sortarii, ASC-DESC
     * @return - informatiile despre sortare
     */
    protected final Sort constructSort(final String sortBy, final String sortOrder) {
        Sort sortInfo = null;
        if (sortBy != null) {
            sortInfo = new Sort(Sort.Direction.fromString(sortOrder), sortBy);
        }
        return sortInfo;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<T> findAllPaginatedAndSortedRawWithFilter(final int page, final int size, final String sortBy,
                                                                final String sortOrder, final String filterObject){
        final Sort sortInfo = constructSort(sortBy, sortOrder);
        return getDao().findAll(new PageRequest(page, size, sortInfo));
    }


}
