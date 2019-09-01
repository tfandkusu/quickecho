package jp.bellware.echo.di

import jp.bellware.echo.main.MainViewModel
import jp.bellware.echo.store.MainStore
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
            viewModel { MainViewModel(androidContext()) }
            viewModel { MainStore() }
        }
        startKoin {
            modules(myModule)
        }
    }
}