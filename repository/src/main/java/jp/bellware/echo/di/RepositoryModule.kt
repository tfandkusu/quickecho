package jp.bellware.echo.di

import jp.bellware.echo.repository.SettingRepository
import jp.bellware.echo.repository.SettingRepositoryImpl
import jp.bellware.echo.repository.SoundRepository
import jp.bellware.echo.repository.SoundRepositoryImpl
import org.koin.dsl.module

val localDataStoreModuleInRepositoryModule = localDataStoreModule
val repositoryModule = module {
    // Repository
    factory { SettingRepositoryImpl(get()) as SettingRepository }
    factory { SoundRepositoryImpl(get()) as SoundRepository }
}