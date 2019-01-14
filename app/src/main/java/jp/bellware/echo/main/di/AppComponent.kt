package jp.bellware.echo.main.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    ActivityModule::class, AppModule::class
])
interface AppComponent{
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application) : Builder

        fun build() : AppComponent

    }

    fun inject(app : App)


}