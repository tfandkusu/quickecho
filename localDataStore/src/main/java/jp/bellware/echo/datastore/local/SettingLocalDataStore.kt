package jp.bellware.echo.datastore.local

import android.content.SharedPreferences
import com.tfcporciuncula.flow.FlowSharedPreferences
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * ローカルファイルからの設定の出し入れを行う
 */
interface SettingLocalDataStore {
    /**
     * 効果音が有効か否かを取得する
     */
    fun isSoundEffect(): Flow<Boolean>

    /**
     * 音声メモボタンを表示するか取得する
     */
    fun isShowSoundMemoButton(): Flow<Boolean>

    /**
     * 毎回永続保存するか取得する
     */
    fun isSaveEveryTime(): Flow<Boolean>
}

class SettingLocalDataStoreImpl @Inject constructor(private val pref: SharedPreferences) : SettingLocalDataStore {

    companion object {
        private const val PREF_SOUND_EFFECT = "soundEffect"

        private const val PREF_SHOW_SOUND_MEMO = "showSoundMemo"

        private const val PREF_SAVE_EVERY_TIME = "saveEveryTime"
    }

    @ExperimentalCoroutinesApi
    override fun isSoundEffect(): Flow<Boolean> {
        val flowSharedPreferences = FlowSharedPreferences(pref)
        val value = flowSharedPreferences.getBoolean(PREF_SOUND_EFFECT, true)
        return value.asFlow()
    }

    @ExperimentalCoroutinesApi
    override fun isShowSoundMemoButton(): Flow<Boolean> {
        val flowSharedPreferences = FlowSharedPreferences(pref)
        val value = flowSharedPreferences.getBoolean(PREF_SHOW_SOUND_MEMO, true)
        return value.asFlow()

    }

    @ExperimentalCoroutinesApi
    override fun isSaveEveryTime(): Flow<Boolean> {
        val flowSharedPreferences = FlowSharedPreferences(pref)
        val value = flowSharedPreferences.getBoolean(PREF_SAVE_EVERY_TIME, false)
        return value.asFlow()
    }
}
