package jp.bellware.util

import de.greenrobot.event.EventBus

/**
 * Actionを受信するオブジェクトを登録および登録解除する担当
 */
interface ActionReceiver {
    fun register(obj: Any)

    fun unregister(obj: Any)
}

class ActionReceiverImpl : ActionReceiver {
    override fun register(obj: Any) {
        EventBus.getDefault().register(obj)

    }

    override fun unregister(obj: Any) {
        EventBus.getDefault().unregister(obj)
    }
}