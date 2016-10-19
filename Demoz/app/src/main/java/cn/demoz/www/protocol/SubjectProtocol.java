package cn.demoz.www.protocol;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.demoz.www.bean.SubjectInfo;

public class SubjectProtocol extends BaseProtocol<List<SubjectInfo>> {

    @Override
    public List<SubjectInfo> parseJson(String json) {
        List<SubjectInfo> subjectInfos = new ArrayList<SubjectInfo>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String des = jsonObject.getString("des");
                String url = jsonObject.getString("url");
                SubjectInfo info = new SubjectInfo(des, url);
                subjectInfos.add(info);

            }
            return subjectInfos;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getKey() {
        return "subject";
    }


}
