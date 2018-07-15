package jp.bellware.echo.main;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import jp.bellware.echo.R;
import jp.bellware.echo.main.view.VisualVolumeView;

/**
 * 視覚的ボリューム隔離クラス
 */
public class VisualVolumeHandler {

    /**
     * アニメーション担当
     */
    private QRecAnimator animator = new QRecAnimator();

    private Handler handler = new Handler();

    /**
     * 録音中フラグ
     */
    private boolean recording = false;

    /**
     * 再生中フラグ
     */
    private boolean playing = false;


    private Callback cb;

    public interface Callback {
        float getRecordVisualVolume();

        float getPlayVisualVolume();

        void onUpdateVolume(float volume);
    }


    /**
     * 視覚的ボリューム更新タスク
     */
    private Runnable updateTask = new Runnable() {
        @Override
        public void run() {
            if (recording) {
                cb.onUpdateVolume(cb.getRecordVisualVolume());
            } else if (playing) {
                cb.onUpdateVolume(cb.getPlayVisualVolume());
            } else {
                cb.onUpdateVolume(0);
            }
            handler.postDelayed(this, 1000 / 30);
        }
    };


    public void onCreate(Context context, Callback cb) {
        this.cb = cb;
    }

    public void onResume() {

    }

    public void onPause() {
        handler.removeCallbacks(updateTask);
        playing = false;
        recording = false;
    }

    public void reset() {
        playing = false;
        recording = false;
        cb.onUpdateVolume(0);
    }


    public void play() {
        playing = true;
        recording = false;
        handler.removeCallbacks(updateTask);
        updateTask.run();
    }

    public void record() {
        playing = false;
        recording = true;
        handler.removeCallbacks(updateTask);
        updateTask.run();
    }

    public void stop() {
        playing = false;
        recording = false;
        handler.removeCallbacks(updateTask);
    }


}
