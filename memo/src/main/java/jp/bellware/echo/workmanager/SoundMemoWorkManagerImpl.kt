package jp.bellware.echo.workmanager

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SoundMemoWorkManagerImpl
@Inject constructor(@ApplicationContext private val context: Context) : SoundMemoWorkManager {
    override fun save(fileName: String) {
        val uploadWorkRequest = OneTimeWorkRequestBuilder<SoundMemoSaveWorker>()
            .setInputData(
                workDataOf(
                    SoundMemoSaveWorker.PARAM_FILE_NAME to fileName
                )
            )
            .build()
        WorkManager.getInstance(context).enqueue(uploadWorkRequest)
    }
}
