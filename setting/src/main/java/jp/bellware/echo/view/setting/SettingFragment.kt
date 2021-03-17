package jp.bellware.echo.view.setting

// import com.google.android.gms.appinvite.AppInviteInvitation;

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceFragmentCompat
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import jp.bellware.echo.setting.R
import jp.bellware.echo.util.QuickEchoFlags

/**
 * 設定画面のフラグメント
 */
class SettingFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        addPreferencesFromResource(R.xml.setting)
        // 開発中の音声メモ機能を無効化
        val soundMemo = findPreference<PreferenceCategory>(CATEGORY_SOUND_MEMO)
        if (!QuickEchoFlags.SOUND_MEMO) {
            soundMemo?.isVisible = false
        }
        run {
            // 商品情報
            val pref = findPreference<Preference>(PREF_ABOUT)
            pref?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                val action = SettingFragmentDirections.actionSettingFragmentToAboutFragment()
                findNavController().navigate(action)
                true
            }
        }
        run {
            // OSS
            val pref = findPreference<Preference>(PREF_OSS_1)
            pref?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                callOSS()
                true
            }
        }
        run {
            // OSS
            val pref = findPreference<Preference>(PREF_OSS_2)
            pref?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                val action = SettingFragmentDirections.actionSettingFragmentToOss2Fragment()
                findNavController().navigate(action)
                true
            }
        }
        run {
            // プライバシーポリシー
            val pref = findPreference<Preference>(PREF_PP)
            pref?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                callPP()
                true
            }
        }
        // TODO ディープリンクを復活させる
    }

    private fun callOSS() {
        OssLicensesMenuActivity.setActivityTitle(getString(R.string.pref_oss_1))
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

        private const val CATEGORY_SOUND_MEMO = "categorySoundMemo"

        private const val PREF_ABOUT = "about"

        private const val PREF_OSS_1 = "oss1"

        private const val PREF_OSS_2 = "oss2"

        private const val PREF_PP = "pp"
    }
}
