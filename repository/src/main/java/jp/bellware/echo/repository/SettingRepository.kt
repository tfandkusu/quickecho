package jp.bellware.echo.repository

import jp.bellware.echo.datastore.local.SettingLocalDataStore
import kotlinx.coroutines.flow.Flow

interface SettingRepository {
    fun isSoundEffect(): Flow<Boolean>
}

class SettingRepositoryImpl(private val dataStore: SettingLocalDataStore) : SettingRepository {
    override fun isSoundEffect(): Flow<Boolean> {
        return dataStore.isSoundEffect()
    }
}