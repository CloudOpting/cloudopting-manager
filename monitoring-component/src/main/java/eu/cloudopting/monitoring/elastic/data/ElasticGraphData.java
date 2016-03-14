package eu.cloudopting.monitoring.elastic.data;

import java.io.Serializable;

import eu.cloudopting.monitoring.elastic.data.ElasticData;

/**
 * @author Xavier Cases Camats (xavier.cases@worldline.com)
 */
public class ElasticGraphData implements Serializable
{
    private String xkey;
    private String[] labels;
    private ElasticData[] data;
    private String[] lineColors;
    private String[] ykeys;
    private String title;
    private String description;
    private String type;

    public String getXkey (){
        return xkey;
    }

    public void setXkey (String xkey){
        this.xkey = xkey;
    }

    public String[] getLabels (){
        return labels;
    }

    public void setLabels (String[] labels){
        this.labels = labels;
    }

    public ElasticData[] getData (){
        return data;
    }

    public void setData (ElasticData[] data){
        this.data = data;
    }

    public String[] getLineColors () {
        return lineColors;
    }

    public void setLineColors (String[] lineColors){
        this.lineColors = lineColors;
    }

    public String[] getYkeys (){
        return ykeys;
    }

    public void setYkeys (String[] ykeys) {
        this.ykeys = ykeys;
    }

    public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
    public String toString() {
        return "ClassPojo [xkey = "+xkey+", labels = "+labels+", data = "+data+", lineColors = "+lineColors+", ykeys = "+ykeys+"]";
    }
}