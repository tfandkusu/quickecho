package jp.bellware.echo.datastore.local

import android.content.SharedPreferences
import com.tfcporciuncula.flow.FlowSharedPreferences
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface SettingLocalDataStore {
    fun isSoundEffect(): Flow<Boolean>
}

/**
 * ローカルファイルからの設定の出し入れを行う
 */
class SettingLocalDataStoreImpl @Inject constructor(private val pref: SharedPreferences) : SettingLocalDataStore {

    companion object {
        private const val PREF_SOUND_EFFECT = "soundEffect"
    }

    /**
     * 効果音が有効か否かを取得する
     */
    @ExperimentalCoroutinesApi
    override fun isSoundEffect(): Flow<Boolean> {
        val flowSharedPreferences = FlowSharedPreferences(pref)
        val prefSoundEffect = flowSharedPreferences.getBoolean(PREF_SOUND_EFFECT, true)
        return prefSoundEffect.asFlow()
    }
}
