package cn.demoz.www.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import cn.demoz.www.R;
import cn.demoz.www.base.BaseDemosFragment;

/**
 * Created by Jason on 2016/10/16.
 */
public class UnknownFragment extends BaseDemosFragment {

    private String mDemoDesc = "";

    @Override
    public View setDemoContentView(Context context, LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.layout_unknown_fragment, null);
        return view;
    }

    @Override
    protected boolean isDemoDescShown() {
        return false;
    }

    @Override
    protected String getDemoDesc() {
        return mDemoDesc != null ? mDemoDesc : "";
    }
}
