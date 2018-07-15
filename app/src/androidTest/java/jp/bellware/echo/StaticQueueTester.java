package jp.bellware.echo;

import junit.framework.TestCase;

import jp.bellware.echo.data.StaticQueue;

/**
 *
 */
public class StaticQueueTester extends TestCase {
    public void test(){
        StaticQueue<Integer> queue = new StaticQueue<Integer>(3);
        assertEquals(0,queue.size());
        //
        queue.add(1);
        assertEquals(1, queue.size());
        assertEquals(1,queue.get(0).intValue());
        //
        queue.add(2);
        assertEquals(2, queue.size());
        assertEquals(2,queue.get(0).intValue());
        assertEquals(1,queue.get(1).intValue());
        //
        queue.add(3);
        assertEquals(3, queue.size());
        assertEquals(3,queue.get(0).intValue());
        assertEquals(2,queue.get(1).intValue());
        assertEquals(1,queue.get(2).intValue());
        //
        queue.add(4);
        assertEquals(3, queue.size());
        assertEquals(4,queue.get(0).intValue());
        assertEquals(3,queue.get(1).intValue());
        assertEquals(2,queue.get(2).intValue());
    }
}
