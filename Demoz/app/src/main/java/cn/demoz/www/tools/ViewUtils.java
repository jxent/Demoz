package cn.demoz.www.tools;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

public class ViewUtils {
    public static void removeParent(View v) {
        //  先找到父控件 在通过其去移除孩子
        ViewParent parent = v.getParent();
        //所有的控件 都有父控件  父控件一般情况下 就是ViewGroup
        if (parent instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) parent;
            group.removeView(v);
        }
    }
}
