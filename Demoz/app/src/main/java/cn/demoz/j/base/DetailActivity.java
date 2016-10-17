package cn.demoz.j.base;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;

import cn.demoz.j.R;
import cn.demoz.j.bean.AppInfo;
import cn.demoz.j.holder.DetailBottomHolder;
import cn.demoz.j.holder.DetailDesHolder;
import cn.demoz.j.holder.DetailInfoHolder;
import cn.demoz.j.holder.DetailSafeHolder;
import cn.demoz.j.holder.DetailScreenHolder;
import cn.demoz.j.protocol.DetailProtocol;
import cn.demoz.j.tools.UiUtils;
import cn.demoz.j.view.LoadingPage;

public class DetailActivity extends BaseActivity {

    private String packageName;
    private AppInfo data;

    @Override
    protected void initView() {
        LoadingPage loadingPage = new LoadingPage(this) {
            @Override
            protected LoadResult load() {
                return DetailActivity.this.load();
            }

            @Override
            public View createSuccessView() {
                return DetailActivity.this.createSuccessView();
            }
        };
        loadingPage.show();  //  必须调用show方法 才会请求服务器 加载新的界面
        setContentView(loadingPage);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();  // 获取到打开当前activity的意图对象
        packageName = intent.getStringExtra("packageName");
        super.onCreate(savedInstanceState);


    }

    private FrameLayout bottom_layout, detail_info, detail_safe, detail_des;
    private HorizontalScrollView detail_screen;
    private DetailInfoHolder detailInfoHolder;
    private DetailScreenHolder screenHolder;
    private DetailSafeHolder safeHolder;
    private DetailDesHolder desHolder;
    private DetailBottomHolder bottomHolder;

    /**
     * 加载成功的界面
     *
     * @return
     */
    protected View createSuccessView() {
        View view = UiUtils.inflate(R.layout.activity_detail);
        // 添加信息区域
        bottom_layout = (FrameLayout) view.findViewById(R.id.bottom_layout);
        bottomHolder = new DetailBottomHolder();
        bottomHolder.setData(data);
        bottom_layout.addView(bottomHolder.getContentView());

        //  操作 应用程序信息
        detail_info = (FrameLayout) view.findViewById(R.id.detail_info);
        detailInfoHolder = new DetailInfoHolder();
        detailInfoHolder.setData(data);
        detail_info.addView(detailInfoHolder.getContentView());

        //安全标记
        detail_safe = (FrameLayout) view.findViewById(R.id.detail_safe);
        safeHolder = new DetailSafeHolder();
        safeHolder.setData(data);
        detail_safe.addView(safeHolder.getContentView());

        detail_des = (FrameLayout) view.findViewById(R.id.detail_des);
        desHolder = new DetailDesHolder();
        desHolder.setData(data);
        detail_des.addView(desHolder.getContentView());
        // 中间5张图片
        detail_screen = (HorizontalScrollView) view.findViewById(R.id.detail_screen);
        screenHolder = new DetailScreenHolder();
        screenHolder.setData(data);
        detail_screen.addView(screenHolder.getContentView());

        return view;
    }

    /**
     * 请求服务器加载数据
     *
     * @return
     */
    protected LoadingPage.LoadResult load() {
        DetailProtocol protocol = new DetailProtocol(packageName);
        data = protocol.load(0);
        if (data == null) {
            return LoadingPage.LoadResult.error;
        } else {
            return LoadingPage.LoadResult.success;
        }
    }

    @Override
    protected void initActionBar() {
        super.initActionBar();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}
