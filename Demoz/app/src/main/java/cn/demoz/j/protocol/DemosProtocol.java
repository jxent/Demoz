package cn.demoz.j.protocol;

import android.util.SparseArray;

import org.json.JSONArray;
import org.json.JSONException;

public class DemosProtocol extends BaseProtocol<SparseArray<String>> {

    @Override
    public SparseArray<String> parseJson(String json) {
        SparseArray<String> datas = new SparseArray<String>();
        try {
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                String str = array.getString(i);
                datas.put(i, str);
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
    public SparseArray<String> load(int index) {
        SparseArray<String> datas = new SparseArray<String>();
        datas.put(1001, "加载大图的ImageView");
        datas.append(1002, "仿QQ消息气泡");
        datas.append(1003, "美团选择菜单");
        datas.append(1004, "自定义按钮水波纹效果");
        datas.append(1005, "开关控件");
        datas.append(1006, "仿优酷菜单");

        return datas;
    }


}
