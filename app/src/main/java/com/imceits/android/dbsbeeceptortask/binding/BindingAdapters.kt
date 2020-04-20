package com.imceits.android.dbsbeeceptortask.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

object BindingAdapters {

    @JvmStatic
    @BindingAdapter("image_url")
    fun setImgUrl(imageView: ImageView, url: String?) {
        url?.let {
            Glide.with(imageView.context).load(it).into(imageView)
        }
    }
}