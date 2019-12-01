package jp.bellware.echo.store

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

enum class AnimationStatus {
    /**
     * フェードインで表示する
     */
    FI,
    /**
     * 削除アニメーションで退出する
     */
    DELETE,
    /**
     * Activity復帰時に表示状態を再現する
     */
    VISIBLE,
    /**
     * Activity復帰時に非表示状態を再現する
     */
    INVISIBLE
}

/**
 * Activity復帰時にはアニメーションを行わないためのLiveData
 */
class AnimationLiveData {
    /**
     * LiveData
     */
    private val liveData = MutableLiveData<AnimationStatus>()
    /**
     * 最後にObserverに渡したバリュー
     */
    private var lastValue: AnimationStatus? = null

    /**
     * 値を監視する
     * @param owner ライフサイクルオーナー
     * @param observer 監視イベント
     */
    fun observe(owner: LifecycleOwner, observer: Observer<AnimationStatus>) {
        liveData.observe(owner, Observer<AnimationStatus> { v ->
            if (lastValue == AnimationStatus.FI)
                observer.onChanged(AnimationStatus.VISIBLE)
            else if (lastValue == AnimationStatus.DELETE)
                observer.onChanged(AnimationStatus.INVISIBLE)
            else
                observer.onChanged(v)
            lastValue = v
        })
    }

    var value: AnimationStatus?
        /**
         * 値を更新する
         */
        set(v) {
            liveData.value = v
            lastValue = null
        }
        /**
         * 値を取得する
         */
        get() {
            return liveData.value
        }

}