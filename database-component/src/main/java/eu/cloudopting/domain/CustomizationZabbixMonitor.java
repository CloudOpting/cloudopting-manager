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
@Table(name = "CUSTOMIZATION_ZABBIX_MONITOR")
@SequenceGenerator(name = "customizationZabbixMonitorGen", sequenceName = "customization_zabbix_monitor_id_seq")
public class CustomizationZabbixMonitor implements BaseEntity {

	private static final long serialVersionUID = 4898488281115670383L;

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customizationZabbixMonitorGen")
    private Long id;
	
    @Column(name = "hostname")
    private String hostname;
    
    @Column(name = "item")
    private String item;
    
    @Column(name = "history")
    private String history;
    
    @Column(name = "sortfield")
    private String sortfield;
    
    @Column(name = "limit_")
    private Long limit;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "chart_type")
    private String chartType;
    
    @Column(name = "unit")
    private String unit;
    
    @ManyToOne
    @JoinColumn(name = "customization_id", referencedColumnName = "id")
    private Customizations customization;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getHistory() {
		return history;
	}

	public void setHistory(String history) {
		this.history = history;
	}

	public String getSortfield() {
		return sortfield;
	}

	public void setSortfield(String sortfield) {
		this.sortfield = sortfield;
	}

	public Long getLimit() {
		return limit;
	}

	public void setLimit(Long limit) {
		this.limit = limit;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getChartType() {
		return chartType;
	}

	public void setChartType(String chartType) {
		this.chartType = chartType;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Customizations getCustomization() {
		return customization;
	}

	public void setCustomization(Customizations customization) {
		this.customization = customization;
	}
}
