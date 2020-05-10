package jp.bellware.echo.datastore.local

import android.content.Context
import android.media.MediaCodec
import android.media.MediaCodec.BufferInfo
import android.media.MediaCodecInfo
import android.media.MediaExtractor
import android.media.MediaFormat
import android.os.Environment
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.ByteBuffer
import kotlin.experimental.or


/**
 * AACファイル1つを読み込んで音声データに戻す担当
 */
object AacDecoder {
    private const val TIMEOUT_US = 1000L

    /**
     * 一時保存した音声を読み込む
     */
    suspend fun load(context: Context): ShortArray {
        // こちらを参考に作成する
        // https://github.com/taehwandev/MediaCodecExample/blob/master/src/net/thdev/mediacodecexample/decoder/AudioDecoderThread.java
        val outputStream = ByteArrayOutputStream()
        // 読み込みファイルオープン
        context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)?.let { file ->
            try {
                val path = "%s/output.aac".format(file.absolutePath)
                val fis = FileInputStream(path)
                // メディアファイルから情報取得するオブジェクトを生成する
                val extractor = MediaExtractor()
                extractor.setDataSource(fis.fd)
                Timber.d("trackCount = " + extractor.trackCount)
                extractor.selectTrack(0)
                val format = makeAACCodecSpecificData(MediaCodecInfo.CodecProfileLevel.AACObjectLC,
                        AacEncodeSession.SAMPLE_RATE,
                        AacEncodeSession.CHANNEL)
                val decoder = MediaCodec.createDecoderByType("audio/mp4a-latm")
                decoder.configure(format, null, null, 0)
                decoder.start()

                var eof = false
                while (!eof) {
                    Timber.d("dequeueInputBuffer")
                    val inIndex = decoder.dequeueInputBuffer(TIMEOUT_US)
                    Timber.d("inIndex = $inIndex")
                    if (inIndex >= 0) {
                        Timber.d("getInputBuffer")
                        val buffer = decoder.getInputBuffer(inIndex)
                        buffer?.let {
                            Timber.d("readSampleData")
                            val sampleSize = extractor.readSampleData(it, 0)
                            Timber.d("sampleSize = $sampleSize")
                            if (sampleSize < 0) {
                                Timber.d("queueInputBuffer")
                                decoder.queueInputBuffer(inIndex, 0, 0,
                                        0, MediaCodec.BUFFER_FLAG_END_OF_STREAM)
                                eof = true
                            } else {
                                Timber.d("queueInputBuffer")
                                decoder.queueInputBuffer(inIndex, 0, sampleSize,
                                        extractor.sampleTime, 0)
                                Timber.d("extractor.advance")
                                extractor.advance()
                            }
                        }
                        if (eof)
                            break
                        val info = BufferInfo()
                        Timber.d("dequeueOutputBuffer")
                        val outIndex = decoder.dequeueOutputBuffer(info, TIMEOUT_US)
                        Timber.d("outIndex = $outIndex size = " + info.size)
                        if (outIndex >= 0) {
                            Timber.d("getOutputBuffer")
                            val outBuffer = decoder.getOutputBuffer(outIndex)
                            outBuffer?.let {
                                val chunk = ByteArray(info.size)
                                it.get(chunk)
                                it.clear()
                                Timber.d("releaseOutputBuffer")
                                decoder.releaseOutputBuffer(outIndex, false)
                                @Suppress("BlockingMethodInNonBlockingContext")
                                outputStream.write(chunk)
                            }
                        }
                    }
                }
                val byteArray = outputStream.toByteArray()
                context.getExternalFilesDir(Environment.DIRECTORY_MUSIC).let {
                    val path = "%s/debug.raw".format(file.absolutePath)
                    Timber.d(path)
                    val fos = FileOutputStream(path)
                    fos.write(byteArray)
                    fos.close()
                }
                return byteArray.asList()
                        .chunked(2)
                        .map { (l, h) ->
                            val li = if (l >= 0)
                                l.toInt()
                            else
                                256 + l
                            val hi = if (h >= 0)
                                h.toInt()
                            else
                                256 + h
                            li + 256 * hi
                        }
                        .map { it.toShort() }
                        .toShortArray()
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
    @Suppress("SameParameterValue")
    private fun makeAACCodecSpecificData(audioProfile: Int, sampleRate: Int, channelConfig: Int): MediaFormat? {
        val format = MediaFormat()
        format.setString(MediaFormat.KEY_MIME, "audio/mp4a-latm")
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