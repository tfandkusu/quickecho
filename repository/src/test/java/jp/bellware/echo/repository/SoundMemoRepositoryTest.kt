package jp.bellware.echo.repository

import io.kotlintest.shouldBe
import io.mockk.MockKAnnotations
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.impl.annotations.MockK
import jp.bellware.echo.datastore.local.SoundMemoLocalDataStore
import jp.bellware.echo.datastore.local.schema.LocalSoundMemo
import jp.bellware.echo.mapper.SoundMemoMapper
import jp.bellware.echo.repository.data.SoundMemo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class SoundMemoRepositoryTest {
    @MockK(relaxed = true)
    private lateinit var localDataStore: SoundMemoLocalDataStore

    lateinit var repository: SoundMemoRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = SoundMemoRepositoryImpl(localDataStore)
    }

    @Test
    fun add() = runBlocking {
        val m = SoundMemo(0,
                true,
                System.currentTimeMillis(),
                "test1.aac",
                1,
                139.0,
                35.0,
                "東京都",
                "港区",
                "赤坂3-1-6",
                2,
                "録音したこと")
        val lm = SoundMemoMapper.map(m)
        repository.add(m)
        coVerifySequence {
            localDataStore.insert(lm)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun index() = runBlocking {
        val lm = LocalSoundMemo(1,
                true,
                System.currentTimeMillis(),
                "test1.aac",
                1,
                139.0,
                35.0,
                "東京都",
                "港区",
                "赤坂3-1-6",
                2,
                "録音したこと")
        val m = SoundMemoMapper.map(lm)
        every {
            localDataStore.index()
        } returns flow {
            emit(listOf(lm))
        }
        repository.index().take(1).collect {
            it shouldBe listOf(m)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getLastId() = runBlocking {
        every {
            localDataStore.getLastId()
        } returns flow {
            emit(1L)
        }
        repository.getLastId().take(1).collect {
            it shouldBe 1L
        }
    }
}
