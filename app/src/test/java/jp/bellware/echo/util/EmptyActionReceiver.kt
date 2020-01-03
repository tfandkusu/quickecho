package jp.bellware.echo.util

import jp.bellware.util.ActionReceiver

/**
 * テスト用に空実装
 */
object EmptyActionReceiver : ActionReceiver {
    override fun register(obj: Any) {
    }

    override fun unregister(obj: Any) {
    }
}