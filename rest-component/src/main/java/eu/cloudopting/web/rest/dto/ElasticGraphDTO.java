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
        return labels.clone();
    }

    public void setLabels (String[] labels){
        this.labels = labels.clone();
    }

    public ElasticData[] getData (){
        return data.clone();
    }

    public void setData (ElasticData[] data){
        this.data = data.clone();
    }

    public String[] getLineColors () {
        return lineColors.clone();
    }

    public void setLineColors (String[] lineColors){
        this.lineColors = lineColors.clone();
    }

    public String[] getYkeys (){
        return ykeys.clone();
    }

    public void setYkeys (String[] ykeys) {
        this.ykeys = ykeys.clone();
    }

    @Override
    public String toString() {
        return "ClassPojo [xkey = "+xkey+", labels = "+labels+", data = "+data+", lineColors = "+lineColors+", ykeys = "+ykeys+"]";
    }
}
