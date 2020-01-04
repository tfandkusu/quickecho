package jp.bellware.echo.store

import androidx.lifecycle.ViewModel
import jp.bellware.echo.util.ActionReceiver


/**
 * FluxのStoreはこちらを継承する
 * 単体テストのためにこちらの設定が必要
 * http://tools.android.com/tech-docs/unit-testing-support#TOC-Method-...-not-mocked.-
 */
open class Store(private val actionReceiver: ActionReceiver) : ViewModel() {

    init {
        @Suppress("LeakingThis")
        actionReceiver.register(this)
    }

    override fun onCleared() {
        actionReceiver.unregister(this)
    }

}