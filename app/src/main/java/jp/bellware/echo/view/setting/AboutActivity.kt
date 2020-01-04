package jp.bellware.echo.view.setting

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import jp.bellware.echo.R
import kotlinx.android.synthetic.main.activity_about.*

/**
 * アプリについて画面
 */
class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        setSupportActionBar(toolbar)
        val lsab = supportActionBar
        lsab?.setHomeButtonEnabled(true)
        lsab?.setDisplayHomeAsUpEnabled(true)
        //バージョン名を設定
        try {
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            val versionText = "${getString(R.string.version)} ${packageInfo.versionName}"
            version.text = versionText
        } catch (e: PackageManager.NameNotFoundException) {
            //おこらない
        }

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
