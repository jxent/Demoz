package cn.demoz.www.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Random;

import cn.demoz.www.R;
import cn.demoz.www.bean.FlowDemosItemBean;
import cn.demoz.www.demos.activity.FlowDemosShowingActivity;
import cn.demoz.www.protocol.DemosProtocol;
import cn.demoz.www.tools.DrawableUtils;
import cn.demoz.www.tools.UiUtils;
import cn.demoz.www.view.FlowLayout;
import cn.demoz.www.view.LoadingPage;

public class FlowDemosFragment extends BaseFragment {

    private SparseArray<FlowDemosItemBean> datas;

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
        FlowLayout flowLayout = new FlowLayout(UiUtils.getContext());
        int padding = UiUtils.dip2px(13);
        flowLayout.setPadding(padding, padding, padding, padding);
        //layout.setOrientation(LinearLayout.VERTICAL);// 设置线性布局的方向
        int backColor = 0xffcecece;
        Drawable pressedDrawable = DrawableUtils.createShape(backColor);// 按下显示的图片
        // 遍历json解析后的对象数组
        for (int i = 0; i < datas.size(); i++) {
            final TextView textView = new TextView(UiUtils.getContext());
            final FlowDemosItemBean bean = datas.get(datas.keyAt(i));
            textView.setText(bean.getName());
            textView.setTag(bean);

            Random random = new Random();   //创建随机
            int red = random.nextInt(200) + 22;
            int green = random.nextInt(200) + 22;
            int blue = random.nextInt(200) + 22;
            int color = Color.rgb(red, green, blue);//范围 0-255
            GradientDrawable createShape = DrawableUtils.createShape(color); // 默认显示的图片
            StateListDrawable selectorDrawable = DrawableUtils.createSelectorDrawable(pressedDrawable, createShape);// 创建状态选择器
            textView.setBackgroundDrawable(selectorDrawable);
            textView.setTextColor(Color.WHITE);
            //textView.setTextSize(UiUtils.dip2px(14));
            int textPaddingV = UiUtils.dip2px(4);
            int textPaddingH = UiUtils.dip2px(7);
            textView.setPadding(textPaddingH, textPaddingV, textPaddingH, textPaddingV); //设置padding
            textView.setGravity(Gravity.CENTER);
            textView.setClickable(true);//设置textView可以被点击
            textView.setOnClickListener(new OnClickListener() {  // 设置点击事件

                @Override
                public void onClick(View v) {
                    // 将所有操作放到工具类中处理
                    Intent intent = new Intent(getActivity(), FlowDemosShowingActivity.class);
                    intent.putExtra("fragment_bean", (FlowDemosItemBean)textView.getTag());
                    getActivity().startActivity(intent);
//                    FlowDemosShowingIntentOperation.toDemosFragmentOperation(getActivity(), (int)textView.getTag());
                }
            });
            flowLayout.addView(textView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));// -2 包裹内容
        }

        if(flowLayout.getLayoutParams() == null){
            Log.e("jason", "flowLayout's LayoutParams is null");
        }

        // 这里FlowLayout并没有自己的LayoutParams，在被添加到scrollView中后，会被添加一个默认的LayoutParams(宽和高都是wrap_content -1)，但之后肯定被做了拉伸处理，
        // flowLayout的LayoutParams会变成match_parent(-2)
        scrollView.addView(flowLayout);
        ViewGroup.LayoutParams params = flowLayout.getLayoutParams();
        Log.e("jason", "flowLayout params.width : " + params.width);
        Log.e("jason", "flowLayout params.height : " + params.height);
        return scrollView;
    }

    @Override
    protected LoadingPage.LoadResult load() {
        DemosProtocol protocol = new DemosProtocol();
        datas = protocol.load(0);
        return checkData(datas);
    }

    /**
     * 校验数据
     */
    public LoadingPage.LoadResult checkData(SparseArray datas) {
        if (datas == null) {
            return LoadingPage.LoadResult.error;//  请求服务器失败
        } else {
            if (datas.size() == 0) {
                return LoadingPage.LoadResult.empty;
            } else {
                return LoadingPage.LoadResult.success;
            }
        }

    }
}
