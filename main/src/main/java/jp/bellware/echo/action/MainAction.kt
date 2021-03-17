package jp.bellware.echo.action

// メイン画面のアクション

/**
 * 準備完了アクション
 */
object MainReadyAction : Action()

/**
 * 録音演出アクション
 */
object MainPreRecordAction : Action()

/**
 * 録音アクション
 */
object MainRecordAction : Action()

/**
 * 再生演出アクション
 */
object MainPrePlayAction : Action()

/**
 * 再生アクション
 */
object MainPlayAction : Action()

/**
 * 削除アクション
 */
object MainDeleteAction : Action()

/**
 * 再再生アクション
 */
object MainReplayAction : Action()

/**
 * 停止アクション
 */
object MainStopAction : Action()

/**
 * ボリュームが0アクション
 */
object MainMuteAction : Action()

/**
 * 録音されていないアクション
 */
object MainNoRecordAction : Action()

/**
 * バックキーが押されたアクション
 */
object MainBackPressedAction : Action()

/**
 * 最大録音時間オーバーアクション
 */
data class MainMaxRecordTimeOverAction(val includeSound: Boolean) : Action()

/**
 * 一時保存音声読込開始アクション
 */
object MainRestoreStartAction : Action()

/**
 * 一時保存音声読込終了アクション
 */
object MainRestoreEndAction : Action()

/**
 * 音声メモ呼び出しアクション
 */
object MainSoundMemoAction : Action()

/**
 * 音声メモボタン表示非表示切り替えアクション
 * @param show 表示フラグ
 */
data class MainSoundMemoButtonVisibilityAction(val show: Boolean) : Action()
