package jp.bellware.echo.main;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import jp.bellware.echo.R;

/**
 * 効果音担当
 */
public class SoundEffectHandler {


    private SoundPool soundPool;

    /**
     * 効果音有効フラグ
     */
    private boolean enabled = true;

    /**
     * 録音開始効果音ID
     */
    private int startId;

    /**
     * 録音終了効果音ID
     */
    private int playId;

    /**
     * 削除効果音ID
     */
    private int deleteId;

    @SuppressWarnings("deprecation")
    public void onCreate(Context context, final Runnable onLoadFinished){
        //サウンドプール
        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            private int count;
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                ++count;
                if(count >= 3)
                    onLoadFinished.run();
            }
        });
        startId = soundPool.load(context, R.raw.start, 1);
        playId = soundPool.load(context, R.raw.play, 1);
        deleteId = soundPool.load(context,R.raw.delete,1);
    }

    public void start(){
        play(startId);
    }

    public void play(){
        play(playId);
    }

    public void delete() {
        play(deleteId);
    }

    public void onDestroy(){
        soundPool.release();
    }

    private void play(int id){
        if(isEnabled()) {
            soundPool.play(id, 1, 1, 0, 0, 1);
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
