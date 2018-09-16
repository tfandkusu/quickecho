package jp.bellware.echo.setting

import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem

import jp.bellware.echo.R
import jp.bellware.echo.analytics.AnalyticsHandler

import kotlinx.android.synthetic.main.activity_about.*

/**
 * アプリについて画面
 */
class AboutActivity : AppCompatActivity() {

    private val ah = AnalyticsHandler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        setSupportActionBar(toolbar)
        val lsab = supportActionBar
        lsab?.setHomeButtonEnabled(true)
        lsab?.setDisplayHomeAsUpEnabled(true)
        //バージョンを挿入
        var versionText = ""
        try {
            val pm = packageManager
            val packageInfo = pm.getPackageInfo(packageName, 0)
            versionText = packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            //おこらない
        }

        version.text = getString(R.string.version) + " " + versionText
        //Analytics
        ah.onCreate(this)
    }

    override fun onResume() {
        super.onResume()
        ah.onResume()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            finish()
            return true
        }
        return false
    }
}
