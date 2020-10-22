package jp.bellware.echo.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import jp.bellware.echo.usecase.memo.SoundMemoUseCase
import jp.bellware.echo.usecase.memo.SoundMemoUseCaseImpl
import jp.bellware.echo.workmanager.SoundMemoWorkManager
import jp.bellware.echo.workmanager.SoundMemoWorkManagerImpl
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
abstract class HiltSoundMemoModule {
    @Binds
    @Singleton
    abstract fun bindSoundMemoWorkManager(
            soundMemoWorkManager: SoundMemoWorkManagerImpl
    ): SoundMemoWorkManager

    @Binds
    @Singleton
    abstract fun bindSoundMemoUseCase(soundMemoUseCase: SoundMemoUseCaseImpl): SoundMemoUseCase
}
