package jp.bellware.echo.store

import androidx.lifecycle.MutableLiveData
import jp.bellware.echo.action.DummyAction

class MainStore : Store() {

    /**
     * 録音ボタン表示
     */
    val record = MutableLiveData<Boolean>()

    /**
     * 録音ボタン表示
     */
    val play = MutableLiveData<Boolean>()

    /**
     * 録音ボタン表示
     */
    val stop = MutableLiveData<Boolean>()

    /**
     * 録音ボタン表示
     */
    val replay = MutableLiveData<Boolean>()

    /**
     * 削除ボタン表示
     */
    val delete = MutableLiveData<Boolean>()

    init {
        // 初期状態設定
        record.value = true
        play.value = false
        stop.value = false
        replay.value = false
        delete.value = false
    }

    fun onEvent(action: DummyAction) {

    }

}