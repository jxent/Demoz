package cn.demoz.www.fragment;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import cn.demoz.www.R;
import cn.demoz.www.bean.CategoryInfo;
import cn.demoz.www.holder.BaseHolder;
import cn.demoz.www.tools.UiUtils;

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
