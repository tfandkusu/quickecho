package jp.bellware.echo.di

import androidx.preference.PreferenceManager
import jp.bellware.echo.datastore.local.SettingLocalDataStore
import jp.bellware.echo.datastore.local.SettingLocalDataStoreImpl
import jp.bellware.echo.datastore.local.SoundMemoryLocalDataStore
import jp.bellware.echo.datastore.local.SoundMemoryLocalDataStoreImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val localDataStoreModule = module {
    // LocalDataStore
    single { SoundMemoryLocalDataStoreImpl() as SoundMemoryLocalDataStore }
    factory { SettingLocalDataStoreImpl(PreferenceManager.getDefaultSharedPreferences(androidContext())) as SettingLocalDataStore }
}


