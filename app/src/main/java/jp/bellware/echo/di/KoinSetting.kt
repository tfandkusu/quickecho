package jp.bellware.echo.di

import android.app.Application
import jp.bellware.echo.actioncreator.DelayActionCreatorHelper
import jp.bellware.echo.actioncreator.DelayActionCreatorHelperImpl
import jp.bellware.echo.actioncreator.MainActionCreator
import jp.bellware.echo.store.MainStore
import jp.bellware.echo.util.ActionReceiver
import jp.bellware.echo.util.ActionReceiverImpl
import jp.bellware.echo.util.Dispatcher
import jp.bellware.echo.util.DispatcherImpl
import jp.bellware.echo.view.main.*
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

/**
 * DIのKOINの設定
 */
object KoinSetting {
    fun start(application: Application) {
        // DIの設定
        val appModule = module {
            single { ActionReceiverImpl() as ActionReceiver }
            single { DispatcherImpl() as Dispatcher }
            // ActionCreator
            single { DelayActionCreatorHelperImpl() as DelayActionCreatorHelper }
            single { MainActionCreator(get(), get()) }
            // Store
            viewModel { MainStore(get()) }
            // ViewHelper
            viewModel { SoundEffectViewHelper(androidContext(), get()) }
            viewModel { RecordViewHelper(get()) }
            viewModel { PlayViewHelper(get()) }
            viewModel { VisualVolumeViewHelper() }
            viewModel { TimerViewHelper() }
            single { AnimatorViewHelper() }
        }
        startKoin {
            androidContext(application.applicationContext)
            modules(listOf(appModule, repositoryModule, localDataStoreModuleInRepositoryModule))
        }
    }
}
