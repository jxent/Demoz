package cn.demoz.j.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lidroid.xutils.BitmapUtils;

import java.util.List;

import cn.demoz.j.tools.BitmapHelper;
import cn.demoz.j.tools.ViewUtils;
import cn.demoz.j.view.LoadingPage;

public abstract class BaseFragment extends Fragment {

    private LoadingPage loadingPage;
    protected BitmapUtils bitmapUtils;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bitmapUtils = BitmapHelper.getBitmapUtils();

        if (loadingPage == null) {  // 之前的frameLayout 已经记录了一个父控件了  父控件是之前显示后被添加到的ViewPager
            loadingPage = new LoadingPage(getActivity()) {

                @Override
                public View createSuccessView() {
                    return BaseFragment.this.createSuccessView();
                }

                @Override
                protected LoadResult load() {
                    return BaseFragment.this.load();
                }
            };
        } else {
            // 由于Fragment被工厂类缓存起来了，而且在ViewPager中展示，所以会有试图绑定的问题
            ViewUtils.removeParent(loadingPage);// 移除frameLayout之前的父控件
        }

        return loadingPage;  //  拿到当前viewPager 添加这个framelayout
    }

    /***
     * 创建成功的界面
     *
     * @return
     */
    public abstract View createSuccessView();

    /**
     * 请求服务器
     *
     * @return
     */
    protected abstract LoadingPage.LoadResult load();

    public void show() {
        if (loadingPage != null) {
            loadingPage.show();
        }
    }


    /**
     * 校验数据
     */
    public LoadingPage.LoadResult checkData(List datas) {
        if (datas == null) {
            return LoadingPage.LoadResult.error;//  请求服务器失败
        } else {
            if (datas.size() == 0) {
                return LoadingPage.LoadResult.empty;
            } else {
                return LoadingPage.LoadResult.success;
            }
        }

    }


}
