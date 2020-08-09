package jp.bellware.echo.workmanager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import timber.log.Timber

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

    override suspend fun doWork(): Result {
        Timber.d("filename = " + inputData.getString(PARAM_FILE_NAME))
        return Result.success()
    }

}
