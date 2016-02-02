package eu.cloudopting.web.rest.dto;

import java.io.Serializable;

/**
 * @author Xavier Cases Camats (xavier.cases@worldline.com)
 */
public class ElasticData implements Serializable {

    private String time;
    private String occurrence;

    public ElasticData() {
    }

    public ElasticData(String time, String occurrence) {
        this.time = time;
        this.occurrence = occurrence;
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

    public String getOccurence () {
        return occurrence;
    }

    public void setOccurence (String occurrence){
        this.occurrence = occurrence;
    }


    @Override
    public String toString() {
        return "ClassPojo [time = "+time+", occurrence = "+occurrence+"]";
    }
}