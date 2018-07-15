package jp.bellware.echo;

import jp.bellware.echo.filter.VisualVolumeProcessor;
import jp.bellware.echo.filter.ZeroCross;
import jp.bellware.echo.filter.ZeroCrossRecordVisualVolumeProcessor;
import junit.framework.TestCase;

/**
 * Created by toya on 15/10/24.
 */
public class ZeroCrossTester extends TestCase{
    public void testZero1(){
        ZeroCross zc = new ZeroCross();
        for(int i = 0; i < 10000;++i){
            zc.filter(0);
        }
        assertEquals(0, zc.getCross());
    }

    public void testZero2(){
        ZeroCross zc = new ZeroCross();
        for(int i = 0; i < 10000;++i){
            if(i % 2 == 0)
                zc.filter(0.005f);
            else
                zc.filter(-0.005f);
        }
        assertEquals(0,zc.getCross());
    }

    public void testSome(){
        ZeroCross zc = new ZeroCross();
        for(int i = 0; i < 5000;++i){
            if(i == 1)
                zc.filter(0.2f);
            else if(i == 2)
                zc.filter(-0.2f);
            else if(i == 1000)
                zc.filter(0.2f);
            else if(i == 1001)
                zc.filter(-0.2f);
            else if(i == 2000)
                zc.filter(0.005f);
            else if(i == 2001)
                zc.filter(-0.005f);
            else if(i == 3000)
                zc.filter(0.2f);
            else if(i == 3001)
                zc.filter(-0.2f);
            else
                zc.filter(0);
        }
        assertEquals(4,zc.getCross());
    }

}
