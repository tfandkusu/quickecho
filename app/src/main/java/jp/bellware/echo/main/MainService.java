package jp.bellware.echo.main;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.View;
import jp.bellware.echo.R;
import jp.bellware.echo.analytics.AnalyticsHandler;
import jp.bellware.echo.data.QRecStorage;
import jp.bellware.echo.setting.SettingFragment;
import jp.bellware.util.BWU;

/**
 * 録音、作成機能のためのサービス
 */
public class MainService extends Service {

    /**
     * サービス生存時間は接続Activity消失から3秒後
     */
    private static final int END_TIME = 2000;

    /**
     * 録音時間制限は2分
     */
    private static final int TIME_LIMIT = 2 * 60 * 1000;

    private Handler handler = new Handler();

    private Runnable endTask = new Runnable() {
        @Override
        public void run() {
            BWU.log("MainService#endTask");
            stopSelf();
        }
    };

    public class MainServiceBinder extends Binder {
        public MainService getService() {
            return MainService.this;
        }
    }

    private final IBinder binder = new MainServiceBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    /**
     * 効果音担当
     */
    private SoundEffectHandler seh = new SoundEffectHandler();

    /**
     * 視覚的ボリューム担当
     */
    private VisualVolumeHandler vvh = new VisualVolumeHandler();

    /**
     * 保存担当
     */
    private QRecStorage storage = new QRecStorage();

    /**
     * 録音担当
     */
    private RecordHandler record = new RecordHandler(storage);

    /**
     * 再生担当
     */
    private PlayHandler play = new PlayHandler(storage);

    private AnalyticsHandler ah = new AnalyticsHandler();


    /**
     * 録音可能時間
     */
    private Runnable timeLimitTask = new Runnable() {
        @Override
        public void run() {
            if (record.isIncludeSound()) {
                status = QRecStatus.STOPPING_RECORD;
            } else {
                seh.delete();
                status = QRecStatus.DELETE_RECORDING;
            }
            if (cb != null)
                cb.onShowWarningMessage(R.string.warning_time_limit);
            update();
        }
    };

    /**
     * 遅延タスク
     */
    private Runnable delayTask = new Runnable() {
        @Override
        public void run() {
        }
    };

    /**
     * 現在の状態
     */
    private QRecStatus status = QRecStatus.INIT;


    private MainCallback cb = null;


    @Override
    public void onCreate() {
        super.onCreate();
        BWU.log("MainService#onCreate");
        //終了タスクを予約
        handler.postDelayed(endTask, END_TIME);
        //設定を反映
        onSettingUpdated();
        //視覚的ボリューム
        vvh.onCreate(this, new VisualVolumeHandler.Callback() {
            @Override
            public float getRecordVisualVolume() {
                return record.getVisualVolume();
            }

            @Override
            public float getPlayVisualVolume() {
                return play.getVisualVolume();
            }

            @Override
            public void onUpdateVolume(float volume) {
                if (cb != null) {
                    cb.onUpdateVolume(volume);
                }
            }
        });
        record.onResume();
        play.onResume();
        vvh.onResume();
        //分析
        ah.onCreate(this);
        update();
    }

    /**
     * コールバックを設定する
     *
     * @param cb
     */
    public void setCallback(MainCallback cb) {
        handler.removeCallbacks(endTask);
        if (cb == null) {
            handler.postDelayed(endTask, END_TIME);
        } else {
            cb.onUpdateStatus(false, status);
            cb.onUpdateVolume(0);
        }
        this.cb = cb;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        BWU.log("MainService#onDestroy");
        handler.removeCallbacks(timeLimitTask);
        record.onPause();
        play.onPause();
        status = QRecStatus.READY_FIRST;
        vvh.onPause();
        seh.onDestroy();
        handler.removeCallbacks(delayTask);
    }

    /**
     * 戻るボタンが押された時に呼ばれる
     *
     * @return
     */
    public boolean onBackPressed() {
        if (status == QRecStatus.RECORDING ||
                status == QRecStatus.STOPPING_RECORD) {
            seh.delete();
            status = QRecStatus.DELETE_RECORDING;
            update();
            return true;
        } else if (status == QRecStatus.STOP ||
                status == QRecStatus.PLAYING ||
                status == QRecStatus.STOPPING_PLAYING) {
            seh.delete();
            status = QRecStatus.DELETE_PLAYING;
            update();
            return true;
        } else {
            return false;
        }
    }


    public void onSettingUpdated() {
        //設定が更新された時に呼ばれる
        seh.setEnabled(SettingFragment.isSoundEffect(this));
    }

    /**
     * 録音ボタンがクリックされた時のイベント
     */
    public void onRecordClicked() {
        if (status == QRecStatus.READY_FIRST ||
                status == QRecStatus.READY) {
            status = QRecStatus.STARTING_RECORD;
            update();
        } else if (status == QRecStatus.STOP) {
            status = QRecStatus.STARTING_RECORD;
            update();
        } else if (status == QRecStatus.PLAYING) {
            status = QRecStatus.STARTING_RECORD;
            update();
        }
    }

