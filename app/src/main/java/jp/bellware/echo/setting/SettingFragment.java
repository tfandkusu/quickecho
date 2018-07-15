package jp.bellware.echo.setting;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

//import com.google.android.gms.appinvite.AppInviteInvitation;
import jp.bellware.echo.R;

public class SettingFragment extends PreferenceFragment {

    private static final String PREF_SOUND_EFFECT = "soundEffect";

    private static final String PREF_ABOUT = "about";

    private static final String PREF_INVITE = "invite";

    private static final String PREF_OSS = "oss";

    private static final String PREF_PAGE = "page";

    private static final String PREF_PP = "pp";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);
        {
            //紹介
            Preference pref = findPreference(PREF_INVITE);
            pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    callInviteActivity();
                    return true;
                }
            });
        }
        {
            //商品情報
            Preference pref = findPreference(PREF_ABOUT);
            pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    callAboutActivity();
                    return true;
                }
            });
        }
        {
            //OSS
            Preference pref = findPreference(PREF_OSS);
            pref.setOnPreferenceClickListener(new android.preference.Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(android.preference.Preference preference) {
                    callOSS();
                    return true;
                }
            });
        }
        {
            //プライバシーポリシー
            Preference pref = findPreference(PREF_PP);
            pref.setOnPreferenceClickListener(new android.preference.Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(android.preference.Preference preference) {
                    callPP();
                    return true;
                }
            });

        }
    }

    private void callInviteActivity() {
        //ソースコード公開用に招待は無効
//        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.pref_invite))
//                .setDeepLink(Uri.parse("https://cp999.app.goo.gl/cffF"))
//                .build();
//        startActivityForResult(intent,1);
    }

    private void callAboutActivity() {
        Intent intent = new Intent(getActivity(),AboutActivity.class);
        startActivity(intent);
    }

    private void callOSS() {
        Intent intent = new Intent(getActivity(),HtmlViewerActivity.class);
        intent.putExtra(HtmlViewerActivity.EXTRA_URL,"file:///android_asset/license.txt");
        startActivity(intent);
    }

    private void callPP() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://quick-echo.firebaseapp.com/"));
        startActivity(intent);
    }

    /**
     * 効果音設定を取得する
     * @param context
     * @return
     */
    public static final boolean isSoundEffect(Context context){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getBoolean(PREF_SOUND_EFFECT,true);
    }
}
