package jp.bellware.echo.datastore.local

import android.content.Context
import android.media.MediaCodec
import android.media.MediaExtractor
import android.media.MediaFormat
import android.os.Environment
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.nio.ByteBuffer


/**
 * AACファイル1つを読み込んで音声データに戻す担当
 */
object AacDecoder {

    /**
     * 一時保存した音声を読み込む
     */
    suspend fun load(context: Context): ShortArray {
        // こちらを参考に作成する
        // https://github.com/fabio-delorenzo-wowza/Android_MediaCodec_AudioPlayer/blob/master/app/src/main/java/com/fabio/mediacodectestone/MainActivity.java
        @Suppress("SpellCheckingInspection") val baos = ByteArrayOutputStream()
        // 読み込みファイルオープン
        context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)?.let { file ->
            val path = "%s/output.aac".format(file.absolutePath)
            val fis = FileInputStream(path)
            // メディアファイルから情報取得するオブジェクトを生成する
            val extractor = MediaExtractor()
            extractor.setDataSource(fis.fd)
            // MediaCodecの作成
            val mediaCodec = MediaCodec.createDecoderByType(MediaFormat.MIMETYPE_AUDIO_AAC)
            val mediaFormat = MediaFormat.createAudioFormat(MediaFormat.MIMETYPE_AUDIO_AAC,
                    AacEncodeSession.SAMPLE_RATE, AacEncodeSession.CHANNEL)
            mediaCodec.configure(mediaFormat, null, null, 0)
            mediaCodec.start()
            // 終了フラグ
            var inputEos = false
            var outputEos = false
            while (!inputEos && !outputEos) {
                // MediaCodecに入力する
                val inputBufferId = mediaCodec.dequeueInputBuffer(-1)
                if (inputBufferId >= 0) {
                    val inputBuffer = mediaCodec.getInputBuffer(inputBufferId)
                    inputBuffer?.let {
                        var presentationTimeUs = 0L
                        var sampleSize = extractor.readSampleData(inputBuffer, 0)
                        extractor.advance()
                        if (sampleSize < 0) {
                            inputEos = true;
                            sampleSize = 0;
                        } else {
                            presentationTimeUs = extractor.sampleTime
                        }
                        mediaCodec.queueInputBuffer(inputBufferId,
                                0,
                                sampleSize,
                                presentationTimeUs,
                                if (inputEos) MediaCodec.BUFFER_FLAG_END_OF_STREAM else 0)
                    }
                } else {
                    // TODO エラー処理
                    Timber.d("dequeueInputBuffer returns minus index")
                }
                // MediaCodecから取り出す
                val info = MediaCodec.BufferInfo()
                val outputBufferId = mediaCodec.dequeueOutputBuffer(info, -1)

                if (outputBufferId >= 0) {
                    val outputBuffer = mediaCodec.getOutputBuffer(outputBufferId)
                    outputBuffer?.let {
                        val byteArray = ByteArray(info.size)
                        it.get(byteArray)
                        baos.write(byteArray)
                    }
                    mediaCodec.releaseOutputBuffer(outputBufferId, false)
                    if (info.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM != 0) {
                        outputEos = true
                    }
                } else {
                    // TODO エラー処理
                    Timber.d("dequeueInputBuffer returns minus index")
                }
            }
            return ByteBuffer.wrap(baos.toByteArray()).asShortBuffer().array()
        }
        return shortArrayOf()
    }
}