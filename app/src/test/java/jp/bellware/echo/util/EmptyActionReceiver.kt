package jp.bellware.echo.util

/**
 * テスト用に空実装
 */
object EmptyActionReceiver : ActionReceiver {
    override fun register(obj: Any) {
    }

    override fun unregister(obj: Any) {
    }
}