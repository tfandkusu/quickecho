package jp.bellware.echo.action.memo

import jp.bellware.echo.action.Action
import jp.bellware.echo.repository.data.SoundMemo

/**
 * 音声メモ一覧の更新
 */
data class SoundMemoListUpdateAction(val items: List<SoundMemo>) : Action()

/**
 * 最後に保存した音声のid。
 */
data class SoundMemoLastSaveIdAction(val id: Long) : Action()
