package jp.bellware.echo.repository

import jp.bellware.echo.datastore.local.SoundFileLocalDataStore
import jp.bellware.echo.datastore.local.SoundMemoryLocalDataStore

/**
 * 録音担当リポジトリ
 */
interface SoundRepository {

    /**
     * 最大のパケットの大きさ
     */
    val packetSize: Int

    /**
     * 合計長さ
     */
    val length: Int

    /**
     * 録音出来たボリューム
     */
    val gain: Float

    /**
     * 録音を開始
     */
    fun start()

    /**
     * 録音を終了
     */
    fun stop()

    /**
     * 録音をクリア
     */
    fun clear()

    /**
     * 音声パケットを追加
     */
    fun add(shortData: ShortArray, floatData: FloatArray)

    /**
     * 音声パケットを取得。記録されているパケット数以上のインデックスを設定すると、nullが返却される。
     * @param index インデックス。大きい方が新しい。
     */
    operator fun get(index: Int): FloatArray?

    /**
     * デバッグ用にwavファイルを保存する
     */
    fun saveForDebug()
}

class SoundRepositoryImpl(private val soundMemoryLocalDataStore: SoundMemoryLocalDataStore,
                          private val soundFileLocalDataStore: SoundFileLocalDataStore) : SoundRepository {
    override val packetSize: Int
        get() = soundMemoryLocalDataStore.packetSize
    override val length: Int
        get() = soundMemoryLocalDataStore.length
    override val gain: Float
        get() = soundMemoryLocalDataStore.gain

    override fun start() {
        soundFileLocalDataStore.start()
    }

    override fun stop() {
        soundFileLocalDataStore.stop()
    }

    override fun clear() {
        soundMemoryLocalDataStore.clear()
    }

    override fun add(shortData: ShortArray, floatData: FloatArray) {
        soundMemoryLocalDataStore.add(floatData)
        soundFileLocalDataStore.add(shortData)
    }

    override fun get(index: Int): FloatArray? {
        return soundMemoryLocalDataStore[index]
    }

    override fun saveForDebug() {
        soundMemoryLocalDataStore.save()
    }

}
