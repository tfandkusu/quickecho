package jp.bellware.echo.repository

import io.kotlintest.shouldBe
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import jp.bellware.echo.datastore.local.SettingLocalDataStore
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

    @Test
    fun isSoundEffect() {
        every {
            localDataStore.isSoundEffect()
        } returns true
        repository.isSoundEffect() shouldBe true
        verifySequence {
            localDataStore.isSoundEffect()
        }
    }

}