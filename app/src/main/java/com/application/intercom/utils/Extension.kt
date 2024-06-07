package com.application.intercom.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import com.application.intercom.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions


fun Context.shortToast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
fun Context.longToast(message: String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()
fun View.hideKeyboard() {
    val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(windowToken, 0)
}
fun ImageView.loadImagesWithGlideExt(url: String) {
    Glide.with(this)
        .load(url)
        .centerCrop()
        .error(R.drawable.iv_dummy_img)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)
}