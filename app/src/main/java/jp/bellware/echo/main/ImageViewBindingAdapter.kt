package jp.bellware.echo.main

import android.databinding.BindingAdapter
import android.widget.ImageView


/**
 * ImageViewに表示する画像を、image_src属性で設定するBindingAdapter
 */
@BindingAdapter("image_src")
fun setImageSrc(imageView: ImageView, resId: Int) {
    imageView.setImageResource(resId)
}
