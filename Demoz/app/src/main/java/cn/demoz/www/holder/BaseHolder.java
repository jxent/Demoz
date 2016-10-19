package cn.demoz.www.holder;

import android.view.View;

import com.lidroid.xutils.BitmapUtils;

import cn.demoz.www.tools.BitmapHelper;

/**
 * 总体上，BaseHolder接受数据集合，返回view视图
 * Model --> View 吸收数据Model，转化为View对象
 * @param <Data>
 */
public abstract class BaseHolder<Data> {
    private View contentView;
    private Data data;
    protected BitmapUtils bitmapUtils;

    public BaseHolder() {
        bitmapUtils = BitmapHelper.getBitmapUtils();
        contentView = initView();
        contentView.setTag(this);
    }

    /**
     * 创建界面
     */
    public abstract View initView();

    public View getContentView() {
        return contentView;
    }

    public void setData(Data data) {
        this.data = data;
        refreshView(data);
    }

    /**
     * 根据数据刷新界面
     */
    public abstract void refreshView(Data data);
}
