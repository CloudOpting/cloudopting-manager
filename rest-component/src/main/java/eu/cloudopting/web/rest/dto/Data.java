package eu.cloudopting.web.rest.dto;

import java.io.Serializable;

/**
 * @author Xavier Cases Camats (xavier.cases@worldline.com)
 */
public class Data implements Serializable {

    private String time;
    private String ram;
    private String disk;
    private String cpu;

    public Data() {
    }

    public Data(String time, String ram, String disk, String cpu) {
        this.time = time;
        this.ram = ram;
        this.disk = disk;
        this.cpu = cpu;
    }

    public Data(String time) {
        this.time = time;
    }

    public String getTime () {
        return time;
    }

    public void setTime (String time){
        this.time = time;
    }

    public String getRam () {
        return ram;
    }

    public void setRam (String ram){
        this.ram = ram;
    }

    public String getDisk (){
        return disk;
    }

    public void setDisk (String disk){
        this.disk = disk;
    }

    public String getCpu (){
        return cpu;
    }

    public void setCpu (String cpu) {
        this.cpu = cpu;
    }

    @Override
    public String toString() {
        return "ClassPojo [time = "+time+", ram = "+ram+", disk = "+disk+", cpu = "+cpu+"]";
    }
}