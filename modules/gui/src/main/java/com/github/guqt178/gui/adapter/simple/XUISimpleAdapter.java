package com.github.guqt178.gui.adapter.simple;

import android.content.Context;
import android.view.Gravity;
import android.view.View;


import com.github.guqt178.gui.R;
import com.github.guqt178.gui.adapter.listview.BaseListAdapter;
import com.github.guqt178.gui.utils.DensityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 简单的适配器
 *
 * @author xuexiang
 * @since 2019/1/14 下午10:07
 */
public class XUISimpleAdapter extends BaseListAdapter<AdapterItem, ViewHolder> {

    private int mPaddingLeftPx;

    public XUISimpleAdapter(Context context) {
        super(context);
    }

    public XUISimpleAdapter(Context context, List<AdapterItem> data) {
        super(context, data);
    }

    public XUISimpleAdapter(Context context, AdapterItem[] data) {
        super(context, data);
    }

    @Override
    protected ViewHolder newViewHolder(View convertView) {
        ViewHolder holder = new ViewHolder();
        holder.mLLContentView = convertView.findViewById(R.id.ll_content);
        holder.mTvTitle = convertView.findViewById(R.id.tv_title);
        holder.mIvIcon = convertView.findViewById(R.id.iv_icon);

        if (mPaddingLeftPx != 0) {
            holder.mLLContentView.setPadding(mPaddingLeftPx, 0, 0, 0);
            holder.mLLContentView.setGravity(Gravity.CENTER_VERTICAL);
        } else {
            holder.mLLContentView.setGravity(Gravity.CENTER);
        }
        return holder;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.xui_adapter_listview_simple_item;
    }

    @Override
    protected void convert(ViewHolder holder, AdapterItem item, int position) {
        holder.mTvTitle.setText(item.getTitle());
        if (item.getIcon() != null) {
            holder.mIvIcon.setVisibility(View.VISIBLE);
            holder.mIvIcon.setImageDrawable(item.getIcon());
        } else {
            holder.mIvIcon.setVisibility(View.GONE);
        }

    }

    public XUISimpleAdapter setPaddingLeftPx(int paddingLeftPx) {
        mPaddingLeftPx = paddingLeftPx;
        return this;
    }

    public XUISimpleAdapter setPaddingLeftDp(int paddingLeftDp) {
        mPaddingLeftPx = DensityUtils.dp2px(paddingLeftDp);
        return this;
    }

    /**
     * 创建简单的适配器【不含图标】
     *
     * @param context
     * @param data
     * @return
     */
    public static XUISimpleAdapter create(Context context, String[] data) {
        if (data != null && data.length > 0) {
            List<AdapterItem> lists = new ArrayList<>();
            for (int i = 0; i < data.length; i++) {
                lists.add(new AdapterItem(data[i]));
            }
            return new XUISimpleAdapter(context, lists);
        } else {
            return new XUISimpleAdapter(context);
        }
    }

    /**
     * 创建简单的适配器【不含图标】
     *
     * @param context
     * @param data
     * @return
     */
    public static XUISimpleAdapter create(Context context, List<String> data) {
        if (data != null && data.size() > 0) {
            List<AdapterItem> lists = new ArrayList<>();
            for (int i = 0; i < data.size(); i++) {
                lists.add(new AdapterItem(data.get(i)));
            }
            return new XUISimpleAdapter(context, lists);
        } else {
            return new XUISimpleAdapter(context);
        }
    }

}
