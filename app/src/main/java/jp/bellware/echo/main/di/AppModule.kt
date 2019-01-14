package jp.bellware.echo.main.di

import dagger.Module
import dagger.Provides

@Module
class AppModule {

    @Provides
    fun provideInjected() : Injected = Injected()
}