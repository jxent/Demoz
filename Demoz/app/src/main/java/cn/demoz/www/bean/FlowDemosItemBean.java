package cn.demoz.www.bean;

import java.io.Serializable;

/**
 * flow_demos.json中的json数据结构
 * Created by Jason on 2016/10/16.
 */
public class FlowDemosItemBean implements Serializable{

    private Integer key;
    private String name;
    private Boolean showing;
    private String clazz;

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getShowing() {
        return showing;
    }

    public void setShowing(Boolean showing) {
        this.showing = showing;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }
}
