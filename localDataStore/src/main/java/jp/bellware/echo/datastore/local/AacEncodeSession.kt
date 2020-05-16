package jp.bellware.echo.datastore.local

import android.content.Context
import android.media.MediaCodec
import android.media.MediaCodecInfo
import android.media.MediaCodecList
import android.media.MediaFormat
import timber.log.Timber
import java.io.FileOutputStream
import java.util.concurrent.Executors

/**
 * 1ファイル分のAACファイル作成担当
 */
class AacEncodeSession {

    companion object {
        const val SAMPLE_RATE = 44100

        /**
         * モノラル録音
         */
        const val CHANNEL = 1

        /**
         * 一時保存用ファイル
         */
        const val SOUND_FILE_NAME = "tmp.aac"
    }


    /**
     * 音声パケットキュー
     */
    private val queue = mutableListOf<ShortArray>()

    private var mediaCodec: MediaCodec? = null

    private var fos: FileOutputStream? = null

    private var path: String = ""

    /**
     * 保存用タスクの非同期実行担当
     */
    private var executor = Executors.newSingleThreadExecutor()

    /**
     * 録音を開始する
     * @param context Application Context
     */
    fun start(context: Context) {
        // MediaCodecの作成
        val mediaCodecList = MediaCodecList(MediaCodecList.REGULAR_CODECS)
        val audioFormatName = mediaCodecList.findEncoderForFormat(MediaFormat.createAudioFormat(MediaFormat.MIMETYPE_AUDIO_AAC,
                SAMPLE_RATE, CHANNEL))
        mediaCodec = MediaCodec.createByCodecName(audioFormatName)
        val audioFormat = MediaFormat.createAudioFormat(MediaFormat.MIMETYPE_AUDIO_AAC, SAMPLE_RATE, CHANNEL)
        audioFormat.setInteger(MediaFormat.KEY_BIT_RATE, 128 * 1024)
        // AAC LCが一般的なプロファイル
        audioFormat.setInteger(MediaFormat.KEY_AAC_PROFILE,
                MediaCodecInfo.CodecProfileLevel.AACObjectLC)
        mediaCodec?.configure(audioFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
        // 書き出し用ファイルを非同期で開く
        openFileAsync(context)
        // 非同期処理のためのコールバックを設定する
        mediaCodec?.setCallback(object : MediaCodec.Callback() {

            private var presentationTimeUs = 0L

            override fun onInputBufferAvailable(codec: MediaCodec, index: Int) {
                val inputBuffer = codec.getInputBuffer(index)
                inputBuffer?.let {
                    val shortBuffer = it.asShortBuffer()
                    val data = pull(shortBuffer.remaining())
                    shortBuffer.put(data)
                    codec.queueInputBuffer(index, 0, data.size * 2, presentationTimeUs, 0)
                    presentationTimeUs += 1000000 * data.size / SAMPLE_RATE
                }
            }

            override fun onOutputBufferAvailable(codec: MediaCodec, index: Int, info: MediaCodec.BufferInfo) {
                // ここはメインスレッド
                val outputBuffer = codec.getOutputBuffer(index)
                outputBuffer?.let {
                    @Suppress("SpellCheckingInspection") val adtsSize = 7
                    val array = ByteArray(it.remaining() + adtsSize)
                    addADTStoPacket(array, array.size, SAMPLE_RATE, CHANNEL)
                    it.get(array, adtsSize, array.size - adtsSize)
                    // エンコードされた配列を非同期で保存する
                    writeArrayAsync(array)
                    codec.releaseOutputBuffer(index, false)
                }
            }

            override fun onOutputFormatChanged(codec: MediaCodec, format: MediaFormat) {
            }

            override fun onError(codec: MediaCodec, e: MediaCodec.CodecException) {
                Timber.d(e)
            }
        })
        // 録音開始
        mediaCodec?.start()
    }

    /**
     * 音声パケットを追加する
     * @param data 音声パケット
     */
    @Synchronized
    fun add(data: ShortArray) {
        queue.add(data)
    }

    /**
     * キューから音声パケットを取得する
     * @param size 取得可能なShort型要素数。これより大きい音声パケットは返却しない。
     * @return 音声パケット
     */
    @Synchronized
    private fun pull(size: Int): ShortArray {
        return if (queue.isNotEmpty()) {
            val packet = queue.removeAt(0)
            if (size >= packet.size) {
                // パケットをすべて返却できる
                packet
            } else {
                // sizeまで返却し、残りはキューの先頭に戻す
                val ret = packet.copyOfRange(0, size)
                val remain = packet.copyOfRange(size, packet.size)
                queue.add(0, remain)
                ret
            }
        } else {
            shortArrayOf()
        }
    }

    /**
     * 非同期で書き出しファイルを開く
     */
    private fun openFileAsync(context: Context) {
        executor.submit {
            fos = context.openFileOutput(SOUND_FILE_NAME, Context.MODE_PRIVATE)
        }
    }

    /**
     * 非同期でファイル保存する
     */
    private fun writeArrayAsync(array: ByteArray) {
        executor.submit {
            fos?.write(array)
        }
    }

    /**
     * 非同期でファイルを閉じる
     */
    private fun closeFileAsync() {
        executor.submit {
            fos?.close()
            fos = null
        }
    }

    /**
     * 録音を終了する
     */
    fun stop() {
        mediaCodec?.stop()
        mediaCodec?.release()
        closeFileAsync()
    }


    /**
     *
     * From https://github.com/HelloHuDi/AudioCapture/blob/master/audiocapture/src/main/java/com/hd/audiocapture/writer/AccFileWriter.java
     *
     * add acc file header
     * Add ADTS header at the beginning of each and every AAC packet. This is
     * needed as MediaCodec encoder generates a packet of raw AAC data.
     * Note the packetLen must count in the ADTS header itself.
     */
    private fun addADTStoPacket(packet: ByteArray, packetLen: Int,
                                @Suppress("SameParameterValue") sampleInHz: Int,
                                @Suppress("SameParameterValue") chanCfgCounts: Int) {
        val profile = 2 // AAC LC
        var freqIdx = 8 // 16KHz    39=MediaCodecInfo.CodecProfileLevel.AACObjectELD;
        when (sampleInHz) {
            96000 -> freqIdx = 0
            88200 -> freqIdx = 1
            64000 -> freqIdx = 2
            48000 -> freqIdx = 3
            44100 -> freqIdx = 4
            32000 -> freqIdx = 5
            24000 -> freqIdx = 6
            22050 -> freqIdx = 7
            16000 -> freqIdx = 8
            2000 -> freqIdx = 9
            11025 -> freqIdx = 10
            8000 -> freqIdx = 11
            else -> {
            }
        }
        // fill in ADTS data
        packet[0] = 0xFF.toByte()
        packet[1] = 0xF9.toByte()
        packet[2] = ((profile - 1 shl 6) + (freqIdx shl 2) + (chanCfgCounts shr 2)).toByte()
        packet[3] = ((chanCfgCounts and 3 shl 6) + (packetLen shr 11)).toByte()
        packet[4] = (packetLen and 0x7FF shr 3).toByte()
        packet[5] = ((packetLen and 7 shl 5) + 0x1F).toByte()
        packet[6] = 0xFC.toByte()
    }

}