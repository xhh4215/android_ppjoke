package xh.org.video.android_ppjoke.utils

import android.content.ComponentName
import androidx.fragment.app.FragmentActivity
import androidx.navigation.ActivityNavigator
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavGraphNavigator
import androidx.navigation.fragment.FragmentNavigator
import xh.org.video.android_ppjoke.model.Destination
import xh.org.video.android_ppjoke.navigator.FixFragmentNavigator
import xh.org.video.common.AppGlobals

/***
 *
 */
class NavGraphBuilder {
    companion object {

        fun build(controller: NavController, activity: FragmentActivity, containerId: Int) {
            val provider = controller.navigatorProvider
//            val fragmentNavigator = FixFragmentNavigator(activity, activity.supportFragmentManager, containerId)
            val fragmentNavigator = provider.getNavigator(FragmentNavigator::class.java)
            val activityNavigator = provider.getNavigator(ActivityNavigator::class.java)
            provider.addNavigator(fragmentNavigator)
            val navGraph = NavGraph(NavGraphNavigator(provider))
            val destConfig = AppConfig.getDestination()
            destConfig.values.forEach {
                if (it.isFragment) {
                    val destination = fragmentNavigator.createDestination()
                    destination.className = it.clazzName
                    destination.id = it.id
                     destination.addDeepLink(it.pageUrl)
                    navGraph.addDestination(destination)
                } else {
                    val destination = activityNavigator.createDestination()
                    destination.setComponentName(
                        ComponentName(
                            AppGlobals.get()!!.packageName,
                            it.clazzName
                        )
                    )
                    destination.id = it.id
                    destination.addDeepLink(it.pageUrl)
                    navGraph.addDestination(destination)
                }
                if (it.isStart) {
                    navGraph.startDestination = it.id
                }
            }
            controller.graph = navGraph
        }

    }
}