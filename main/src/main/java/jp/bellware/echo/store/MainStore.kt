package jp.bellware.echo.store

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import jp.bellware.echo.action.*
import jp.bellware.echo.util.ActionReceiver

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
     * 終了する。保存はしない。
     */
    STOP,

    /**
     * 終了して保存する
     */
    STOP_AND_SAVE
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
    RECORD, PLAY, STOP
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

class MainStore @ViewModelInject constructor(actionReceiver: ActionReceiver) : Store(actionReceiver) {
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
     * 音声メモボタンの表示
     */
    val showSoundMemoButton = MutableLiveData(true)

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
    val requestForVisualVolume = SingleLiveEvent<VisualVolumeRequest>()

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

    /**
     * バックキーのデフォルト挙動を防ぐ
     */
    val overrideBackKey = MutableLiveData(false)

    private val _progress = MutableLiveData(false)

    /**
     * プログレス表示
     */
    val progress: LiveData<Boolean> = _progress

    /**
     * 音声メモ画面呼び出し
     */
    val callSoundMemo = SingleLiveEvent<Boolean>()

    /**
     * 再生中または停止中フラグ。このフラグはsavedInstanceに保存する。
     */
    var playOrStop = false

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
     * 音声メモボタン表示非表示切り替え
     */
    fun onEvent(action: MainSoundMemoButtonVisibilityAction) {
        showSoundMemoButton.value = action.show
    }

    /**
     * 効果音読み込み完了
     */
    fun onEvent(action: MainReadyAction) {
        // 録音ボタンが押せるようになる
        clickable = true
        init()
        // バックキーをオーバーライド
        overrideBackKey.value = false

    }

    /**
     * 録音開始要求
     */
    fun onEvent(action: MainPreRecordAction) {
        // クリック無効
        clickable = false
        // 状態を表示
        status.value = AnimationStatus.FI1
        icon.value = StatusIcon.RECORD
        // 爆発エフェクト
        explosion.value = true
        // 録音効果音
        soundEffect.value = QrecSoundEffect.START
        // 再生していたら止める
        requestForPlay.value = RPRequest.STOP
        // 視覚的ボリュームをリセット
        requestForVisualVolume.value = VisualVolumeRequest.STOP
        // 再生ボタンと削除ボタンだけを表示
        record.value = AnimationStatus.INVISIBLE
        play.value = AnimationStatus.VISIBLE
        replay.value = AnimationStatus.INVISIBLE
        stop.value = AnimationStatus.INVISIBLE
        // 削除ボタンを表示
        delete.value = AnimationStatus.FI1
        // 再生中または停止中ではない
        playOrStop = false
    }

    /**
     * 実際に録音
     */
    fun onEvent(action: MainRecordAction) {
        // クリックできる
        clickable = true
        // 録音する
        requestForRecord.value = RPRequest.START
        requestForVisualVolume.value = VisualVolumeRequest.RECORD
        // タイマースタート
        requestForTimer.value = TimerRequest.START
        // バックキーをオーバーライド
        overrideBackKey.value = true
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
        requestForVisualVolume.value = VisualVolumeRequest.STOP
        // タイマーキャンセル
        requestForTimer.value = TimerRequest.CANCEL
        // 再生中または停止中ではない
        playOrStop = false
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
        status.value = AnimationStatus.FI2
        icon.value = StatusIcon.PLAY
        // ボタン表示変更
        record.value = AnimationStatus.VISIBLE
        play.value = AnimationStatus.INVISIBLE
        replay.value = AnimationStatus.FI1
        stop.value = AnimationStatus.FI1
        // 録音を停止
        requestForRecord.value = RPRequest.STOP_AND_SAVE
        // 視覚的ボリュームをリセット
        requestForVisualVolume.value = VisualVolumeRequest.STOP
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
        playOrStop = true
    }

    /**
     * 再再生
     */
    fun onEvent(action: MainRequestReplayAction) {
        // 再生する
        requestForPlay.value = RPRequest.START
    }

    /**
     * 停止
     */
    fun onEvent(action: MainRequestStopAction) {
        // 停止する
        requestForPlay.value = RPRequest.STOP
        // 以下、プロセス復帰用
        // ステータス表示再現
        status.value = AnimationStatus.VISIBLE
        icon.value = StatusIcon.PLAY
        playOrStop = true
        // ボタン表示状態再現
        record.value = AnimationStatus.VISIBLE
        delete.value = AnimationStatus.VISIBLE
        play.value = AnimationStatus.INVISIBLE
        replay.value = AnimationStatus.VISIBLE
        stop.value = AnimationStatus.VISIBLE
    }

    /**
     * 再生中終端に到達
     */
    fun onEvent(action: MainPlayEndAction) {
        requestForVisualVolume.value = VisualVolumeRequest.STOP
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

    /**
     * バックキーが押された
     */
    fun onEvent(action: MainBackPressedAction) {
        // バックキーを終了にする
        overrideBackKey.value = false
        // 削除ボタンを押した扱いにする
        clickDelete.value = true
    }

    fun onEvent(action: MainRestoreStartAction) {
        _progress.value = true
        overrideBackKey.value = true
    }

    fun onEvent(action: MainRestoreEndAction) {
        _progress.value = false
    }

    fun onEvent(action: MainSoundMemoAction) {
        callSoundMemo.value = true
    }

    fun onEvent(action: MainPlayStartAction) {
        requestForVisualVolume.value = VisualVolumeRequest.PLAY
    }

}
