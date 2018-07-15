package jp.bellware.echo.main;

/**
 * MainServiceからのコールバック
 */
public interface MainCallback {
    /**
     * 状態を更新する
     * @param animation アニメーションフラグ
     * @param status 状態
     */
    void onUpdateStatus(boolean animation, QRecStatus status);

    /**
     * 視覚的ボリュームを更新する
     * @param volume
     */
    void onUpdateVolume(float volume);

    /**
     * 警告メッセージ表示
     */
    void onShowWarningMessage(int resId);

}
