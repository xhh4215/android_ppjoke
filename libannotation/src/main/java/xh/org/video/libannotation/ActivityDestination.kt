package xh.org.video.libannotation

/***
 * @author 栾桂明
 * @desc  是一个标记在Activity上的注解用来收集信息
 * @important :在创建一个注解的时候 声明属性和正常的创建一个属性是相同的
 */
@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
annotation class ActivityDestination(
    val pageUrl: String,
    val isLogin: Boolean = false,
    val isStart: Boolean = false
)