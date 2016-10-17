package cn.demoz.j.fragment;

import android.support.v4.app.Fragment;
import android.util.SparseArray;

import cn.demoz.j.bean.FlowDemosItemBean;

/**
 * Fragment工厂
 */
public class FragmentFactory {

    private static final String FRAGMENT_FLAG = "fragment_flag";
    private static SparseArray<BaseFragment> mFragments = new SparseArray<BaseFragment>();
    private static SparseArray<Fragment> mFlowDemosShowingFragments = new SparseArray<Fragment>();

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

    public static BaseFragment getFragment(int position) {
        BaseFragment fragment = null;
        fragment = mFragments.get(position);  //在集合中取出来Fragment
        if (fragment == null) {  //如果再集合中没有取出来 需要重新创建
            if (position == 0) {
                fragment = new FlowDemosFragment();
            } else if (position == 1) {
                fragment = new CategoryFragment();
            } else if (position == 2) {
                fragment = new GameFragment();
            } else if (position == 3) {
                fragment = new SubjectFragment();
            } else if (position == 4) {
                fragment = new BestFragment();
            } else if (position == 5) {
                fragment = new HomeFragment();
            }
            if (fragment != null) {
                mFragments.put(position, fragment);// 把创建好的Fragment存放到集合中缓存起来
            }
        }
        return fragment;
    }

    public static Fragment getFlowDemosShowingFragment(FlowDemosItemBean bean){
        Fragment fragment = mFlowDemosShowingFragments.get(bean.getKey());  //在集合中取出来Fragment;
        String fragmentClassName = bean.getClazz();
        if(fragment == null) {
            // 利用反射尝试加载出bean中的类，如果项目中尚未配置此类，则返回默认的UnknownFragment
            try {
                Class<Fragment> clazz = (Class<Fragment>) Class.forName(fragmentClassName);
                if (clazz != null) {
                    fragment = clazz.newInstance();
                    mFlowDemosShowingFragments.append(bean.getKey(), fragment);
                } else {
                    return new UnknownFragment();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return new UnknownFragment();
            }
        }


//        switch (bean.getKey()){
//            case LOAD_HUGE_IMAGE_VIEW:
//                fragment = new HugeImageViewFragment();
//                break;
//            case LIKE_QQ_MESSAGE_BALL:
//                fragment = new QQBubbleFragment();
//                break;
//            case MEITUAN_SELECTION_MENU:
//                break;
//            case BUTTON_WATER_RIPPLE:
//                break;
//            case SWITCH_BUTTON:
//                break;
//            case LIKE_YOUKU_MENU:
//                break;
//            default:
//                break;
//        }
        return fragment;
    }
}
