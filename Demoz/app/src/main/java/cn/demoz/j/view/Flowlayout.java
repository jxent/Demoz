package cn.demoz.j.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.demoz.j.tools.UiUtils;

public class FlowLayout extends ViewGroup {
    private int horizontalSpacing = UiUtils.dip2px(13);
    private int verticalSpacing = UiUtils.dip2px(13);

    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private Line currentLine;// 当前的行
    private int usedWidth = 0;// 当前行使用的宽度，用于判断是否需要换行
    private List<Line> mLines = new ArrayList<Line>();
    private int parentWidth;

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // 测量 当前控件FlowLayout 父类是有义务测量每个孩子的
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mLines.clear();
        currentLine = null;
        usedWidth = 0;
        // 获取当前父容器(FlowLayout)的模式
        int parentWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        int parentHeightMode = MeasureSpec.getMode(heightMeasureSpec);
        // 获取当前父容器(FlowLayout)的尺寸
        parentWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        int height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingBottom() - getPaddingTop();

        int childWidthMode;
        int childHeightMode;
        //  为了测量每个孩子 需要指定每个孩子测量规则(子类添加的是TextView，并且是包裹内容wrap_content的)
        //
        //  如果（父类可能性 match_parent、100dp | wrap_content）
        //                  父类模式         EXACTLY          AT_MOST          UNSUPPORTED
        //  那么（子类固定是 wrap_content）
        //                  子类模式         AT_MOST          AT_MOST          UNSUPPORTED
        //                  子类大小       parent_size       parent_size            0

        childWidthMode = parentWidthMode == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST : parentWidthMode;
        childHeightMode = parentHeightMode == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST : parentHeightMode;

        // 因为添加的子view都是TextView，规则都相同，每个子view共用相同的MeasureSpec
        int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthMode, parentWidth);
        int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeightMode, height);

        currentLine = new Line();// 创建了第一行
        Log.e("jason", "children count is :" + getChildCount());
        // 循环调用子view的measure方法
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            // 调用子view的measure方法，measure-->onMeasure 这样将测量传递下去
            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);

            // 依据子View的宽度，进行换行操作
            int measuredWidth = child.getMeasuredWidth();
            usedWidth += measuredWidth;
            if (usedWidth <= parentWidth) { // 加上子view宽度和父view宽度比较
                currentLine.addChild(child);
                usedWidth += horizontalSpacing; // 加上间隔宽度再和父view宽度比较
                if (usedWidth > parentWidth) {
                    //换行
                    newLine();
                }
            } else {
                //换行
                if (currentLine.getChildCount() < 1) {
                    currentLine.addChild(child);  // 保证当前行里面最少有一个孩子
                    newLine();
                }else {
                    newLine();
                    currentLine.addChild(child);
                    usedWidth += measuredWidth;
                    usedWidth += horizontalSpacing;
                }
//                newLine();
            }
        }
        if (!mLines.contains(currentLine)) {
            mLines.add(currentLine);// 添加最后一行
        }
        int totalHeight = 0;
        for (Line line : mLines) {
            totalHeight += line.getHeight();
        }
        totalHeight += verticalSpacing * (mLines.size() - 1) + getPaddingTop() + getPaddingBottom();

        Log.e("jason", "计算得到的总高度：" + totalHeight);
        setMeasuredDimension(parentWidth + getPaddingLeft() + getPaddingRight(),
                resolveSize(totalHeight, heightMeasureSpec));
    }

    private void newLine() {
        mLines.add(currentLine);// 记录之前的行
        currentLine = new Line(); // 创建新的一行
        usedWidth = 0;
    }

    // 分配每个孩子的位置
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        l += getPaddingLeft();
        t += getPaddingTop();
        for (int i = 0; i < mLines.size(); i++) {
            Line line = mLines.get(i);
            line.layout(l, t);  //交给每一行去分配
            t += line.getHeight() + verticalSpacing;
        }
    }

    private class Line {
        int height = 0; //当前行的高度
        int lineWidth = 0;
        private List<View> children = new ArrayList<View>();

        /**
         * 添加一个孩子
         *
         * @param child
         */
        public void addChild(View child) {
            children.add(child);
            if (child.getMeasuredHeight() > height) {
                height = child.getMeasuredHeight();
            }
            lineWidth += child.getMeasuredWidth();
        }

        public int getHeight() {
            return height;
        }

        /**
         * 返回孩子的数量
         *
         * @return
         */
        public int getChildCount() {
            return children.size();
        }

        public void layout(int l, int t) {
            lineWidth += horizontalSpacing * (children.size() - 1);
            int surplusChild = 0;
            int surplus = parentWidth - lineWidth;
            if (surplus > 0 && children.size() > 0) {
                surplusChild = surplus / children.size();
            }
            for (int i = 0; i < children.size(); i++) {
                View child = children.get(i);
                //  getMeasuredWidth()   控件实际的大小
                // getWidth()  控件显示的大小
                child.layout(l, t, l + child.getMeasuredWidth() + surplusChild, t + child.getMeasuredHeight());
                l += child.getMeasuredWidth() + surplusChild;
                l += horizontalSpacing;
            }
        }

    }
}
