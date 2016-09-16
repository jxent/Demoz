package cn.demoz.j.demoactivity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import java.io.IOException;
import java.io.InputStream;

import cn.demoz.j.R;
import cn.demoz.j.view.LargeImageView;

public class HugeImageViewActivity extends ActionBarActivity {

    private LargeImageView mLargeImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.huge_image_view_activity);

        mLargeImageView = (LargeImageView) findViewById(R.id.id_large_image);
        try {
            InputStream inputStream = getAssets().open("world_map.jpg");
            mLargeImageView.setInputStream(inputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
