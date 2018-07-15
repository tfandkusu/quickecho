package jp.bellware.echo.main;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import android.widget.ImageView;
import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.bellware.echo.R;
import jp.bellware.echo.main.view.ExplosionView;
import jp.bellware.echo.main.view.MyFAB;
import jp.bellware.echo.main.view.VisualVolumeView;
import jp.bellware.echo.setting.SettingActivity;
import jp.bellware.util.BWU;


public class MainActivity extends AppCompatActivity implements MainCallback {


    private static final int CODE_SETTING = 1;

    private Handler handler = new Handler();


    /**
     * 警告担当
     */
    private WarningHandler wh = new WarningHandler();

    /**
     * 表示FPS
     */
    private int fps;

    private AudioManager audioManager;


    /**
     * アニメ
     */
    private QRecAnimator animator = new QRecAnimator();

    /**
     * DP
     */
    @BindDimen(R.dimen.dp)
    float dp;

    /**
     * 状態画像+視覚的ボリューム
     */
    @BindView(R.id.status)
    FrameLayout statusFrameLayout;

    /**
     * 状態画像
     */
    @BindView(R.id.status_image)
    ImageView statusImageView;

    /**
     * 視覚的ボリューム
     */
    @BindView(R.id.visual_volume)
    VisualVolumeView vvv;

    /**
     * 波紋エフェクト
     */
    @BindView(R.id.explosion)
    ExplosionView explosionView;

    /**
     * 録音ボタン
     */
    @BindView(R.id.record)
    MyFAB recordButton;
    /**
     * 再生ボタン
     */
    @BindView(R.id.play)
    MyFAB playButton;
    /**
     * 再再生ボタン
     */
    @BindView(R.id.replay)
    MyFAB replayButton;
    /**
     * 停止ボタン
     */
    @BindView(R.id.stop)
    MyFAB stopButton;

    /**
     * 削除ボタン
     */
    @BindView(R.id.delete)
    MyFAB deleteButton;

    /**
     * FPS計算タスク
     */
    private Runnable fpsTask = new Runnable() {
        @Override
        public void run() {
            BWU.log("fps = " + fps);
            fps = 0;
            handler.postDelayed(this, 1000);
        }
    };


