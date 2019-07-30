package jp.bellware.echo.setting

import android.content.pm.PackageManager
import androidx.databinding.DataBindingUtil
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem

import jp.bellware.echo.R
import jp.bellware.echo.analytics.AnalyticsHandler
import jp.bellware.echo.databinding.ActivityAboutBinding

import kotlinx.android.synthetic.main.activity_about.*

/**
 * アプリについて画面
 */
class AboutActivity : AppCompatActivity() {

    private val ah = AnalyticsHandler()

    /**
     * ビューモデル
     */
    private val viewModel = AboutViewModel(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //データバインディング設定
        val binding = DataBindingUtil.setContentView<ActivityAboutBinding>(this, R.layout.activity_about)
        binding.viewModel = viewModel
        setSupportActionBar(toolbar)
        val lsab = supportActionBar
        lsab?.setHomeButtonEnabled(true)
        lsab?.setDisplayHomeAsUpEnabled(true)
        //Analytics
        ah.onCreate(this)
        //
        viewModel.onCreate()
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
