package jp.bellware.echo.repository

import io.kotlintest.shouldBe
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import jp.bellware.echo.datastore.local.SoundFileLocalDataStore
import jp.bellware.echo.datastore.local.SoundMemoryLocalDataStore
import jp.bellware.echo.workmanager.SoundMemoWorkManager
import org.junit.Before
import org.junit.Test

class SoundRepositoryTest {
    @MockK(relaxed = true)
    lateinit var memoryLocalDataStore: SoundMemoryLocalDataStore

    @MockK(relaxed = true)
    lateinit var fileLocalDataStore: SoundFileLocalDataStore

    @MockK(relaxed = true)
    lateinit var soundMemoWorkManager: SoundMemoWorkManager

    private lateinit var repository: SoundRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = SoundRepositoryImpl(memoryLocalDataStore, fileLocalDataStore, soundMemoWorkManager)
    }

    @Test
    fun start() {
        repository.start()
        verifySequence {
            fileLocalDataStore.start(any())
        }
    }

    @Test
    fun stop() {
        repository.stop(true)
        verifySequence {
            fileLocalDataStore.stop(true)
        }
    }

    @Test
    fun clear() {
        repository.clear()
        verifySequence {
            memoryLocalDataStore.clear()
        }
    }

    @Test
    fun packageSize() {
        every {
            memoryLocalDataStore.packetSize
        } returns 10
        repository.packetSize shouldBe 10
    }

    @Test
    fun length() {
        every {
            memoryLocalDataStore.length
        } returns 10
        repository.length shouldBe 10
    }

    @Test
    fun gain() {
        every {
            memoryLocalDataStore.gain
        } returns 0.9f
        repository.gain shouldBe 0.9f
    }

    @Test
    fun add() {
        repository.add(
                shortArrayOf((Short.MAX_VALUE * 7 / 10).toShort(),
                        (Short.MIN_VALUE * 8 / 10).toShort(),
                        (Short.MAX_VALUE * 9 / 10).toShort()),
                floatArrayOf(0.7f, -0.8f, 0.9f))
        verifySequence {
            memoryLocalDataStore.add(floatArrayOf(0.7f, -0.8f, 0.9f))
            fileLocalDataStore.add(
                    shortArrayOf((Short.MAX_VALUE * 7 / 10).toShort(),
                            (Short.MIN_VALUE * 8 / 10).toShort(),
                            (Short.MAX_VALUE * 9 / 10).toShort()))
        }
    }

    @Test
    fun get() {
        every {
            memoryLocalDataStore[1]
        } returns floatArrayOf(0.7f, -0.8f, 0.9f)
        repository[1] shouldBe floatArrayOf(0.7f, -0.8f, 0.9f)
    }

    @Test
    fun saveForDebug() {
        repository.saveForDebug()
        verifySequence {
            memoryLocalDataStore.save()
        }
    }
}
