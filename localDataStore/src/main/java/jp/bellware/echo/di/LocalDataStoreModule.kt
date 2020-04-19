package jp.bellware.echo.di

import androidx.preference.PreferenceManager
import jp.bellware.echo.datastore.local.*
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val localDataStoreModule = module {
    // LocalDataStore
    single { SoundMemoryLocalDataStoreImpl() as SoundMemoryLocalDataStore }
    single { SoundFileLocalDataStoreImpl(androidContext()) as SoundFileLocalDataStore }
    factory { SettingLocalDataStoreImpl(PreferenceManager.getDefaultSharedPreferences(androidContext())) as SettingLocalDataStore }
}


