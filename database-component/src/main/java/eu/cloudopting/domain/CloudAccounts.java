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
@Table(schema = "public",name = "cloud_accounts")
@Configurable
public class CloudAccounts implements BaseEntity {

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("");

	public static final EntityManager entityManager() {
        EntityManager em = new CloudAccounts().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countCloudAccountses() {
        return entityManager().createQuery("SELECT COUNT(o) FROM CloudAccounts o", Long.class).getSingleResult();
    }

	public static List<CloudAccounts> findAllCloudAccountses() {
        return entityManager().createQuery("SELECT o FROM CloudAccounts o", CloudAccounts.class).getResultList();
    }

	public static List<CloudAccounts> findAllCloudAccountses(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM CloudAccounts o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, CloudAccounts.class).getResultList();
    }

	public static CloudAccounts findCloudAccounts(Long id) {
        if (id == null) return null;
        return entityManager().find(CloudAccounts.class, id);
    }

	public static List<CloudAccounts> findCloudAccountsEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM CloudAccounts o", CloudAccounts.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<CloudAccounts> findCloudAccountsEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM CloudAccounts o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, CloudAccounts.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            CloudAccounts attached = CloudAccounts.findCloudAccounts(this.id);
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
    public CloudAccounts merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        CloudAccounts merged = this.entityManager.merge(this);
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
        return new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).setExcludeFieldNames("organizationId", "providerId").toString();
    }

	@ManyToOne
    @JoinColumn(name = "organization_id", referencedColumnName = "id", nullable = false)
    private Organizations organizationId;

    @OneToMany(mappedBy = "cloudAccount")
    private Set<Customizations> customizationss;


	@ManyToOne
    @JoinColumn(name = "provider_id", referencedColumnName = "id", nullable = false)
    private Providers providerId;

	@Column(name = "name", length = 50)
    @NotNull
    private String name;

	@Column(name = "api_key", length = 50)
    @NotNull
    private String apiKey;

	@Column(name = "secret_key", length = 50)
    @NotNull
    private String secretKey;

	@Column(name = "endpoint", length = 100)
    @NotNull
    private String endpoint;

	public Organizations getOrganizationId() {
        return organizationId;
    }

	public void setOrganizationId(Organizations organizationId) {
        this.organizationId = organizationId;
    }

	public Providers getProviderId() {
        return providerId;
    }

	public void setProviderId(Providers providerId) {
        this.providerId = providerId;
    }

	public String getName() {
        return name;
    }

	public void setName(String name) {
        this.name = name;
    }

	public String getApiKey() {
        return apiKey;
    }

	public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

	public String getSecretKey() {
        return secretKey;
    }

	public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

	public String getEndpoint() {
        return endpoint;
    }

	public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public Set<Customizations> getCustomizationss() {
        return customizationss;
    }

    public void setCustomizationss(Set<Customizations> customizationss) {
        this.customizationss = customizationss;
    }
}
