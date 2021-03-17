package jp.bellware.echo.util

import android.content.Context
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.soloader.SoLoader
import jp.bellware.echo.BuildConfig

/**
 * Flipperの設定担当
 */
object FlipperSetting {
    /**
     * ApplicationのonCreateから呼ぶ
     * @param context ApplicationContext
     */
    fun start(context: Context) {
        SoLoader.init(context, false)
        if (BuildConfig.DEBUG && FlipperUtils.shouldEnableFlipper(context)) {
            val client = AndroidFlipperClient.getInstance(context)
            client.addPlugin(InspectorFlipperPlugin(context, DescriptorMapping.withDefaults()))
            client.start()
        }
    }
}
