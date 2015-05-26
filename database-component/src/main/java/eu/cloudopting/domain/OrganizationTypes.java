package eu.cloudopting.domain;

import eu.cloudopting.events.api.entity.BaseEntity;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Entity
@Table(schema = "public", name = "organization_types")
@Configurable
public class OrganizationTypes implements BaseEntity {

    @PersistenceContext
    transient EntityManager entityManager;

    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("");

    public static final EntityManager entityManager() {
        EntityManager em = new OrganizationTypes().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countOrganizationTypeses() {
        return entityManager().createQuery("SELECT COUNT(o) FROM OrganizationTypes o", Long.class).getSingleResult();
    }

    public static List<OrganizationTypes> findAllOrganizationTypeses() {
        return entityManager().createQuery("SELECT o FROM OrganizationTypes o", OrganizationTypes.class).getResultList();
    }

    public static List<OrganizationTypes> findAllOrganizationTypeses(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM OrganizationTypes o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, OrganizationTypes.class).getResultList();
    }

    public static OrganizationTypes findOrganizationTypes(Long id) {
        if (id == null) return null;
        return entityManager().find(OrganizationTypes.class, id);
    }

    public static List<OrganizationTypes> findOrganizationTypesEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM OrganizationTypes o", OrganizationTypes.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    public static List<OrganizationTypes> findOrganizationTypesEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM OrganizationTypes o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, OrganizationTypes.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    @Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }

    @Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            OrganizationTypes attached = OrganizationTypes.findOrganizationTypes(this.id);
            this.entityManager.remove(attached);
        }
    }

    @Transactional
    public void flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }

    @Transactional
    public void clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }

    @Transactional
    public OrganizationTypes merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        OrganizationTypes merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).setExcludeFieldNames("organizationss").toString();
    }

    @OneToMany(mappedBy = "organizationType")
    private Set<Organizations> organizationss;

    @Column(name = "types", length = 20, unique = true)
    @NotNull
    private String types;

    public Set<Organizations> getOrganizationss() {
        return organizationss;
    }

    public void setOrganizationss(Set<Organizations> organizationss) {
        this.organizationss = organizationss;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }
}
