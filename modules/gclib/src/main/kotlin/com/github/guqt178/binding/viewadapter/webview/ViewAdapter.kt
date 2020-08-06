package com.github.guqt178.binding.viewadapter.webview

import android.databinding.BindingAdapter
import android.text.TextUtils
import android.webkit.WebView

@BindingAdapter("render")
fun loadHtml(webView: WebView, html: String?) {
    if (!TextUtils.isEmpty(html)) {
        webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)
    }
}