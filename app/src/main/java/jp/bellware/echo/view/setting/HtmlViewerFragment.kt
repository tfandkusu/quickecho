package jp.bellware.echo.view.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import jp.bellware.echo.R
import kotlinx.android.synthetic.main.fragment_html_viewer.*

/**
 * HTML Viewer
 */
class HtmlViewerFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_html_viewer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val safeArgs: HtmlViewerFragmentArgs by navArgs()
        //WebViewの設定
        web.loadUrl(safeArgs.url)
        web.setOnLongClickListener { true }
        web.isHapticFeedbackEnabled = false
    }

}
