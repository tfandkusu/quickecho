package jp.bellware.echo.repository

import jp.bellware.echo.datastore.local.SoundMemoLocalDataStore
import jp.bellware.echo.mapper.SoundMemoMapper
import jp.bellware.echo.repository.data.SoundMemo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * 音声メモ リポジトリ
 */
interface SoundMemoRepository {
    /**
     * 音声メモの追加
     */
    suspend fun add(soundMemo: SoundMemo)

    /**
     * 音声メモ一覧
     */
    fun index(): Flow<List<SoundMemo>>
}

class SoundMemoRepositoryImpl @Inject constructor(private val localDataStore: SoundMemoLocalDataStore) : SoundMemoRepository {
    override suspend fun add(soundMemo: SoundMemo) {
        val localSoundMemo = SoundMemoMapper.map(soundMemo)
        localDataStore.insert(localSoundMemo)
    }

    override fun index(): Flow<List<SoundMemo>> {
        return localDataStore.index().map { list ->
            list.map { SoundMemoMapper.map(it) }
        }
    }
}
