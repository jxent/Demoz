package cn.demoz.www.tools;

import android.content.Context;
import android.net.Uri;

/**
 * Created by Jason on 2016/10/22.
 */
public class ResourceUtil {

    public static final String ANDROID_RESOURCE = "android.resource://";
    public static final String FOREWARD_SLASH = "/";

    /**
     * 从资源id转换成Uri
     */
    public static Uri parseResIdToUri(Context context, int resId){
        return Uri.parse(ANDROID_RESOURCE + context.getPackageName()
            + FOREWARD_SLASH + resId);
    }
}
