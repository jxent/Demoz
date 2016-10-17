package cn.demoz.j.fragment;

import android.view.View;

import java.util.List;

import cn.demoz.j.adapter.ListBaseAdapter;
import cn.demoz.j.bean.AppInfo;
import cn.demoz.j.protocol.AppProtocol;
import cn.demoz.j.tools.UiUtils;
import cn.demoz.j.view.BaseListView;
import cn.demoz.j.view.LoadingPage;

public class BestFragment extends BaseFragment {

    private List<AppInfo> datas;

    /**
     * 当加载成功的时候 显示的界面
     */
    @Override
    public View createSuccessView() {
        BaseListView listView = new BaseListView(UiUtils.getContext());
        listView.setAdapter(new ListBaseAdapter(datas, listView) {

            @Override
            protected List<AppInfo> onload() {
                AppProtocol protocol = new AppProtocol();
                List<AppInfo> load = protocol.load(0);
                datas.addAll(load);
                return load;
            }

        });
        return listView;
    }

    /**
     * 请求服务器 获取服务器的数据
     */
    protected LoadingPage.LoadResult load() {
        AppProtocol protocol = new AppProtocol();
        datas = protocol.load(0);
        return checkData(datas); // 检查数据 有三种结果  成功, 错误,空
    }
}
