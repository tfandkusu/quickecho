package jp.bellware.echo.main;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;


import android.os.Handler;
import jp.bellware.echo.data.QRecStorage;
import jp.bellware.echo.filter.FirstCut;
import jp.bellware.echo.filter.GainDetector;
import jp.bellware.echo.filter.VisualVolumeProcessor;
import jp.bellware.echo.filter.ZeroCrossRecordVisualVolumeProcessor;
import jp.bellware.util.BWU;

/**
 * 録音担当
 */
public class RecordHandler {
    /**
     * サンプル数
     */
    public static final int SAMPLE_RATE = 44100;

    /**
     * 視覚的ボリュームの適用範囲外サンプル数
     */
    public static final int FC = 2*SAMPLE_RATE/10;

    /**
     *
     */
    private final QRecStorage storage;

    private Handler handler = new Handler();

    /**
     *
     */
    private AudioRecord record;


    /**
     * バッファサイズ
     */
    private int packetSize = 0;
    /**
     * 録音スレッド
     */
    private Thread thread;

    /**
     * 最初をカットする
     */
    private FirstCut fc = new FirstCut(FC);


    private VisualVolumeProcessor vvp = new ZeroCrossRecordVisualVolumeProcessor();

    private GainDetector gd = new GainDetector();

    /**
     * 録音中フラグ
     */
    private boolean recording = false;

    public RecordHandler(QRecStorage storage) {
        this.storage = storage;
    }

    public void onResume() {
        int recordMinBufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT);
        int playMinBufferSize = AudioTrack.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT);
        BWU.log("RecordHandler#onResume recordMinBufferSize = " + recordMinBufferSize);
        BWU.log("RecordHandler#onResume playMinBufferSize = " + playMinBufferSize);
        packetSize = Math.max(recordMinBufferSize,playMinBufferSize);
        record = new AudioRecord(MediaRecorder.AudioSource.DEFAULT, SAMPLE_RATE,
                AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, packetSize*2);
        record.startRecording();
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //これがないと音が途切れる
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
                while(true) {
                    short[] data = new short[packetSize];
                    int size = record.read(data, 0, packetSize);
                    if (size >= 1) {
                       addPacket(data);
                    } else {
                        break;
                    }
                }
                BWU.log("RecordHandler#onResume RecordThread end");
            }
        });
        thread.start();
    }


    public void onPause(){
        //これを呼び出すとreadメソッドの返り値が0となる
        record.stop();
        if(thread != null) {
            try {
                thread.join();
            } catch (InterruptedException e) {
            }
        }
        record.release();
        record = null;
        clear();
    }

    public synchronized void start(){
        recording = true;
        vvp.reset();
        gd.reset();
        fc.reset();
        clear();
    }

    public synchronized void stop(){
        recording = false;
    }

    private synchronized void addPacket(short[] packet){
        if(recording) {
            float[] fd = storage.convert(packet);
            storage.add(fd);
            for(float s : fd){
                vvp.filter(fc.filter(s));
                gd.filter(s);
            }
            storage.setGain(gd.getMax());
        }
    }

    private synchronized void clear(){
        storage.clear();
    }


    public synchronized float getVisualVolume(){
        return vvp.getVolume();
    }

    public synchronized boolean isIncludeSound(){
        return vvp.isIncludeSound();
    }


}
