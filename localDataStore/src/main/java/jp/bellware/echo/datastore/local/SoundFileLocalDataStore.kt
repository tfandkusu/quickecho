package jp.bellware.echo.datastore.local

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


/**
 * 録音音声をファイルに入出力する
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

    /**
     * 録音音声を読み込み
     * @return 波形データ
     */
    fun load(): Flow<ShortArray>

}

class SoundFileLocalDataStoreImpl @Inject constructor(@ApplicationContext private val context: Context,
                                                      private val pref: SharedPreferences) : SoundFileLocalDataStore {

    private var session: AacEncodeSession? = null

    companion object {
        /**
         * 直近保存ファイル名
         */
        private const val PREF_RECENT_FILE_NAME = "recentFileName";
    }

    override fun start() {
        session = AacEncodeSession { fileName ->
            // 一時保存ファイル名書き込み
            pref.edit().putString(PREF_RECENT_FILE_NAME, fileName).apply()
            // TODO 音声メモにも書き込む
        }
        session?.start(context)
    }

    override fun add(data: ShortArray) {
        session?.add(data)
    }


    override fun stop() {
        session?.stop()
        session = null
    }

    override fun load(): Flow<ShortArray> {
        val fileName = pref.getString(PREF_RECENT_FILE_NAME, "") ?: ""
        return AacDecoder.load(context, fileName)
    }

}
