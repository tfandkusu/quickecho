package jp.bellware.echo.repository

import javax.inject.Inject
import jp.bellware.echo.datastore.local.SettingLocalDataStore
import kotlinx.coroutines.flow.Flow

/**
 * 設定リポジトリ
 */
interface SettingRepository {
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

class SettingRepositoryImpl @Inject constructor(private val dataStore: SettingLocalDataStore) :
    SettingRepository {
    override fun isSoundEffect(): Flow<Boolean> {
        return dataStore.isSoundEffect()
    }

    override fun isShowSoundMemoButton(): Flow<Boolean> {
        return dataStore.isShowSoundMemoButton()
    }

    override fun isSaveEveryTime(): Flow<Boolean> {
        return dataStore.isSaveEveryTime()
    }
}
