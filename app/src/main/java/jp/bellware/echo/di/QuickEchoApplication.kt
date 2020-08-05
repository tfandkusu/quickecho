package jp.bellware.echo.di

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import jp.bellware.echo.BuildConfig
import jp.bellware.echo.util.FlipperSetting
import jp.bellware.echo.view.memo.QuickEchoFlutterUtil
import timber.log.Timber


@HiltAndroidApp
class QuickEchoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
        QuickEchoFlutterUtil.onCreate(this)
        KoinSetting.start(this)
        FlipperSetting.start(this)
    }
}
