package jp.bellware.echo.analytics;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
//import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Firebase Analytics担当クラス
 * ソースコード公開用に空実装にする
 */
public class AnalyticsHandler {

//    /**
//     * Firebase Analytics
//     */
//    private FirebaseAnalytics fa;


    public void onCreate(Context context) {
        //fa = FirebaseAnalytics.getInstance(context);
    }

    public void onResume() {
    }

    /**
     * イベントを送る
     *
     * @param action
     */
    public void sendAction(String action) {
//        Bundle bundle = new Bundle();
//        fa.logEvent(action, bundle);
    }

    /**
     * 値付きイベントを送る
     *
     * @param action
     * @param value
     */
    public void sendAction(String action, long value) {
//        Bundle bundle = new Bundle();
//        bundle.putLong(FirebaseAnalytics.Param.VALUE, value);
//        fa.logEvent(action, bundle);
    }
}
