package xh.org.video.common.widget

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import xh.org.video.common.R

/***
 * 默认的没有数据的页面使用的View
 */
class EmptyView : LinearLayout {
    //显示的图片
    private val icon: ImageView

    //显示的标题
    private val title: TextView

    // 空页面的出事的刷新动作的触发按钮
    private val action: Button

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    constructor(context: Context, attributeSet: AttributeSet?, styleAttr: Int) : super(
        context,
        attributeSet,
        styleAttr
    ) {
        orientation = VERTICAL
        LayoutInflater.from(context).inflate(R.layout.layout_emptyview, this, true)
        gravity = Gravity.CENTER
        icon = findViewById(R.id.empty_icon)
        title = findViewById(R.id.empty_text)
        action = findViewById(R.id.empty_action)
    }

    fun setEmptyIcon(@DrawableRes iconRes: Int) {
        icon.setImageResource(iconRes)
    }

    fun setTitle(text: String) {
        if (TextUtils.isEmpty(text)) {
            title.visibility = View.GONE
        } else {
            title.text = text
            title.visibility = View.VISIBLE
        }
    }

    fun setButton(
        text: String,
        listener: OnClickListener
    ) {
        if (TextUtils.isEmpty(text)) {
            action.visibility = View.GONE
        } else {
            action.text = text
            action.visibility = View.VISIBLE
            action.setOnClickListener(listener)
        }
    }
}