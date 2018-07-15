package jp.bellware.echo.main;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.*;
import android.os.Process;

import java.util.Arrays;

import jp.bellware.echo.data.QRecStorage;
import jp.bellware.echo.filter.FadeOut;
import jp.bellware.echo.filter.FirstCut;
import jp.bellware.echo.filter.PlayVisualVolumeProcessor;

/**
 * 音声再生担当
 */
public class PlayHandler {
    private AudioTrack track;

    private final QRecStorage storage;

    private Thread thread;

    private boolean playing = false;

    private PlayVisualVolumeProcessor vvp = new PlayVisualVolumeProcessor();

    private Handler handler = new Handler();

    private FadeOut fo;

    private FirstCut fc = new FirstCut(RecordHandler.FC);


    /**
     * 再生パケットインデックス
     */
    private int index;

    public PlayHandler(QRecStorage storage) {
        this.storage = storage;
    }

    public void onResume() {
    }

    public void onPause() {
        stop();
    }

    /**
     * 再生する
     */
    public void play(final Runnable onEndListener) {
        stop();
        if (storage.getLength() == 0) {
            //長さ0の時はすぐに終わる
            onEndListener.run();
            return;
        }
        track = new AudioTrack(AudioManager.STREAM_MUSIC,
                RecordHandler.SAMPLE_RATE,
                AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT,
                storage.getPacketSize() * 2, AudioTrack.MODE_STREAM);
        playing = true;
        //最初のパケットは効果音が入っていることがあるので捨てる
        index = 1;
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //これがないと音が途切れる
                android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO);
                if (false) {
                    //デバッグ用セーブ
                    storage.save();
                }

                fo = new FadeOut(storage.getLength(), RecordHandler.SAMPLE_RATE * 3 / 10);
                fc.reset();
                track.play();
                try {
                    Thread.sleep(storage.getPacketSize() * 1000 / 44100);
                } catch (InterruptedException e) {
                }
                while (true) {
                    float[] packet = pullPacket(onEndListener);
                    if (packet != null) {
                        filter(packet);
                        short[] sd = storage.convert(packet);
                        int result = track.write(sd, 0, sd.length);
                        if (result < 0)
                            break;
                    } else {
                        break;
                    }
                }
            }
        });

        thread.start();
    }

    private float[] pullPacket(final Runnable onEndListener) {
        if (playing) {
            float[] packet = storage.get(index);
            ++index;
            if (packet == null) {
                //終端
                if (onEndListener != null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            onEndListener.run();
                        }
                    });
                }
                return null;
            } else {
                return Arrays.copyOf(packet,packet.length);
            }
        } else
            return null;
    }


    /**
     * フィルターをかける
     *
     * @param packet
     */
    private synchronized void filter(float[] packet) {
        for (int i = 0; i < packet.length; ++i) {
            //ボリューム調整
            float s = packet[i];
            s = s / storage.getGain();
            //フェードアウト
            s = fo.filter(s);
            //視覚的ボリューム
            vvp.filter(fc.filter(s));
            //置き換え
            packet[i] = s;
        }

    }

    public synchronized float getVisualVolume() {
        return vvp.getVolume();
    }

    public void stop() {
        if (track != null) {
            playing = false;
            try {
                thread.join();
            } catch (InterruptedException e) {
            }
            track.release();
            track = null;
        }
    }


}