    /**
     * 再生ボタンがクリックされた時のイベント
     */
    public void onPlayClicked() {
        if (status == QRecStatus.RECORDING) {
            if (record.isIncludeSound()) {
                status = QRecStatus.STOPPING_RECORD;
            } else {
                seh.delete();
                status = QRecStatus.DELETE_RECORDING;
                if (cb != null) {
                    cb.onShowWarningMessage(R.string.warning_no_sound);
                }
            }
            update();
        }
    }

    /**
     * 再再生ボタンがクリックされた時のイベント
     */
    public void onReplayClicked() {
        if (status == QRecStatus.PLAYING ||
                status == QRecStatus.STOP) {
            status = QRecStatus.PLAYING;
            update();
        }
    }

    /**
     * 停止ボタンがクリックされた時のイベント
     */
    public void onStopClicked() {
        if (status == QRecStatus.PLAYING) {
            status = QRecStatus.STOPPING_PLAYING;
            update();
            ah.sendAction("stop", storage.getLength() / 44100);
        }
    }

    /**
     * 削除ボタンがクリックされた時のイベント
     */
    public void onDeleteClicked() {
        if (status == QRecStatus.RECORDING) {
            seh.delete();
            status = QRecStatus.DELETE_RECORDING;
            update();
        } else if (status == QRecStatus.PLAYING ||
                status == QRecStatus.STOP) {
            seh.delete();
            status = QRecStatus.DELETE_PLAYING;
            update();
        }
    }

    /**
     * 状態に応じて更新する
     */
    private void update() {
        if (status == QRecStatus.INIT) {
            //効果音読み込み
            seh.onCreate(this, new Runnable() {
                @Override
                public void run() {
                    //初期化完了
                    status = QRecStatus.READY_FIRST;
                    update();
                }
            });
        } else if (status == QRecStatus.DELETE_RECORDING ||
                status == QRecStatus.DELETE_PLAYING) {
            //録音済生取り消し
            play.stop();
            record.stop();
            //タイムリミットタスク解除
            handler.removeCallbacks(timeLimitTask);
            //Analytics
            ah.sendAction("delete", storage.getLength() / 44100);
            //削除→録音可能
            this.delayTask = new Runnable() {
                @Override
                public void run() {
                    status = QRecStatus.READY;
                    update();
                }
            };
            handler.postDelayed(delayTask, 200);
        } else if (status == QRecStatus.READY_FIRST ||
                status == QRecStatus.READY) {
            play.stop();
            record.stop();
        } else if (status == QRecStatus.STOP) {
            //視覚的ボリューム
            vvh.stop();
        } else if (status == QRecStatus.STARTING_RECORD) {
            //音を鳴らす
            seh.start();
            //再生終了
            play.stop();
            //視覚的ボリュームをリセット
            vvh.reset();
            //効果音が鳴り終わったら録音開始
            delayTask = new Runnable() {
                @Override
                public void run() {
                    status = QRecStatus.RECORDING;
                    update();
                }
            };
            handler.postDelayed(delayTask, 500);
            //イベントを送る
            ah.sendAction("record");
        } else if (status == QRecStatus.RECORDING) {
            //録音開始
            record.start();
            //タイムリミットタスク予約
            handler.postDelayed(timeLimitTask, TIME_LIMIT);
            //視覚的ボリューム
            vvh.record();
        } else if (status == QRecStatus.STOPPING_RECORD) {
            //録音終了
            record.stop();
            //タイムリミットタスク解除
            handler.removeCallbacks(timeLimitTask);
            //終了SE
            seh.play();
            //視覚的ボリュームをリセット
            vvh.reset();
            //効果音が鳴り終わったら再生開始
            delayTask = new Runnable() {
                @Override
                public void run() {
                    status = QRecStatus.PLAYING;
                    update();
                }
            };
            handler.postDelayed(delayTask, 550);
        } else if (status == QRecStatus.PLAYING) {
            //最後まで再生したイベント設定
            play.play(new Runnable() {
                @Override
                public void run() {
                    status = QRecStatus.STOPPING_PLAYING;
                    update();
                }
            });
            //視覚的ボリュームをリセット
            vvh.reset();
            //視覚的ボリューム
            vvh.play();
            //Analytics
            ah.sendAction("play", storage.getLength() / 44100);

        } else if (status == QRecStatus.STOPPING_PLAYING) {
            vvh.reset();
            play.stop();
            status = QRecStatus.STOP;
            update();
        }
        //状態更新をActivityに通知
        if (cb != null) {
            cb.onUpdateStatus(true, status);
        }
    }
}
