package jp.bellware.echo.store

import androidx.lifecycle.MutableLiveData
import jp.bellware.echo.action.*
import jp.bellware.util.ActionReceiver

/**
 * 録音、再生状態アイコン
 */
enum class StatusIcon {
    /**
     * 録音
     */
    RECORD,
    /**
     * 再生
     */
    PLAY
}

/**
 * 再生、録音担当に対する要求
 */
enum class RPRequest {
    /**
     * 開始する
     */
    START,
    /**
     * 終了する
     */
    STOP
}

/**
 * 効果音一覧
 */
enum class QrecSoundEffect {
    START, PLAY, DELETE
}

/**
 * 視覚的ボリュームに対する要求
 */
enum class VisualVolumeRequest {
    RECORD, PLAY, RESET, STOP
}

/**
 * タイマーに対する要求
 */
enum class TimerRequest {
    START, CANCEL
}

/**
 * 警告表示
 */
enum class WarningMessage {
    /**
     * ボリュームが0
     */
    MUTE,
    /**
     * 録音時間オーバー
     */
    RECORD_TIME,
    /**
     * 録音されていない
     */
    NO_RECORD
}

class MainStore(actionReceiver: ActionReceiver) : Store(actionReceiver) {
    /**
     * ステータス表示
     */
    val status = AnimationLiveData()

    /**
     * アイコン表示
     */
    val icon = MutableLiveData<StatusIcon>()

    /**
     * 爆発エフェクト
     */
    val explosion = SingleLiveEvent<Boolean>()

    /**
     * 録音ボタン表示
     */
    val record = AnimationLiveData()

    /**
     * ボタンが押せる
     */
    var clickable = false

    /**
     * 録音ボタン表示
     */
    val play = AnimationLiveData()

    /**
     * 録音ボタン表示
     */
    val stop = AnimationLiveData()

    /**
     * 録音ボタン表示
     */
    val replay = AnimationLiveData()

    /**
     * 削除ボタン表示
     */
    val delete = AnimationLiveData()

    /**
     * 効果音
     */
    val soundEffect = SingleLiveEvent<QrecSoundEffect>()

    /**
     * 再生担当への要求
     */
    val requestForPlay = SingleLiveEvent<RPRequest>()

    /**
     * 録音担当への要求
     */
    val requestForRecord = SingleLiveEvent<RPRequest>()

    /**
     * 視覚的ボリュームへの要求
     */
    val visualVolume = SingleLiveEvent<VisualVolumeRequest>()

    /**
     * タイマーに対する要求
     */
    val requestForTimer = SingleLiveEvent<TimerRequest>()

    /**
     * 警告表示
     */
    val warning = SingleLiveEvent<WarningMessage>()

    /**
     * 再生ボタンを押す
     */
    val clickPlay = SingleLiveEvent<Boolean>()

    /**
     * 削除ボタンを押す
     */
    val clickDelete = SingleLiveEvent<Boolean>()

    init {
        // 初期状態設定
        clickable = false
        init()
    }

    /**
     * 録音ボタンだけの状態
     */
    private fun init() {
        status.value = AnimationStatus.INVISIBLE
        record.value = AnimationStatus.VISIBLE
        play.value = AnimationStatus.INVISIBLE
        stop.value = AnimationStatus.INVISIBLE
        replay.value = AnimationStatus.INVISIBLE
        delete.value = AnimationStatus.INVISIBLE
    }

    /**
     * 効果音読み込み完了
     */
    fun onEvent(action: MainReadyAction) {
        // 録音ボタンが押せるようになる
        clickable = true
        init()
    }

    /**
     * 録音開始要求
     */
    fun onEvent(action: MainPreRecordAction) {
        // クリック無効
        clickable = false
        // 状態を表示
        status.value = AnimationStatus.FI
        icon.value = StatusIcon.RECORD
        // 爆発エフェクト
        explosion.value = true
        // 録音効果音
        soundEffect.value = QrecSoundEffect.START
        // 再生していたら止める
        requestForPlay.value = RPRequest.STOP
        // 視覚的ボリュームをリセット
        visualVolume.value = VisualVolumeRequest.RESET
        // 再生ボタンと削除ボタンだけを表示
        record.value = AnimationStatus.INVISIBLE
        play.value = AnimationStatus.VISIBLE
        replay.value = AnimationStatus.INVISIBLE
        stop.value = AnimationStatus.INVISIBLE
        // 削除ボタンを表示
        delete.value = AnimationStatus.FI
    }

    /**
     * 実際に録音
     */
    fun onEvent(action: MainRecordAction) {
        // クリックできる
        clickable = true
        // 録音する
        requestForRecord.value = RPRequest.START
        visualVolume.value = VisualVolumeRequest.RECORD
        // タイマースタート
        requestForTimer.value = TimerRequest.START
    }

    /**
     * 削除
     */
    fun onEvent(action: MainDeleteAction) {
        // クリックできない
        clickable = false
        // 削除効果音
        soundEffect.value = QrecSoundEffect.DELETE
        // 削除エフェクト
        status.value = AnimationStatus.DELETE
        replay.value = AnimationStatus.DELETE
        stop.value = AnimationStatus.DELETE
        delete.value = AnimationStatus.DELETE
        // 録音再生を停止
        requestForRecord.value = RPRequest.STOP
        requestForPlay.value = RPRequest.STOP
        visualVolume.value = VisualVolumeRequest.STOP
        // タイマーキャンセル
        requestForTimer.value = TimerRequest.CANCEL
    }

    /**
     * 再生の準備
     */
    fun onEvent(action: MainPrePlayAction) {
        // クリックできない
        clickable = false
        // 再生効果音
        soundEffect.value = QrecSoundEffect.PLAY
        // ステータス表示変更
        status.value = AnimationStatus.FI
        icon.value = StatusIcon.PLAY
        // ボタン表示変更
        record.value = AnimationStatus.VISIBLE
        play.value = AnimationStatus.INVISIBLE
        replay.value = AnimationStatus.FI
        stop.value = AnimationStatus.FI
        // 録音を停止
        requestForRecord.value = RPRequest.STOP
        // 視覚的ボリュームをリセット
        visualVolume.value = VisualVolumeRequest.RESET
        // タイマーキャンセル
        requestForTimer.value = TimerRequest.CANCEL
    }

    /**
     * 再生そのもの
     */
    fun onEvent(action: MainPlayAction) {
        // クリックできる
        clickable = true
        // 再生する
        requestForPlay.value = RPRequest.START
        visualVolume.value = VisualVolumeRequest.PLAY
    }

    /**
     * 再再生
     */
    fun onEvent(action: MainReplayAction) {
        // 再生する
        requestForPlay.value = RPRequest.START
        visualVolume.value = VisualVolumeRequest.PLAY
    }

    /**
     * 停止
     */
    fun onEvent(action: MainStopAction) {
        // 停止する
        requestForPlay.value = RPRequest.STOP
        visualVolume.value = VisualVolumeRequest.RESET
    }


    /**
     * ボリューム0の時
     */
    fun onEvent(action: MainMuteAction) {
        warning.value = WarningMessage.MUTE
    }

    /**
     * 録音時間超過
     */
    fun onEvent(action: MainMaxRecordTimeOverAction) {
        warning.value = WarningMessage.RECORD_TIME
        if (action.includeSound)
            clickPlay.value = true
        else
            clickDelete.value = true
    }

    /**
     * 録音されていない
     */
    fun onEvent(action: MainNoRecordAction) {
        warning.value = WarningMessage.NO_RECORD
    }

}