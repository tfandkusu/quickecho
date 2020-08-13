package jp.bellware.echo.view.memo

import android.content.Context
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.plugin.common.EventChannel
import timber.log.Timber
import java.util.*
import kotlin.collections.HashMap

/**
 * Flutterの初期化
 */
object QuickEchoFlutterUtil {

    const val ENGINE_ID = "my_engine_id"

    fun onCreate(context: Context) {
        val flutterEngine = FlutterEngine(context)
        val eventChannel = EventChannel(flutterEngine.dartExecutor.binaryMessenger,
                "jp.bellware.echo.sound_memo:sound_memo")
        eventChannel.setStreamHandler(object : EventChannel.StreamHandler {
            override fun onListen(arguments: Any?, events: EventChannel.EventSink) {
                Timber.d("onListen")
                val map = HashMap<String, Any>()
                map["id"] = 1L
                map["fileName"] = "output1"
                map["date"] = Date().time
                events.success(map)
            }

            override fun onCancel(arguments: Any?) {
                Timber.d("onCancel")
            }
        })
        flutterEngine.dartExecutor.executeDartEntrypoint(
                DartExecutor.DartEntrypoint.createDefault()
        );
        FlutterEngineCache
                .getInstance()
                .put(ENGINE_ID, flutterEngine)
    }
}
