package cn.demoz.www.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.List;

import cn.demoz.www.holder.BaseHolder;
import cn.demoz.www.holder.MoreHolder;
import cn.demoz.www.manager.ThreadManager;
import cn.demoz.www.tools.UiUtils;

public abstract class DefaultAdapter<Data> extends BaseAdapter implements OnItemClickListener {
    protected List<Data> datas;
    private static final int DEFAULT_ITEM = 0;
    private static final int MORE_ITEM = 1;
    private ListView lv;

    public List<Data> getDatas() {
        return datas;
    }

    public void setDatas(List<Data> datas) {
        this.datas = datas;
    }

    public DefaultAdapter(List<Data> datas, ListView lv) {
        this.datas = datas;
        // 给ListView设置条目的点击事件
        lv.setOnItemClickListener(this);
        this.lv = lv;
    }

    // ListView 条目点击事件回调的方法
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        //Toast.makeText(UiUtils.getContext(), "position:"+position, 0).show();
        position = position - lv.getHeaderViewsCount();// 获取到顶部条目的数量   位置去掉顶部view的数量
        onInnerItemClick(position);
    }

    /**
     * 在该方法去处理条目的点击事件
     */
    public void onInnerItemClick(int position) {

    }

    @Override
    public int getCount() {
        return datas.size() + 1; // 最后的一个条目 就是加载更多的条目
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    /**
     * 根据位置 判断当前条目是什么类型
     */
    @Override
    public int getItemViewType(int position) {  //20
        if (position == datas.size()) { // 当前是最后一个条目
            return MORE_ITEM;
        }
        return getInnerItemViewType(position); // 如果不是最后一个条目 返回默认类型
    }

    protected int getInnerItemViewType(int position) {
        return DEFAULT_ITEM;
    }

    /**
     * 当前ListView 有几种不同的条目类型
     */
    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount() + 1; // 2 有两种不同的类型
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        BaseHolder holder = null;

        switch (getItemViewType(position)) {  // 判断当前条目时什么类型
            case MORE_ITEM:
                if (convertView == null) {
                    holder = getMoreHolder();
                } else {
                    holder = (BaseHolder) convertView.getTag();
                }
                break;
            default:
                if (convertView == null) {
                    holder = getHolder();
                } else {
                    holder = (BaseHolder) convertView.getTag();
                }
                if (position < datas.size()) {
                    holder.setData(datas.get(position));
                }
                break;
        }
        return holder.getContentView();  //  如果当前Holder 恰好是MoreHolder  证明MoreHOlder已经显示
    }

    private MoreHolder holder;

    private BaseHolder getMoreHolder() {
        if (holder != null) {
            return holder;
        } else {
            holder = new MoreHolder(this, hasMore());
            return holder;
        }
    }

    /**
     * 是否有额外的数据
     *
     * @return
     */
    protected boolean hasMore() {
        return true;
    }

    protected abstract BaseHolder<Data> getHolder();

    /**
     * 当加载更多条目显示的时候 调用该方法
     */
    public void loadMore() {
        ThreadManager.getInstance().createLongPool().execute(new Runnable() {

            @Override
            public void run() {
                // 在子线程中加载更多
                final List<Data> newData = onload();
                UiUtils.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (newData == null) {
                            holder.setData(MoreHolder.LOAD_ERROR);//
                        } else if (newData.size() == 0) {
                            holder.setData(MoreHolder.HAS_NO_MORE);
                        } else {
                            // 成功了
                            holder.setData(MoreHolder.HAS_MORE);
                            datas.addAll(newData);//  给listView之前的集合添加一个新的集合
                            notifyDataSetChanged();// 刷新界面

                        }

                    }
                });


            }
        });
    }

    /**
     * 加载更多数据
     */
    protected abstract List<Data> onload();

}
