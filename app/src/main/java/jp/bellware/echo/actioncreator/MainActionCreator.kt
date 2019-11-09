package jp.bellware.echo.actioncreator

import jp.bellware.echo.action.MainSoundLoadedAction
import jp.bellware.util.BWU
import jp.bellware.util.Dispatcher

class MainActionCreator(dispatcher: Dispatcher) : ActionCreator(dispatcher) {
    /**
     * 音声の読み込みが完了
     */
    fun onSoundLoaded() {
        dispatcher.dispatch(MainSoundLoadedAction)
    }

    /**
     * 録音ボタンが押された
     */
    fun onRecordClick() {
        BWU.log("onRecordClick")
    }
}