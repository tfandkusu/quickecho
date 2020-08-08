package jp.bellware.echo.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import jp.bellware.echo.util.ActionReceiver
import jp.bellware.echo.util.ActionReceiverImpl
import jp.bellware.echo.util.Dispatcher
import jp.bellware.echo.util.DispatcherImpl
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
abstract class HiltFluxModule {
    @Binds
    @Singleton
    abstract fun bindActionReceiver(actionReceiver: ActionReceiverImpl): ActionReceiver

    @Binds
    @Singleton
    abstract fun bindDispatcher(dispatcher: DispatcherImpl): Dispatcher
}
