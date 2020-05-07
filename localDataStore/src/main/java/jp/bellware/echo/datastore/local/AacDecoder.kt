package jp.bellware.echo.datastore.local

import android.content.Context
import android.media.MediaCodec
import android.media.MediaCodecInfo
import android.media.MediaExtractor
import android.media.MediaFormat
import android.os.Environment
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.io.IOException
import java.nio.ByteBuffer
import kotlin.experimental.or


/**
 * AACファイル1つを読み込んで音声データに戻す担当
 */
object AacDecoder {

    /**
     * 一時保存した音声を読み込む
     */
    suspend fun load(context: Context): ShortArray {
        // こちらを参考に作成する
        // https://github.com/taehwandev/MediaCodecExample/blob/master/src/net/thdev/mediacodecexample/decoder/AudioDecoderThread.java
        @Suppress("SpellCheckingInspection") val baos = ByteArrayOutputStream()
        // 読み込みファイルオープン
        context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)?.let { file ->
            try {
                val path = "%s/output.aac".format(file.absolutePath)
                val fis = FileInputStream(path)
                // メディアファイルから情報取得するオブジェクトを生成する
                val extractor = MediaExtractor()
                extractor.setDataSource(fis.fd)
                val format = makeAACCodecSpecificData(MediaCodecInfo.CodecProfileLevel.AACObjectLC,
                        AacEncodeSession.SAMPLE_RATE,
                        AacEncodeSession.CHANNEL)
                val decoder = MediaCodec.createDecoderByType("audio/mp4a-latm")
                decoder.configure(format, null, null, 0)

                return baos.toByteArray().asList().chunked(2).map { (l, h) ->
                    l.toInt() + h shl 8
                }.map { it.toShort() }.toShortArray()
            } catch (e: IOException) {
                // ファイルが無ければ呼ばない
                return shortArrayOf()
            }
        }
        return shortArrayOf()
    }

    /**
     *
     * こちらより引用
     *
     * https://github.com/taehwandev/MediaCodecExample/blob/master/src/net/thdev/mediacodecexample/decoder/AudioDecoderThread.java
     *
     * TODO apacheライセンスなのでどこかに表記する。
     *
     * The code profile, Sample rate, channel Count is used to
     * produce the AAC Codec SpecificData.
     * Android 4.4.2/frameworks/av/media/libstagefright/avc_utils.cpp refer
     * to the portion of the code written.
     *
     * MPEG-4 Audio refer : http://wiki.multimedia.cx/index.php?title=MPEG-4_Audio#Audio_Specific_Config
     *
     * @param audioProfile is MPEG-4 Audio Object Types
     * @param sampleRate
     * @param channelConfig
     * @return MediaFormat
     */
    private fun makeAACCodecSpecificData(audioProfile: Int, sampleRate: Int, channelConfig: Int): MediaFormat? {
        val format = MediaFormat()
        format.setString(MediaFormat.KEY_MIME, "audio/mp4a-latm")
        format.setInteger(MediaFormat.KEY_SAMPLE_RATE, sampleRate)
        format.setInteger(MediaFormat.KEY_CHANNEL_COUNT, channelConfig)
        val samplingFreq = intArrayOf(
                96000, 88200, 64000, 48000, 44100, 32000, 24000, 22050,
                16000, 12000, 11025, 8000
        )

        // Search the Sampling Frequencies
        var sampleIndex = -1
        for (i in samplingFreq.indices) {
            if (samplingFreq[i] == sampleRate) {
                sampleIndex = i
            }
        }
        if (sampleIndex == -1) {
            return null
        }
        val csd = ByteBuffer.allocate(2)
        csd.put((audioProfile shl 3 or (sampleIndex shr 1)).toByte())
        csd.position(1)
        csd.put(((sampleIndex shl 7 and 0x80).toByte() or ((channelConfig shl 3).toByte())))
        csd.flip()
        format.setByteBuffer("csd-0", csd) // add csd-0
        return format
    }
}