package jp.bellware.echo.setting

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_html_viewer.*
import jp.bellware.echo.R

/**
 * HTML Viewer
 */
class HtmlViewerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_html_viewer)
        //ツールバーの設定
        setSupportActionBar(toolbar)
        val lsab = supportActionBar
        lsab?.setHomeButtonEnabled(true)
        lsab?.setDisplayHomeAsUpEnabled(true)
        //WebViewの設定
        web.loadUrl(intent.getStringExtra(EXTRA_URL))
        web.setOnLongClickListener { true }
        web.isHapticFeedbackEnabled = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return false
    }

    companion object {

        val EXTRA_URL = "url"
    }
}
