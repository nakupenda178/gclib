package com.github.guqt178.helper

import android.content.Intent

interface OnResult {
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)

}