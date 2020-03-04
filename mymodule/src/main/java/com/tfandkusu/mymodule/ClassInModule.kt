package com.tfandkusu.mymodule

import android.util.Log

object ClassInModule {
    fun func() {
        Log.d("QuickEcho", "Call my module")
        // ライブラリモジュールからはBuildConfigにアクセスできない

        // Debug buildかRelease buildかで実装が変わる
        DebugOrRelease.func()
    }
}