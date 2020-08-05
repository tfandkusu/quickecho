package jp.bellware.echo.repository

import jp.bellware.echo.datastore.local.SettingLocalDataStore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface SettingRepository {
    fun isSoundEffect(): Flow<Boolean>
}

class SettingRepositoryImpl @Inject constructor(private val dataStore: SettingLocalDataStore) : SettingRepository {
    override fun isSoundEffect(): Flow<Boolean> {
        return dataStore.isSoundEffect()
    }
}
