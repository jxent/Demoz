package cn.demoz.j.adapter;

import android.content.Intent;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import cn.demoz.j.base.DetailActivity;
import cn.demoz.j.domain.AppInfo;
import cn.demoz.j.holder.BaseHolder;
import cn.demoz.j.holder.ListBaseHolder;
import cn.demoz.j.tools.UiUtils;

public abstract class ListBaseAdapter extends DefaultAdapter<AppInfo> {
    public ListBaseAdapter(List<AppInfo> datas, ListView listView) {
        super(datas, listView);
    }

    @Override
    protected BaseHolder<AppInfo> getHolder() {
        return new ListBaseHolder();
    }

    @Override
    protected abstract List<AppInfo> onload();


    @Override
    public void onInnerItemClick(int position) {
        super.onInnerItemClick(position);
        Toast.makeText(UiUtils.getContext(), "position:" + position, Toast.LENGTH_SHORT).show();
        AppInfo appInfo = datas.get(position);
        Intent intent = new Intent(UiUtils.getContext(), DetailActivity.class);
        intent.putExtra("packageName", appInfo.getPackageName());
        UiUtils.startActivity(intent);
    }
}
