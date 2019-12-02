package jp.bellware.echo.di

import android.app.Application
import android.preference.PreferenceManager
import jp.bellware.echo.actioncreator.DelayActionCreatorHelper
import jp.bellware.echo.actioncreator.DelayActionCreatorHelperImpl
import jp.bellware.echo.actioncreator.MainActionCreator
import jp.bellware.echo.datastore.local.SettingLocalDatastore
import jp.bellware.echo.datastore.local.SettingLocalDatastoreImpl
import jp.bellware.echo.datastore.local.SoundLocalDatastore
import jp.bellware.echo.datastore.local.SoundLocalDatastoreImpl
import jp.bellware.echo.main.MainViewModel
import jp.bellware.echo.main.SoundEffectHandler
import jp.bellware.echo.main2.*
import jp.bellware.echo.repository.SettingRepository
import jp.bellware.echo.repository.SettingRepositoryImpl
import jp.bellware.echo.store.MainStore
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
            single { DispatcherImpl() as Dispatcher }
            single { SoundLocalDatastoreImpl() as SoundLocalDatastore }
            factory { SettingLocalDatastoreImpl(PreferenceManager.getDefaultSharedPreferences(androidContext())) as SettingLocalDatastore }
            factory { SettingRepositoryImpl(get()) as SettingRepository }
            viewModel { MainViewModel(androidContext()) }
            viewModel { MainStore() }
            viewModel { SoundEffectViewHelper(get()) }
            viewModel { RecordViewHelper(get()) }
            viewModel { PlayViewHelper(get()) }
            viewModel { VisualVolumeViewHelper() }
            viewModel { DelayTaskViewHelper() }
            single { AnimatorViewHelper() }
            factory { SoundEffectHandler() }
            single { DelayActionCreatorHelperImpl() as DelayActionCreatorHelper }
            single { MainActionCreator(get(), get()) }
        }
        startKoin {
            androidContext(application.applicationContext)
            modules(myModule)
        }
    }
}