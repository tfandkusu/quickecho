package jp.bellware.echo.util

import com.crashlytics.android.Crashlytics

object EchoCrashlytics {
    /**
     * テストのためにクラッシュする
     */
    fun crash() {
        Crashlytics.getInstance().crash()
    }
}