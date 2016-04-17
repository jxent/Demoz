package cn.demoz.j.protocol;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class DemosProtocol extends BaseProtocol<List<String>> {

    @Override
    public List<String> paserJson(String json) {
        List<String> datas = new ArrayList<String>();
        try {
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                String str = array.getString(i);
                datas.add(str);
            }
            return datas;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public String getKey() {
        return "hot";
    }

    @Override
    public List<String> load(int index) {
        List<String> datas = new ArrayList<String>();
        datas.add("加载大图的ImageView");
        datas.add("仿QQ消息气泡");
        datas.add("美团选择菜单");
        datas.add("自定义按钮水波纹效果");
        datas.add("开关控件");
        datas.add("仿优酷菜单");

        return datas;
    }


}
