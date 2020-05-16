package jp.bellware.echo.view.setting

//import com.google.android.gms.appinvite.AppInviteInvitation;

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import jp.bellware.echo.navigation.SettingNavigation
import jp.bellware.echo.setting.R
import org.koin.android.ext.android.inject

/**
 * 設定画面のフラグメント
 */
class SettingFragment : PreferenceFragmentCompat() {

    private val navigation: SettingNavigation by inject()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.setting)
        run {
            //商品情報
            val pref = findPreference<Preference>(PREF_ABOUT)
            pref?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                navigation.navigateToAbout(this)
                true
            }
        }
        run {
            //OSS
            val pref = findPreference<Preference>(PREF_OSS_1)
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
        navigation.navigateToLink(this)
    }

    private fun callOSS() {
        OssLicensesMenuActivity.setActivityTitle(getString(R.string.pref_oss_1));
        val intent = Intent(requireContext(), OssLicensesMenuActivity::class.java)
        intent.putExtra("title", getString(R.string.oss_license_title))
        startActivity(intent)
    }

    private fun callPP() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("https://quick-echo.firebaseapp.com/")
        startActivity(intent)
    }

    companion object {

        private const val PREF_ABOUT = "about"

        private const val PREF_OSS_1 = "oss1"

        private const val PREF_PP = "pp"

    }
}
