package jp.bellware.echo.view.main

import android.media.AudioManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import jp.bellware.echo.main.R
import kotlinx.android.synthetic.main.activity_main.toolbar

/**
 * メイン画面
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    companion object {
        /**
         * Firebase Dynamic Linksから送られてきた画面タイプ
         * （Firebase Dynamic Linksの調査が終わったので不使用）
         */
        const val EXTRA_TYPE = "type"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        // ボリューム調整を音楽にする
        volumeControlStream = AudioManager.STREAM_MUSIC
    }
}
