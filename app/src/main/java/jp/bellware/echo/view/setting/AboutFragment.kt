package jp.bellware.echo.view.setting

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import jp.bellware.echo.R
import kotlinx.android.synthetic.main.fragment_about.*

/**
 * アプリについて画面
 */
class AboutFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //バージョン名を設定
        try {
            val packageName = requireContext().packageName
            val packageInfo = requireContext().packageManager.getPackageInfo(packageName, 0)
            val versionText = "${getString(R.string.version)} ${packageInfo.versionName}"
            version.text = versionText
        } catch (e: PackageManager.NameNotFoundException) {
            //おこらない
        }

    }
}
