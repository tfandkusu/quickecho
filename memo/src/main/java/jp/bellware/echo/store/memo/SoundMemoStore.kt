package jp.bellware.echo.store.memo

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import jp.bellware.echo.action.MainPlayEndAction
import jp.bellware.echo.action.MainPlayVisualVolumeUpdateAction
import jp.bellware.echo.action.memo.SoundMemoDayHeader
import jp.bellware.echo.action.memo.SoundMemoLastSaveIdAction
import jp.bellware.echo.action.memo.SoundMemoListUpdateAction
import jp.bellware.echo.repository.data.SoundMemo
import jp.bellware.echo.store.Store
import jp.bellware.echo.util.ActionReceiver

/**
 * 音声メモ一覧更新情報
 * @param soundMemos 各音声メモ
 * @param dayHeaders 日付ヘッダー
 * @param playing 再生中フラグ
 * @param playingId 再生中の音声メモID
 * @param volume 再生中音声の視覚的ボリューム
 */
data class SoundMemoItems(val soundMemos: List<SoundMemo>,
                          val dayHeaders: Map<Int, SoundMemoDayHeader>,
                          val playing: Boolean,
                          val playingId: Long, val volume: Float)

class SoundMemoStore @ViewModelInject constructor(actionReceiver: ActionReceiver) : Store(actionReceiver) {

    private val _items = MutableLiveData<SoundMemoItems>(SoundMemoItems(listOf(), mapOf(), false, 0, 0f))

    val items: LiveData<SoundMemoItems> = _items

    /**
     * 一覧更新
     */
    fun onEvent(action: SoundMemoListUpdateAction) {
        _items.value?.let {
            _items.value = it.copy(soundMemos = action.items, dayHeaders = action.dayHeaders)
        }
    }

    /**
     * 再生中音声更新
     */
    fun onEvent(action: SoundMemoLastSaveIdAction) {
        _items.value?.let {
            _items.value = it.copy(playingId = action.id)
        }
    }

    /**
     * 再生中音声の再生終了
     */
    fun onEvent(action: MainPlayEndAction) {
        _items.value?.let {
            _items.value = it.copy(playingId = 0, playing = false, volume = 0f)
        }
    }

    /**
     * 再生中の音声の視覚的ボリューム
     */
    fun onEvent(action: MainPlayVisualVolumeUpdateAction) {
        _items.value?.let {
            _items.value = it.copy(playing = true, volume = action.volume)
        }
    }
}
