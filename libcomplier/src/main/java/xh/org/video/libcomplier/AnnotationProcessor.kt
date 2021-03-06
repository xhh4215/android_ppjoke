package xh.org.video.libcomplier

import com.alibaba.fastjson.JSONObject
import com.google.auto.service.AutoService
import xh.org.video.libannotation.ActivityDestination
import xh.org.video.libannotation.FragmentDestination
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.lang.Exception
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic
import javax.tools.StandardLocation
import kotlin.math.abs

/***
 * @author 栾桂明
 * @desc  这是一个解析注解的类
 *
 */
@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes(
    "xh.org.video.libannotation.ActivityDestination",
    "xh.org.video.libannotation.FragmentDestination"
)
class AnnotationProcessor : AbstractProcessor() {
    private lateinit var messager: Messager
    private lateinit var filer: Filer
    private val OUTPUT_FILE_NAME = "destination.json"

    /***
     * 初始化的方法
     */
    override fun init(processingEnvironment: ProcessingEnvironment) {
        super.init(processingEnvironment)
        messager = processingEnvironment.messager
        messager.printMessage(Diagnostic.Kind.NOTE, "注解解析器初始化");

        filer = processingEnvironment.filer
    }

    override fun process(
        elements: MutableSet<out TypeElement>,
        roundEnvironment: RoundEnvironment
    ): Boolean {
        val activityDestinations =
            roundEnvironment.getElementsAnnotatedWith(ActivityDestination::class.java)

        val fragmentDestinations =
            roundEnvironment.getElementsAnnotatedWith(FragmentDestination::class.java)

        if (fragmentDestinations.isNotEmpty() || activityDestinations.isNotEmpty()) {
            val destMap = HashMap<String, JSONObject>()
            handleDestination(fragmentDestinations, FragmentDestination::class.java, destMap)
            handleDestination(activityDestinations, ActivityDestination::class.java, destMap)
            var fileOutputStream: FileOutputStream? = null
            var outputStreamWriter: OutputStreamWriter? = null
            try {
                val resource =
                    filer.createResource(StandardLocation.CLASS_OUTPUT, "", OUTPUT_FILE_NAME)
                val resourcePath = resource.toUri().path
                messager.printMessage(Diagnostic.Kind.NOTE, "resourcePath:" + resourcePath)
                val path = resourcePath.substring(0, resourcePath.indexOf("app/") + 4)
                val assertPath = path + "src/main/assets/"
                messager.printMessage(Diagnostic.Kind.NOTE, "assertPath:" + assertPath)

                val file = File(assertPath)
                if (!file.exists()) {
                    file.mkdirs()
                }
                val outputFile = File(file, OUTPUT_FILE_NAME)
                if (outputFile.exists()) {
                    outputFile.delete()
                }
                outputFile.createNewFile()
                val content = JSONObject.toJSONString(destMap)
                fileOutputStream = FileOutputStream(outputFile)
                outputStreamWriter = OutputStreamWriter(fileOutputStream, "UTF-8")
                outputStreamWriter.write(content)
                outputStreamWriter.flush()

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                outputStreamWriter?.close()
                fileOutputStream?.close()
            }
        }
        return true

    }

    /****
     * 注解信息的具体解析方法
     */
    private fun handleDestination(
        elements: Set<Element>,
        annotationClass: Class<out Annotation>,
        destMap: java.util.HashMap<String, JSONObject>
    ) {
        for (element in elements) {
            val typeElement = element as TypeElement
            var pageUrl = ""
            val id = abs(typeElement.hashCode())
            val clazzName = typeElement.qualifiedName.toString()
            var needLogin = false
            var isStart = false
            var isFragment = false

            val annotation = typeElement.getAnnotation(annotationClass)
            if (annotation is ActivityDestination) {
                pageUrl = annotation.pageUrl
                needLogin = annotation.isLogin
                isStart = annotation.isStart
                isFragment = false
            } else if (annotation is FragmentDestination) {
                pageUrl = annotation.pageUrl
                needLogin = annotation.isLogin
                isStart = annotation.isStart
                isFragment = true
            }
            if (destMap.containsKey(pageUrl)) {
                messager.printMessage(Diagnostic.Kind.ERROR, "不同的页面不能使用相同的pageUrl$clazzName")
            } else {
                val jsonObject = JSONObject()
                jsonObject["id"] = id
                jsonObject["pageUrl"] = pageUrl
                jsonObject["needLogin"] = needLogin
                jsonObject["isStart"] = isStart
                jsonObject["clazzName"] = clazzName
                jsonObject["isFragment"] = isFragment
                destMap[pageUrl] = jsonObject

            }
        }

    }

}