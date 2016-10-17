package cn.demoz.j.bean;

import java.io.Serializable;

/**
 * flow_demos.json中的json数据结构
 * Created by Jason on 2016/10/16.
 */
public class FlowDemosBean implements Serializable {

    private FlowDemosItemBean flowDemosItemBean;

    public FlowDemosItemBean getFlowDemosItemBean() {
        return flowDemosItemBean;
    }

    public void setFlowDemosItemBean(FlowDemosItemBean flowDemosItemBean) {
        this.flowDemosItemBean = flowDemosItemBean;
    }
}
