package jp.bellware.echo.store.memo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import jp.bellware.echo.util.EmptyActionReceiver
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SoundMemoStoreTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    /**
     * テスト対象
     */
    private lateinit var store: SoundMemoStore

    @Before
    fun setUp() {
        store = SoundMemoStore(EmptyActionReceiver)
    }

    @Test
    fun update() {
        // TODO 再構成する
//        val items = listOf(SoundMemo(1,
//                false,
//                1000,
//                "output1.aac",
//                1,
//                139.737056,
//                35.677,
//                "東京都",
//                "港区",
//                "赤坂3-1-6",
//                2,
//                "録音したこと"),
//                SoundMemo(2,
//                        true,
//                        2000,
//                        "output2.aac",
//                        1,
//                        139.623833,
//                        35.906278,
//                        "埼玉県",
//                        "大宮区",
//                        "",
//                        SoundMemo.TEXT_STATUS_NOT_IMPLEMENTED,
//                        ""))
//        store.items.value shouldBe null
//        store.onEvent(SoundMemoListUpdateAction(items))
//        store.items.value shouldBe items
    }
}
