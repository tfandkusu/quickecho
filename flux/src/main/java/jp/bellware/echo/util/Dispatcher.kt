package jp.bellware.echo.util

import de.greenrobot.event.EventBus
import javax.inject.Inject
import jp.bellware.echo.action.Action

/**
 * FluxのDispatcher
 */
interface Dispatcher {
    /**
     * Actionを投げる。必ずメインスレッドで実行すること。
     */
    fun dispatch(action: Action)
}

class DispatcherImpl @Inject constructor() : Dispatcher {
    override fun dispatch(action: Action) {
        EventBus.getDefault().post(action)
    }
}
