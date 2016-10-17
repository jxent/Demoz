package cn.demoz.j.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.demoz.j.R;
import cn.demoz.j.adapter.DefaultAdapter;
import cn.demoz.j.bean.SubjectInfo;
import cn.demoz.j.holder.BaseHolder;
import cn.demoz.j.http.HttpHelper;
import cn.demoz.j.protocol.SubjectProtocol;
import cn.demoz.j.tools.UiUtils;
import cn.demoz.j.view.BaseListView;
import cn.demoz.j.view.LoadingPage;

public class SubjectFragment extends BaseFragment {

    private List<SubjectInfo> datas;

    @Override
    public View createSuccessView() {
        BaseListView listView = new BaseListView(UiUtils.getContext());
        listView.setAdapter(new SubjectAdapter(datas, listView));
        return listView;
    }

    private class SubjectAdapter extends DefaultAdapter<SubjectInfo> {

        public SubjectAdapter(List<SubjectInfo> datas, ListView lv) {
            super(datas, lv);
        }

        @Override
        protected BaseHolder<SubjectInfo> getHolder() {
            return new SubjectHolder();
        }

        @Override
        protected List<SubjectInfo> onload() {
            SubjectProtocol protocol = new SubjectProtocol();
            List<SubjectInfo> load = protocol.load(datas.size());
            datas.addAll(load);
            return load;
        }

        @Override
        public void onInnerItemClick(int position) {
            super.onInnerItemClick(position);
            Toast.makeText(UiUtils.getContext(), datas.get(position).getDes(), Toast.LENGTH_SHORT).show();
        }

    }

    class SubjectHolder extends BaseHolder<SubjectInfo> {
        ImageView item_icon;
        TextView item_txt;

        @Override
        public View initView() {
            View contentView = UiUtils.inflate(R.layout.item_subject);
            this.item_icon = (ImageView) contentView.findViewById(R.id.item_icon);
            this.item_txt = (TextView) contentView.findViewById(R.id.item_txt);
            return contentView;
        }

        @Override
        public void refreshView(SubjectInfo data) {
            this.item_txt.setText(data.getDes());
            bitmapUtils.display(this.item_icon, HttpHelper.URL + "image?name=" + data.getUrl());
        }
    }

    @Override
    protected LoadingPage.LoadResult load() {
        SubjectProtocol protocol = new SubjectProtocol();
        datas = protocol.load(0);
        return checkData(datas);
    }


}
