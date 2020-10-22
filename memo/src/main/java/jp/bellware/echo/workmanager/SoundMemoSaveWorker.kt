package jp.bellware.echo.workmanager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ApplicationComponent
import jp.bellware.echo.usecase.memo.SoundMemoUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * 音声メモを保存する
 */
class SoundMemoSaveWorker(appContext: Context, workerParams: WorkerParameters) : CoroutineWorker(appContext, workerParams) {
    companion object {
        /**
         * ローカル保存したAACファイルのファイル名
         */
        const val PARAM_FILE_NAME = "fileName"
    }

    /**
     * Hiltでサポートされていないクラスに依存関係を注入する
     * https://developer.android.com/training/dependency-injection/hilt-android#not-supported
     */
    @EntryPoint
    @InstallIn(ApplicationComponent::class)
    interface SoundMemoSaveWorkerEntryPoint {
        fun soundMemoUseCase(): SoundMemoUseCase
    }

    @ExperimentalCoroutinesApi
    override suspend fun doWork(): Result {
        // 音声ファイル名
        val fileName = inputData.getString(PARAM_FILE_NAME) ?: ""
        // 音声メモ保存担当UseCaseを取得する
        val hiltEntryPoint =
                EntryPointAccessors.fromApplication(applicationContext, SoundMemoSaveWorkerEntryPoint::class.java)
        val useCase = hiltEntryPoint.soundMemoUseCase()
        // UseCase呼び出し
        useCase.save(fileName)
        return Result.success()
    }

}
