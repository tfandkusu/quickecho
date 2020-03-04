package com.tfandkusu.mymodule

import android.util.Log

class ClassInModule {
    fun func() {
        Log.d("QuickEcho", "Call my module")
        // ライブラリモジュールからはBuildConfigにアクセスできない
    }
}