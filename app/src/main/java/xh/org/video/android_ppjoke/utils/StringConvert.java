package xh.org.video.android_ppjoke.utils;

public class StringConvert {
    public static String convertFeedUgc(int value) {
        if (value < 10000) {
            return String.valueOf(value);
        }
        return value / 10000 + "万";
    }
}
