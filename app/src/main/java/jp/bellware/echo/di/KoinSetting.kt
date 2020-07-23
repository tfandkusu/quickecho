package jp.bellware.echo.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * DIのKOINの設定
 */
object KoinSetting {
    fun start(application: Application) {
        startKoin {
            androidContext(application.applicationContext)
            modules(listOf(localDataStoreModuleInMainModule, repositoryModuleInMainModule, fluxModule, mainModule))
        }
    }
}
