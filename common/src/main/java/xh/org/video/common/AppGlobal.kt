package xh.org.video.common

import android.app.Application
import java.lang.reflect.InvocationTargetException
/***
 * 一种通过反射的方式获取全局的Application
 */
object AppGlobals {
    var application: Application? = null
    fun get(): Application? {
        if (application == null) {
            try {
                application = Class.forName("android.app.ActivityThread")
                    .getMethod("currentApplication")
                    .invoke(null) as Application
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
        return application
    }
}