package jp.bellware.echo.repository

import io.kotlintest.shouldBe
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import jp.bellware.echo.datastore.local.SettingLocalDataStore
import jp.bellware.echo.util.testFlow
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
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
        } returns testFlow(true)
        repository.isSoundEffect().collect {
            it shouldBe true
        }
        verifySequence {
            localDataStore.isSoundEffect()
        }
    }
}