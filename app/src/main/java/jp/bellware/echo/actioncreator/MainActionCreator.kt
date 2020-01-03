package jp.bellware.echo.actioncreator

import jp.bellware.echo.action.*
import jp.bellware.util.Dispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActionCreator(dispatcher: Dispatcher, private val delayActionCreatorHelper: DelayActionCreatorHelper) : ActionCreator(dispatcher) {
    /**
     * 音声の読み込みが完了
     */
    fun onSoundLoaded() {
        dispatcher.dispatch(MainReadyAction)
    }

    /**
     * 録音ボタンが押された
     */
    fun onRecordClick() = GlobalScope.launch(Dispatchers.Main) {
        dispatcher.dispatch(MainPreRecordAction)
        delayActionCreatorHelper.delay(500)
        dispatcher.dispatch(MainRecordAction)
    }

    /**
     * 削除ボタンが押された
     */
    fun onDeleteClick() = GlobalScope.launch(Dispatchers.Main) {
        dispatcher.dispatch(MainDeleteAction)
        delayActionCreatorHelper.delay(200)
        dispatcher.dispatch(MainReadyAction)
    }

    /**
     * 再生ボタンが押された
     */
    fun onPlayClick() = GlobalScope.launch(Dispatchers.Main) {
        dispatcher.dispatch(MainPrePlayAction)
        delayActionCreatorHelper.delay(550)
        dispatcher.dispatch(MainPlayAction)
    }

    /**
     * 再再生ボタンが押された
     */
    fun onReplayClick() {
        dispatcher.dispatch(MainReplayAction)
    }

    /**
     * 停止ボタンが押された
     */
    fun onStopClick() {
        dispatcher.dispatch(MainStopAction)
    }

    /**
     * ボリュームが0の時に呼ばれる
     */
    fun onMute() {
        dispatcher.dispatch(MainMuteAction)
    }
}