package jp.bellware.echo.view.pager

import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.flutter.embedding.android.FlutterFragment
import jp.bellware.echo.main.R
import kotlinx.android.synthetic.main.activity_pager.*


/**
 * 横スワイプで録音画面と音声メモ画面を切り替える画面
 */
class PagerActivity : AppCompatActivity() {
    companion object {
        /**
         * Firebase Dynamic Linksから送られてきた画面タイプ
         */
        const val EXTRA_TYPE = "type"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pager)
        setSupportActionBar(toolbar)

        viewPager.adapter = QuickEchoFragmentStateAdapter(this)
        viewPager.offscreenPageLimit = 1

        //ボリューム調整を音楽にする
        volumeControlStream = AudioManager.STREAM_MUSIC
    }

    override fun onPostResume() {
        super.onPostResume()
        findFlutterFragment()?.onPostResume()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        findFlutterFragment()?.onNewIntent(intent)
    }

    override fun onBackPressed() {
        findFlutterFragment()?.onBackPressed()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        findFlutterFragment()?.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
        )
    }

    override fun onUserLeaveHint() {
        findFlutterFragment()?.onUserLeaveHint()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        findFlutterFragment()?.onTrimMemory(level)
    }

    private fun findFlutterFragment(): FlutterFragment? {
        return supportFragmentManager.findFragmentByTag("f1") as FlutterFragment?
    }
}
