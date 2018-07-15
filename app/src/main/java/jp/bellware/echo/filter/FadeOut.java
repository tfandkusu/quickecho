package jp.bellware.echo.filter;

/**
 * フェードアウト
 */
public class FadeOut {

    /**
     * 全体の長さ
     */
    private int length;

    /**
     * フェードアウト区間
     */
    private int duration;

    /**
     * 現在処理位置
     */
    private int index;

    public FadeOut(int length, int duration) {
        this.length = length;
        this.duration = duration;
    }

    public float filter(float s){
        float v;
        if(index > length - duration){
            v = s*(length - index)/duration;
        }else{
            v = s;
        }
        index += 1;
        return v;
    }
}
