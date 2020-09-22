package jp.bellware.echo.repository

import io.kotlintest.shouldBe
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import jp.bellware.echo.datastore.local.SettingLocalDataStore
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class SettingRepositoryTest {
    @MockK(relaxed = true)
    lateinit var localDataStore: SettingLocalDataStore

    private lateinit var repository: SettingRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = SettingRepositoryImpl(localDataStore)
    }

    @InternalCoroutinesApi
    @Test
    fun isSoundEffect() = runBlocking {
        every {
            localDataStore.isSoundEffect()
        } returns flow {
            emit(true)
        }
        repository.isSoundEffect().collect {
            it shouldBe true
        }
        verifySequence {
            localDataStore.isSoundEffect()
        }
    }

    @InternalCoroutinesApi
    @Test
    fun isShowSoundMemoButton() = runBlocking {
        every {
            localDataStore.isShowSoundMemoButton()
        } returns flow {
            emit(true)
        }
        repository.isShowSoundMemoButton().collect {
            it shouldBe true
        }
        verifySequence {
            localDataStore.isShowSoundMemoButton()
        }
    }

    @InternalCoroutinesApi
    @Test
    fun isSaveEveryTime() = runBlocking {
        every {
            localDataStore.isSaveEveryTime()
        } returns flow {
            emit(true)
        }
        repository.isSaveEveryTime().collect {
            it shouldBe true
        }
        verifySequence {
            localDataStore.isSaveEveryTime()
        }
    }
}

