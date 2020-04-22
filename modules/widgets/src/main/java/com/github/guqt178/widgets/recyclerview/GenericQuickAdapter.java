package com.github.guqt178.widgets.recyclerview;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.CallSuper;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
//databinding adapter
//X是泛型,可以是你在item中所使用的java bean
public class GenericQuickAdapter<X> extends BaseQuickAdapter<X, GenericQuickAdapter.GenericViewHolder> {
    //BR中item的变量名
    protected final int mName;
    //BR中position的变量名
    protected final int mPosition;

    //layoutResId是DataBinding风格的xml
    public GenericQuickAdapter(int layoutResId, int name, int position) {
        super(layoutResId);
        mName = name;
        mPosition = position;
        openLoadAnimation();
    }

    //layoutResId是DataBinding风格的xml
    public GenericQuickAdapter(int layoutResId, int name) {
        this(layoutResId, name, -1);
    }

    @Override
    @CallSuper
    protected void convert(GenericViewHolder helper, X item) {
        //触发DataBinding
        helper.getBinding().setVariable(mName, item);
        if (mPosition != -1) {
            int layoutPosition = helper.getLayoutPosition();
            helper.getBinding().setVariable(mPosition, layoutPosition);
        }
    }

    public static class GenericViewHolder extends BaseViewHolder {
        private ViewDataBinding mBinding;

        public GenericViewHolder(View view) {
            super(view);
            //绑定View获得ViewDataBinding
            mBinding = DataBindingUtil.bind(view);
        }

        @SuppressWarnings("unchecked")
        public <T extends ViewDataBinding> T getBinding() {
            return (T) mBinding;
        }
    }
}
