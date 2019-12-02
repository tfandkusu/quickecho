package jp.bellware.echo.actioncreator

import jp.bellware.echo.action.MainDeleteAction
import jp.bellware.echo.action.MainPreRecordAction
import jp.bellware.echo.action.MainReadyAction
import jp.bellware.echo.action.MainRecordAction
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


    fun onDeleteClick() = GlobalScope.launch(Dispatchers.Main) {
        dispatcher.dispatch(MainDeleteAction)
        delayActionCreatorHelper.delay(200)
        dispatcher.dispatch(MainReadyAction)
    }
}