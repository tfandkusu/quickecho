package jp.bellware.echo.datastore.local

import android.content.Context
import androidx.room.withTransaction
import dagger.hilt.android.qualifiers.ApplicationContext
import jp.bellware.echo.datastore.local.schema.LocalSoundMemo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * 音声メモをローカルのファイルとSQLiteデータベースに保存する担当
 */
interface SoundMemoLocalDataStore {
    fun index(): Flow<List<LocalSoundMemo>>

    /**
     * 最後に保存した音声メモのID
     */
    fun getLastId(): Flow<Long>

    /**
     * 音声メモを追加する。
     * 一時保存の音声は最大5件保存する。
     */
    suspend fun insert(localSoundMemo: LocalSoundMemo): Long

    suspend fun update(localSoundMemo: LocalSoundMemo)

    suspend fun delete(localSoundMemo: LocalSoundMemo)

    /**
     * すべて削除する(テスト用)
     */
    suspend fun clear()
}

class SoundMemoLocalDataStoreImpl @Inject constructor(@ApplicationContext val context: Context, private val db: QuickEchoDatabase) : SoundMemoLocalDataStore {

    private val dao = db.soundMemoDao()

    override fun index(): Flow<List<LocalSoundMemo>> {
        return dao.index()
    }

    override fun getLastId(): Flow<Long> {
        return dao.getLastId().map {
            it ?: 0L
        }
    }

    override suspend fun insert(localSoundMemo: LocalSoundMemo): Long {
        // 削除AACファイル一覧
        val deleteFiles = mutableListOf<String>()
        val id = db.withTransaction {
            val id = dao.insert(localSoundMemo)
            dao.indexTemporal().mapIndexed { index, localSoundMemo ->
                if (index >= 5) {
                    deleteFiles.add(localSoundMemo.fileName)
                    dao.delete(localSoundMemo)
                }
            }
            id
        }
        // AACファイルを削除する
        withContext(Dispatchers.IO) {
            deleteFiles.map {
                context.deleteFile(it)
            }
        }
        return id
    }

    override suspend fun update(localSoundMemo: LocalSoundMemo) {
        dao.update(localSoundMemo)
    }

    override suspend fun delete(localSoundMemo: LocalSoundMemo) {
        dao.delete(localSoundMemo)
        // AACファイルを削除する
        withContext(Dispatchers.IO) {
            context.deleteFile(localSoundMemo.fileName)
        }
    }

    override suspend fun clear() {
        dao.clear()
    }

}
