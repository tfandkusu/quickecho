package jp.bellware.echo.di

import android.app.Application
import jp.bellware.echo.navigation.MainNavigation
import jp.bellware.echo.navigation.MainNavigationImpl
import jp.bellware.echo.navigation.SettingNavigation
import jp.bellware.echo.navigation.SettingNavigationImpl
import jp.bellware.echo.view.pager.FlutterFragmentFactory
import jp.bellware.echo.view.pager.FlutterFragmentFactoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

/**
 * DIのKOINの設定
 */
object KoinSetting {
    @Suppress("USELESS_CAST")
    fun start(application: Application) {
        // DIの設定
        val appModule = module {
            // Navigation
            single { SettingNavigationImpl() as SettingNavigation }
            single { MainNavigationImpl() as MainNavigation }
            single { FlutterFragmentFactoryImpl() as FlutterFragmentFactory }
        }
        startKoin {
            androidContext(application.applicationContext)
            modules(listOf(appModule, localDataStoreModuleInMainModule, repositoryModuleInMainModule, fluxModule, mainModule))
        }
    }
}
