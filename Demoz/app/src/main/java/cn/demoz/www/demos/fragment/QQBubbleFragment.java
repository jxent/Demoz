package cn.demoz.www.demos.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import cn.demoz.www.base.BaseDemosFragment;
import cn.demoz.www.demos.view.GooView;
import cn.demoz.www.view.LargeImageView;

public class QQBubbleFragment extends BaseDemosFragment {

    private LargeImageView mLargeImageView;
    private String mDemoDesc = "效果仿QQ未读消息小气泡，拖拽出一定范围外，自动断开，可以用在未读通知等控件处...";

    @Override
    public View setDemoContentView(Context context, LayoutInflater inflater) {



        GooView gooView = new GooView(context);
//        gooView.setInitXY(200, 200);

        return gooView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean childWantToCloseElastic(){
        return true;
    }

    @Override
    protected String getDemoDesc() {
        return mDemoDesc != null ? mDemoDesc : "";
    }
}
