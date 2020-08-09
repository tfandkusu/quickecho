package jp.bellware.echo.workmanager

/**
 * 音声メモのWorkManager制御
 */
interface SoundMemoWorkManager {
    /**
     * 保存する
     * @param fileName ファイル名
     */
    fun save(fileName: String)
}
