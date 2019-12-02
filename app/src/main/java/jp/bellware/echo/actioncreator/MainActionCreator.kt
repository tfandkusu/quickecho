package jp.bellware.echo.actioncreator

import jp.bellware.echo.action.MainDeleteAction
import jp.bellware.echo.action.MainRecordAction
import jp.bellware.echo.action.MainSoundLoadedAction
import jp.bellware.echo.action.MainStartRecordRequestAction
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
        dispatcher.dispatch(MainStartRecordRequestAction)
    }

    /**
     * 実際に録音する。
     * 効果音が録音音声に混ざるのを防ぐため録音開始を引っ張る意図。
     */
    fun startRecord() {
        dispatcher.dispatch(MainRecordAction)
    }

    fun onDeleteClick() {
        dispatcher.dispatch(MainDeleteAction)
    }
}