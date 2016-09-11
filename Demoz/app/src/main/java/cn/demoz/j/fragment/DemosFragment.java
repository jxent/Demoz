package cn.demoz.j.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

import cn.demoz.j.R;
import cn.demoz.j.demoactivity.HugeImageViewActivity;
import cn.demoz.j.protocol.DemosProtocol;
import cn.demoz.j.tools.DrawableUtils;
import cn.demoz.j.tools.UiUtils;
import cn.demoz.j.view.FlowLayout;
import cn.demoz.j.view.LoadingPage;

public class DemosFragment extends BaseFragment {

    private List<String> datas;

    // 当Fragment挂载的activity创建的时候调用
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        show();
    }

    @Override
    public View createSuccessView() {
        ScrollView scrollView = new ScrollView(UiUtils.getContext());
        scrollView.setBackgroundResource(R.drawable.grid_item_bg_normal);
        FlowLayout layout = new FlowLayout(UiUtils.getContext());
        int padding = UiUtils.dip2px(13);
        layout.setPadding(padding, padding, padding, padding);
        //layout.setOrientation(LinearLayout.VERTICAL);// 设置线性布局的方向
        int backColor = 0xffcecece;
        Drawable pressedDrawable = DrawableUtils.createShape(backColor);// 按下显示的图片
        for (int i = 0; i < datas.size(); i++) {
            final TextView textView = new TextView(UiUtils.getContext());
            final String str = datas.get(i);
            textView.setText(str);

            Random random = new Random();   //创建随机
            int red = random.nextInt(200) + 22;
            int green = random.nextInt(200) + 22;
            int blue = random.nextInt(200) + 22;
            int color = Color.rgb(red, green, blue);//范围 0-255
            GradientDrawable createShape = DrawableUtils.createShape(color); // 默认显示的图片
            StateListDrawable createSelectorDrawable = DrawableUtils.createSelectorDrawable(pressedDrawable, createShape);// 创建状态选择器
            textView.setBackgroundDrawable(createSelectorDrawable);
            textView.setTextColor(Color.WHITE);
            //textView.setTextSize(UiUtils.dip2px(14));
            int textPaddingV = UiUtils.dip2px(4);
            int textPaddingH = UiUtils.dip2px(7);
            textView.setPadding(textPaddingH, textPaddingV, textPaddingH, textPaddingV); //设置padding
            textView.setClickable(true);//设置textView可以被点击
            textView.setOnClickListener(new OnClickListener() {  // 设置点击事件

                @Override
                public void onClick(View v) {
                    String text = (String) textView.getText();
                    if ("加载大图的ImageView".equals(text)) {
                        startActivity(new Intent(UiUtils.getContext(), HugeImageViewActivity.class));
                    }
                }
            });
            layout.addView(textView, new LayoutParams(LayoutParams.WRAP_CONTENT, -2));// -2 包裹内容
        }

        scrollView.addView(layout);
        return scrollView;
    }

    @Override
    protected LoadingPage.LoadResult load() {
        DemosProtocol protocol = new DemosProtocol();
        datas = protocol.load(0);
        return checkData(datas);
    }
}
