package com.example.itunesapp2.extensions

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.itunesapp2.R
import com.google.android.material.snackbar.Snackbar

fun View.visible(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.GONE
}

fun Fragment.showSnackMessage(message: String) {
    view?.let {
        val snackbar = Snackbar.make(it, message, Snackbar.LENGTH_LONG)
        snackbar.view.setBackgroundColor(Color.GRAY)
        val messageTextView = snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        messageTextView.setTextColor(Color.WHITE)
        snackbar.show()
    }
}

fun ImageView.loadImage(
    url: String?, ctx: Context? = null
) {

    Glide.with(ctx ?: context).load(url).apply(RequestOptions().apply {
        placeholder(this@loadImage.drawable)
        error(R.drawable.image_not_found)
    }).apply(RequestOptions.centerCropTransform()).into(this)
}

fun Activity.hideKeyboard() {
    currentFocus?.apply {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}