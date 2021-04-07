package xh.org.video.libannotation

/***
 * @author 栾桂明
 * @desc  是一个标记在Fragment上的注解用来收集信息
 * @important :在创建一个注解的时候 声明属性和正常的创建一个属性是相同的
 */
@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
annotation class FragmentDestination(
    // 对应的页面地址
    val pageUrl: String,
    //是否需要登录
    val isLogin: Boolean = false,
    //是否是默认启动页
    val isStart: Boolean = false
)