package jp.bellware.echo.main

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class QuickEchoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // DIの設定
        val myModule = module {
            viewModel { MainViewModel(androidContext()) }
        }
        startKoin {
            modules(myModule)
        }
    }
}