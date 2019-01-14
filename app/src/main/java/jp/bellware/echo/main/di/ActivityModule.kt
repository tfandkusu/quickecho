package jp.bellware.echo.main.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import jp.bellware.echo.main.MainActivity

@Module
abstract class ActivityModule{

    @ContributesAndroidInjector
    abstract fun contributesMainActivity() : MainActivity

}