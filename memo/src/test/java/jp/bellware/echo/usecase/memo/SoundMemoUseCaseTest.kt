package jp.bellware.echo.usecase.memo

import io.mockk.MockKAnnotations
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.impl.annotations.MockK
import jp.bellware.echo.repository.CurrentTimeRepository
import jp.bellware.echo.repository.SettingRepository
import jp.bellware.echo.repository.SoundMemoRepository
import jp.bellware.echo.repository.data.SoundMemo
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class SoundMemoUseCaseTest {
    @MockK(relaxed = true)
    lateinit var settingRepository: SettingRepository

    @MockK(relaxed = true)
    lateinit var soundMemoRepository: SoundMemoRepository

    @MockK(relaxed = true)
    lateinit var currentTimeRepository: CurrentTimeRepository

    private lateinit var useCase: SoundMemoUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = SoundMemoUseCaseImpl(settingRepository, soundMemoRepository, currentTimeRepository)
    }

    /**
     * 永続セーブ
     */
    @Test
    fun savePermanently() = runBlocking {
        val now = System.currentTimeMillis()
        every {
            currentTimeRepository.now()
        } returns now
        every {
            settingRepository.isSaveEveryTime()
        } returns flow {
            emit(true)
        }
        useCase.save("test1.aac")
        coVerifySequence {
            soundMemoRepository.add(SoundMemo(0,
                    false,
                    now,
                    "test1.aac",
                    SoundMemo.LOCATION_STATUS_NOT_IMPLEMENTED,
                    0.0,
                    0.0,
                    "",
                    "",
                    "",
                    SoundMemo.TEXT_STATUS_NOT_IMPLEMENTED,
                    ""))
        }
    }


    /**
     * 一時セーブ
     */
    @Test
    fun saveTemporal() = runBlocking {
        val now = System.currentTimeMillis()
        every {
            currentTimeRepository.now()
        } returns now
        every {
            settingRepository.isSaveEveryTime()
        } returns flow {
            emit(false)
        }
        useCase.save("test1.aac")
        coVerifySequence {
            soundMemoRepository.add(SoundMemo(0,
                    true,
                    now,
                    "test1.aac",
                    SoundMemo.LOCATION_STATUS_NOT_IMPLEMENTED,
                    0.0,
                    0.0,
                    "",
                    "",
                    "",
                    SoundMemo.TEXT_STATUS_NOT_IMPLEMENTED,
                    ""))
        }
    }
}
