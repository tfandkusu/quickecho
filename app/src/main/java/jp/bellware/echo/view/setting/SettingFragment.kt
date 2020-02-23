package jp.bellware.echo.view.setting

//import com.google.android.gms.appinvite.AppInviteInvitation;
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import jp.bellware.echo.R


/**
 * 設定画面のフラグメント
 */
class SettingFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.setting)
        run {
            //商品情報
            val pref = findPreference<Preference>(PREF_ABOUT)
            pref?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                callAboutFragment()
                true
            }
        }
        run {
            //OSS
            val pref = findPreference<Preference>(PREF_OSS)
            pref?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                callOSS()
                true
            }
        }
        run {
            //プライバシーポリシー
            val pref = findPreference<Preference>(PREF_PP)
            pref?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                callPP()
                true
            }
        }
        val safeArgs: SettingFragmentArgs by navArgs()
        if (safeArgs.type == AboutFragment.LINK_TYPE) {
            val action = SettingFragmentDirections.actionSettingFragmentToAboutFragmentWithNoAnimation()
            findNavController().navigate(action)
        }
    }

    private fun callAboutFragment() {
        val action = SettingFragmentDirections.actionSettingFragmentToAboutFragment()
        findNavController().navigate(action)
    }

    private fun callOSS() {
        val action = SettingFragmentDirections.actionSettingFragmentToHtmlViewerFragment("file:///android_asset/license.txt")
        findNavController().navigate(action)
    }

    private fun callPP() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("https://quick-echo.firebaseapp.com/")
        startActivity(intent)
    }

    companion object {

        private const val PREF_ABOUT = "about"

        private const val PREF_OSS = "oss"

        private const val PREF_PP = "pp"

    }
}
