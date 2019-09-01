package jp.bellware.echo.store

import androidx.lifecycle.ViewModel
import de.greenrobot.event.EventBus

/**
 * FluxのStoreはこちらを継承する
 */
open class Store : ViewModel() {

    init {
        @Suppress("LeakingThis")
        EventBus.getDefault().register(this)
    }

    override fun onCleared() {
        EventBus.getDefault().unregister(this)
    }

    /**
     * 単体テストの時はこちらで購読解除する。
     */
    fun clear(){
        EventBus.getDefault().unregister(this)
    }
}