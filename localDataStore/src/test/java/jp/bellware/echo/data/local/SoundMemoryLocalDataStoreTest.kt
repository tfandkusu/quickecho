package jp.bellware.echo.data.local

import android.content.Context
import io.kotlintest.shouldBe
import io.mockk.mockk
import jp.bellware.echo.datastore.local.SoundMemoryLocalDataStore
import jp.bellware.echo.datastore.local.SoundMemoryLocalDataStoreImpl
import org.junit.Before
import org.junit.Test

class SoundMemoryLocalDataStoreTest {
    private lateinit var dataStore: SoundMemoryLocalDataStore

    @Before
    fun setUp() {
        dataStore = SoundMemoryLocalDataStoreImpl(mockk<Context>())
    }

    @Test
    fun test() {
        // 音声パケットを追加する
        dataStore.add(listOf(0.2f, -0.3f, 0.4f).toFloatArray())
        // これまでの最大音量
        dataStore.gain shouldBe 0.4f
        // これまでのサンプル数
        dataStore.length shouldBe 3
        // これまでの最大パケット数
        dataStore.packetSize shouldBe 3
        dataStore[0] shouldBe listOf(0.2f, -0.3f, 0.4f).toFloatArray()
        dataStore[1] shouldBe null
        dataStore.add(listOf(0.5f, -0.5f, 0.4f, -0.1f).toFloatArray())
        dataStore.gain shouldBe 0.5f
        dataStore.length shouldBe 7
        dataStore.packetSize shouldBe 4
        dataStore[0] shouldBe listOf(0.2f, -0.3f, 0.4f).toFloatArray()
        dataStore[1] shouldBe listOf(0.5f, -0.5f, 0.4f, -0.1f).toFloatArray()
        dataStore[2] shouldBe null
        dataStore.clear()
        dataStore.gain shouldBe 0f
        dataStore.length shouldBe 0
        dataStore.packetSize shouldBe 0
        dataStore[0] shouldBe null
    }
}
