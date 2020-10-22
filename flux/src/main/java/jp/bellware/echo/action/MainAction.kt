package jp.bellware.echo.action

/**
 * 視覚的ボリュームを更新するアクション。音声メモ画面で使われる。
 * @param volume 視覚的ボリュームの量。0-1。
 */
data class MainPlayVisualVolumeUpdateAction(val volume: Float) : Action()

/**
 * 実際の再生が終了したアクション
 */
object MainPlayEndAction : Action()
