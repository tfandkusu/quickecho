package jp.bellware.echo.view.memo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.flutter.embedding.android.FlutterFragment
import jp.bellware.echo.memo.R


/**
 * 音声メモ画面。
 */
class SoundMemoActivity : AppCompatActivity() {

    companion object {
        private const val TAG_FLUTTER_FRAGMENT = "flutterFragment"
    }

    private var fragment: FlutterFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_sound_memo)

        fragment = supportFragmentManager.findFragmentByTag(TAG_FLUTTER_FRAGMENT) as FlutterFragment?
        if (fragment == null) {
            fragment = FlutterFragment.withCachedEngine("my_engine_id").build()
            fragment?.let {
                supportFragmentManager
                        .beginTransaction()
                        .add(R.id.root, it, TAG_FLUTTER_FRAGMENT)
                        .commit()
            }
        }
    }

    override fun onPostResume() {
        super.onPostResume()
        fragment?.onPostResume()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        fragment?.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
        )
    }

    override fun onUserLeaveHint() {
        fragment?.onUserLeaveHint()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        fragment?.onTrimMemory(level)
    }

}
