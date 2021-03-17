package jp.bellware.echo.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton
import jp.bellware.echo.workmanager.SoundMemoWorkManager
import jp.bellware.echo.workmanager.SoundMemoWorkManagerImpl

@Module
@InstallIn(ApplicationComponent::class)
abstract class HiltSoundMemoModule {
    @Binds
    @Singleton
    abstract fun bindSoundMemoWorkManager(
        soundMemoWorkManager: SoundMemoWorkManagerImpl
    ): SoundMemoWorkManager
}
