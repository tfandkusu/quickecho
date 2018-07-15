package jp.bellware.echo.filter;


/**
 * ゼロ点クロスを用いた視覚的ボリューム
 */
public class ZeroCrossRecordVisualVolumeProcessor implements VisualVolumeProcessor {
    /**
     * ゼロクロス担当
     */
    private ZeroCross zc = new ZeroCross();

    /**
     * 現在ボリューム取得のためのサイズ
     */
    private int PACKET_SIZE = 441;

    /**
     * ボリュームが増える最大速度
     */
    private float upSpeed = 0.03f;

    /**
     * ボリュームが減る最大速度
     */
    private float downSpeed = 0.03f;

    /**
     * ボリューム基準
     */
    private float base = 0;

    /**
     * 現在ボリューム
     */
    private float volume = 0;

    /**
     * サンプルインデックス
     */
    private int index = 0;

    /**
     * 音声を含んでいるフラグ
     */
    private boolean include  = false;

    /**
     * ボリューム計測用
     */
    private BasisStats packet = new BasisStats(PACKET_SIZE);



    @Override
    public void reset() {
        zc.reset();
        volume = 0;
        index = 0;
        base = 0;
        include = false;
        packet.reset();
    }

    @Override
    public void filter(float s) {
        zc.filter(s);
        //絶対値にする
        s = Math.abs(s);
        packet.filter(s);
        if (index % PACKET_SIZE == PACKET_SIZE - 1) {
            /**
             * 現在ボリューム
             */
            float cv;
            if (zc.isSpeaking()) {
                include = true;
                packet.calculate();
                //入力中
                if(base == 0){
                    base = packet.getMax();
                    if(base == 0)
                        base = 0.01f;
                }
                cv = packet.getMax() / base;
            } else {
                //無音
                cv = 0;
            }
            if(cv  < volume - downSpeed){
                volume = volume - downSpeed;
            }else if(cv > volume + upSpeed) {
                volume = volume + upSpeed;
            }else{
                volume = cv;
            }
            if(volume < 0)
                volume = 0;
            else if(volume > 1)
                volume = 1;
        }
        ++index;
    }

    @Override
    public float getVolume() {
        //使わなくなった。
        return volume;
    }

    @Override
    public boolean isIncludeSound() {
        //ここだけ使う
        return include;
    }
}
