package jp.bellware.echo.datastore.local

import android.content.SharedPreferences

interface SettingLocalDatastore {
    fun isSoundEffect(): Boolean
}

/**
 * ローカルファイルからの設定の出し入れを行う
 */
class SettingLocalDatastoreImpl(private val pref: SharedPreferences) : SettingLocalDatastore {

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