package jp.bellware.echo.action.memo

import jp.bellware.echo.action.Action
import jp.bellware.echo.repository.data.SoundMemo
import jp.bellware.echo.repository.data.YMD

/**
 * 音声メモの日付ヘッダー情報
 * @param today 今日フラグ
 * @param thisYear 今年フラグ
 * @param ymd 日付
 */
data class SoundMemoDayHeader(val today: Boolean, val thisYear: Boolean, val ymd: YMD)

/**
 * 音声メモ一覧の更新
 * @param items 音声メモ一覧
 * @param dayHeaders 日付ヘッダー一覧。キーは挿入する音声メモのインデックス。
 */
data class SoundMemoListUpdateAction(val items: List<SoundMemo>, val dayHeaders: Map<Int, SoundMemoDayHeader>) : Action()

/**
 * 最後に保存した音声のid。
 */
data class SoundMemoLastSaveIdAction(val id: Long) : Action()
