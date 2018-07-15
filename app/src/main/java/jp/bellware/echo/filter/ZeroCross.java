package jp.bellware.echo.filter;

import jp.bellware.echo.data.StaticQueue;
import jp.bellware.util.BWU;

/**
 * ゼロクロスを計算する
 */
public class ZeroCross {

    /**
     * topインデックスとボトムインデックスの差
     */
    private static final int WINDOW_SIZE = 4410;

    /**
     * 振幅基準値
     */
    private static final float AMP = 0.01f;

    /**
     * クロス基準値
     */
    private static final int CROSS = 10;

    private StaticQueue<Float> queue = new StaticQueue<>(WINDOW_SIZE);

    /**
     * トップインデックス
     */
    private int top = 0;

    /**
     * ボトムインデックス
     */
    private int bottom = 0;

    /**
     * トップの前サンプル符号
     */
    private int topSign = 0;

    /**
     * ボトムの前サンプル符号
     */
    private int bottomSign = 0;

    /**
     * 零点クロス回数
     */
    private int cross = 0;

    public void reset() {
        top = 0;
        bottom = 0;
        topSign = 0;
        bottomSign = 0;
        cross = 0;
    }

    public void filter(float s) {
        //トップについて
        //クロス判定
        if(topSign == 1 && s < 0) {
            ++cross;
            topSign = 0;
        }
        else if(topSign == -1 && s > 0) {
            ++cross;
            topSign = 0;
        }
        //符号
        if(s < -AMP)
            topSign = -1;
        else if(s > AMP)
            topSign = 1;
        //ボトムについて
        bottom = top - WINDOW_SIZE;
        if(bottom >= 0){
            float t = queue.get(queue.size() - 1);
            //クロス判定
            if(bottomSign == 1 && t < 0) {
                --cross;
                bottomSign = 0;
            }
            else if(bottomSign == -1 && t > 0) {
                --cross;
                bottomSign = 0;
            }
            //符号
            if(t < -AMP)
                bottomSign = -1;
            else if(t > AMP)
                bottomSign = 1;
        }
        //キューに追加
        queue.add(s);
        top++;
    }

    /**
     * ゼロクロス
     * @return
     */
    public int getCross(){
        return cross;
    }

    /**
     * 音声入力中
     * @return
     */
    public boolean isSpeaking(){
        return cross >= CROSS;
    }

}
