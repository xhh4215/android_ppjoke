package xh.org.video.android_ppjoke.utils

class StringConvert {
    companion object {
        fun convertFeedUgc(count: Int): String? {
            return if (count < 10000) {
                count.toString()
            } else (count / 10000).toString() + "万"
        }
    }

}