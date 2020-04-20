package jp.bellware.echo.repository

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.AudioTrack
import jp.bellware.echo.datastore.local.SoundFileLocalDataStore
import jp.bellware.echo.datastore.local.SoundMemoryLocalDataStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlin.math.max

/**
 * 録音担当リポジトリ
 */
interface SoundRepository {

    companion object {
        /**
         * サンプル数
         */
        const val SAMPLE_RATE = 44100
    }

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

    /**
     * 録音または再生するためも適切パケットサイズを取得する
     */
    fun getSuitablePackageSize(): Int

    /**
     *  AACファイルで保存している音声を読みこんでメモリーに格納する
     */
    suspend fun restore()
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

    override fun getSuitablePackageSize(): Int {
        val recordMinBufferSize = AudioRecord.getMinBufferSize(SoundRepository.SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT)
        val playMinBufferSize = AudioTrack.getMinBufferSize(SoundRepository.SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT)
        return max(recordMinBufferSize, playMinBufferSize)
    }

    @ExperimentalCoroutinesApi
    override suspend fun restore() {
        soundFileLocalDataStore.load().take(1).collect {
        }
    }

}
