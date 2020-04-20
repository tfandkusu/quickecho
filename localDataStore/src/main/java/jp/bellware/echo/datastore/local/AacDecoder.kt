package jp.bellware.echo.datastore.local

import android.content.Context
import android.media.MediaCodec
import android.media.MediaFormat
import android.os.Environment
import java.io.FileInputStream

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
        

        // 読み込みファイルオープン
        var fis: FileInputStream? = null
        context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)?.let {
            val path = "%s/output.aac".format(it.absolutePath)
            fis = FileInputStream(path)
        }

        // MediaCodecの作成
        val mediaCodec = MediaCodec.createDecoderByType(MediaFormat.MIMETYPE_AUDIO_AAC)
        val mediaFormat = MediaFormat.createAudioFormat(MediaFormat.MIMETYPE_AUDIO_AAC,
                AacEncodeSession.SAMPLE_RATE, AacEncodeSession.CHANNEL)
        mediaCodec.configure(mediaFormat, null, null, 0)
        mediaCodec.start()

        return shortArrayOf()
    }
}