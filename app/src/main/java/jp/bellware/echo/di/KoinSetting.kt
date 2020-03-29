package jp.bellware.echo.di

import android.app.Application
import androidx.preference.PreferenceManager
import jp.bellware.echo.actioncreator.DelayActionCreatorHelper
import jp.bellware.echo.actioncreator.DelayActionCreatorHelperImpl
import jp.bellware.echo.actioncreator.MainActionCreator
import jp.bellware.echo.datastore.local.SettingLocalDataStore
import jp.bellware.echo.datastore.local.SettingLocalDataStoreImpl
import jp.bellware.echo.datastore.local.SoundMemoryLocalDataStore
import jp.bellware.echo.datastore.local.SoundMemoryLocalDataStoreImpl
import jp.bellware.echo.repository.SettingRepository
import jp.bellware.echo.repository.SettingRepositoryImpl
import jp.bellware.echo.repository.SoundRepository
import jp.bellware.echo.repository.SoundRepositoryImpl
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
        val myModule = module {
            single { ActionReceiverImpl() as ActionReceiver }
            single { DispatcherImpl() as Dispatcher }
            // LocalDataStore
            single { SoundMemoryLocalDataStoreImpl() as SoundMemoryLocalDataStore }
            factory { SettingLocalDataStoreImpl(PreferenceManager.getDefaultSharedPreferences(androidContext())) as SettingLocalDataStore }
            // Repository
            factory { SettingRepositoryImpl(get()) as SettingRepository }
            factory { SoundRepositoryImpl(get()) as SoundRepository }
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
            modules(myModule)
        }
    }
}
