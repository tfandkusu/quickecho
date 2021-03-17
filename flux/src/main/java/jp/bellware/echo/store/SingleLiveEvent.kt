package jp.bellware.echo.store

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

/**
 *
 * 同じ値を一度だけ通知するLiveData
 */
class SingleLiveEvent<T> {

    /**
     * LiveData
     */
    private val liveData = MutableLiveData<T>()

    /**
     * onChangedを呼んだフラグ
     */
    private var onChangedFlag = false

    /**
     * 値を監視する
     * @param owner ライフサイクルオーナー
     * @param observer 監視イベント
     */
    fun observe(owner: LifecycleOwner, observer: Observer<T>) {
        liveData.observe(
            owner,
            Observer<T> { t ->
                if (!onChangedFlag) {
                    observer.onChanged(t)
                    onChangedFlag = true
                }
            }
        )
    }

    var value: T?
        /**
         * 値を更新する
         */
        set(v) {
            onChangedFlag = false
            liveData.value = v
        }
        /**
         * 値を取得する
         */
        get() {
            return liveData.value
        }
}
