package jp.bellware.echo.store.memo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.kotlintest.shouldBe
import jp.bellware.echo.action.MainPlayEndAction
import jp.bellware.echo.action.MainPlayVisualVolumeUpdateAction
import jp.bellware.echo.action.memo.SoundMemoDayHeader
import jp.bellware.echo.action.memo.SoundMemoLastSaveIdAction
import jp.bellware.echo.action.memo.SoundMemoListUpdateAction
import jp.bellware.echo.repository.data.SoundMemo
import jp.bellware.echo.repository.data.YMD
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
        // 初期状態確認
        store.items.value shouldBe SoundMemoItems(listOf(), mapOf(), false, 0L, 0f)
        val items = listOf(
                SoundMemo(2,
                        true,
                        2000,
                        "output2.aac",
                        1,
                        139.623833,
                        35.906278,
                        "埼玉県",
                        "大宮区",
                        "",
                        SoundMemo.TEXT_STATUS_NOT_IMPLEMENTED,
                        ""),
                SoundMemo(1,
                        false,
                        1000,
                        "output1.aac",
                        1,
                        139.737056,
                        35.677,
                        "東京都",
                        "港区",
                        "赤坂3-1-6",
                        2,
                        "録音したこと"))
        val dayHeaders = mapOf(0 to SoundMemoDayHeader(true, true, YMD(2020, 10, 22)),
                1 to SoundMemoDayHeader(false, true, YMD(2020, 10, 21))
        )
        // 音声メモ一覧取得
        store.onEvent(SoundMemoListUpdateAction(items, dayHeaders))
        store.items.value shouldBe SoundMemoItems(items, dayHeaders, false, 0L, 0f)
        // 最終保存ID取得
        store.onEvent(SoundMemoLastSaveIdAction(1L))
        store.items.value shouldBe SoundMemoItems(items, dayHeaders, false, 1L, 0f)
        // 視覚的ボリューム取得
        store.onEvent(MainPlayVisualVolumeUpdateAction(0.5f))
        store.items.value shouldBe SoundMemoItems(items, dayHeaders, true, 1L, 0.5f)
        // 再生終了
        store.onEvent(MainPlayEndAction)
        store.items.value shouldBe SoundMemoItems(items, dayHeaders, false, 0L, 0f)
    }
}
