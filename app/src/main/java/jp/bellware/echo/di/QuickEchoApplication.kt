package jp.bellware.echo.di

import android.app.Application

class QuickEchoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        KoinSetting.start(this)
    }
}