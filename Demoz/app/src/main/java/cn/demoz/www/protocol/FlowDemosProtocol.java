package cn.demoz.www.protocol;

import android.util.SparseArray;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import cn.demoz.www.bean.FlowDemosItemBean;

public class FlowDemosProtocol extends BaseProtocol<SparseArray<FlowDemosItemBean>> {

    @Override
    public SparseArray<FlowDemosItemBean> parseJson(String json) {
        SparseArray<FlowDemosItemBean> datas = new SparseArray<FlowDemosItemBean>();
        try {
            JSONObject root = JSON.parseObject(json);
            int code = (int) root.get("code");
            if(code == 100) {
                JSONArray items = root.getJSONArray("items");
                for (int i = 0; i< items.size(); i++) {
                    FlowDemosItemBean bean = JSON.parseObject(items.get(i).toString(),
                            FlowDemosItemBean.class);
                    datas.append(1001 + i, bean);
                }
            }else if(code == -100) {

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            return datas;
        }
    }

    @Override
    public String getKey() {
        return "flow_demos";
    }

}
