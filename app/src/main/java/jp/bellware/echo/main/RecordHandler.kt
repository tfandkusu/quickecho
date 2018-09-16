package jp.bellware.echo.main

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.AudioTrack
import android.media.MediaRecorder


import android.os.Handler
import jp.bellware.echo.data.QRecStorage
import jp.bellware.echo.filter.FirstCut
import jp.bellware.echo.filter.GainDetector
import jp.bellware.echo.filter.PacketConverter
import jp.bellware.echo.filter.VisualVolumeProcessor
import jp.bellware.echo.filter.ZeroCrossRecordVisualVolumeProcessor
import jp.bellware.util.BWU

/**
 * 録音担当
 */
class RecordHandler(
        /**
         *
         */
        private val storage: QRecStorage) {

    private val handler = Handler()

    /**
     *
     */
    private var record: AudioRecord? = null


    /**
     * バッファサイズ
     */
    private var packetSize = 0
    /**
     * 録音スレッド
     */
    private var thread: Thread? = null

    /**
     * 最初をカットする
     */
    private val fc = FirstCut(FC)


    private val vvp = ZeroCrossRecordVisualVolumeProcessor()


    /**
     * パケットの変換担当
     */
    private val converter = PacketConverter()

    /**
     * 録音中フラグ
     */
    private var recording = false


    val visualVolume: Float
        @Synchronized get() = vvp.getVolume()

    val isIncludeSound: Boolean
        @Synchronized get() = vvp.isIncludeSound()

    fun onResume() {
        val recordMinBufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT)
        val playMinBufferSize = AudioTrack.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT)
        BWU.log("RecordHandler#onResume recordMinBufferSize = $recordMinBufferSize")
        BWU.log("RecordHandler#onResume playMinBufferSize = $playMinBufferSize")
        packetSize = Math.max(recordMinBufferSize, playMinBufferSize)
        record = AudioRecord(MediaRecorder.AudioSource.DEFAULT, SAMPLE_RATE,
                AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, packetSize * 2)
        val lrecord = record
        lrecord?.startRecording()
        thread = Thread(Runnable {
            //これがないと音が途切れる
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO)
            while (true) {
                val data = ShortArray(packetSize)
                var size = 0
                if (lrecord != null)
                    size = lrecord.read(data, 0, packetSize)
                if (size >= 1) {
                    addPacket(data)
                } else {
                    break
                }
            }
            BWU.log("RecordHandler#onResume RecordThread end")
        })
        val lthread = thread
        lthread?.start()
    }


    fun onPause() {
        val lrecord = record
        val lthread = thread
        if (lrecord != null && lthread != null) {
            //これを呼び出すとreadメソッドの返り値が0となる
            lrecord.stop()
            try {
                lthread.join()
            } catch (e: InterruptedException) {
            }
            lrecord.release()
            record = null
            clear()
        }
    }

    @Synchronized
    fun start() {
        recording = true
        vvp.reset()
        fc.reset()
        clear()
    }

    @Synchronized
    fun stop() {
        recording = false
    }

    @Synchronized
    private fun addPacket(packet: ShortArray) {
        if (recording) {
            val fd = converter.convert(packet)
            storage.add(fd)
            for (s in fd) {
                vvp.filter(fc.filter(s))
            }
        }
    }

    @Synchronized
    private fun clear() {
        storage.clear()
    }

    companion object {
        /**
         * サンプル数
         */
        val SAMPLE_RATE = 44100

        /**
         * 視覚的ボリュームの適用範囲外サンプル数
         */
        val FC = 2 * SAMPLE_RATE / 10
    }


}
