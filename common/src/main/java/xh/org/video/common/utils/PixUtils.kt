package xh.org.video.common.utils

import xh.org.video.common.AppGlobals

class PixUtils {
    companion object {
        /***
         * dp数值转化为px
         */
        fun dp2px(value: Int): Int {
            val displayMetrics = AppGlobals.application!!.resources.displayMetrics
            return (displayMetrics.density * value + 0.5f).toInt()
        }

        /***
         * 获取屏幕宽度
         */
        fun screenWidth() = AppGlobals.application!!.resources.displayMetrics.widthPixels

        /***
         * 获取屏幕高度
         */
        fun screenHeight() = AppGlobals.application!!.resources.displayMetrics.heightPixels
    }

}