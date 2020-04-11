package jp.bellware.echo.repository

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
     * 録音をクリア
     */
    fun clear()

    /**
     * 音声パケットを追加
     */
    fun add(data: FloatArray)

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

class SoundRepositoryImpl(private val soundMemoryLocalDataStore: SoundMemoryLocalDataStore) : SoundRepository {
    override val packetSize: Int
        get() = soundMemoryLocalDataStore.packetSize
    override val length: Int
        get() = soundMemoryLocalDataStore.length
    override val gain: Float
        get() = soundMemoryLocalDataStore.gain

    override fun clear() {
        soundMemoryLocalDataStore.clear()
    }

    override fun add(data: FloatArray) {
        soundMemoryLocalDataStore.add(data)
    }

    override fun get(index: Int): FloatArray? {
        return soundMemoryLocalDataStore[index]
    }

    override fun saveForDebug() {
        soundMemoryLocalDataStore.save()
    }

}
