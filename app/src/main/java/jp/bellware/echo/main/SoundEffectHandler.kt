package jp.bellware.echo.main

import android.content.Context
import android.media.AudioManager
import android.media.SoundPool

import jp.bellware.echo.R

/**
 * 効果音担当
 */
class SoundEffectHandler {


    private lateinit var soundPool: SoundPool

    /**
     * 効果音有効フラグ
     */
    var isEnabled = true

    /**
     * 録音開始効果音ID
     */
    private var startId: Int = 0

    /**
     * 録音終了効果音ID
     */
    private var playId: Int = 0

    /**
     * 削除効果音ID
     */
    private var deleteId: Int = 0

    fun onCreate(context: Context, onLoadFinished: () -> Unit) {
        //サウンドプール
        soundPool = SoundPool(1, AudioManager.STREAM_MUSIC, 0)
        soundPool.setOnLoadCompleteListener(object : SoundPool.OnLoadCompleteListener {
            private var count: Int = 0
            override fun onLoadComplete(soundPool: SoundPool, sampleId: Int, status: Int) {
                ++count
                if (count >= 3)
                    onLoadFinished()
            }
        })
        startId = soundPool.load(context, R.raw.start, 1)
        playId = soundPool.load(context, R.raw.play, 1)
        deleteId = soundPool.load(context, R.raw.delete, 1)
    }

    fun start() {
        play(startId)
    }

    fun play() {
        play(playId)
    }

    fun delete() {
        play(deleteId)
    }

    fun onDestroy() {
        soundPool.release()
    }

    private fun play(id: Int) {
        if (isEnabled) {
            soundPool.play(id, 1f, 1f, 0, 0, 1f)
        }
    }
}
