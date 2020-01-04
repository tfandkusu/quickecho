package jp.bellware.echo.view.main


import android.media.AudioFormat
import android.media.AudioRecord
import android.media.AudioTrack
import android.media.MediaRecorder
import androidx.lifecycle.ViewModel
import jp.bellware.echo.datastore.local.SoundLocalDataStore
import jp.bellware.echo.util.BWU
import jp.bellware.echo.util.filter.FirstCut
import jp.bellware.echo.util.filter.PacketConverter
import jp.bellware.echo.util.filter.ZeroCrossRecordVisualVolumeProcessor

/**
 * 録音担当ViewHelper
 * @param storage 記録担当
 */
class RecordViewHelper(
        private val storage: SoundLocalDataStore) : ViewModel() {

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


    /**
     * 視覚的ボリューム担当
     */
    private val vvp = ZeroCrossRecordVisualVolumeProcessor()


    /**
     * パケットの変換担当
     */
    private val converter = PacketConverter()

    /**
     * 録音中フラグ
     */
    private var recording = false


    /**
     * 視覚的ボリューム
     */
    val visualVolume: Float
        @Synchronized get() = vvp.getVolume()

    /**
     * 音声が含まれているフラグ
     */
    val isIncludeSound: Boolean
        @Synchronized get() = vvp.isIncludeSound()

    init {
        startRecord()
    }

    /**
     * 録音を開始する
     */
    private fun startRecord() {
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


    /**
     * 録音を終了する
     */
    private fun stopRecord() {
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

    /**
     * 音声パケットの記録を開始する
     */
    @Synchronized
    fun start() {
        recording = true
        vvp.reset()
        fc.reset()
        clear()
    }

    /**
     * 音声パケットの記録を終了する
     */
    @Synchronized
    fun stop() {
        recording = false
    }

    override fun onCleared() {
        stopRecord()
    }

    /**
     * 音声パケットを記録中の時のみ記録する
     */
    @Synchronized
    private fun addPacket(packet: ShortArray) {
        if (recording) {
            val fd = converter.convert(packet)
            storage.add(fd)
            for (s in fd) {
                vvp.add(fc.filter(s))
            }
        }
    }

    /**
     * 録音音声をクリアする
     */
    @Synchronized
    private fun clear() {
        storage.clear()
    }

    companion object {
        /**
         * サンプル数
         */
        private const val SAMPLE_RATE = 44100

        /**
         * 視覚的ボリュームの適用範囲外サンプル数
         */
        private const val FC = 2 * SAMPLE_RATE / 10
    }


}
