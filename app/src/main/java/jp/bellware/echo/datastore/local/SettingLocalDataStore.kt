package jp.bellware.echo.datastore.local

import android.content.SharedPreferences

interface SettingLocalDataStore {
    fun isSoundEffect(): Boolean
}

/**
 * ローカルファイルからの設定の出し入れを行う
 */
class SettingLocalDataStoreImpl(private val pref: SharedPreferences) : SettingLocalDataStore {

    companion object {
        private const val PREF_SOUND_EFFECT = "soundEffect"
    }

    /**
     * 効果音が有効か否かを取得する
     */
    override fun isSoundEffect(): Boolean {
        return pref.getBoolean(PREF_SOUND_EFFECT, true)
    }
}