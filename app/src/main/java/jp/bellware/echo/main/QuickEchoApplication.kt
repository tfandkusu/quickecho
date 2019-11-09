package jp.bellware.echo.main

import android.app.Application
import jp.bellware.echo.di.KoinSetting

class QuickEchoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        KoinSetting.start(this)
    }
}