package cn.demoz.www.demos.fragment;

import android.view.LayoutInflater;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;

import cn.demoz.www.R;
import cn.demoz.www.base.BaseDemosFragment;
import cn.demoz.www.view.LargeImageView;

public class HugeImageViewFragment extends BaseDemosFragment {

    private LargeImageView mLargeImageView;

    @Override
    public View setDemoContentView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.huge_image_view_activity, null);
        mLargeImageView = (LargeImageView) view.findViewById(R.id.id_large_image);
        try {
            InputStream inputStream = getActivity().getAssets().open("world_map.jpg");
            mLargeImageView.setInputStream(inputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return view;
    }


}