    private MainService service = null;

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            service = ((MainService.MainServiceBinder) binder).getService();
            service.setCallback(MainActivity.this);
            service.onSettingUpdated();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BWU.log("MainActivity#onCreate");
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //ツールバー設定
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        //広告の設定
        setUpAd();
        //ボリューム調整を音楽にする
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        //AudioManagerを取得
        this.audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        //警告担当
        wh.onCreate(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        BWU.log("MainActivity#onStart");
        //サービスを開始
        Intent intent = new Intent(this, MainService.class);
        startService(intent);
        bindService(intent, conn, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        BWU.log("MainActivity#onResume");
        wh.onResume();
        if (this.audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) == 0) {
            wh.show(R.string.warning_volume);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        BWU.log("MainActivity#onPause");
        handler.removeCallbacks(fpsTask);
        wh.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        BWU.log("MainActivity#onStop");
        if (service != null) {
            service.setCallback(null);
            service = null;
        }
        unbindService(conn);
    }

    @Override
    public void onBackPressed() {
        if (service != null) {
            if (service.onBackPressed()) {

            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BWU.log("MainActivity#onDestroy");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.setting) {
            callSettingActivity();
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_SETTING) {
            if (service != null) {
                service.onSettingUpdated();
            }
        }
    }

    /**
     * 設定画面
     */
    private void callSettingActivity() {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivityForResult(intent, CODE_SETTING);
    }

    /**
     * 広告を設定する
     */
    private void setUpAd() {
//        AdView mAdView = (AdView) findViewById(R.id.ad);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
    }

    /**
     * 録音ボタンがクリックされた時のイベント
     */
    @OnClick(R.id.record)
    public void onRecordClicked() {
        if (service != null)
            service.onRecordClicked();

    }

    /**
     * 再生ボタンがクリックされた時のイベント
     */
    @OnClick(R.id.play)
    public void onPlayClicked() {
        if (service != null)
            service.onPlayClicked();
    }

    /**
     * 再再生ボタンがクリックされた時のイベント
     */
    @OnClick(R.id.replay)
    public void onReplayClicked() {
        if (service != null) {
            service.onReplayClicked();
        }
    }

    /**
     * 停止ボタンがクリックされた時のイベント
     */
    @OnClick(R.id.stop)
    public void onStopClicked() {
        if (service != null) {
            service.onStopClicked();
        }
    }

    /**
     * 削除ボタンがクリックされた時のイベント
     */
    @OnClick(R.id.delete)
    public void onDeleteClicked() {
        if (service != null) {
            service.onDeleteClicked();
        }
    }


    @Override
    public void onUpdateStatus(boolean animation, QRecStatus status) {
        BWU.log("MainActivity#onUpdateStatus " + status);
        //ステータスビュー
        if (status == QRecStatus.INIT) {
            //初期化中
            //何も表示しない
            statusFrameLayout.setVisibility(View.INVISIBLE);
            recordButton.setVisibility(View.INVISIBLE);
            playButton.setVisibility(View.INVISIBLE);
            replayButton.setVisibility(View.INVISIBLE);
            stopButton.setVisibility(View.INVISIBLE);
            deleteButton.setVisibility(View.INVISIBLE);
        } else if (status == QRecStatus.DELETE_RECORDING ||
                status == QRecStatus.DELETE_PLAYING) {
            //アニメーション開始
            if (animation) {
                animator.startDeleteAnimation(statusFrameLayout);
                animator.startDeleteAnimation(deleteButton);
                if (status == QRecStatus.DELETE_PLAYING) {
                    animator.startDeleteAnimation(replayButton);
                    animator.startDeleteAnimation(stopButton);
                } else {
                    replayButton.setVisibility(View.INVISIBLE);
                    stopButton.setVisibility(View.INVISIBLE);
                }
            } else {
                statusFrameLayout.setVisibility(View.INVISIBLE);
                recordButton.setVisibility(View.VISIBLE);
                playButton.setVisibility(View.INVISIBLE);
                replayButton.setVisibility(View.INVISIBLE);
                stopButton.setVisibility(View.INVISIBLE);
                deleteButton.setVisibility(View.INVISIBLE);
            }
        } else if (status == QRecStatus.READY_FIRST ||
                status == QRecStatus.READY) {
            //録音ボタンにする
            statusFrameLayout.setVisibility(View.INVISIBLE);
            recordButton.setVisibility(View.VISIBLE);
            playButton.setVisibility(View.INVISIBLE);
            replayButton.setVisibility(View.INVISIBLE);
            stopButton.setVisibility(View.INVISIBLE);
            deleteButton.setVisibility(View.INVISIBLE);
        } else if (status == QRecStatus.STOP) {
            //停止状態
            statusFrameLayout.setVisibility(View.VISIBLE);
            statusImageView.setImageResource(R.drawable.speaker_48dp);
            recordButton.setVisibility(View.VISIBLE);
            playButton.setVisibility(View.INVISIBLE);
            replayButton.setVisibility(View.VISIBLE);
            stopButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
        } else if (status == QRecStatus.STARTING_RECORD) {
            //スピーカーアイコン表示
            statusImageView.setImageResource(R.drawable.microphone_48dp);
            //再生ボタンにする
            recordButton.setVisibility(View.INVISIBLE);
            playButton.setVisibility(View.VISIBLE);
            //サブコントロールは非表示
            replayButton.setVisibility(View.INVISIBLE);
            stopButton.setVisibility(View.INVISIBLE);
            if (animation) {
                //爆発エフェクト
                explosionView.startRecordAnimation();
                //ステータスはフェードイン
                animator.fadeIn(statusFrameLayout);
                //削除ボタンはフェードイン
                if (deleteButton.getVisibility() == View.INVISIBLE) {
                    animator.fadeIn(deleteButton);
                }
            } else {
                statusFrameLayout.setVisibility(View.VISIBLE);
                deleteButton.setVisibility(View.VISIBLE);
            }

        } else if (status == QRecStatus.RECORDING) {
            statusFrameLayout.setVisibility(View.VISIBLE);
            statusImageView.setImageResource(R.drawable.microphone_48dp);
            recordButton.setVisibility(View.INVISIBLE);
            playButton.setVisibility(View.VISIBLE);
            replayButton.setVisibility(View.INVISIBLE);
            stopButton.setVisibility(View.INVISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
        } else if (status == QRecStatus.STOPPING_RECORD) {
            //スピーカーを表示
            statusImageView.setImageResource(R.drawable.speaker_48dp);
            //録音ボタンにする
            recordButton.setVisibility(View.VISIBLE);
            playButton.setVisibility(View.INVISIBLE);
            if (animation) {
                //ステータスを表示
                animator.fadeIn(statusFrameLayout);
                //サブコントロール表示
                animator.fadeIn(replayButton);
                animator.fadeIn(stopButton);
            } else {
                statusFrameLayout.setVisibility(View.VISIBLE);
                replayButton.setVisibility(View.VISIBLE);
                stopButton.setVisibility(View.VISIBLE);
            }
        } else if (status == QRecStatus.PLAYING) {
            statusFrameLayout.setVisibility(View.VISIBLE);
            statusImageView.setImageResource(R.drawable.speaker_48dp);
            recordButton.setVisibility(View.VISIBLE);
            playButton.setVisibility(View.INVISIBLE);
            replayButton.setVisibility(View.VISIBLE);
            stopButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
        } else if (status == QRecStatus.STOPPING_PLAYING) {
        }
    }

    @Override
    public void onUpdateVolume(float volume) {
        vvv.setVolume(volume);
    }

    @Override
    public void onShowWarningMessage(int resId) {
        wh.show(resId);
    }
}
