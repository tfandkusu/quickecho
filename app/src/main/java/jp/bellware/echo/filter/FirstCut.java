package jp.bellware.echo.filter;

/**
 * 最初をカットする
 */
public class FirstCut {
    private final int first;
    private int index = 0;

    public FirstCut(int first) {
        this.first = first;
    }

    public float filter(float s){
        float d = 0;
        if(index >= first)
            d = s;
        ++index;
        return d;
    }

    public void reset() {
        index = 0;
    }
}
