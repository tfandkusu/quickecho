package jp.bellware.echo.di

import android.app.Application
import jp.bellware.echo.BuildConfig
import timber.log.Timber
import timber.log.Timber.DebugTree


class QuickEchoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG)
            Timber.plant(DebugTree())
        KoinSetting.start(this)
    }
}