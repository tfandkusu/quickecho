package jp.bellware.echo.di

import jp.bellware.echo.actioncreator.MainActionCreator
import jp.bellware.echo.main.MainViewModel
import jp.bellware.echo.main.SoundEffectHandler
import jp.bellware.echo.main2.SoundEffectViewModel
import jp.bellware.echo.store.MainStore
import jp.bellware.util.Dispatcher
import jp.bellware.util.DispatcherImpl
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

/**
 * DIのKOINの設定
 */
object KoinSetting {
    fun start() {
        // DIの設定
        val myModule = module {
            single { DispatcherImpl() as Dispatcher }
            viewModel { MainViewModel(androidContext()) }
            viewModel { MainStore() }
            viewModel { SoundEffectViewModel() }
            factory { SoundEffectHandler() }
            factory { MainActionCreator(get()) }
        }
        startKoin {
            modules(myModule)
        }
    }
}