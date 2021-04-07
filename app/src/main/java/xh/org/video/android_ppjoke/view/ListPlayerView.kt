package xh.org.video.android_ppjoke.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.PixelCopy
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import xh.org.video.android_ppjoke.R
import xh.org.video.common.utils.PixUtils

class ListPlayerView : FrameLayout {
    private var bufferView: View
    private var cover: PPImageView
    private var blur: PPImageView
    private var playBtn: ImageView
    private lateinit var mCategory: String
    private lateinit var mVideoUrl: String

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    constructor(context: Context, attributeSet: AttributeSet?, styleAttr: Int) : super(
        context, attributeSet, styleAttr
    ) {
        LayoutInflater.from(context).inflate(R.layout.layout_player_view, this, true)
        bufferView = findViewById(R.id.buffer_view)
        cover = findViewById(R.id.cover)
        blur = findViewById(R.id.blur_background)
        playBtn = findViewById(R.id.play_btn)
    }

    /***
     * category 视频播放器的标识  和单独的页面绑定
     */
    fun bindData(
        category: String,
        widthPx: Int,
        heightPx: Int,
        coverUrl: String,
        videoUrl: String
    ) {
        mCategory = category
        mVideoUrl = videoUrl
        cover.setImageViewUrl(cover, coverUrl, false)
        if (widthPx < heightPx) {
            blur.setBlurImageUrl(coverUrl, 10)
            blur.visibility = View.VISIBLE
        } else {
            blur.visibility = View.GONE

        }
        setSize(widthPx, heightPx)

    }

    private fun setSize(widthPx: Int, heightPx: Int) {
        val widthMax = PixUtils.screenWidth()
        val heightMax = widthMax
        //根据传入的宽高 计算出来的值
        var layoutHeight = 0
        var layoutWidth = widthMax

        //封面的宽高
        var coverWidth: Int? = null
        var coverHeight: Int? = null

        if (widthPx >= heightPx) {
            coverWidth = widthMax
            layoutHeight = (heightPx / (widthPx * 1.0 / widthMax)).toInt()
            coverHeight = (heightPx / (widthPx * 1.0 / widthMax)).toInt()
        } else {
            layoutHeight = heightMax
            coverHeight = heightMax
            coverWidth = (widthPx / heightPx * 1.0 / heightMax).toInt()
        }
        val params = layoutParams
        params.width = layoutWidth
        params.height = layoutHeight
        layoutParams = params
        val blurParams = blur.layoutParams
        blurParams.width = layoutWidth
        blurParams.height = layoutHeight
        blur.layoutParams = blurParams

        val coverParams = cover.layoutParams as FrameLayout.LayoutParams
        coverParams.width = coverWidth
        coverParams.height = coverHeight
        cover.layoutParams = coverParams
        val playBtnParams = playBtn.layoutParams as FrameLayout.LayoutParams
        playBtnParams.gravity = Gravity.CENTER
        playBtn.layoutParams = playBtnParams

    }

}