package jp.bellware.echo.repository

import jp.bellware.echo.datastore.local.SettingLocalDataStore

interface SettingRepository {
    fun isSoundEffect(): Boolean
}

class SettingRepositoryImpl(private val dataStore: SettingLocalDataStore) : SettingRepository {
    override fun isSoundEffect(): Boolean {
        return dataStore.isSoundEffect()
    }
}