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
@Table(schema = "public",name = "providers")
@Configurable
public class Providers implements BaseEntity {

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

	@OneToMany(mappedBy = "statusId")
    private Set<Applications> applicationss;

	@OneToMany(mappedBy = "statusId")
    private Set<Customizations> customizationss;

	@Column(name = "provider", length = 20, unique = true)
    @NotNull
    private String provider;
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
	public String getProvider() {
        return provider;
    }

	public void setProvider(String provider) {
        this.provider = provider;
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("");

	public static final EntityManager entityManager() {
        EntityManager em = new Providers().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countProviders() {
        return entityManager().createQuery("SELECT COUNT(o) FROM providers o", Long.class).getSingleResult();
    }

	public static List<Providers> findAllProviders() {
        return entityManager().createQuery("SELECT o FROM providers o", Providers.class).getResultList();
    }

	public static List<Providers> findAllProviders(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM providers o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Providers.class).getResultList();
    }

	public static Providers findProvider(Long id) {
        if (id == null) return null;
        return entityManager().find(Providers.class, id);
    }

	public static List<Providers> findProvidersEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM providers o", Providers.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<Providers> findProvidersEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM providers o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Providers.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            Providers attached = Providers.findProvider(this.id);
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
    public Providers merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Providers merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
}
