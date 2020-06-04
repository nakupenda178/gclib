package com.github.guqt178.widgets.layout;


import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.github.guqt178.widgets.R;


/**
 * 类似viewpager,但不用写adapter,
 * 用法
 * <xxxx.PageView
 * app:p_page_one="@layout/page_one"
 * app:p_page_three="@layout/page_three"
 * app:p_disable_scroll="true" //true 只能代码左右切换
 * app:p_page_two="@layout/page_two"/>
 *
 * pageView.nextPage
 */
public class PageView extends HorizontalScrollView {
    // <editor-fold defaultstate="collapsed" desc="成员变量">
    private int mBaseScrollX;//滑动基线。也就是点击并滑动之前的x值，以此值计算相对滑动距离。
    private int mScrollX = 200;//滑动多长距离翻页

    private int mScreenWidth;
    private int mScreenHeight;

    private int mWidth = 0; // pageview的宽

    private LinearLayout mContainer;
    private int mPageCount;//页面数量
    private boolean disableScroll;//是否启用手动切换


    private LayoutInflater mInflater;
    private int pageOneLayoutId = -1; //第一个界面布局id
    private int pageTwoLayoutId = -1;//第一个界面布局id
    private int pageThreeLayoutId = -1;//第一个界面布局id
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="构造">

    public PageView(Context context) {
        this(context, null);
    }


    public PageView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public PageView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public PageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;

        //
        setHorizontalScrollBarEnabled(false);
        mInflater = LayoutInflater.from(context);

        parseAttrs(context, attrs);
        addContainer(context);

    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="内部方法">

    private void parseAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PageView);
        this.pageOneLayoutId = ta.getResourceId(R.styleable.PageView_p_page_one, -1);
        this.pageTwoLayoutId = ta.getResourceId(R.styleable.PageView_p_page_two, -1);
        this.pageThreeLayoutId = ta.getResourceId(R.styleable.PageView_p_page_three, -1);
        this.disableScroll = ta.getBoolean(R.styleable.PageView_p_disable_scroll, true);
        ta.recycle();
    }

    private void addContainer(Context context) {
        removeAllViews();
        LinearLayout container = new LinearLayout(context);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(container, lp);
        mContainer = container;
    }


    /**
     * 获取相对滑动位置。由右向左滑动，返回正值；由左向右滑动，返回负值。
     *
     * @return
     */
    private int getBaseScrollX() {
        return getScrollX() - mBaseScrollX;
    }

    /**
     * 使相对于基线移动x距离。
     *
     * @param x x为正值时右移；为负值时左移。
     */
    private void baseSmoothScrollTo(int x) {
        smoothScrollTo(x + mBaseScrollX, 0);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_UP://归位
                if (disableScroll) return false;//不允许手动切换直接返回false

                int scrollX = getBaseScrollX();
                //左滑，大于一半，移到下一页
                if (scrollX > mScrollX) {
                    baseSmoothScrollTo(mWidth);
                    mBaseScrollX += mWidth;
                }
                //左滑，不到一半，返回原位
                else if (scrollX > 0) {
                    baseSmoothScrollTo(0);
                }
                //右滑，不到一半，返回原位
                else if (scrollX > -mScrollX) {
                    baseSmoothScrollTo(0);
                }
                //右滑，大于一半，移到下一页
                else {
                    baseSmoothScrollTo(-mWidth);
                    mBaseScrollX -= mWidth;
                }
                return true;
            case MotionEvent.ACTION_MOVE://不允许手动切换直接返回false
                return !disableScroll && super.onTouchEvent(ev);
        }
        return super.onTouchEvent(ev);
    }

    /*
     *//**
     * 禁止滑动,只能代码切换
     *
     * @param ev
     * @return
     *//*
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_MOVE://禁止手动滑动
            case MotionEvent.ACTION_UP://禁止手动滑动
                return false;
        }
        return super.onTouchEvent(ev);
    }*/

    // <editor-fold defaultstate="collapsed" desc="代码切换">

    /**
     * 下一页
     */
    public void nextPage() {
        smoothScrollBy(mWidth, 0);
    }

    /**
     * 上一页
     */
    public void prePage() {
        smoothScrollBy(-mWidth, 0);

    }
    // </editor-fold>


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
    }


    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus) {
            addChildView();
        }
    }

    /**
     * 添加子view
     */
    private void addChildView() {

        if (pageOneLayoutId != -1) {
            View pageOne = mInflater.inflate(pageOneLayoutId, null);
            addPage0(pageOne, -1);
        }

        if (pageTwoLayoutId != -1) {
            View pageTwo = mInflater.inflate(pageTwoLayoutId, null);
            addPage0(pageTwo, -1);
        }

        if (pageThreeLayoutId != -1) {
            View pageThree = mInflater.inflate(pageThreeLayoutId, null);
            addPage0(pageThree, -1);
        }
    }

    /**
     * 添加一个页面。
     *
     * @param page
     */
    private void addPage0(View page, int index) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        if (index == -1) {
            mContainer.addView(page, params);
        } else {
            mContainer.addView(page, index, params);
        }
        mPageCount++;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="对外方法">


    @Nullable
    public View getPageAt(int index) {
        if (mPageCount < 1) {
            return null;
        }
        if (index < 0 || index > mContainer.getChildCount() - 1) {
            return null;
        }
        return mContainer.getChildAt(index);
    }


    /**
     * 添加一个页面到最后。
     *
     * @param page
     */
    public void addPage(View page) {
        addPage(page, -1);
    }

    /**
     * 添加一个页面。
     *
     * @param page
     */
    public void addPage(View page, int index) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        if (index == -1) {
            mContainer.addView(page, params);
        } else {
            mContainer.addView(page, index, params);
        }
        mPageCount++;
    }

    /**
     * 移除一个页面。
     *
     * @param index
     */
    public void removePage(int index) {
        if (mPageCount < 1) {
            return;
        }
        if (index < 0 || index > mPageCount - 1) {
            return;
        }
        mContainer.removeViewAt(index);
        mPageCount--;
    }

    /**
     * 移除所有的页面
     */
    public void removeAllPages() {
        if (mPageCount > 0) {
            mContainer.removeAllViews();
        }
    }

    /**
     * 获取页面数量
     *
     * @return
     */
    public int getPageCount() {
        return mPageCount;
    }

    // </editor-fold>

}