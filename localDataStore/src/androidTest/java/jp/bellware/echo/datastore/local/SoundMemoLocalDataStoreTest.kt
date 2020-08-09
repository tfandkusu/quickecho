package jp.bellware.echo.datastore.local

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import jp.bellware.echo.datastore.local.schema.LocalSoundMemo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.FileNotFoundException

class SoundMemoLocalDataStoreTest {

    private lateinit var context: Context

    private lateinit var localDataStore: SoundMemoLocalDataStore

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        val db = QuickEchoDatabaseFactory.create(context)
        localDataStore = SoundMemoLocalDataStoreImpl(context, db)
    }

    /**
     * 1件保存のテスト
     */
    @ExperimentalCoroutinesApi
    @Test
    fun insertOne() = runBlocking {
        localDataStore.clear()
        // 1件保存出来ることを確認する
        val m1 = LocalSoundMemo(0,
                true,
                System.currentTimeMillis(),
                "output1.aac",
                1,
                139.0,
                35.0,
                "東京都",
                "港区",
                "赤坂3-1-6",
                2,
                "録音したこと")
        // ダミーのAACファイルを作る
        withContext(Dispatchers.IO) {
            val fos = context.openFileOutput(m1.fileName, Context.MODE_PRIVATE)
            fos.write(0)
            fos.close()
            // 開けることを確認
            val fis = context.openFileInput(m1.fileName)
            fis.close()
        }
        val id = localDataStore.insert(m1)
        localDataStore.index().take(1).collect {
            it.size shouldBe 1
            val m = it[0]
            m.id shouldNotBe 0
            m.temporal shouldBe true
            m.fileName shouldBe "output1.aac"
            m.locationStatus shouldBe 1
            m.longitude shouldBe 139.0
            m.latitude shouldBe 35.0
            m.prefecture shouldBe "東京都"
            m.city shouldBe "港区"
            m.street shouldBe "赤坂3-1-6"
            m.textStatus shouldBe 2
            m.text shouldBe "録音したこと"
        }
        // 更新出来ることを確認する
        val m1u = m1.copy(id = id, temporal = false)
        localDataStore.update(m1u)
        localDataStore.index().take(1).collect {
            it.size shouldBe 1
            val m = it[0]
            m.temporal shouldBe false
        }
        // 削除できることを確認する
        localDataStore.delete(m1u)
        localDataStore.index().take(1).collect {
            it.size shouldBe 0
        }
        // AACファイルの削除を確認
        withContext(Dispatchers.IO) {
            try {
                context.openFileInput(m1u.fileName)
                Assert.fail()
            } catch (e: FileNotFoundException) {
                // OK
            }
        }
    }

    /**
     * 一時保存音声は5件まで
     */
    fun temporalMax5() {

    }
}
