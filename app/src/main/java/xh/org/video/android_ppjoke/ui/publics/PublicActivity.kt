package xh.org.video.android_ppjoke.ui.publics

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import xh.org.video.android_ppjoke.R
import xh.org.video.libannotation.ActivityDestination

@ActivityDestination("main/tabs/publish", isLogin = true)
class PublicActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_public)
    }
}