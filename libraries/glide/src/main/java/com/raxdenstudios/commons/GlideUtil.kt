package com.raxdenstudios.commons

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.raxdenstudios.commons.ext.ScaleType

object GlideUtil {

  @SuppressLint("CheckResult")
  internal fun loadImage(
    image: Any,
    imageView: ImageView,
    progressView: View? = null,
    errorView: View? = null,
    scaleType: ScaleType = ScaleType.CENTER_CROP,
    cornerType: RoundedCornersTransformation.CornerType = RoundedCornersTransformation.CornerType.ALL,
    cornerRadius: Int = 0
  ) {
    Glide.with(imageView).load(
      when (image) {
        is String -> image
        is Uri -> image
        is Drawable -> image
        is Bitmap -> image
        else -> throw Exception("imageUser param only supports String, Uri, Drawable or Bitmap")
      }
    ).run {
      transition(DrawableTransitionOptions.withCrossFade())
      applyTransformations(
        this,
        scaleType = scaleType,
        cornerRadius = cornerRadius,
        cornerType = cornerType
      )
      listener(object : RequestListener<Drawable> {
        override fun onLoadFailed(
          e: GlideException?,
          model: Any,
          target: Target<Drawable>,
          isFirstResource: Boolean
        ): Boolean {
          errorView?.visibility = View.VISIBLE
          progressView?.visibility = View.GONE
          return false
        }

        override fun onResourceReady(
          resource: Drawable,
          model: Any,
          target: Target<Drawable>,
          dataSource: DataSource,
          isFirstResource: Boolean
        ): Boolean {
          imageView.visibility = View.VISIBLE
          errorView?.visibility = View.GONE
          progressView?.visibility = View.GONE
          return false
        }
      })
      into(imageView)
    }
  }

  private fun applyTransformations(
    requestBuilder: RequestBuilder<Drawable>,
    width: Int = 0,
    height: Int = 0,
    holderId: Int = 0,
    scaleType: ScaleType,
    cornerRadius: Int,
    cornerType: RoundedCornersTransformation.CornerType?,
    blurRadius: Int = 0
  ) {
    val multiTransformation: MutableList<Transformation<Bitmap>> = mutableListOf()
    when (scaleType) {
      ScaleType.CENTER_CROP -> multiTransformation.add(CenterCrop())
      ScaleType.FIT_CENTER -> multiTransformation.add(FitCenter())
      ScaleType.CIRCLE_CROP -> multiTransformation.add(CircleCrop())
      else -> return
    }
    if (cornerRadius > 0) multiTransformation.add(
      RoundedCornersTransformation(
        cornerType,
        cornerRadius
      )
    )
    RequestOptions().let {
      it.priority(Priority.IMMEDIATE)
      if (width > 0 && height > 0) it.override(width, height)
      if (holderId != 0) it.placeholder(holderId)
      if (multiTransformation.isNotEmpty()) it.transforms(*multiTransformation.toTypedArray())
      requestBuilder.apply(it)
    }
  }

}
