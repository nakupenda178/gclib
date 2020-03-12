package com.github.guqt178.utils.result

import android.content.Intent

interface OnResult {
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)

}