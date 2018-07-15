package jp.bellware.echo.filter;

import jp.bellware.echo.data.StaticQueue;

/**
 * 一定の窓内における基本統計
 */
public class BasisStats {

    private StaticQueue<Float> queue;
    /**
     * 窓サイズ
     * @param window
     */
    public BasisStats(int window){
        this.queue = new StaticQueue<>(window);
    }


    public void filter(float s){
        this.queue.add(s);
    }

    private float average;

    private float max;

    private float sum;

    /**
     * 計算する
     */
    public void calculate(){
        max = Float.MIN_VALUE;
        sum = 0;
        for(int i = 0;i < queue.size();++i){
            float v = queue.get(i);
            sum += v;
            if(v > max)
                max = v;
        }
        average = sum/queue.getCapacity();
    }

    public float getAverage() {
        return average;
    }

    public float getMax() {
        return max;
    }

    public float getSum() {
        return sum;
    }

    public void reset(){
        queue.clear();
    }
}
