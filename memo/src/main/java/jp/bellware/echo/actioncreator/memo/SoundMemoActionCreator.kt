package jp.bellware.echo.actioncreator.memo

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.bellware.echo.action.memo.SoundMemoLastSaveIdAction
import jp.bellware.echo.action.memo.SoundMemoListUpdateAction
import jp.bellware.echo.repository.SoundMemoRepository
import jp.bellware.echo.util.Dispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SoundMemoActionCreator @ViewModelInject constructor(private val dispatcher: Dispatcher,
                                                          private val repository: SoundMemoRepository) : ViewModel() {
    fun updateList() = viewModelScope.launch {
        repository.index().collect {
            dispatcher.dispatch(SoundMemoListUpdateAction(it))
        }
    }

    fun checkLastSaveId() = viewModelScope.launch {
        repository.getLastId().collect {
            dispatcher.dispatch(SoundMemoLastSaveIdAction(it))
        }
    }
}
