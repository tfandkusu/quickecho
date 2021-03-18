package jp.bellware.echo.datastore.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import jp.bellware.echo.datastore.local.schema.LocalSoundMemo
import kotlinx.coroutines.flow.Flow

@Dao
interface SoundMemoDao {
    @Query("SELECT * FROM LocalSoundMemo ORDER BY id DESC")
    fun index(): Flow<List<LocalSoundMemo>>

    /**
     * 一時保存の音声メモだけ取得する
     */
    @Query("SELECT * FROM LocalSoundMemo WHERE temporal=1 ORDER BY id DESC")
    suspend fun indexTemporal(): List<LocalSoundMemo>

    @Insert
    suspend fun insert(localSoundMemo: LocalSoundMemo): Long

    @Update
    suspend fun update(localSoundMemo: LocalSoundMemo)

    @Delete
    suspend fun delete(localSoundMemo: LocalSoundMemo)

    @Query("DELETE FROM LocalSoundMemo")
    suspend fun clear()
}
