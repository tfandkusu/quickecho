package jp.bellware.echo.setting

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import jp.bellware.echo.R
import jp.bellware.echo.analytics.AnalyticsHandler
import kotlinx.android.synthetic.main.activity_setting.*

/**
 * 設定画面
 */
class SettingActivity : AppCompatActivity() {

    private val ah = AnalyticsHandler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        //ツールバーの設定
        setSupportActionBar(toolbar)
        val lsab = supportActionBar
        lsab?.setHomeButtonEnabled(true)
        lsab?.setDisplayHomeAsUpEnabled(true)
        //フラグメントの追加
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.preference_frame, SettingFragment())
        ft.commit()
        //Analytics
        ah.onCreate(this)
    }

    override fun onResume() {
        super.onResume()
        ah.onResume()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return false
    }
}
