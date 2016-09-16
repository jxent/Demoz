package cn.demoz.j.fragment;

import android.util.SparseArray;

/**
 * Fragment工厂
 */
public class FragmentFactory {
    private static SparseArray<BaseFragment> mFragments = new SparseArray<BaseFragment>();

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
}
