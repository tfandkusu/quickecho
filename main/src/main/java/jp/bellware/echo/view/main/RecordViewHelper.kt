package jp.bellware.echo.view.main


import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Process
import androidx.lifecycle.ViewModel
import jp.bellware.echo.repository.SoundRepository
import jp.bellware.echo.util.BWU
import jp.bellware.echo.util.filter.FirstCut
import jp.bellware.echo.util.filter.PacketConverter
import jp.bellware.echo.util.filter.ZeroCrossRecordVisualVolumeProcessor

/**
 * 録音担当ViewHelper
 * @param repository 記録担当
 */
class RecordViewHelper(
        private val repository: SoundRepository) : ViewModel() {

    companion object {

        /**
         * 視覚的ボリュームの適用範囲外サンプル数
         */
        private const val FC = 2 * SoundRepository.SAMPLE_RATE / 10
    }

    /**
     * 録音API呼び出し
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
        packetSize = repository.getSuitablePackageSize()
        record = AudioRecord(MediaRecorder.AudioSource.DEFAULT, SoundRepository.SAMPLE_RATE,
                AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, packetSize * 2)
        record?.startRecording()
        thread = Thread(Runnable {
            //これがないと音が途切れる
            Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO)
            while (true) {
                val data = ShortArray(packetSize)
                val size = record?.read(data, 0, packetSize) ?: 0
                if (size >= 1) {
                    addPacket(data)
                } else {
                    break
                }
            }
            BWU.log("RecordHandler#onResume RecordThread end")
        })
        thread?.start()
    }


    /**
     * 録音を終了する
     */
    private fun stopRecord() {
        record?.stop()
        try {
            thread?.join()
        } catch (e: InterruptedException) {
        }
        record?.release()
        record = null
        clear()
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
        repository.start()
    }

    /**
     * 音声パケットの記録を終了する
     */
    @Synchronized
    fun stop() {
        recording = false
        repository.stop()
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
            val fd = PacketConverter.convert(packet)
            repository.add(packet, fd)
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
        repository.clear()
    }
}
