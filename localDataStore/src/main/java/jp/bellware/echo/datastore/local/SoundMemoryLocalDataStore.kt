package jp.bellware.echo.datastore.local

import android.content.Context
import android.os.Environment
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.inject.Inject
import jp.bellware.echo.util.filter.GainDetector

/**
 * 録音音声をメモリーに保持する
 */
interface SoundMemoryLocalDataStore {

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
    fun save()
}

/**
 * 録音音声を保持する
 */
class SoundMemoryLocalDataStoreImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SoundMemoryLocalDataStore {

    /**
     * 最大のパケットの大きさ
     */
    @get:Synchronized
    override var packetSize: Int = 0
        private set

    /**
     * 合計長さ
     */
    @get:Synchronized
    override var length: Int = 0
        private set

    /**
     * 音声パケットリスト
     */
    private
    val packets = ArrayList<FloatArray>()

    /**
     * 録音出来たボリューム
     */
    override var gain = 0f
        private set

    /**
     * ボリューム取得担当
     */
    private
    val gd = GainDetector()

    /**
     * 録音をクリア
     */
    @Synchronized
    override fun clear() {
        packetSize = 0
        gain = 0f
        packets.clear()
        length = 0
        gd.reset()
    }

    /**
     * 音声パケットを追加
     * @param data 音声パケット
     */
    @Synchronized
    override fun add(data: FloatArray) {
        if (data.size > packetSize)
            packetSize = data.size
        packets.add(data)
        length += data.size
        for (s in data)
            gd.add(s)
        gain = gd.max
    }

    /**
     * 音声パケットを取得。記録されているパケット数以上のインデックスを設定すると、nullが返却される。
     * @param index インデックス。大きい方が新しい。
     */
    @Synchronized
    override operator fun get(index: Int): FloatArray? {
        return if (index >= packets.size) null else packets[index]
    }

    /**
     * デバッグ用にwavファイルを保存する
     */
    override fun save() {
        context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)?.let {
            val fos = FileOutputStream(it.absolutePath + "/output.wav")
            // ヘッダ書き込み
            val buffer = ByteBuffer.allocate(44)
            buffer.order(ByteOrder.LITTLE_ENDIAN)
            buffer.put("RIFF".toByteArray())
            buffer.putInt(44 - 8 + length * 2) // これ以降のファイルサイズ
            buffer.put("WAVE".toByteArray())
            buffer.put("fmt ".toByteArray())
            buffer.putInt(16) // fmtチャンクのバイト数
            buffer.putShort(1.toShort()) // リニアPCM
            buffer.putShort(1.toShort()) // モノラル
            buffer.putInt(44100) // サンプリングレート
            buffer.putInt(88200) // データ速度
            buffer.putShort(2.toShort()) // ブロックサイズ
            buffer.putShort(16.toShort()) // サンプルあたりビット数
            buffer.put("data".toByteArray()) // dataチャンク
            buffer.putInt(length * 2) // 波形データのバイト数
            fos.write(buffer.array())
            // データ書き込み
            for (packet in packets) {
                val data = ByteBuffer.allocate(packet.size * 2)
                data.order(ByteOrder.LITTLE_ENDIAN)
                for (i in packet.indices) {
                    val s = (packet[i] * java.lang.Short.MAX_VALUE).toShort()
                    data.putShort(s)
                }
                fos.write(data.array())
            }
            fos.close()
        }
    }
}
