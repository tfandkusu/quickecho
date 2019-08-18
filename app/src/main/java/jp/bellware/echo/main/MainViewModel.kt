package jp.bellware.echo.main

import android.content.Context
import androidx.databinding.ObservableField
import android.view.View
import androidx.lifecycle.ViewModel
import jp.bellware.echo.R
import jp.bellware.util.BWU

/**
 * メイン画面のViewModel
 * @param context
 * @param listener リスナ
 */
class MainViewModel(private val context: Context) : ViewModel() {

    interface Listener {

        /**
         * 録音結果の削除が発生した時のイベント
         */
        fun onDeleteRecord()

        /**
         * 録音開始のイベント
         */
        fun onStartRecord()

        /**
         * 録音終了のイベント
         */
        fun onStopRecord()


        /**
         * 視覚的ボリュームを更新する
         */
        fun onUpdateVolume(volume: Float)

        /**
         * 警告メッセージを表示する
         */
        fun onShowWarningMessage(resId: Int)
    }

    private lateinit var listener: Listener

    /**
     * ステータス表示
     */
    val statusFrameVisibility = ObservableField<Int>()

    /**
     * 録音ボタン表示
     */
    val recordVisibility = ObservableField<Int>()

    /**
     * 再生ボタン表示
     */
    val playVisibility = ObservableField<Int>()

    /**
     * 再再生ボタン表示
     */
    val replayVisibility = ObservableField<Int>()

    /**
     * 停止ボタン表示
     */
    val stopVisibility = ObservableField<Int>()

    /**
     * 削除ボタン表示
     */
    val deleteVisibility = ObservableField<Int>()

    /**
     * ステータス表示
     */
    val statusImageSrc = ObservableField<Int>(R.drawable.microphone_48dp)

    /**
     * メインサービス
     */
    private val helper = MainHelper(context)

    private val mainCB = object : MainHelperCallback {
        override fun onUpdateStatus(animation: Boolean, status: QRecStatus) {
            BWU.log("MainActivity#onUpdateStatus $status")
            //ステータスビュー
            if (status == QRecStatus.INIT) {
                //初期化中
                //何も表示しない
                statusFrameVisibility.set(View.INVISIBLE)
                recordVisibility.set(View.INVISIBLE)
                playVisibility.set(View.INVISIBLE)
                replayVisibility.set(View.INVISIBLE)
                stopVisibility.set(View.INVISIBLE)
                deleteVisibility.set(View.INVISIBLE)
            } else if (status == QRecStatus.DELETE_RECORDING || status == QRecStatus.DELETE_PLAYING) {
                //アニメーション開始
                if (animation) {
                    listener.onDeleteRecord()
                } else {
                    statusFrameVisibility.set(View.INVISIBLE)
                    recordVisibility.set(View.VISIBLE)
                    playVisibility.set(View.INVISIBLE)
                    replayVisibility.set(View.INVISIBLE)
                    stopVisibility.set(View.INVISIBLE)
                    deleteVisibility.set(View.INVISIBLE)
                }
            } else if (status == QRecStatus.READY_FIRST || status == QRecStatus.READY) {
                //録音ボタンにする
                statusFrameVisibility.set(View.INVISIBLE)
                recordVisibility.set(View.VISIBLE)
                playVisibility.set(View.INVISIBLE)
                replayVisibility.set(View.INVISIBLE)
                stopVisibility.set(View.INVISIBLE)
                deleteVisibility.set(View.INVISIBLE)
            } else if (status == QRecStatus.STOP) {
                //停止状態
                statusFrameVisibility.set(View.VISIBLE)
                statusImageSrc.set(R.drawable.speaker_48dp)
                recordVisibility.set(View.VISIBLE)
                playVisibility.set(View.INVISIBLE)
                replayVisibility.set(View.VISIBLE)
                stopVisibility.set(View.VISIBLE)
                deleteVisibility.set(View.VISIBLE)
            } else if (status == QRecStatus.STARTING_RECORD) {
                //スピーカーアイコン表示
                statusImageSrc.set(R.drawable.microphone_48dp)
                //再生ボタンにする
                recordVisibility.set(View.INVISIBLE)
                playVisibility.set(View.VISIBLE)
                //サブコントロールは非表示
                replayVisibility.set(View.INVISIBLE)
                stopVisibility.set(View.INVISIBLE)
                if (animation) {
                    listener.onStartRecord()
                } else {
                    statusFrameVisibility.set(View.VISIBLE)
                    deleteVisibility.set(View.VISIBLE)
                }

            } else if (status == QRecStatus.RECORDING) {
                statusFrameVisibility.set(View.VISIBLE)
                statusImageSrc.set(R.drawable.microphone_48dp)
                recordVisibility.set(View.INVISIBLE)
                playVisibility.set(View.VISIBLE)
                replayVisibility.set(View.INVISIBLE)
                stopVisibility.set(View.INVISIBLE)
                deleteVisibility.set(View.VISIBLE)
            } else if (status == QRecStatus.STOPPING_RECORD) {
                //スピーカーを表示
                statusImageSrc.set(R.drawable.speaker_48dp)
                //録音ボタンにする
                recordVisibility.set(View.VISIBLE)
                playVisibility.set(View.INVISIBLE)
                if (animation) {
                    listener.onStopRecord()
                } else {
                    statusFrameVisibility.set(View.VISIBLE)
                    replayVisibility.set(View.VISIBLE)
                    stopVisibility.set(View.VISIBLE)
                }
            } else if (status == QRecStatus.PLAYING) {
                //再生中
                statusFrameVisibility.set(View.VISIBLE)
                statusImageSrc.set(R.drawable.speaker_48dp)
                recordVisibility.set(View.VISIBLE)
                playVisibility.set(View.INVISIBLE)
                replayVisibility.set(View.VISIBLE)
                stopVisibility.set(View.VISIBLE)
                deleteVisibility.set(View.VISIBLE)
            }
        }

        override fun onUpdateVolume(volume: Float) {
            listener.onUpdateVolume(volume)
        }

        override fun onShowWarningMessage(resId: Int) {
            listener.onShowWarningMessage(resId)
        }

    }

    override fun onCleared() {
        helper.onDestroy()
    }

    private var createdFlag = false

    /**
     * ActivityのonCreateから呼ばれる
     */
    fun onCreate(listener: Listener) {
        if(!createdFlag) {
            helper.onCreate()
            helper.setCallback(mainCB)
            createdFlag = true
        }
        this.listener = listener

    }

    /**
     * ActivityのonStartから呼ばれる
     */
    fun onStart() {
    }

    /**
     * ActivityのonStopから呼ばれる
     */
    fun onStop() {
    }

    /**
     * ActivityのonDestroyから呼ばれる
     */
    fun onDestroy() {
    }


    /**
     * 戻るボタンが押された
     */
    fun onBackPressed(): Boolean {
        return helper.onBackPressed()
    }

    /**
     * 設定が更新された時に呼ばれる
     */
    fun onSettingUpdate() {
        helper.onSettingUpdated()
    }

    /**
     * 録音ボタンが押された
     */
    fun onRecordClicked(view: View) {
        helper.onRecord()
    }

    /**
     * 再生ボタンが押された
     */
    fun onPlayClicked(view: View) {
        helper.onPlay()
    }

    /**
     * 再再生ボタンが押された
     */
    fun onReplayClicked(view: View) {
        helper.onReplay()
    }

    /**
     * 停止ボタンが押された
     */
    fun onStopClicked(view: View) {
        helper.onStop()
    }

    /**
     * 削除ボタンが押された
     */
    fun onDeleteClicked(view: View) {
        helper.onDelete()
    }

}