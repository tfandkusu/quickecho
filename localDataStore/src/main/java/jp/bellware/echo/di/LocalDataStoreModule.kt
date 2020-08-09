package jp.bellware.echo.di

import androidx.preference.PreferenceManager
import jp.bellware.echo.datastore.local.*
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

@Suppress("USELESS_CAST")
val localDataStoreModule = module {
    // LocalDataStore
    single { SoundMemoryLocalDataStoreImpl(androidContext()) as SoundMemoryLocalDataStore }
    single { SoundFileLocalDataStoreImpl(androidContext(), PreferenceManager.getDefaultSharedPreferences(androidContext())) as SoundFileLocalDataStore }
    factory { SettingLocalDataStoreImpl(PreferenceManager.getDefaultSharedPreferences(androidContext())) as SettingLocalDataStore }
}


