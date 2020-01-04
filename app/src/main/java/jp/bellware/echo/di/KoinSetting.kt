package jp.bellware.echo.di

import android.app.Application
import android.preference.PreferenceManager
import jp.bellware.echo.actioncreator.DelayActionCreatorHelper
import jp.bellware.echo.actioncreator.DelayActionCreatorHelperImpl
import jp.bellware.echo.actioncreator.MainActionCreator
import jp.bellware.echo.datastore.local.SettingLocalDataStore
import jp.bellware.echo.datastore.local.SettingLocalDataStoreImpl
import jp.bellware.echo.datastore.local.SoundLocalDataStore
import jp.bellware.echo.datastore.local.SoundLocalDataStoreImpl
import jp.bellware.echo.main2.*
import jp.bellware.echo.repository.SettingRepository
import jp.bellware.echo.repository.SettingRepositoryImpl
import jp.bellware.echo.store.MainStore
import jp.bellware.util.ActionReceiver
import jp.bellware.util.ActionReceiverImpl
import jp.bellware.util.Dispatcher
import jp.bellware.util.DispatcherImpl
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
            single { SoundLocalDataStoreImpl() as SoundLocalDataStore }
            factory { SettingLocalDataStoreImpl(PreferenceManager.getDefaultSharedPreferences(androidContext())) as SettingLocalDataStore }
            factory { SettingRepositoryImpl(get()) as SettingRepository }
            viewModel { MainStore(get()) }
            viewModel { SoundEffectViewHelper(androidContext(), get()) }
            viewModel { RecordViewHelper(get()) }
            viewModel { PlayViewHelper(get()) }
            viewModel { VisualVolumeViewHelper() }
            viewModel { TimerViewHelper() }
            single { AnimatorViewHelper() }
            single { DelayActionCreatorHelperImpl() as DelayActionCreatorHelper }
            single { MainActionCreator(get(), get()) }
        }
        startKoin {
            androidContext(application.applicationContext)
            modules(myModule)
        }
    }
}