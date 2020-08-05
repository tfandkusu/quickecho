package jp.bellware.echo.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import jp.bellware.echo.datastore.local.SoundFileLocalDataStore
import jp.bellware.echo.datastore.local.SoundFileLocalDataStoreImpl
import jp.bellware.echo.datastore.local.SoundMemoryLocalDataStore
import jp.bellware.echo.datastore.local.SoundMemoryLocalDataStoreImpl
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
abstract class HiltLocalDataStoreModule {
    @Binds
    @Singleton
    abstract fun bindSoundMemoryLocalDataStore(
            soundMemoryLocalDataStore: SoundMemoryLocalDataStoreImpl
    ): SoundMemoryLocalDataStore

    @Binds
    @Singleton
    abstract fun bindSoundFileLocalDataStore(
            soundFileLocalDataStore: SoundFileLocalDataStoreImpl
    ): SoundFileLocalDataStore
}
