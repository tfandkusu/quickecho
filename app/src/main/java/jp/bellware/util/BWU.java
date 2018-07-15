package jp.bellware.util;

import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * メソッド１つで済むUtil
 */
public class BWU {
    public static void log(String message) {
        Log.d("BWA", message);
    }

    /**
     * Android 5.0以上か判定する
     */
    public static void v21(Runnable task) {
        if (Build.VERSION.SDK_INT >= 21) {
            task.run();
        }
    }

    public static Fragment findFragmentFromViewPager(AppCompatActivity activity, int id, int position){
        String tag = "android:switcher:" + id + ":" + position;
        Fragment fragment = activity.getSupportFragmentManager().findFragmentByTag(tag);
        return fragment;
    }

    public static void log(Exception e) {
        Log.d("BWA","Error",e);
    }
}
