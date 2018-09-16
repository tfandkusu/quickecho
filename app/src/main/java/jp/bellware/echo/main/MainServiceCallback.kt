package jp.bellware.echo.main

/**
 * MainServiceからのコールバック
 */
interface MainServiceCallback {
    /**
     * 状態を更新する
     * @param animation アニメーションフラグ。バックグランドからの復帰による状態再現の場合はfalseとなる。
     * @param status 状態
     */
    fun onUpdateStatus(animation: Boolean, status: QRecStatus)

    /**
     * 視覚的ボリュームを更新する
     * @param volume ボリューム
     */
    fun onUpdateVolume(volume: Float)

    /**
     * 警告メッセージ表示
     * @param resId メッセージID
     */
    fun onShowWarningMessage(resId: Int)

}
