package jp.bellware.echo.repository

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.AudioTrack
import jp.bellware.echo.datastore.local.SoundFileLocalDataStore
import jp.bellware.echo.datastore.local.SoundMemoryLocalDataStore
import jp.bellware.echo.util.filter.PacketConverter
import jp.bellware.echo.workmanager.SoundMemoWorkManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

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

class SoundRepositoryImpl @Inject constructor(private val soundMemoryLocalDataStore: SoundMemoryLocalDataStore,
                                              private val soundFileLocalDataStore: SoundFileLocalDataStore,
                                              private val soundMemoWorkManager: SoundMemoWorkManager) : SoundRepository {
    override val packetSize: Int
        get() = soundMemoryLocalDataStore.packetSize
    override val length: Int
        get() = soundMemoryLocalDataStore.length
    override val gain: Float
        get() = soundMemoryLocalDataStore.gain

    override fun start() {
        soundFileLocalDataStore.start { fileName ->
            soundMemoWorkManager.save(fileName)
        }
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
        // 波形をすべて読み込む
        soundFileLocalDataStore.load().take(1).collect { data ->
            // パケットサイズを取得する
            val packetSize = getSuitablePackageSize()
            // パケットの数
            val packetCount = data.size / packetSize + 1
            // パケット毎に
            (0 until packetCount).map {
                // パケットを作成する
                val packet = ShortArray(packetSize)
                // パケットにコピーする
                System.arraycopy(data, it * packetSize, packet, 0,
                        min(packetSize, data.size - it * packetSize))
                packet
            }.map {
                // Float版を作る
                PacketConverter.convert(it)
            }.map {
                // メモリーに格納する
                soundMemoryLocalDataStore.add(it)
            }
        }
    }

}
