package jp.bellware.echo.store

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.kotlintest.shouldBe
import jp.bellware.echo.action.*
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
     * 録音、再生、削除
     */
    @Test
    fun recordPlayDelete() {
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
        store.status.value shouldBe AnimationStatus.FI1
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
        store.delete.value shouldBe AnimationStatus.FI1
        // 実際に録音
        store.onEvent(MainRecordAction)
        // クリックできる
        store.clickable shouldBe true
        // 録音する
        store.requestForRecord.value shouldBe RPRequest.START
        store.visualVolume.value shouldBe VisualVolumeRequest.RECORD
        // タイマースタート
        store.requestForTimer.value = TimerRequest.START
        // 再生する
        store.onEvent(MainPrePlayAction)
        // クリックできない
        store.clickable shouldBe false
        // 再生効果音
        store.soundEffect.value shouldBe QrecSoundEffect.PLAY
        // ステータス表示変更
        store.status.value shouldBe AnimationStatus.FI2
        store.icon.value shouldBe StatusIcon.PLAY
        // ボタン表示変更
        store.record.value shouldBe AnimationStatus.VISIBLE
        store.play.value shouldBe AnimationStatus.INVISIBLE
        store.replay.value shouldBe AnimationStatus.FI1
        store.stop.value shouldBe AnimationStatus.FI1
        // 録音を停止
        store.requestForRecord.value shouldBe RPRequest.STOP
        // 視覚的ボリュームをリセット
        store.visualVolume.value shouldBe VisualVolumeRequest.RESET
        // タイマーキャンセル
        store.requestForTimer.value shouldBe TimerRequest.CANCEL
        // 削除する
        store.onEvent(MainDeleteAction)
        // クリックできない
        store.clickable shouldBe false
        // 削除効果音
        store.soundEffect.value shouldBe QrecSoundEffect.DELETE
        // 削除エフェクト
        store.status.value shouldBe AnimationStatus.DELETE
        store.replay.value shouldBe AnimationStatus.DELETE
        store.stop.value shouldBe AnimationStatus.DELETE
        store.delete.value shouldBe AnimationStatus.DELETE
        // 録音再生を停止
        store.requestForRecord.value shouldBe RPRequest.STOP
        store.requestForPlay.value shouldBe RPRequest.STOP
        store.visualVolume.value shouldBe VisualVolumeRequest.STOP
        // タイマーキャンセル
        store.requestForTimer.value shouldBe TimerRequest.CANCEL
        // 初期状態に戻る
        store.onEvent(MainReadyAction)
        // 録音だけ表示されている
        store.clickable shouldBe true
        store.status.value shouldBe AnimationStatus.INVISIBLE
        store.record.value shouldBe AnimationStatus.VISIBLE
        store.play.value shouldBe AnimationStatus.INVISIBLE
        store.stop.value shouldBe AnimationStatus.INVISIBLE
        store.replay.value shouldBe AnimationStatus.INVISIBLE
        store.delete.value shouldBe AnimationStatus.INVISIBLE
    }

    /**
     * 再再生
     */
    @Test
    fun replay() {
        store.onEvent(MainReplayAction)
        store.requestForPlay.value shouldBe RPRequest.START
        store.visualVolume.value shouldBe VisualVolumeRequest.PLAY
    }

    /**
     * 停止
     */
    @Test
    fun stop() {
        store.onEvent(MainStopAction)
        store.requestForPlay.value shouldBe RPRequest.STOP
        store.visualVolume.value shouldBe VisualVolumeRequest.RESET
    }

}