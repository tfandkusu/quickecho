package jp.bellware.echo.data;

import java.util.ArrayList;
import java.util.List;

/**
 * 一定サイズを超えないキュー。
 */
public class StaticQueue<T> {
    /**
     * キャパシティー
     */
    private int capacity;
    private int offset;
    private List<T> array;

    private int size;

    public StaticQueue(int capacity) {
        this.capacity = capacity;
        init();
    }

    private void init() {
        array = new ArrayList<T>();
        for (int i = 0; i < capacity; ++i) {
            array.add(null);
        }
        offset = 0;
        size = 0;
    }

    public void clear(){
        init();
    }


    public void add(T v) {
        array.set(offset, v);
        ++offset;
        if (size < capacity)
            ++size;
        if (capacity <= offset)
            offset = 0;
    }

    /**
     * 値を取得する。インデックスが大きい方が古い。
     *
     * @param i
     * @return
     */
    public T get(int i) {
        int index = offset - i - 1;
        if (index < 0)
            index += array.size();
        return array.get(index);
    }

    public int size() {
        return size;
    }

    public int getCapacity() {
        return capacity;
    }

}
