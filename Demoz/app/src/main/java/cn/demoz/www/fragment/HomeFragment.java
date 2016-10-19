package cn.demoz.www.fragment;

import android.view.View;

import com.lidroid.xutils.bitmap.PauseOnScrollListener;

import java.util.List;

import cn.demoz.www.R;
import cn.demoz.www.adapter.ListBaseAdapter;
import cn.demoz.www.bean.AppInfo;
import cn.demoz.www.holder.HomePictureHolder;
import cn.demoz.www.protocol.HomeProtocol;
import cn.demoz.www.tools.UiUtils;
import cn.demoz.www.view.BaseListView;
import cn.demoz.www.view.LoadingPage;

public class HomeFragment extends BaseFragment {
    private List<AppInfo> datas;
    private List<String> pictures; //  顶部ViewPager 显示界面的数据

    // 当Fragment挂载的activity创建的时候调用
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        show();
//    }

    public View createSuccessView() {
        // 设置头部轮播
        BaseListView listView = new BaseListView(UiUtils.getContext());
        HomePictureHolder holder = new HomePictureHolder();
        holder.setData(pictures);
        View contentView = holder.getContentView(); // 得到holder里面管理的view对象
        //contentView.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
        listView.addHeaderView(contentView); // 把holder里的view对象 添加到listView的上面

        listView.setAdapter(new ListBaseAdapter(datas, listView) {

            @Override
            protected List<AppInfo> onload() {
                HomeProtocol protocol = new HomeProtocol();
                List<AppInfo> load = protocol.load(datas.size());
                datas.addAll(load);
                return load;
            }
        });

        // 第二个参数 慢慢滑动的时候是否加载图片 false  加载   true 不加载
        //  第三个参数  飞速滑动的时候是否加载图片  true 不加载
        listView.setOnScrollListener(new PauseOnScrollListener(bitmapUtils, false, true));
        bitmapUtils.configDefaultLoadingImage(R.drawable.ic_default);  // 设置如果图片加载中显示的图片
        bitmapUtils.configDefaultLoadFailedImage(R.drawable.ic_default);// 加载失败显示的图片

        return listView;
    }

    public LoadingPage.LoadResult load() {
        HomeProtocol protocol = new HomeProtocol();
        datas = protocol.load(0);  // load index 从哪个位置开始获取数据   0  20  40 60
        pictures = protocol.getPictures();
        return checkData(datas);
    }
}
