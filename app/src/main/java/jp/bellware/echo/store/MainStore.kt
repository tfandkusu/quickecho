package jp.bellware.echo.store

import androidx.lifecycle.MutableLiveData
import jp.bellware.echo.R
import jp.bellware.echo.action.MainDeleteAction
import jp.bellware.echo.action.MainPreRecordAction
import jp.bellware.echo.action.MainReadyAction
import jp.bellware.echo.action.MainRecordAction


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

class MainStore : Store() {
    /**
     * ステータス表示
     */
    val status = AnimationLiveData()

    /**
     * アイコン表示
     */
    val icon = MutableLiveData<Int>()

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
    var clickable: Boolean = false

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
        icon.value = R.drawable.microphone_48dp
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
        icon.value = R.drawable.microphone_48dp
        // 爆発エフェクト
        explosion.value = true
        // 録音効果音
        soundEffect.value = QrecSoundEffect.START
        // 再生していたら止める
        requestForPlay.value = RPRequest.STOP
        // 視覚的ボリュームをリセット
        visualVolume.value = VisualVolumeRequest.RESET
        // 再生ボタンを表示
        record.value = AnimationStatus.INVISIBLE
        play.value = AnimationStatus.VISIBLE
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
    }

}