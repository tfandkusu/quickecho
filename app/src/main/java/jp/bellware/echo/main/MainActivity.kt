package jp.bellware.echo.main

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.AudioManager
import android.os.Handler
import android.os.Bundle
import android.os.IBinder
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View

import jp.bellware.echo.R

import jp.bellware.echo.setting.SettingActivity
import jp.bellware.util.BWU
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_control.*
import kotlinx.android.synthetic.main.main_status.*
import android.databinding.DataBindingUtil
import jp.bellware.echo.databinding.ActivityMainBinding

/**
 * メイン画面
 */
class MainActivity : AppCompatActivity() {
    /**
     * 警告担当
     */
    private val wh = WarningHandler()

    /**
     * ボリュームの設定担当
     */
    private lateinit var audioManager: AudioManager


    /**
     * アニメ担当
     */
    private val animator = QRecAnimator()



    /**
     * メイン画面のビューモデル
     */
    private val viewModel = MainViewModel(this, object : MainViewModel.Listener {
        override fun onDeleteRecord() {
            animator.startDeleteAnimation(statusFrame)
            animator.startDeleteAnimation(delete)
            animator.startDeleteAnimation(replay)
            animator.startDeleteAnimation(stop)
        }

        override fun onStartRecord() {
            //爆発エフェクト
            explosion.startRecordAnimation()
            //ステータスはフェードイン
            animator.fadeIn(statusFrame)
            //削除ボタンはフェードイン
            if (delete.visibility == View.INVISIBLE) {
                animator.fadeIn(delete)
            }
        }

        override fun onStopRecord() {
            //ステータスを表示
            animator.fadeIn(statusFrame)
            //サブコントロール表示
            animator.fadeIn(replay)
            animator.fadeIn(stop)
        }

        override fun onUpdateVolume(volume: Float) {
            //視覚的ボリュームを更新する
            visualVolume.setVolume(volume)
        }

        override fun onShowWarningMessage(resId: Int) {
            //警告を表示する
            wh.show(resId)
        }

    });


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BWU.log("MainActivity#onCreate")
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.viewModel = viewModel
        //ツールバー設定
        this.setSupportActionBar(toolbar)
        //広告の設定
        setUpAd()
        //ボリューム調整を音楽にする
        volumeControlStream = AudioManager.STREAM_MUSIC
        //AudioManagerを取得
        this.audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        //警告担当
        wh.onCreate(this)
        viewModel.onCreate()

    }

    override fun onStart() {
        super.onStart()
        BWU.log("MainActivity#onStart")
        viewModel.onStart()
    }

    override fun onResume() {
        super.onResume()
        BWU.log("MainActivity#onResume")
        wh.onResume()
        if (this.audioManager?.getStreamVolume(AudioManager.STREAM_MUSIC) == 0) {
            wh.show(R.string.warning_volume)
        }
    }

    override fun onPause() {
        super.onPause()
        BWU.log("MainActivity#onPause")
        wh.onPause()
    }

    override fun onStop() {
        super.onStop()
        BWU.log("MainActivity#onStop")
        viewModel.onStop()
    }

    override fun onBackPressed() {
        if (viewModel.onBackPressed()) {
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        BWU.log("MainActivity#onDestroy")
        viewModel.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.setting) {
            callSettingActivity()
            return true
        }
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CODE_SETTING) {
            viewModel.onSettingUpdate()
        }
    }

    /**
     * 設定画面
     */
    private fun callSettingActivity() {
        val intent = Intent(this, SettingActivity::class.java)
        startActivityForResult(intent, CODE_SETTING)
    }

    /**
     * 広告を設定する
     */
    private fun setUpAd() {
        //        AdView mAdView = (AdView) findViewById(R.id.ad);
        //        AdRequest adRequest = new AdRequest.Builder().build();
        //        mAdView.loadAd(adRequest);
    }

    companion object {
        private val CODE_SETTING = 1
    }
}
