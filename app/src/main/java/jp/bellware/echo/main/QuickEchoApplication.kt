package jp.bellware.echo.main

import android.app.Application
import jp.bellware.echo.di.KoinSetting
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class QuickEchoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        KoinSetting.start()
    }
}