package jp.bellware.echo.store

import androidx.lifecycle.MutableLiveData
import jp.bellware.echo.R
import jp.bellware.echo.action.MainRecordAction
import jp.bellware.echo.action.MainSoundLoadedAction
import jp.bellware.echo.action.MainStartRecordRequestAction


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
     * 録音ボタンが押せる
     */
    val recordClickable = MutableLiveData<Boolean>()

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
        status.value = AnimationStatus.INVISIBLE
        icon.value = R.drawable.microphone_48dp
        record.value = AnimationStatus.VISIBLE
        recordClickable.value = false
        play.value = AnimationStatus.INVISIBLE
        stop.value = AnimationStatus.INVISIBLE
        replay.value = AnimationStatus.INVISIBLE
        delete.value = AnimationStatus.INVISIBLE
    }

    /**
     * 効果音読み込み完了
     */
    fun onEvent(action: MainSoundLoadedAction) {
        // 録音ボタンが押せるようになる
        recordClickable.value = true
    }

    /**
     * 録音開始要求
     */
    fun onEvent(action: MainStartRecordRequestAction) {
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
        requestForRecord.value = RPRequest.START
        visualVolume.value = VisualVolumeRequest.RECORD
    }

}