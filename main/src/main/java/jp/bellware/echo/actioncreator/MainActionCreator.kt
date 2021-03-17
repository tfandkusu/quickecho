package jp.bellware.echo.actioncreator

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.bellware.echo.action.MainBackPressedAction
import jp.bellware.echo.action.MainDeleteAction
import jp.bellware.echo.action.MainMaxRecordTimeOverAction
import jp.bellware.echo.action.MainMuteAction
import jp.bellware.echo.action.MainNoRecordAction
import jp.bellware.echo.action.MainPlayAction
import jp.bellware.echo.action.MainPrePlayAction
import jp.bellware.echo.action.MainPreRecordAction
import jp.bellware.echo.action.MainReadyAction
import jp.bellware.echo.action.MainRecordAction
import jp.bellware.echo.action.MainReplayAction
import jp.bellware.echo.action.MainRestoreEndAction
import jp.bellware.echo.action.MainRestoreStartAction
import jp.bellware.echo.action.MainSoundMemoAction
import jp.bellware.echo.action.MainSoundMemoButtonVisibilityAction
import jp.bellware.echo.action.MainStopAction
import jp.bellware.echo.repository.SettingRepository
import jp.bellware.echo.repository.SoundRepository
import jp.bellware.echo.util.Dispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * メイン画面のActionCreator
 * @param dispatcher
 * @param delayActionCreatorHelper 一定ミリ秒時間待機担当
 * @param soundRepository 音声Repository
 */
class MainActionCreator @ViewModelInject
constructor(
    private val dispatcher: Dispatcher,
    private val delayActionCreatorHelper: DelayActionCreatorHelper,
    private val settingRepository: SettingRepository,
    private val soundRepository: SoundRepository
) : ViewModel() {

    /**
     * FragmentのonCreateで呼ぶ
     */
    fun onCreate() = viewModelScope.launch {
        settingRepository.isShowSoundMemoButton().collect {
            dispatcher.dispatch(MainSoundMemoButtonVisibilityAction(it))
        }
    }

    /**
     * 音声の読み込みが完了
     * @param playOrStop 再生または停止状態でプロセスキル発生フラグ
     */
    fun onSoundLoaded(playOrStop: Boolean) = viewModelScope.launch {
        dispatcher.dispatch(MainReadyAction)
        if (playOrStop)
            restore()
    }

    /**
     * 録音ボタンが押された
     */
    fun onRecordClick() = viewModelScope.launch(Dispatchers.Main) {
        dispatcher.dispatch(MainPreRecordAction)
        delayActionCreatorHelper.delay(500)
        dispatcher.dispatch(MainRecordAction)
    }

    /**
     * 削除ボタンが押された
     */
    fun onDeleteClick() = viewModelScope.launch(Dispatchers.Main) {
        delete()
    }

    /**
     * 再生ボタンが押された
     * @param includeSound 録音されているフラグ
     */
    fun onPlayClick(includeSound: Boolean) = viewModelScope.launch(Dispatchers.Main) {
        if (includeSound) {
            dispatcher.dispatch(MainPrePlayAction)
            delayActionCreatorHelper.delay(550)
            dispatcher.dispatch(MainPlayAction)
        } else {
            // 音声がなければ削除扱い。
            dispatcher.dispatch(MainNoRecordAction)
            delete()
        }
    }

    /**
     * 削除処理
     */
    private suspend fun delete() {
        dispatcher.dispatch(MainDeleteAction)
        delayActionCreatorHelper.delay(200)
        dispatcher.dispatch(MainReadyAction)
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

    /**
     * 録音可能時間を超過したケース
     * @param includeSound　録音されているフラグ
     */
    fun onMaxRecordTimeOver(includeSound: Boolean) {
        dispatcher.dispatch(MainMaxRecordTimeOverAction(includeSound))
    }

    /**
     * バックキーが押された
     */
    fun onBackPressed() {
        dispatcher.dispatch(MainBackPressedAction)
    }

    /**
     * プロセスキルからの復帰を行う
     */
    private suspend fun restore() {
        // プログレス表示
        dispatcher.dispatch(MainRestoreStartAction)
        // AACファイルで保存している音声を読みこんでメモリーに格納する
        soundRepository.restore()
        // 停止状態にする
        dispatcher.dispatch(MainStopAction)
        // プログレス閉じる
        dispatcher.dispatch(MainRestoreEndAction)
    }

    /**
     * 音声メモボタンが押されたときの処理
     */
    fun onSoundMemoClick() {
        dispatcher.dispatch(MainSoundMemoAction)
    }
}
