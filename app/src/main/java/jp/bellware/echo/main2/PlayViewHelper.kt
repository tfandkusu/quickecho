package jp.bellware.echo.main2

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.os.Handler
import android.os.Process
import androidx.lifecycle.ViewModel
import jp.bellware.echo.datastore.local.SoundLocalDataStore
import jp.bellware.echo.filter.FadeOut
import jp.bellware.echo.filter.FirstCut
import jp.bellware.echo.filter.PacketConverter
import jp.bellware.echo.filter.PlayVisualVolumeProcessor
import jp.bellware.echo.main.RecordHandler
import java.util.*

/**
 * 音声再生担当ViewHelper
 */
class PlayViewHelper(private val storage: SoundLocalDataStore) : ViewModel() {
    private var track: AudioTrack? = null

    private var thread: Thread? = null

    private var playing = false

    private val vvp = PlayVisualVolumeProcessor()

    private val handler = Handler()

    private var fo: FadeOut? = null

    private val fc = FirstCut(RecordHandler.FC)

    /**
     * パケットの変換担当
     */
    private val converter = PacketConverter()


    /**
     * 再生パケットインデックス
     */
    private var index: Int = 0

    val visualVolume: Float
        @Synchronized get() = vvp.getVolume()

    /**
     * 再生する
     * @param onEndListener 再生終了時の処理
     */
    fun play(onEndListener: () -> Unit) {
        stop()
        if (storage.length == 0) {
            //長さ0の時はすぐに終わる
            onEndListener()
            return
        }
        track = AudioTrack(AudioManager.STREAM_MUSIC,
                RecordHandler.SAMPLE_RATE,
                AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT,
                storage.packetSize * 2, AudioTrack.MODE_STREAM)
        val ltrack = track
        playing = true
        //最初のパケットは効果音が入っていることがあるので捨てる
        index = 1
        thread = Thread(Runnable {
            //これがないと音が途切れる
            android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO)
            if (false) {
                //デバッグ用セーブ
                storage.save()
            }

            fo = FadeOut(storage.length, RecordHandler.SAMPLE_RATE * 3 / 10)
            fc.reset()
            if (ltrack != null) {
                ltrack.play()
                try {
                    Thread.sleep((storage.packetSize * 1000 / 44100).toLong())
                } catch (e: InterruptedException) {
                }

                while (true) {
                    val packet = pullPacket(onEndListener)
                    if (packet != null) {
                        filter(packet)
                        val sd = converter.convert(packet)
                        val result = ltrack.write(sd, 0, sd.size)
                        if (result < 0)
                            break
                    } else {
                        break
                    }
                }
            }

        })
        thread?.start()
    }

    /**
     * 再生を終了する
     */
    fun stop() {
        val lthread = thread
        val ltrack = track
        if (lthread != null && ltrack != null) {
            playing = false
            try {
                lthread.join()
            } catch (e: InterruptedException) {
            }

            ltrack.release()
            track = null
        }
    }

    override fun onCleared() {
        stop()
    }

    private fun pullPacket(onEndListener: () -> Unit): FloatArray? {
        if (playing) {
            val packet = storage[index]
            ++index
            if (packet == null) {
                //終端
                if (onEndListener != null) {
                    handler.post { onEndListener() }
                }
                return null
            } else {
                return Arrays.copyOf(packet, packet.size)
            }
        } else
            return null
    }


    /**
     * フィルターをかける
     *
     * @param packet
     */
    @Synchronized
    private fun filter(packet: FloatArray) {
        for (i in packet.indices) {
            //ボリューム調整
            var s = packet[i]
            s /= storage.gain
            //フェードアウト
            val lfo = fo
            if (lfo != null)
                s = lfo.filter(s)
            //視覚的ボリューム
            vvp.add(fc.filter(s))
            //置き換え
            packet[i] = s
        }

    }

}
