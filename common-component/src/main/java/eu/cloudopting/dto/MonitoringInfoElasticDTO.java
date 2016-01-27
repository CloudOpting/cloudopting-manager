package eu.cloudopting.dto;

public class MonitoringInfoElasticDTO {

    private Long id;

    private String container;
    
    private String condition;
  
    private String fields;
    
    private String type;
    
    private String description;
   
    private String title;
 
    private Long pagination;
    
    private Long customizationId;

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

	public Long getCustomizationId() {
		return customizationId;
	}

	public void setCustomizationId(Long customizationId) {
		this.customizationId = customizationId;
	}
}
