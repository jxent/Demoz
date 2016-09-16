package cn.demoz.j.protocol;

import org.json.JSONException;
import org.json.JSONObject;

import cn.demoz.j.domain.UserInfo;

public class UserProtocol extends BaseProtocol<UserInfo> {

    @Override
    public UserInfo parseJson(String json) {
        //"{name:'传智黄盖',email:'huanggai@itcast.cn',url:'image/user.png'}"
        try {
            JSONObject jsonObject = new JSONObject(json);
            String name = jsonObject.getString("name");
            String email = jsonObject.getString("email");
            String url = jsonObject.getString("url");
            UserInfo userInfo = new UserInfo(name, url, email);
            return userInfo;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getKey() {
        return "user";
    }

}
