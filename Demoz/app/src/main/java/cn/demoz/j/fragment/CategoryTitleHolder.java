package cn.demoz.j.fragment;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import cn.demoz.j.R;
import cn.demoz.j.domain.CategoryInfo;
import cn.demoz.j.holder.BaseHolder;
import cn.demoz.j.tools.UiUtils;

public class CategoryTitleHolder extends BaseHolder<CategoryInfo> {

    private TextView tv;

    @Override
    public View initView() {
        tv = new TextView(UiUtils.getContext());
        tv.setTextColor(Color.BLACK);
        tv.setBackgroundDrawable(UiUtils.getDrawalbe(R.drawable.grid_item_bg));
        return tv;
    }

    @Override
    public void refreshView(CategoryInfo data) {
        tv.setText(data.getTitle());
    }

}
