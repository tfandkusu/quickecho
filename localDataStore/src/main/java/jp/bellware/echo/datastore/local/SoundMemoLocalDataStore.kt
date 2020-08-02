package jp.bellware.echo.datastore.local

import androidx.room.withTransaction
import jp.bellware.echo.datastore.local.schema.LocalSoundMemo
import kotlinx.coroutines.flow.Flow

/**
 * 音声メモをローカルのファイルとSQLiteデータベースに保存する担当
 */
interface SoundMemoLocalDataStore {
    fun index(): Flow<List<LocalSoundMemo>>

    /**
     * 音声メモを追加する。
     * 一時保存の音声は最大5件保存する。
     */
    suspend fun insert(localSoundMemo: LocalSoundMemo)

    suspend fun update(localSoundMemo: LocalSoundMemo)

    suspend fun delete(localSoundMemo: LocalSoundMemo)
}

class SoundMemoLocalDataStoreImpl(private val db: QuickEchoDatabase) : SoundMemoLocalDataStore {

    private val dao = db.soundMemoDao()

    override fun index(): Flow<List<LocalSoundMemo>> {
        return dao.index()
    }

    override suspend fun insert(localSoundMemo: LocalSoundMemo) {
        db.withTransaction {
            dao.insert(localSoundMemo)
            dao.indexTemporal().mapIndexed { index, localSoundMemo ->
                if (index >= 5) {
                    dao.delete(localSoundMemo)
                }
            }
        }
    }

    override suspend fun update(localSoundMemo: LocalSoundMemo) {
        dao.update(localSoundMemo)
    }

    override suspend fun delete(localSoundMemo: LocalSoundMemo) {
        dao.delete(localSoundMemo)
    }

}
