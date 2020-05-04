package jp.bellware.echo.store

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.observe

/**
 * 1回しか通知されず、値を外側から変更できないLiveData
 */
class ImmutableSingleLiveEvent<T>(private val liveData: LiveData<T>) {

    private val mediatorLiveData = MediatorLiveData<T>()

    private var onChangedFlag = false

    /**
     * 単体テスト用
     */
    val value
        get() = liveData.value

    init {
        mediatorLiveData.addSource(liveData) {
            onChangedFlag = false
            mediatorLiveData.value = it
        }
    }

    fun observe(owner: LifecycleOwner, onChanged: (T) -> Unit) {
        mediatorLiveData.observe(owner) {
            if (!onChangedFlag) {
                onChanged(it)
                onChangedFlag = true
            }
        }
    }

}