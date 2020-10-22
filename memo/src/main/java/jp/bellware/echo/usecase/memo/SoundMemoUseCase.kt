package jp.bellware.echo.usecase.memo

import jp.bellware.echo.repository.CurrentTimeRepository
import jp.bellware.echo.repository.SettingRepository
import jp.bellware.echo.repository.SoundMemoRepository
import jp.bellware.echo.repository.data.SoundMemo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import javax.inject.Inject

interface SoundMemoUseCase {
    /**
     * 保存する
     * @param fileName ファイル名
     */
    suspend fun save(fileName: String)
}

class SoundMemoUseCaseImpl @Inject constructor(private val settingRepository: SettingRepository,
                                               private val soundMemoRepository: SoundMemoRepository,
                                               private val currentTimeRepository: CurrentTimeRepository) : SoundMemoUseCase {
    @ExperimentalCoroutinesApi
    override suspend fun save(fileName: String) {
        var temporal = true
        settingRepository.isSaveEveryTime().take(1).collect {
            temporal = !it
        }
        // 音声メモを保存する
        val soundMemo = SoundMemo(0,
                temporal,
                currentTimeRepository.now(),
                fileName,
                SoundMemo.LOCATION_STATUS_NOT_IMPLEMENTED,
                0.0,
                0.0,
                "",
                "",
                "",
                SoundMemo.TEXT_STATUS_NOT_IMPLEMENTED,
                "")
        soundMemoRepository.add(soundMemo)

    }
}
