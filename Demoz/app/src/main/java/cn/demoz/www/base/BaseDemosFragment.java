package cn.demoz.www.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

import cn.demoz.www.R;
import cn.demoz.www.demos.view.ElasticScrollView;

public abstract class BaseDemosFragment extends Fragment {

    private static final long ANIM_DURATION = 400;      // 动画的执行时间
    protected static final int DEMO_FOLDED_MAX_HEIGHT = 150;    // 底部描述展开的最大高度

    private boolean isDemoDescOpen = false;     // 是否是打开状态
    protected int demoFoldedHeight;         // 测量和布局结束后，计算出空间的初始高度

    protected ElasticScrollView demoContent;
    protected LinearLayout demoDesc;

    private Context mContext;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        mContext = getActivity();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_demos_common_fragment, null);
        demoContent = (ElasticScrollView) view.findViewById(R.id.demo_content);
        demoDesc = (LinearLayout) view.findViewById(R.id.demo_desc);
        if(isDemoDescShown()){
            demoDesc.setVisibility(View.GONE);
        }
        if(demoContent.getChildCount() != 0){
            demoContent.removeAllViews();
        }
        View demoView = setDemoContentView(mContext, inflater);

        if(childWantToCloseElastic()){
            demoContent.setSwitcher(false);
        }

        if(demoView != null) {
            demoContent.addView(demoView);
            // TODO 这一步很关键，因为这里的父view是ScrollView，如果不设定时，子View的高度mode会是Unsupported，不知道为什么，从而measuredHeight是0，显示不出来
            demoContent.setFillViewport(true);
        }
        return view;
    }

    // 如果子View不想有弹性效果，覆写此方法，并返回true
    protected boolean childWantToCloseElastic(){
        return false;
    }

    protected boolean isDemoDescShown(){
        return false;
    }

    public abstract View setDemoContentView(Context context, LayoutInflater inflater);


    /**
     * 打开、关闭描述信息的动画方法
     */
    public void toggleDemoDesc(LinearLayout toggleView){
        final LinearLayout view = toggleView;
        if(view == null){
            return;
        }

        final int startHeight;
        final int targetHeight;

        if(isDemoDescOpen){
            startHeight = DEMO_FOLDED_MAX_HEIGHT;
            targetHeight = demoFoldedHeight;
            isDemoDescOpen = false; // 如果是打开的，关闭
        }else {
            startHeight = demoFoldedHeight;
            targetHeight = DEMO_FOLDED_MAX_HEIGHT;
            isDemoDescOpen = true;  // 如果是关闭的，打开
        }

        // 在View上执行动画的对象
        ValueAnimator animator = ObjectAnimator.ofInt(startHeight, targetHeight);
        final ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        // [动画值变化]的监听器
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // 监听值的变化
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                int value = (Integer) animator.getAnimatedValue();  // 运行当前时间点的一个值
                Log.e("jason", "动画的值：" + value);
                layoutParams.height = value;
                view.setLayoutParams(layoutParams);   // 刷新界面
            }
        });
        // [动画状态]的监听器
        animator.addListener(new ValueAnimator.AnimatorListener(){

            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        animator.setDuration(ANIM_DURATION);
        animator.start();
    }

}
