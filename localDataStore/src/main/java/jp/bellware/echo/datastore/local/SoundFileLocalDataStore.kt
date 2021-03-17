package jp.bellware.echo.datastore.local

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

/**
 * 録音音声をファイルに入出力する
 */
interface SoundFileLocalDataStore {

    /**
     * 録音開始
     * @param onSaved 保存完了時の処理
     */
    fun start(onSaved: (fileName: String) -> Unit)

    /**
     * 音声パケットを追加
     */
    fun add(data: ShortArray)

    /**
     * 録音終了
     * @param save 保存フラグ
     */
    fun stop(save: Boolean)

    /**
     * 録音音声を読み込み
     * @return 波形データ
     */
    fun load(): Flow<ShortArray>
}

class SoundFileLocalDataStoreImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val pref: SharedPreferences
) : SoundFileLocalDataStore {

    private var session: AacEncodeSession? = null

    companion object {
        /**
         * 直近保存ファイル名
         */
        private const val PREF_RECENT_FILE_NAME = "recentFileName"
    }

    override fun start(onSaved: (fileName: String) -> Unit) {
        session = AacEncodeSession(context) { fileName ->
            // 一時保存ファイル名書き込み
            pref.edit().putString(PREF_RECENT_FILE_NAME, fileName).apply()
            onSaved(fileName)
        }
        session?.start()
    }

    override fun add(data: ShortArray) {
        session?.add(data)
    }

    override fun stop(save: Boolean) {
        session?.stop(save)
        session = null
    }

    override fun load(): Flow<ShortArray> {
        val fileName = pref.getString(PREF_RECENT_FILE_NAME, "") ?: ""
        return AacDecoder.load(context, fileName)
    }
}
