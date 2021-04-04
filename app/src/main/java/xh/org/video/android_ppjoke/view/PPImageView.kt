package xh.org.video.android_ppjoke.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import jp.wasabeef.glide.transformations.BlurTransformation
import xh.org.video.common.utils.PixUtils

open class PPImageView : AppCompatImageView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attr: AttributeSet) : super(context, attr)

    constructor(context: Context, attr: AttributeSet, styleAttr: Int) : super(
        context,
        attr,
        styleAttr
    )


    companion object {
        @JvmStatic
        @BindingAdapter(value = ["image_url", "isCircle"], requireAll = false)
        fun setImageViewUrl(imageView: PPImageView, imageUrl: String, isCircle: Boolean) {
            val builder = Glide.with(imageView).load(imageUrl)
            if (isCircle) {
                builder.transform(CircleCrop())
            }
            val layoutParam = imageView.layoutParams
            if (layoutParam != null && layoutParam.width > 0 && layoutParam.height > 0) {
                builder.override(layoutParam.width, layoutParam.height)

            }
            builder.into(imageView)
        }
    }

    fun setImageViewUrl(
        imageView: PPImageView,
        imageUrl: String,
        isCircle: Boolean,
        isLayout: Boolean = true
    ) {
        val builder = Glide.with(imageView).load(imageUrl)
        if (isCircle) {
            builder.transform(CircleCrop())
        }
        val layoutParam = imageView.layoutParams
        if (layoutParam != null && layoutParam.width > 0 && layoutParam.height > 0) {
            builder.override(layoutParam.width, layoutParam.height)

        }
        builder.into(imageView)
    }

    fun bind(
        widthPx: Int,
        heightPx: Int,
        marginLeft: Int,
        maxWidth: Int = PixUtils.screenWidth(),
        maxHeight: Int = PixUtils.screenHeight(),
        url: String
    ) {
        if (widthPx <= 0 || heightPx <= 0) {
            Glide.with(this).load(url).into(object : SimpleTarget<Drawable>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    var width = resource.intrinsicWidth
                    var height = resource.intrinsicHeight
                    setSize(width, height, marginLeft, maxHeight, maxWidth)
                    setImageDrawable(resource)
                }

            })
            return
        }
        setSize(widthPx, heightPx, marginLeft, maxWidth, maxHeight)
        setImageViewUrl(this, url, false)
    }

    private fun setSize(
        width: Int,
        height: Int,
        marginLeft: Int,
        maxHeight: Int,
        maxWidth: Int
    ) {
        var finalWidth: Int? = null
        var finalHeight: Int? = null
        if (width > height) {
            finalWidth = maxWidth
            finalHeight = (((width * 1.0f) / finalWidth) * height).toInt()
        } else {
            finalHeight = maxHeight
            finalWidth = (((height * 1.0f) / finalHeight) * width).toInt()

        }
        val param = ViewGroup.MarginLayoutParams(finalWidth, finalHeight)
        param.leftMargin = if (height > width) {
            PixUtils.dp2px(marginLeft)
        } else {
            0
        }
        layoutParams = param
    }

    fun setBlurImageUrl(coverUrl: String, radius: Int) {
        Glide.with(this).load(coverUrl)
            .override(50)
            .transform(BlurTransformation())
            .into(object : SimpleTarget<Drawable>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    background = resource
                }

            })
    }
}