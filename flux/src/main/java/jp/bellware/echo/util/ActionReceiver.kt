package jp.bellware.echo.util

import de.greenrobot.event.EventBus
import javax.inject.Inject

/**
 * Actionを受信するオブジェクトを登録および登録解除する担当
 */
interface ActionReceiver {
    fun register(obj: Any)

    fun unregister(obj: Any)
}

class ActionReceiverImpl @Inject constructor() : ActionReceiver {
    override fun register(obj: Any) {
        EventBus.getDefault().register(obj)

    }

    override fun unregister(obj: Any) {
        EventBus.getDefault().unregister(obj)
    }
}
