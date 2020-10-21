package com.cdek.courier.utils

import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.cdek.courier.R
import com.davemorrissey.labs.subscaleview.ImageSource

object DataBindingAdapter {
    @JvmStatic
    @BindingAdapter("loadImage")
    fun loadImage(imageView: ImageView, imageURL: String?) {
        Glide.with(imageView.context)
            .setDefaultRequestOptions(
                RequestOptions()
            )
            .load(imageURL)
            .fitCenter()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .placeholder(R.drawable.thumb_default)
            .into(imageView)
    }

    @JvmStatic
    @BindingAdapter("openImage")
    fun openImage(imageView: ImageView, filePath: String?) {
        imageView.setImageBitmap(BitmapFactory.decodeFile(filePath))
    }

    @JvmStatic
    @BindingAdapter("openImageInZoomView")
    fun openImageInZoomView(
        imageView: com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView,
        filePath: String?
    ) {
        filePath?.let {
            imageView.setImage(ImageSource.bitmap(BitmapFactory.decodeFile(filePath)))
        }
    }

    @JvmStatic
    @BindingAdapter("typeBold")
    fun typeBold(view: TextView, isBold: Boolean) {
        view.setTypeface(null, if (isBold) Typeface.BOLD else Typeface.NORMAL)
    }
}