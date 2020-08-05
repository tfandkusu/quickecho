package jp.bellware.echo.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import jp.bellware.echo.repository.SettingRepository
import jp.bellware.echo.repository.SettingRepositoryImpl
import jp.bellware.echo.repository.SoundRepository
import jp.bellware.echo.repository.SoundRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
abstract class HiltRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindSoundRepository(
            soundRepository: SoundRepositoryImpl
    ): SoundRepository

    @Binds
    @Singleton
    abstract fun bindSettingRepository(
            settingRepository: SettingRepositoryImpl
    ): SettingRepository
}
