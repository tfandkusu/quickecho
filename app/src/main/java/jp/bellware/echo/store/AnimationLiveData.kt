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
            // 多重アニメーション防止
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
         * 値を更新する。不自然な挙動になる入力は自然な挙動に変換される。
         */
        set(v) {
            if (liveData.value == AnimationStatus.INVISIBLE && v == AnimationStatus.DELETE) {
                // 非表示から削除には遷移できない
                liveData.value = AnimationStatus.INVISIBLE
            } else if (liveData.value == AnimationStatus.VISIBLE && v == AnimationStatus.FI) {
                // 表示からフェードインはできない
                liveData.value = AnimationStatus.VISIBLE
            } else {
                liveData.value = v
            }
            lastValue = null
        }
        /**
         * 値を取得する
         */
        get() {
            return liveData.value
        }

}