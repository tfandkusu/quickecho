package jp.bellware.echo.view.main

import android.media.AudioManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import jp.bellware.echo.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        //ボリューム調整を音楽にする
        volumeControlStream = AudioManager.STREAM_MUSIC
    }
}
