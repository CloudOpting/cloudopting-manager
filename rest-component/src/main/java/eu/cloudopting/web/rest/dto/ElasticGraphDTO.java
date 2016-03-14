package eu.cloudopting.web.rest.dto;

import java.io.Serializable;

import eu.cloudopting.monitoring.elastic.data.ElasticData;

/**
 * @author Xavier Cases Camats (xavier.cases@worldline.com)
 */
public class ElasticGraphDTO implements Serializable
{
    private String xkey;
    private String[] labels;
    private ElasticData[] data;
    private String[] lineColors;
    private String[] ykeys;

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

    @Override
    public String toString() {
        return "ClassPojo [xkey = "+xkey+", labels = "+labels+", data = "+data+", lineColors = "+lineColors+", ykeys = "+ykeys+"]";
    }
}