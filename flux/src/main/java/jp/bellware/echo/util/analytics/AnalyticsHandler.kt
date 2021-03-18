package jp.bellware.echo.util.analytics

import android.content.Context

// import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Firebase Analytics担当クラス
 * ソースコード公開用に空実装にする
 */
class AnalyticsHandler {

    //    /**
    //     * Firebase Analytics
    //     */
    //    private FirebaseAnalytics fa;

    fun onCreate(context: Context) {
        // fa = FirebaseAnalytics.getInstance(context);
    }

    fun onResume() {}

    /**
     * イベントを送る
     *
     * @param action
     */
    fun sendAction(action: String) {
        //        Bundle bundle = new Bundle();
        //        fa.logEvent(action, bundle);
    }

    /**
     * 値付きイベントを送る
     *
     * @param action
     * @param value
     */
    fun sendAction(action: String, value: Long) {
        //        Bundle bundle = new Bundle();
        //        bundle.putLong(FirebaseAnalytics.Param.VALUE, value);
        //        fa.logEvent(action, bundle);
    }
}
