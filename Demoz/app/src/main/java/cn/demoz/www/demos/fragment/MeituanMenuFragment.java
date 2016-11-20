package cn.demoz.www.demos.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.demoz.www.base.BaseDemosFragment;
import cn.demoz.www.view.LargeImageView;

public class MeituanMenuFragment extends BaseDemosFragment {

    private LargeImageView mLargeImageView;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.huge_image_view_activity);
//
//        mLargeImageView = (LargeImageView) findViewById(R.id.id_large_image);
//        try {
//            InputStream inputStream = getAssets().open("world_map.jpg");
//            mLargeImageView.setInputStream(inputStream);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        TextView textView = new TextView(getActivity());
        textView.setText("Meituan menu");
//        return super.onCreateView(inflater, container, savedInstanceState);
        return textView;
    }

    @Override
    public View setDemoContentView(Context context, LayoutInflater inflater) {
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
