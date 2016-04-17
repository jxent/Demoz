package cn.demoz.j.fragment;


import android.view.View;

import java.util.List;

import cn.demoz.j.adapter.ListBaseAdapter;
import cn.demoz.j.domain.AppInfo;
import cn.demoz.j.protocol.GameProtocol;
import cn.demoz.j.tools.UiUtils;
import cn.demoz.j.view.BaseListView;
import cn.demoz.j.view.LoadingPage;

public class GameFragment extends BaseFragment {

    private List<AppInfo> datas;

    /**
     * 加载成功的界面
     */
    @Override
    public View createSuccessView() {
        BaseListView listView = new BaseListView(UiUtils.getContext());
        listView.setAdapter(new ListBaseAdapter(datas, listView) {

            @Override
            protected List<AppInfo> onload() {
                GameProtocol protocol = new GameProtocol();
                List<AppInfo> load = protocol.load(datas.size());  //
                datas.addAll(load);
                return load;
            }

        });
        return listView;
    }

    /**
     * 请求服务器加载数据
     */
    @Override
    protected LoadingPage.LoadResult load() {
        GameProtocol protocol = new GameProtocol();
        datas = protocol.load(0);
        return checkData(datas);
    }
}
