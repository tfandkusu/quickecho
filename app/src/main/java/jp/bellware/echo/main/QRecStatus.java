package jp.bellware.echo.main;

/**
 * 現在の状態
 */
public enum QRecStatus {
    /**
     * 初期化中
     */
    INIT,
    /**
     * 初期化完了(1回目)
     */
    READY_FIRST,
    /**
     * 初期化完了(2回目)
     */
    READY,
    /**
     * 停止状態
     */
    STOP,
    /**
     * 録音中削除
     */
    DELETE_RECORDING,

    /**
     * 再生中削除
     */
    DELETE_PLAYING,
    /**
     * 録音開始中
     */
    STARTING_RECORD,
    /**
     * 録音中
     */
    RECORDING,
    /**
     * 録音停止中
     */
    STOPPING_RECORD,
    /**
     * 再生中
     */
    PLAYING,

    /**
     * 再生停止中
     */
    STOPPING_PLAYING
}
