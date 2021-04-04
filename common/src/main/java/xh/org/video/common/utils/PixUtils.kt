package xh.org.video.common.utils

import xh.org.video.common.AppGlobals

class PixUtils {
    companion object {
        fun dp2px(value: Int): Int {
            val displayMetrics = AppGlobals.application!!.resources.displayMetrics
            return (displayMetrics.density * value + 0.5f).toInt()
        }

        fun screenWidth() = AppGlobals.application!!.resources.displayMetrics.widthPixels

        fun screenHeight() = AppGlobals.application!!.resources.displayMetrics.heightPixels
    }

}