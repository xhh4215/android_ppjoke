package xh.org.video.android_ppjoke.utils

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.TypeReference
import xh.org.video.android_ppjoke.model.BottomBar
import xh.org.video.android_ppjoke.model.Destination
import xh.org.video.common.AppGlobals
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class AppConfig {
    companion object {
        private var destinationConfig: HashMap<String, Destination>? = null

        private var bottomBar: BottomBar? = null
        fun getDestination(): HashMap<String, Destination> {
            if (destinationConfig == null) {
                val content = parseFile("destination.json")
                destinationConfig = JSON.parseObject(
                    content,
                    object : TypeReference<java.util.HashMap<String, Destination>>() {}.type
                )

            }
            return destinationConfig!!
        }
        fun getBottomBar():BottomBar{
          if (bottomBar==null){
            val content = parseFile("main_tabs_config.json")
              bottomBar = JSON.parseObject(content,BottomBar::class.java)
          }
            return bottomBar!!
        }

        private fun parseFile(fileName: String): String {
            val assets = AppGlobals.get()!!.resources.assets
            val stringBuilder = StringBuilder()
            var inputStream: InputStream? = null
            try {
                inputStream = assets.open(fileName)
                val bufferReader = BufferedReader(InputStreamReader(inputStream))
                bufferReader.forEachLine {
                    stringBuilder.append(it)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                inputStream?.close()
            }
            return stringBuilder.toString()
        }
    }
}