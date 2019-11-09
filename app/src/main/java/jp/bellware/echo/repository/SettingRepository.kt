package jp.bellware.echo.repository

import jp.bellware.echo.datastore.local.SettingLocalDatastore

interface SettingRepository {
    fun isSoundEffect(): Boolean
}

class SettingRepositoryImpl(private val dataStore: SettingLocalDatastore) : SettingRepository {
    override fun isSoundEffect(): Boolean {
        return dataStore.isSoundEffect()
    }
}