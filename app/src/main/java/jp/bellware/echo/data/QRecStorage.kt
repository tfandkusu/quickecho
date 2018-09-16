package jp.bellware.echo.data

import android.os.Environment
import jp.bellware.echo.filter.GainDetector

import java.io.FileOutputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.ArrayList

/**
 * 録音音声を保持する
 */
class QRecStorage {

    /**
     * 最大のパケットの大きさ
     */
    @get:Synchronized
    var packetSize: Int = 0
        private set

    /**
     * 合計長さ
     */
    @get:Synchronized
    var length: Int = 0
        private set


    /**
     * 音声パケットリスト
     */
    private val packets = ArrayList<FloatArray>()

    /**
     * 録音出来たボリューム
     */
    var gain = 0f


    /**
     * ボリューム取得担当
     */
    private val gd = GainDetector()


    @Synchronized
    fun clear() {
        packetSize = 0
        gain = 0f
        packets.clear()
        length = 0
        gd.reset()
    }


    @Synchronized
    fun add(data: FloatArray) {
        if (data.size > packetSize)
            packetSize = data.size
        packets.add(data)
        length += data.size
        for(s in data)
            gd.add(s)
        gain = gd.max
    }


    @Synchronized
    operator fun get(index: Int): FloatArray? {
        return if (index >= packets.size) null else packets[index]
    }

    /**
     * デバッグ用にwavファイルを保存する
     */
    fun save() {
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(Environment.getExternalStorageDirectory().absolutePath + "/output.wav")
            //ヘッダ書き込み
            val buffer = ByteBuffer.allocate(44)
            buffer.order(ByteOrder.LITTLE_ENDIAN)
            buffer.put("RIFF".toByteArray())
            buffer.putInt(44 - 8 + length * 2)//これ以降のファイルサイズ
            buffer.put("WAVE".toByteArray())
            buffer.put("fmt ".toByteArray())
            buffer.putInt(16)//fmtチャンクのバイト数
            buffer.putShort(1.toShort())//リニアPCM
            buffer.putShort(1.toShort())//モノラル
            buffer.putInt(44100)//サンプリングレート
            buffer.putInt(88200)//データ速度
            buffer.putShort(2.toShort())//ブロックサイズ
            buffer.putShort(16.toShort())//サンプルあたりビット数
            buffer.put("data".toByteArray())//dataチャンク
            buffer.putInt(length * 2)//波形データのバイト数
            fos.write(buffer.array())
            //データ書き込み
            for (packet in packets) {
                val data = ByteBuffer.allocate(packet.size * 2)
                data.order(ByteOrder.LITTLE_ENDIAN)
                for (i in packet.indices) {
                    val s = (packet[i] * java.lang.Short.MAX_VALUE).toShort()
                    data.putShort(s)
                }
                fos.write(data.array())
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (fos != null) {
                try {
                    fos.close()
                } catch (e: IOException) {
                }

            }
        }
    }
}
