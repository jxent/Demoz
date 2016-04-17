package cn.demoz.j.view;

import android.content.Context;

import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import cn.demoz.j.R;
import cn.demoz.j.manager.ThreadManager;
import cn.demoz.j.tools.UiUtils;

/***
 * 创建了自定义帧布局 把baseFragment 一部分代码 抽取到这个类中
 *
 * @author jason
 */
public abstract class LoadingPage extends FrameLayout {

    public static final int STATE_UNKOWN = 0;   // 未知状态
    public static final int STATE_LOADING = 1;  // 加载中状态
    public static final int STATE_ERROR = 2;    // 加载出错状态
    public static final int STATE_EMPTY = 3;    // 空页面状态
    public static final int STATE_SUCCESS = 4;  // 成功状态
    public int state = STATE_UNKOWN;            // 位置状态

    private View loadingView;   // 加载中的界面
    private View errorView;     // 错误界面
    private View emptyView;     // 空界面
    private View successView;   // 加载成功的界面

    public LoadingPage(Context context) {
        super(context);
        init();
    }

    public LoadingPage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public LoadingPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /* 创建所有加载中、失效等状态页面 */
    private void init() {
        loadingView = createLoadingView();  // 创建了加载中的界面
        if (loadingView != null) {
            this.addView(loadingView, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        }
        errorView = createErrorView();      // 加载错误界面
        if (errorView != null) {
            this.addView(errorView, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        }
        emptyView = createEmptyView();      // 加载空的界面
        if (emptyView != null) {
            this.addView(emptyView, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        }
        showPage();// 根据不同的状态显示不同的界面
    }

    /* 根据不同的状态显示不同的界面 */
    private void showPage() {
        if (loadingView != null) {
            loadingView.setVisibility(state == STATE_UNKOWN
                    || state == STATE_LOADING ? View.VISIBLE : View.INVISIBLE);
        }
        if (errorView != null) {
            errorView.setVisibility(state == STATE_ERROR ? View.VISIBLE
                    : View.INVISIBLE);
        }
        if (emptyView != null) {
            emptyView.setVisibility(state == STATE_EMPTY ? View.VISIBLE
                    : View.INVISIBLE);
        }
        // 加载成功界面
        if (state == STATE_SUCCESS) {
            if (successView == null) {
                successView = createSuccessView();
                this.addView(successView, new LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            }
            successView.setVisibility(View.VISIBLE);
        } else {
            if (successView != null) {
                successView.setVisibility(View.INVISIBLE);
            }
        }
    }

    /* 创建了空的界面 */
    private View createEmptyView() {
        View view = View.inflate(UiUtils.getContext(), R.layout.loadpage_empty, null);
        return view;
    }

    /* 创建了错误界面 */
    private View createErrorView() {
        View view = View.inflate(UiUtils.getContext(), R.layout.loadpage_error, null);
        Button page_bt = (Button) view.findViewById(R.id.page_bt);
        page_bt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                show();
            }
        });
        return view;
    }

    /* 创建加载中的界面 */
    private View createLoadingView() {
        return View.inflate(UiUtils.getContext(), R.layout.loadpage_loading, null);
    }

    /* 加载结果 */
    public enum LoadResult {
        error(2), empty(3), success(4);

        int value;

        LoadResult(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

    }

    /**
     *  使用线程池发起请求服务器的请求
     *  根据服务器的数据 切换状态
     */
    public void show() {
        if (state == STATE_ERROR || state == STATE_EMPTY) {
            state = STATE_LOADING;
        }
        // 请求服务器 获取服务器上数据 进行判断
        // 请求服务器 返回一个结果
        ThreadManager.getInstance().createLongPool().execute(new Runnable() {

            @Override
            public void run() {
                SystemClock.sleep(500);
                final LoadResult result = load();
                UiUtils.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (result != null) {
                            state = result.getValue();
                            showPage(); // 状态改变了,重新判断当前应该显示哪个界面
                        }
                    }
                });
            }
        });


        showPage();

    }

    /***
     * 创建成功的界面
     *
     * @return
     */
    public abstract View createSuccessView();

    /**
     * 连接、请求服务器数据
     *
     * @return
     */
    protected abstract LoadResult load();
}
