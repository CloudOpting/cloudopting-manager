package eu.cloudopting.monitoring.elastic.data;

import java.io.Serializable;

/**
 * @author Xavier Cases Camats (xavier.cases@worldline.com)
 */
public class ElasticData implements Serializable {

    private String time;
    private String value;

    public ElasticData() {
    }

    public ElasticData(String time, String value) {
        this.time = time;
        this.value = value;
    }

    public ElasticData(String time) {
        this.time = time;
    }

    public String getTime () {
        return time;
    }

    public void setTime (String time){
        this.time = time;
    }

    public String getValue () {
        return value;
    }

    public void setvalue (String value){
        this.value = value;
    }


    @Override
    public String toString() {
        return "ClassPojo [time = "+time+", value = "+value+"]";
    }
}