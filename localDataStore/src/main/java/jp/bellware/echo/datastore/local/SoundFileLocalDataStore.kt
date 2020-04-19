package jp.bellware.echo.datastore.local

import android.content.Context


/**
 * 録音音声をファイルに保存する
 */
interface SoundFileLocalDataStore {

    /**
     * 録音開始
     */
    fun start()

    /**
     * 音声パケットを追加
     */
    fun add(data: ShortArray)

    /**
     * 録音終了
     */
    fun stop()

}

class SoundFileLocalDataStoreImpl(private val context: Context) : SoundFileLocalDataStore {

    private var session: AacEncodeSession? = null

    override fun start() {
        session = AacEncodeSession()
        session?.start(context)
    }

    override fun add(data: ShortArray) {
        session?.add(data)
    }


    override fun stop() {
        session?.stop()
        session = null
    }

}