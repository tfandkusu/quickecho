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

    @Test
    fun showSoundMemoButton() {
        store.showSoundMemoButton.value shouldBe true
        store.onEvent(MainSoundMemoButtonVisibilityAction(false))
        store.showSoundMemoButton.value shouldBe false
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
        store.overrideBackKey.value shouldBe false
        store.playOrStop shouldBe false
        // 準備完了
        store.onEvent(MainReadyAction)
        store.clickable shouldBe true
        store.overrideBackKey.value shouldBe false
        // 録音開始要求
        store.onEvent(MainPreRecordAction)
        // クリック無効
        store.clickable shouldBe false
        store.overrideBackKey.value shouldBe false
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
        // バックキーをオーバーライド
        store.overrideBackKey.value shouldBe true
        // 録音する
        store.requestForRecord.value shouldBe RPRequest.START
        store.visualVolume.value shouldBe VisualVolumeRequest.RECORD
        // タイマースタート
        store.requestForTimer.value shouldBe TimerRequest.START
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
        // 録音を停止して保存する
        store.requestForRecord.value shouldBe RPRequest.STOP_AND_SAVE
        // 視覚的ボリュームをリセット
        store.visualVolume.value shouldBe VisualVolumeRequest.RESET
        // タイマーキャンセル
        store.requestForTimer.value shouldBe TimerRequest.CANCEL
        // 再生開始
        store.onEvent(MainPlayAction)
        // クリックできる
        store.clickable shouldBe true
        // 再生する
        store.requestForPlay.value shouldBe RPRequest.START
        store.visualVolume.value shouldBe VisualVolumeRequest.PLAY
        // 再生または停止中
        store.playOrStop shouldBe true
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
        // 再生または停止中を解除
        store.playOrStop shouldBe false
        // 初期状態に戻る
        store.onEvent(MainReadyAction)
        // 録音だけ表示されている
        store.clickable shouldBe true
        store.overrideBackKey.value shouldBe false
        store.status.value shouldBe AnimationStatus.INVISIBLE
        store.record.value shouldBe AnimationStatus.VISIBLE
        store.play.value shouldBe AnimationStatus.INVISIBLE
        store.stop.value shouldBe AnimationStatus.INVISIBLE
        store.replay.value shouldBe AnimationStatus.INVISIBLE
        store.delete.value shouldBe AnimationStatus.INVISIBLE
        store.playOrStop shouldBe false
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

    @Test
    fun playEnd() {
        store.visualVolume.value shouldBe null
        store.onEvent(MainPlayEndAction)
        store.visualVolume.value shouldBe VisualVolumeRequest.RESET
    }

    /**
     * 停止
     */
    @Test
    fun stop() {
        store.onEvent(MainStopAction)
        store.requestForPlay.value shouldBe RPRequest.STOP
        store.visualVolume.value shouldBe VisualVolumeRequest.RESET
        // 以下、プロセス復帰用
        // ステータス表示再現
        store.status.value shouldBe AnimationStatus.VISIBLE
        store.icon.value shouldBe StatusIcon.PLAY
        store.playOrStop shouldBe true
        // ボタン表示状態再現
        store.record.value shouldBe AnimationStatus.VISIBLE
        store.delete.value shouldBe AnimationStatus.VISIBLE
        store.play.value shouldBe AnimationStatus.INVISIBLE
        store.replay.value shouldBe AnimationStatus.VISIBLE
        store.stop.value shouldBe AnimationStatus.VISIBLE
    }


    /**
     * ボリューム0の時
     */
    @Test
    fun mute() {
        store.onEvent(MainMuteAction)
        store.warning.value shouldBe WarningMessage.MUTE
    }

    /**
     * 録音可能時間超過(録音されている)
     */
    @Test
    fun timeLimitIncludeSound() {
        store.onEvent(MainMaxRecordTimeOverAction(true))
        store.warning.value shouldBe WarningMessage.RECORD_TIME
        store.clickPlay.value shouldBe true
    }

    /**
     * 録音可能時間超過(録音されていない)
     */
    @Test
    fun timeLimitNoSound() {
        store.onEvent(MainMaxRecordTimeOverAction(false))
        store.warning.value shouldBe WarningMessage.RECORD_TIME
        store.clickDelete.value shouldBe true
    }

    /**
     * 再生したときに録音されていないケース
     */
    @Test
    fun noSound() {
        store.onEvent(MainNoRecordAction)
        store.warning.value shouldBe WarningMessage.NO_RECORD
    }

    @Test
    fun backPressed() {
        store.overrideBackKey.value = true
        store.onEvent(MainBackPressedAction)
        store.overrideBackKey.value shouldBe false
        store.clickDelete.value shouldBe true
    }

    @Test
    fun restore() {
        store.progress.value shouldBe false
        store.overrideBackKey.value shouldBe false
        store.onEvent(MainRestoreStartAction)
        store.progress.value shouldBe true
        store.overrideBackKey.value shouldBe true
        store.onEvent(MainRestoreEndAction)
        store.progress.value shouldBe false
    }

    @Test
    fun soundMemo() {
        store.callSoundMemo.value shouldBe null
        store.onEvent(MainSoundMemoAction)
        store.callSoundMemo.value shouldBe true
    }
}
