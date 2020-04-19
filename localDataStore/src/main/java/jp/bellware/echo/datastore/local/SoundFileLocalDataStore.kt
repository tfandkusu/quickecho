package jp.bellware.echo.datastore.local

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaCodec
import android.media.MediaCodecInfo
import android.media.MediaCodecList
import android.media.MediaFormat
import android.os.Environment
import timber.log.Timber
import java.io.FileOutputStream


/**
 * 録音音声をファイルに保存する
 */
interface SoundFileLocalDataStore {

    /**
     * 録音開始
     */
    fun start()

    /**
     * 音声パケットを追加
     */
    fun add(data: ShortArray)

    /**
     * 録音終了
     */
    fun stop()

}

class SoundFileLocalDataStoreImpl(private val context: Context) : SoundFileLocalDataStore {

    private val queue = mutableListOf<ShortArray>()

    var mediaCodec: MediaCodec? = null

    var fos: FileOutputStream? = null

    @SuppressLint("SdCardPath")
    override fun start() {
        Timber.d("start")
        // Make media codec
        val mediaCodecList = MediaCodecList(MediaCodecList.REGULAR_CODECS)
        val audioFormatName = mediaCodecList.findEncoderForFormat(MediaFormat.createAudioFormat(MediaFormat.MIMETYPE_AUDIO_AAC,
                44100, 1))
        mediaCodec = MediaCodec.createByCodecName(audioFormatName)
        val audioFormat = MediaFormat.createAudioFormat(MediaFormat.MIMETYPE_AUDIO_AAC, 44100, 1)
        audioFormat.setInteger(MediaFormat.KEY_BIT_RATE, 128 * 1024)
        audioFormat.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, 0)
        audioFormat.setInteger(MediaFormat.KEY_AAC_PROFILE,
                MediaCodecInfo.CodecProfileLevel.AACObjectLC /* TODO あとで意味を確認する */)
        mediaCodec?.configure(audioFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
        // Open file to write
        context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)?.let {
            val path = "%s/output.aac".format(it.absolutePath)
            try {
                fos = FileOutputStream(path)
            } catch (e: Throwable) {
                Timber.d(e)
            }
        }
        mediaCodec?.setCallback(object : MediaCodec.Callback() {

            private var presentationTimeUs = 0L

            override fun onInputBufferAvailable(codec: MediaCodec, index: Int) {
                Timber.d("onInputBufferAvailable $index")

                val inputBuffer = codec.getInputBuffer(index)
                inputBuffer?.let {
                    val shortBuffer = it.asShortBuffer()
                    val data = pull()
                    shortBuffer.put(data)
                    codec.queueInputBuffer(index, 0, data.size * 2, presentationTimeUs, 0)
                    presentationTimeUs += 1000000 * data.size / 44100
                }
            }

            override fun onOutputBufferAvailable(codec: MediaCodec, index: Int, info: MediaCodec.BufferInfo) {
                Timber.d("onOutputBufferAvailable $index")

                val outputBuffer = codec.getOutputBuffer(index)
                outputBuffer?.let {
                    val array = it.array()
                    fos?.write(array)
                    codec.releaseOutputBuffer(index, false)
                }
            }

            override fun onOutputFormatChanged(codec: MediaCodec, format: MediaFormat) {
            }

            override fun onError(codec: MediaCodec, e: MediaCodec.CodecException) {
                Timber.d(e)
            }
        })
        try {
            mediaCodec?.start()
        } catch (e: Throwable) {
            Timber.d(e)
        }
    }

    @Synchronized
    override fun add(data: ShortArray) {
        queue.add(data)
    }

    @Synchronized
    private fun pull(): ShortArray {
        if (queue.isNotEmpty())
            return queue.removeAt(0)
        else
            return shortArrayOf()
    }

    override fun stop() {
        Timber.d("stop %d".format(queue.size))
        mediaCodec?.stop()
        mediaCodec?.release()
        fos?.close()
    }

}