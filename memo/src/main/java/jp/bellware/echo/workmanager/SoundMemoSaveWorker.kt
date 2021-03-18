package jp.bellware.echo.workmanager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ApplicationComponent
import jp.bellware.echo.repository.SoundMemoRepository
import jp.bellware.echo.repository.data.SoundMemo

/**
 * 音声メモを保存する
 */
class SoundMemoSaveWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {
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
        fun soundMemoRepository(): SoundMemoRepository
    }

    override suspend fun doWork(): Result {
        // 音声メモ保存担当リポジトリを取得する
        val hiltEntryPoint =
            EntryPointAccessors.fromApplication(
                applicationContext,
                SoundMemoSaveWorkerEntryPoint::class.java
            )
        val repository = hiltEntryPoint.soundMemoRepository()
        // 音声メモを保存する
        val soundMemo = SoundMemo(
            0,
            true,
            System.currentTimeMillis(),
            inputData.getString(PARAM_FILE_NAME) ?: "",
            SoundMemo.LOCATION_STATUS_NOT_IMPLEMENTED,
            0.0,
            0.0,
            "",
            "",
            "",
            SoundMemo.TEXT_STATUS_NOT_IMPLEMENTED,
            ""
        )
        repository.add(soundMemo)
        return Result.success()
    }
}
