package eu.cloudopting.domain;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;
import eu.cloudopting.events.api.entity.BaseEntity;

@Entity
@Table(schema = "public",name = "organization_types")
@Configurable
public class OrganizationTypes implements BaseEntity {

	public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).setExcludeFieldNames("applicationss", "customizationss").toString();
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
/*
	@OneToMany(mappedBy = "statusId")
    private Set<Applications> applicationss;

	@OneToMany(mappedBy = "statusId")
    private Set<Customizations> customizationss;
*/
	@Column(name = "types", length = 20, unique = true)
    @NotNull
    private String types;
/*
	public Set<Applications> getApplicationss() {
        return applicationss;
    }

	public void setApplicationss(Set<Applications> applicationss) {
        this.applicationss = applicationss;
    }

	public Set<Customizations> getCustomizationss() {
        return customizationss;
    }

	public void setCustomizationss(Set<Customizations> customizationss) {
        this.customizationss = customizationss;
    }
*/
	public String getType() {
        return types;
    }

	public void setType(String types) {
        this.types = types;
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("");

	public static final EntityManager entityManager() {
        EntityManager em = new OrganizationTypes().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countOrganizationTypes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM organization_types o", Long.class).getSingleResult();
    }

	public static List<OrganizationTypes> findAllOrganizationTypes() {
        return entityManager().createQuery("SELECT o FROM organization_types o", OrganizationTypes.class).getResultList();
    }

	public static List<OrganizationTypes> findAllOrganizationTypes(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM organization_types o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, OrganizationTypes.class).getResultList();
    }

	public static OrganizationTypes findOrganizationType(Long id) {
        if (id == null) return null;
        return entityManager().find(OrganizationTypes.class, id);
    }

	public static List<OrganizationTypes> findOrganizationTypesEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM organization_types o", OrganizationTypes.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<OrganizationTypes> findOrganizationTypesEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM organization_types o";
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
            OrganizationTypes attached = OrganizationTypes.findOrganizationType(this.id);
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
}
