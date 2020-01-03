package jp.bellware.echo.store

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.kotlintest.shouldBe
import jp.bellware.echo.action.MainPreRecordAction
import jp.bellware.echo.action.MainReadyAction
import jp.bellware.echo.action.MainRecordAction
import jp.bellware.echo.util.EmptyActionReceiver
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainStoreTest {
    /**
     * LiveDataを書き換えるのに必要
     */
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    /**
     * テスト対象
     */
    private lateinit var store: MainStore

    @Before
    fun setUp() {
        store = MainStore(EmptyActionReceiver)
    }

    /**
     * 録音開始まで
     */
    @Test
    fun record() {
        // 初期状態
        store.status.value shouldBe AnimationStatus.INVISIBLE
        store.record.value shouldBe AnimationStatus.VISIBLE
        store.play.value shouldBe AnimationStatus.INVISIBLE
        store.stop.value shouldBe AnimationStatus.INVISIBLE
        store.replay.value shouldBe AnimationStatus.INVISIBLE
        store.delete.value shouldBe AnimationStatus.INVISIBLE
        store.clickable shouldBe false
        // 準備完了
        store.onEvent(MainReadyAction)
        store.clickable shouldBe true
        // 録音開始要求
        store.onEvent(MainPreRecordAction)
        // クリック無効
        store.clickable shouldBe false
        // 状態を表示
        store.status.value shouldBe AnimationStatus.FI
        store.icon.value shouldBe StatusIcon.RECORD
        // 爆発エフェクト
        store.explosion.value shouldBe true
        // 録音効果音
        store.soundEffect.value shouldBe QrecSoundEffect.START
        // 再生していたら止める
        store.requestForPlay.value shouldBe RPRequest.STOP
        // 視覚的ボリュームをリセット
        store.visualVolume.value shouldBe VisualVolumeRequest.RESET
        // 再生ボタンと削除ボタンだけを表示
        store.record.value shouldBe AnimationStatus.INVISIBLE
        store.play.value shouldBe AnimationStatus.VISIBLE
        store.replay.value shouldBe AnimationStatus.INVISIBLE
        store.stop.value shouldBe AnimationStatus.INVISIBLE
        store.delete.value shouldBe AnimationStatus.FI
        // 実際に録音
        store.onEvent(MainRecordAction)
        // クリックできる
        store.clickable shouldBe true
        // 録音する
        store.requestForRecord.value shouldBe RPRequest.START
        store.visualVolume.value shouldBe VisualVolumeRequest.RECORD
        // タイマースタート
        store.requestForTimer.value = TimerRequest.START
    }

}