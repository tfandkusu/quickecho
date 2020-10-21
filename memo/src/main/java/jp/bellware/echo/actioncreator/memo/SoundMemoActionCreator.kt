package jp.bellware.echo.actioncreator.memo

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.bellware.echo.action.memo.SoundMemoDayHeader
import jp.bellware.echo.action.memo.SoundMemoLastSaveIdAction
import jp.bellware.echo.action.memo.SoundMemoListUpdateAction
import jp.bellware.echo.repository.CurrentTimeRepository
import jp.bellware.echo.repository.SoundMemoRepository
import jp.bellware.echo.repository.data.YMD
import jp.bellware.echo.repository.data.toYMD
import jp.bellware.echo.util.Dispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SoundMemoActionCreator @ViewModelInject constructor(private val dispatcher: Dispatcher,
                                                          private val repository: SoundMemoRepository,
                                                          private val currentTimeRepository: CurrentTimeRepository) : ViewModel() {
    fun updateList() = viewModelScope.launch {
        repository.index().collect { items ->
            val now = currentTimeRepository.now()
            val today = toYMD(now)
            val dayHeaders = mutableListOf<SoundMemoDayHeader>()
            var previous: YMD? = null
            items.mapIndexed { index, soundMemo ->
                val ymd = toYMD(soundMemo.createdAt)
                if (index == 0) {
                    dayHeaders.add(SoundMemoDayHeader(index, today == ymd, today.year == ymd.year, ymd))
                } else if (previous != ymd) {
                    dayHeaders.add(SoundMemoDayHeader(index, today == ymd, today.year == ymd.year, ymd))
                }
                previous = ymd
            }
            dispatcher.dispatch(SoundMemoListUpdateAction(items, dayHeaders))
        }
    }

    fun checkLastSaveId() = viewModelScope.launch {
        repository.getLastId().collect {
            dispatcher.dispatch(SoundMemoLastSaveIdAction(it))
        }
    }
}
