package cn.demoz.www.demos.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.widget.FrameLayout;

import cn.demoz.www.R;
import cn.demoz.www.bean.FlowDemosItemBean;
import cn.demoz.www.fragment.FragmentFactory;
import cn.demoz.www.view.LargeImageView;

public class FlowDemosShowingActivity extends ActionBarActivity {

    private LargeImageView mLargeImageView;
    private FrameLayout content;

    FlowDemosItemBean tokenBean;
    Fragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flow_demos_showing_layout);

        if(getIntent() != null){
            tokenBean = (FlowDemosItemBean) getIntent().getSerializableExtra("fragment_bean");
        }
        // 通过key得到Fragment
        if(tokenBean != null) {
            fragment = FragmentFactory.getFlowDemosShowingFragment(tokenBean);
        }

        content = (FrameLayout)  findViewById(R.id.content);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.content, fragment).commit();



    }

}
