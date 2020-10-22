package jp.bellware.echo.view.main

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.os.Handler
import android.os.Process
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import jp.bellware.echo.repository.SoundRepository
import jp.bellware.echo.util.filter.FadeOut
import jp.bellware.echo.util.filter.FirstCut
import jp.bellware.echo.util.filter.PacketConverter
import jp.bellware.echo.util.filter.PlayVisualVolumeProcessor
import java.util.*

/**
 * 音声再生担当ViewHelper
 * @param repository 録音データ格納
 */
class PlayViewHelper @ViewModelInject constructor(private val repository: SoundRepository) : ViewModel() {

    companion object {
        /**
         * サンプル数
         */
        private const val SAMPLE_RATE = 44100

        /**
         * 視覚的ボリュームの範囲外サンプル数
         */
        private const val FC = 2 * SAMPLE_RATE / 10
    }

    private var track: AudioTrack? = null

    private var thread: Thread? = null

    private val vvp = PlayVisualVolumeProcessor()

    private val handler = Handler()

    private lateinit var fo: FadeOut

    private val fc = FirstCut(FC)

    /**
     * 再生パケットインデックス
     */
    private var index: Int = 0

    val visualVolume: Float
        @Synchronized get() = vvp.getVolume()

    private var playing: Boolean = false
        @Synchronized set
        @Synchronized get

    /**
     * 再生する
     * @param onStart 再生開始時の処理
     * @param onEnd 正妻終了時の処理
     */
    fun play(onStart: () -> Unit, onEnd: () -> Unit) {
        stop()
        if (repository.length == 0) {
            //長さ0の時はすぐに終わる
            onEnd()
            return
        }
        val attributes = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build()
        val format = AudioFormat.Builder().setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                .setEncoding(AudioFormat.ENCODING_PCM_16BIT).setSampleRate(44100).build()
        track = AudioTrack(attributes, format,
                repository.packetSize * 2, AudioTrack.MODE_STREAM, AudioManager.AUDIO_SESSION_ID_GENERATE)
        //最初のパケットは効果音が入っていることがあるので捨てる
        index = 1
        thread = Thread(Runnable {
            //これがないと音が途切れる
            Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO)
            if (false) {
                //デバッグ用セーブ
                repository.saveForDebug()
            }
            fo = FadeOut(repository.length, SAMPLE_RATE * 3 / 10)
            fc.reset()
            handler.post {
                onStart()
            }
            track?.let {
                it.play()
                while (playing) {
                    val packet = pullPacket()
                    if (packet != null) {
                        filter(packet)
                        val shortPacket = PacketConverter.convert(packet)
                        val result = it.write(shortPacket, 0, shortPacket.size)
                        if (result < 0)
                            break
                    } else {
                        break
                    }
                }
            }
            handler.post {
                onEnd()
            }
        })
        playing = true
        thread?.start()
    }

    /**
     * 再生を終了する
     */
    fun stop() {
        track?.stop()
        playing = false
        try {
            thread?.join()
        } catch (e: InterruptedException) {
        }
        track?.release()
        track = null
    }

    override fun onCleared() {
        stop()
    }

    private fun pullPacket(): FloatArray? {
        val packet = repository[index]
        ++index
        return if (packet == null) {
            null
        } else {
            Arrays.copyOf(packet, packet.size)
        }
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
            s /= repository.gain
            //フェードアウト
            fo.filter(s)
            //視覚的ボリューム
            vvp.add(fc.filter(s))
            //置き換え
            packet[i] = s
        }

    }

}
