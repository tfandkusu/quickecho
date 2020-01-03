package jp.bellware.echo.main2

import android.content.Context
import android.media.AudioManager
import android.media.SoundPool
import androidx.lifecycle.ViewModel
import jp.bellware.echo.R
import jp.bellware.echo.repository.SettingRepository

/**
 * 効果音担当ViewModel
 */
class SoundEffectViewHelper(private val context: Context,
                            val repository: SettingRepository) : ViewModel() {
    /**
     * 効果音読み込みと再生の担当
     */
    private lateinit var soundPool: SoundPool

    /**
     * 効果音有効フラグ
     */
    private var isEnabled = true

    /**
     * 読み込み済みフラグ
     */
    private var count = 0

    /**
     * 録音開始効果音ID
     */
    private var startId = 0

    /**
     * 録音終了効果音ID
     */
    private var playId = 0

    /**
     * 削除効果音ID
     */
    private var deleteId = 0

    /**
     * ActivityのonCreateから呼ばれる
     */
    fun onCreate(onLoadFinished: () -> Unit) {
        // プロセスで1度だけ事項
        if (startId != 0 || playId != 0 || deleteId != 0)
            return
        //サウンドプール
        soundPool = SoundPool(1, AudioManager.STREAM_MUSIC, 0)
        soundPool.setOnLoadCompleteListener { _, _, _ ->
            ++count
            if (count >= 3)
                onLoadFinished()
        }
        startId = soundPool.load(context, R.raw.start, 1)
        playId = soundPool.load(context, R.raw.play, 1)
        deleteId = soundPool.load(context, R.raw.delete, 1)

        onSettingUpdate()
    }

    /**
     * 録音開始の効果音再生
     */
    fun start() {
        play(startId)
    }

    /**
     * 再生開始の効果音再生
     */
    fun play() {
        play(playId)
    }

    /**
     * 削除の効果音再生
     */
    fun delete() {
        play(deleteId)
    }

    override fun onCleared() {
        soundPool.release()
    }

    /**
     * 効果音を再生
     */
    private fun play(id: Int) {
        if (isEnabled) {
            soundPool.play(id, 1f, 1f, 0, 0, 1f)
        }
    }

    fun onSettingUpdate() {
        // 設定読み込み
        isEnabled = repository.isSoundEffect()
    }
}
