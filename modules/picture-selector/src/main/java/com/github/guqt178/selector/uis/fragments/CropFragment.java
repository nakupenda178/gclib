package com.github.guqt178.selector.uis.fragments;


import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.github.guqt178.selector.R;
import com.github.guqt178.selector.data.ConstantData;
import com.github.guqt178.selector.entities.FileEntity;
import com.github.guqt178.selector.uis.views.CropImageLayout;
import com.github.guqt178.selector.utils.PSUtil;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created On  on 17/12/25 14:24
 * Function：
 * Desc：
 */
public class CropFragment extends BaseFragment {
    private CropImageLayout mLayoutCrop;

    private FileEntity mItem;
    private boolean isBacked;

    @Override
    protected int getLayout() {
        return R.layout.ps_fragment_crop;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mLayoutCrop = mRoot.findViewById(R.id.ps_layout_crop);

        update(getArguments());
    }

    @Override
    public void update(Bundle bundle) {
        if (bundle == null) {
            return;
        }
        initBaseInfo(bundle);
        isBacked = false;
        mItem = bundle.getParcelable(ConstantData.KEY_CURR_ITEM);

        mLayoutCrop.setCrop(mCanCrop);
        if (mItem != null) {
            mLayoutCrop.setImage(mItem.getPath());
        }

        setEnsure();
    }

    @Override
    protected ArrayList<String> getSelectedPictures() {
        ArrayList<String> selectedPics = new ArrayList<>();
        selectedPics.add(mItem.getPath());
        return selectedPics;
    }

    @Override
    protected ArrayList<String> getSelectedVideos() {
        return null;
    }

    @Override
    protected void onBackClicked() {
        isBacked = true;
        if (mSelectType == ConstantData.VALUE_TYPE_CAMERA) {
            getActivity().onBackPressed();
            return;
        }
        super.onBackClicked();
    }

    @Override
    protected void onEnsureClicked() {
        if (!mCanCrop) {
            super.onEnsureClicked();
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = mLayoutCrop.cropBitmap();
                if (bitmap == null) {
                    return;
                }
                String path = PSUtil.getRootDir() + ConstantData.FOLDER_CROP;
                String imagePath = path + "/" + Calendar.getInstance().getTimeInMillis() + ".jpg";
                PSUtil.saveBitmap(bitmap, path, imagePath);
                if (isBacked) {
                    return;
                }
                mItem.setPath(imagePath);
                mHandler.sendEmptyMessage(0);
            }
        }).start();
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (isBacked) {
                return;
            }
            if (msg.what == 0) {
                CropFragment.super.onEnsureClicked();
                PSUtil.scanFile(getActivity(), mItem.getPath());
            }
        }
    };
}
