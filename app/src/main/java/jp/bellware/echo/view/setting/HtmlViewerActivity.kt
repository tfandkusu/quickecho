package jp.bellware.echo.view.setting

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import jp.bellware.echo.R
import kotlinx.android.synthetic.main.activity_html_viewer.*

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
