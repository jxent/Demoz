package cn.demoz.www.tools;

import android.content.Context;
import android.content.Intent;

import cn.demoz.www.demos.activity.FlowDemosShowingActivity;

public class FlowDemosShowingIntentOperation {

    private static final String FRAGMENT_FLAG = "fragment_flag";

    // 各种效果的key
    public static final int UNKNOWN_STYLE = -1000;
    public static final int LOAD_HUGE_IMAGE_VIEW = 1001;
    public static final int LIKE_QQ_MESSAGE_BALL = 1002;
    public static final int MEITUAN_SELECTION_MENU = 1003;
    public static final int BUTTON_WATER_RIPPLE = 1004;
    public static final int SWITCH_BUTTON = 1005;
    public static final int LIKE_YOUKU_MENU = 1006;
    public static final int VARIABLE_STYLE1 = 1007;
    public static final int VARIABLE_STYLE2 = 1008;
    public static final int VARIABLE_STYLE3 = 1009;
    public static final int VARIABLE_STYLE4 = 1010;

    public static void toDemosFragmentOperation(Context context, int key){
        Intent intent = new Intent(UiUtils.getContext(), FlowDemosShowingActivity.class);
        switch (key){
            case LOAD_HUGE_IMAGE_VIEW:
                intent.putExtra(FRAGMENT_FLAG, key);
                break;
            case LIKE_QQ_MESSAGE_BALL:
                intent.putExtra(FRAGMENT_FLAG, key);
                break;
            case MEITUAN_SELECTION_MENU:
                intent.putExtra(FRAGMENT_FLAG, key);
                break;
            case BUTTON_WATER_RIPPLE:
                intent.putExtra(FRAGMENT_FLAG, key);
                break;
            case SWITCH_BUTTON:
                intent.putExtra(FRAGMENT_FLAG, key);
                break;
            case LIKE_YOUKU_MENU:
                intent.putExtra(FRAGMENT_FLAG, key);
                break;
            default:
                intent.putExtra(FRAGMENT_FLAG, key);
                break;
        }
        context.startActivity(intent);

    }
}
