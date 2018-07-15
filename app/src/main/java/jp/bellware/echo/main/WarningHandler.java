package jp.bellware.echo.main;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;
import android.widget.Toast;
import jp.bellware.echo.R;

/**
 * 警告表示担当
 */
public class WarningHandler {
    private Activity activity;

    private Handler handler = new Handler();

    private View view;

    private TextView textView;

    private Runnable task = new Runnable() {
        @Override
        public void run() {
            Animation a = new AlphaAnimation(1,0);
            a.setDuration(300);
            a.setFillAfter(true);
            view.startAnimation(a);
        }
    };

    public void onCreate(Activity activity){
        this.activity = activity;
        view = activity.findViewById(R.id.warning_mute);
        view.setVisibility(View.GONE);
        textView = (TextView) activity.findViewById(R.id.warning_mute_text);
    }

    public void show(int resId){
        textView.setText(resId);
        Animation a = new AlphaAnimation(0,1);
        a.setDuration(300);
        view.startAnimation(a);
        view.setVisibility(View.VISIBLE);
        handler.removeCallbacks(task);
        handler.postDelayed(task,3000);
    }

    public void onResume(){

    }

    public void onPause(){
        handler.removeCallbacks(task);
        view.setVisibility(View.GONE);
    }
}
