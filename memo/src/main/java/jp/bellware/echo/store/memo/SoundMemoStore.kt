package jp.bellware.echo.store.memo

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import jp.bellware.echo.action.memo.SoundMemoListUpdateAction
import jp.bellware.echo.repository.data.SoundMemo
import jp.bellware.echo.store.Store
import jp.bellware.echo.util.ActionReceiver

class SoundMemoStore @ViewModelInject constructor(actionReceiver: ActionReceiver) : Store(actionReceiver) {

    private val _items = MutableLiveData<List<SoundMemo>>()

    val items: LiveData<List<SoundMemo>> = _items

    fun onEvent(action: SoundMemoListUpdateAction) {
        _items.value = action.items
    }
}
