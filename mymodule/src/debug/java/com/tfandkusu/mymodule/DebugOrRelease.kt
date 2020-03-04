package com.tfandkusu.mymodule

import android.util.Log

/**
 * Build Typeで実装を変更することができるかの確認用クラス。
 * Build TypeはアプリケーションモジュールのBuild Typeに同期する。
 */
object DebugOrRelease {
    fun func() {
        Log.d("QuickEcho", "It's debug build")
    }
}