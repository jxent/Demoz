package cn.demoz.www.demos.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.jason.slog.SLog;

import java.io.IOException;
import java.io.InputStream;

import cn.demoz.www.base.BaseDemosFragment;
import cn.demoz.www.view.LargeImageView;

public class HugeImageViewFragment extends BaseDemosFragment {

    private String mDemoDesc = "显示一张巨图的demo，用到BitmapRegionDecoder，参考：http://blog.csdn.net/lmj623565791/article/details/49300989";
    @Override
    public View setDemoContentView(Context context, LayoutInflater inflater) {
        LargeImageView largeImageView = new LargeImageView(context);
        largeImageView.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        try {
            InputStream inputStream = getActivity().getAssets().open("world_map.jpg");
            largeImageView.setInputStream(inputStream);

        } catch (IOException e) {
            SLog.e("HugeImageViewFragment", "读取图片 world_map.jpg 错误！");
            e.printStackTrace();
        }

        return largeImageView;
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
