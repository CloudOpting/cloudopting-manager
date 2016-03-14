package eu.cloudopting.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import eu.cloudopting.events.api.entity.BaseEntity;

@Entity
@Table(name = "MONITORING_INFO_ELASTIC")
@SequenceGenerator(name = "monitoringInfoElasticGen", sequenceName = "monitoring_info_elastic_id_seq")
public class MonitoringInfoElastic implements BaseEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "monitoringInfoElasticGen")
    private Long id;

    @Column(name = "container")
    private String container;
    
    @Column(name = "condition")
    private String condition;
  
    @Column(name = "fields")
    private String fields;
    
    @Column(name = "type")
    private String type;
    
    @Column(name = "description")
    private String description;
   
    @Column(name = "title")
    private String title;
 
    @Column(name = "pagination")
    private Long pagination;
    
    @ManyToOne
    @JoinColumn(name = "customization_id", referencedColumnName = "id")
    private Customizations customization;
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContainer() {
		return container;
	}

	public void setContainer(String container) {
		this.container = container;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getFields() {
		return fields;
	}

	public void setFields(String fields) {
		this.fields = fields;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getPagination() {
		return pagination;
	}

	public void setPagination(Long pagination) {
		this.pagination = pagination;
	}

	public Customizations getCustomization() {
		return customization;
	}

	public void setCustomization(Customizations customization) {
		this.customization = customization;
	}
}
