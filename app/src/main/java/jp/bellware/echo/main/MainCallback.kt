package jp.bellware.echo.main

/**
 * MainServiceからのコールバック
 */
interface MainCallback {
    /**
     * 状態を更新する
     * @param animation アニメーションフラグ
     * @param status 状態
     */
    fun onUpdateStatus(animation: Boolean, status: QRecStatus)

    /**
     * 視覚的ボリュームを更新する
     * @param volume
     */
    fun onUpdateVolume(volume: Float)

    /**
     * 警告メッセージ表示
     */
    fun onShowWarningMessage(resId: Int)

}
