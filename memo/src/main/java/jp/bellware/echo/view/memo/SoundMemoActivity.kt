package jp.bellware.echo.view.memo

import android.media.AudioManager
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import jp.bellware.echo.memo.R
import kotlinx.android.synthetic.main.activity_sound_memo.*


/**
 * 音声メモ画面。
 */
class SoundMemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sound_memo)
        setSupportActionBar(toolbar)
        volumeControlStream = AudioManager.STREAM_MUSIC
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            finish()
            true
        } else {
            false
        }
    }
}
