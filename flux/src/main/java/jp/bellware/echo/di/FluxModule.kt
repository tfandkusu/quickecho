package jp.bellware.echo.di

import jp.bellware.echo.util.ActionReceiver
import jp.bellware.echo.util.ActionReceiverImpl
import jp.bellware.echo.util.Dispatcher
import jp.bellware.echo.util.DispatcherImpl
import org.koin.dsl.module

val fluxModule = module {
    single { ActionReceiverImpl() as ActionReceiver }
    single { DispatcherImpl() as Dispatcher }
}
