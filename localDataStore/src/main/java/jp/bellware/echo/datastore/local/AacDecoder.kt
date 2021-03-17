package jp.bellware.echo.datastore.local

import android.content.Context
import android.media.MediaCodec
import android.media.MediaCodecInfo
import android.media.MediaExtractor
import android.media.MediaFormat
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import kotlin.experimental.or
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

/**
 * AACファイル1つを読み込んで音声データに戻す担当
 */
object AacDecoder {
    private const val TIMEOUT_US = 1000L

    /**
     * 一時保存した音声を読み込む
     */
    fun load(context: Context, fileName: String): Flow<ShortArray> = flow {
        val data = withContext(Dispatchers.Default) {
            loadAacFile(context, fileName)
        }
        emit(data)
    }

    /**
     * AACファイルから音声RAWデータに変換する。
     * こちらを参考に作成する。
     * https://github.com/taehwandev/MediaCodecExample/blob/master/src/net/thdev/mediacodecexample/decoder/AudioDecoderThread.java
     */
    private fun loadAacFile(context: Context, fileName: String): ShortArray {
        val outputStream = ByteArrayOutputStream()
        val fis = context.openFileInput(fileName)
        // メディアファイルから情報取得するオブジェクトを生成する
        val extractor = MediaExtractor()
        extractor.setDataSource(fis.fd)
        extractor.selectTrack(0)
        val format = makeAACCodecSpecificData(
            MediaCodecInfo.CodecProfileLevel.AACObjectLC,
            AacEncodeSession.SAMPLE_RATE,
            AacEncodeSession.CHANNEL
        )
        val decoder = MediaCodec.createDecoderByType(MediaFormat.MIMETYPE_AUDIO_AAC)
        decoder.configure(format, null, null, 0)
        decoder.start()

        var eof = false
        while (!eof) {
            val inIndex = decoder.dequeueInputBuffer(TIMEOUT_US)
            if (inIndex >= 0) {
                val buffer = decoder.getInputBuffer(inIndex)
                buffer?.let {
                    val sampleSize = extractor.readSampleData(it, 0)
                    if (sampleSize < 0) {
                        decoder.queueInputBuffer(
                            inIndex, 0, 0,
                            0, MediaCodec.BUFFER_FLAG_END_OF_STREAM
                        )
                        eof = true
                    } else {
                        decoder.queueInputBuffer(
                            inIndex, 0, sampleSize,
                            extractor.sampleTime, 0
                        )
                        extractor.advance()
                    }
                }
                if (eof)
                    break
                val info = MediaCodec.BufferInfo()
                val outIndex = decoder.dequeueOutputBuffer(info, TIMEOUT_US)
                if (outIndex >= 0) {
                    val outBuffer = decoder.getOutputBuffer(outIndex)
                    outBuffer?.let {
                        val chunk = ByteArray(info.size)
                        it.get(chunk)
                        it.clear()
                        decoder.releaseOutputBuffer(outIndex, false)
                        outputStream.write(chunk)
                    }
                }
            }
        }
        val byteArray = outputStream.toByteArray()
        val shortArray = ShortArray(byteArray.size / 2)
        shortArray.indices.map {
            val l = byteArray[2 * it]
            val h = byteArray[2 * it + 1]
            val li = if (l >= 0)
                l.toInt()
            else
                256 + l
            val hi = if (h >= 0)
                h.toInt()
            else
                256 + h
            val v = li or (hi shl 8)
            shortArray[it] = v.toShort()
        }
        return shortArray
    }

    /**
     *
     * こちらより引用
     *
     * https://github.com/taehwandev/MediaCodecExample/blob/master/src/net/thdev/mediacodecexample/decoder/AudioDecoderThread.java
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
    @Suppress("SameParameterValue")
    private fun makeAACCodecSpecificData(
        audioProfile: Int,
        sampleRate: Int,
        channelConfig: Int
    ): MediaFormat? {
        val format = MediaFormat()
        format.setString(MediaFormat.KEY_MIME, MediaFormat.MIMETYPE_AUDIO_AAC)
        format.setInteger(MediaFormat.KEY_AAC_PROFILE, audioProfile)
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
