package cn.demoz.www.app;

import android.annotation.SuppressLint;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import cn.demoz.www.R;
import cn.demoz.www.base.BaseActivity;
import cn.demoz.www.fragment.BaseFragment;
import cn.demoz.www.fragment.FragmentFactory;
import cn.demoz.www.holder.MenuHolder;
import cn.demoz.www.tools.UiUtils;

public class MainActivity extends BaseActivity implements
        SearchView.OnQueryTextListener {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ViewPager mViewPager;
    private PagerTabStrip pager_tab_strip;
    private String[] tab_names;  // 标签的名字
    private FrameLayout fl_menu; // 菜单的根布局

    @Override
    protected void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        /*
		 *	1）显示Navigation Drawer的 Activity 对象
			2）DrawerLayout 对象
			3）一个用来指示Navigation Drawer的 drawable资源
			4）一个用来描述打开Navigation Drawer的文本 (用于支持可访问性)。
			5）一个用来描述关闭Navigation Drawer的文本(用于支持可访问性). 
		 */
        drawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout, R.drawable.ic_drawer_am, R.string.open_drawer,
                R.string.close_drawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                Toast.makeText(getApplicationContext(), "抽屉关闭了", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Toast.makeText(getApplicationContext(), "抽屉打开了", Toast.LENGTH_SHORT).show();
            }

        };
        //  让开关和actionbar建立关系
        drawerToggle.syncState();
        mDrawerLayout.setDrawerListener(drawerToggle);

    }

    @Override
    protected void init() {
        tab_names = UiUtils.getStringArray(R.array.tab_names);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_main);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl);
        mViewPager = (ViewPager) findViewById(R.id.vp);
        mViewPager.setAdapter(new MainAdapter(getSupportFragmentManager()));
        mViewPager.setOnPageChangeListener( new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                BaseFragment createFragment = FragmentFactory.getFragment(position);
                createFragment.show();//  当切换界面的时候 重新请求服务器
            }

        });

        pager_tab_strip = (PagerTabStrip) findViewById(R.id.pager_tab_strip);
        //  设置标签下划线的颜色
        pager_tab_strip.setTabIndicatorColor(getResources().getColor(R.color.indicatorcolor));

        // 添加菜单
        fl_menu = (FrameLayout) findViewById(R.id.fl_menu);
        MenuHolder holder = new MenuHolder();
        //  之前已经登录过了
        //holder.setData(data)
        fl_menu.addView(holder.getContentView());
    }

    // 继承FragmentStatePagerAdapter
    private class MainAdapter extends FragmentStatePagerAdapter {
        public MainAdapter(FragmentManager fm) {
            super(fm);
        }

        // 每个条目返回的fragment
        //  0
        @Override
        public Fragment getItem(int position) {
            //  通过Fragment工厂  生产Fragment
            return FragmentFactory.getFragment(position);
        }

        // 一共有几个条目
        @Override
        public int getCount() {
            return tab_names.length;
        }

        // 返回每个条目的标题
        @Override
        public CharSequence getPageTitle(int position) {
            return tab_names[position];
        }
    }

    @SuppressLint("NewApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        // 如果运行的环境 (部署到什么版本的手机 )大于3.0
        if (android.os.Build.VERSION.SDK_INT > 11) {
            SearchView searchView = (SearchView) menu.findItem(
                    R.id.action_search).getActionView();
            searchView.setOnQueryTextListener(this);// 搜索的监听
        }
        return true;
    }


    /**
     * 处理actionBar菜单条目的点击事件
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            Toast.makeText(getApplicationContext(), "搜索", Toast.LENGTH_SHORT).show();
        }
        return drawerToggle.onOptionsItemSelected(item) | super.onOptionsItemSelected(item);
    }

    // 当搜索提交的时候
    @Override
    public boolean onQueryTextSubmit(String query) {
        Toast.makeText(getApplicationContext(), query, Toast.LENGTH_SHORT).show();
        return true;
    }

    // 当搜索的文本发生变化
    @Override
    public boolean onQueryTextChange(String newText) {
        return true;
    }
}
