package jp.bellware.echo.di

import android.app.Application
import jp.bellware.echo.BuildConfig
import jp.bellware.echo.util.FlipperSetting
import timber.log.Timber


class QuickEchoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
        KoinSetting.start(this)

        FlipperSetting.start(this)
    }
}
