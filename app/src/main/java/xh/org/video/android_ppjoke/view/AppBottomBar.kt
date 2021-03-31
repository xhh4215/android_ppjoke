package xh.org.video.android_ppjoke.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import xh.org.video.android_ppjoke.R
import xh.org.video.android_ppjoke.utils.AppConfig

class AppBottomBar : BottomNavigationView {
    companion object {
        val icons =
            intArrayOf(
                R.drawable.icon_tab_home,
                R.drawable.icon_tab_sofa
                , R.drawable.icon_tab_publish,
                R.drawable.icon_tab_find,
                R.drawable.icon_tab_mine
                )
    }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    @SuppressLint("RestrictedApi")
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttrInt: Int) : super(
        context,
        attributeSet,
        defStyleAttrInt
    ) {
        val bottomBar = AppConfig.getBottomBar()
        val tabs = bottomBar.tabs
        val states = arrayOfNulls<IntArray>(2)
        states[0] = intArrayOf(android.R.attr.state_selected)
        states[1] = intArrayOf()
        val colors = intArrayOf(
            Color.parseColor(bottomBar.activeColor),
            Color.parseColor(bottomBar.inActiveColor)
        )
        val colorStateList = ColorStateList(states, colors)
        itemIconTintList = colorStateList
        itemTextColor = colorStateList
        labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED
        selectedItemId = bottomBar.selectTab
        tabs.forEach {
            if (!it.isEnable) return
            val id = getId(it.pageUrl)
            Log.d("tag", "" + id)
            if (id < 0) {
                return
            }
            val item = menu.add(0, id, it.index, it.title)
            item.setIcon(icons[it.index])
        }
        tabs.forEach {
            val iconSize = dp2px(it.size)
            val menuView = getChildAt(0) as BottomNavigationMenuView
            val itemView = menuView.getChildAt(it.index) as BottomNavigationItemView
            itemView.setIconSize(iconSize)
            if (TextUtils.isEmpty(it.title)) {
                itemView.setIconTintList(ColorStateList.valueOf(Color.parseColor(it.tintColor)))
                itemView.setShifting(false)
            }
        }
    }

    private fun getId(url: String): Int {
        val destination = AppConfig.getDestination()[url]
        return destination?.id ?: -1
    }

    private fun dp2px(size: Int): Int {
        return (context.resources.displayMetrics.density * size + 0.5f).toInt()

    }
}