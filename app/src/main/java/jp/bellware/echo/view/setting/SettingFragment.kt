package jp.bellware.echo.view.setting

//import com.google.android.gms.appinvite.AppInviteInvitation;
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import jp.bellware.echo.R


/**
 * 設定画面のフラグメント
 */
class SettingFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.setting)
        run {
            //紹介
            val pref = findPreference<Preference>(PREF_INVITE)
            pref?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                callInviteActivity()
                true
            }
        }
        run {
            //商品情報
            val pref = findPreference<Preference>(PREF_ABOUT)
            pref?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                callAboutActivity()
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

    }

    private fun callInviteActivity() {
        //ソースコード公開用に招待は無効
        //        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.pref_invite))
        //                .setDeepLink(Uri.parse("https://cp999.app.goo.gl/cffF"))
        //                .build();
        //        startActivityForResult(intent,1);
    }

    private fun callAboutActivity() {
        val intent = Intent(activity, AboutActivity::class.java)
        startActivity(intent)
    }

    private fun callOSS() {
        val intent = Intent(activity, HtmlViewerActivity::class.java)
        intent.putExtra(HtmlViewerActivity.EXTRA_URL, "file:///android_asset/license.txt")
        startActivity(intent)
    }

    private fun callPP() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("https://quick-echo.firebaseapp.com/")
        startActivity(intent)
    }

    companion object {

        private val PREF_SOUND_EFFECT = "soundEffect"

        private val PREF_ABOUT = "about"

        private val PREF_INVITE = "invite"

        private val PREF_OSS = "oss"

        private val PREF_PAGE = "page"

        private val PREF_PP = "pp"

        /**
         * 効果音設定を取得する
         * @param context
         * @return
         */
        fun isSoundEffect(context: Context): Boolean {
            val pref = PreferenceManager.getDefaultSharedPreferences(context)
            return pref.getBoolean(PREF_SOUND_EFFECT, true)
        }
    }
}
