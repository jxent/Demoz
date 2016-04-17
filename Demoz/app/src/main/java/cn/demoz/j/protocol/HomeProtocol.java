package cn.demoz.j.protocol;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.demoz.j.domain.AppInfo;

public class HomeProtocol extends BaseProtocol<List<AppInfo>> {
    private List<String> pictures;

    //  1 把整个json文件写到一个本地文件中  **
    // 2  把每条数据都摘出来存到数据库中
    // 见到大括号 就用JsonObject ,见到中括号就是JsonArray
    public List<AppInfo> paserJson(String json) {
        List<AppInfo> appInfos = new ArrayList();
        pictures = new ArrayList();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray2 = jsonObject.getJSONArray("picture");
            for (int i = 0; i < jsonArray2.length(); i++) {
                String str = jsonArray2.getString(i);
                pictures.add(str);
            }

            JSONArray jsonArray = jsonObject.getJSONArray("list");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                long id = jsonObj.getLong("id");
                String name = jsonObj.getString("name");
                String packageName = jsonObj.getString("packageName");
                String iconUrl = jsonObj.getString("iconUrl");
                float stars = Float.parseFloat(jsonObj.getString("stars"));
                long size = jsonObj.getLong("size");
                String downloadUrl = jsonObj.getString("downloadUrl");
                String des = jsonObj.getString("des");
                AppInfo info = new AppInfo(id, name, packageName, iconUrl, stars, size, downloadUrl, des);
                appInfos.add(info);
            }
            return appInfos;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }


    public List<String> getPictures() {
        return pictures;
    }


    @Override
    public String getKey() {
        return "home";
    }


}
