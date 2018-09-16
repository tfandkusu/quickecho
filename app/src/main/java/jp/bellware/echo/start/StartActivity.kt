package jp.bellware.echo.start

import android.app.Activity
import android.content.Intent
import android.os.Bundle

import jp.bellware.echo.main.MainActivity

/**
 * 開始アクティビティ。多重起動を防ぐためにsingleInstanceになっている
 */
class StartActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent(this, PermissionActivity::class.java)
        startActivity(intent)
        finish()
    }
}
