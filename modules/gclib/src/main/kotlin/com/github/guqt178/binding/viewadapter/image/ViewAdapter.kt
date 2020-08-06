package com.github.guqt178.binding.viewadapter.image

import android.databinding.BindingAdapter
import android.text.TextUtils
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

@BindingAdapter(value = ["url", "placeholderRes"], requireAll = false)
fun setImageUri(
    imageView: ImageView,
    url: String?,
    placeholderRes: Int
) {
    if (!TextUtils.isEmpty(url)) {
        //使用Glide框架加载图片
        Glide.with(imageView.context)
            .load(url)
            .apply(RequestOptions().placeholder(placeholderRes))
            .into(imageView)
    } else {
        imageView.setImageResource(0)
    }
}