package com.github.guqt178.selector.uis.fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.guqt178.selector.R;
import com.github.guqt178.selector.entities.FileEntity;
import com.github.guqt178.selector.uis.views.CropImageLayout;

/**
 * Created On  on 17/12/27 15:32
 * Function：
 * Desc：
 */
public class ImageFragment extends Fragment {
    private View mRoot;
    private CropImageLayout mLayoutCrop;

    private FileEntity mEntity;

    public ImageFragment() {
    }

    @SuppressLint("ValidFragment")
    public ImageFragment(FileEntity entity) {
        this.mEntity = entity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRoot == null) {
            mRoot = inflater.inflate(R.layout.ps_fragment_image, null);
        }
        init();
        return mRoot;
    }

    private void init() {
        mLayoutCrop = mRoot.findViewById(R.id.ps_layout_crop);
        mLayoutCrop.setCrop(false);
        mLayoutCrop.setImage(mEntity.getPath());
    }
}
