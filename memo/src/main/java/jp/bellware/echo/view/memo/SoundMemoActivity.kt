package jp.bellware.echo.view.memo

import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import jp.bellware.echo.memo.R
import jp.bellware.echo.view.setting.SettingActivityAlias
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.setting -> {
                callSettingActivity()
                true
            }
            else -> {
                false
            }
        }
    }

    private fun callSettingActivity() {
        val intent = Intent(this, SettingActivityAlias::class.java)
        startActivity(intent)
    }
}
