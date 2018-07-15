package jp.bellware.echo.data;

import android.os.Environment;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

/**
 * 録音音声を保持する
 */
public class QRecStorage {

    private int packetSize;

    /**
     * 合計長さ
     */
    private int length;


    /**
     *
     */
    private List<float[]> packets = new ArrayList<float[]>();

    /**
     * 録音出来たボリューム
     */
    private float gain = 0;

    /**
     * shortの最大値に1を足したもの
     */
    private int SHORT_MAX_P1 = 32768;


    public synchronized void clear(){
        packetSize = 0;
        gain = 0;
        packets.clear();
        length = 0;
    }

    /**
     * flaot型に変換する
     * @param data
     * @return
     */
    public float[] convert(short[] data){
        float[] fd = new float[data.length];
        for(int i = 0;i < data.length; ++i){
            fd[i] = ((float)data[i])/SHORT_MAX_P1;
        }
        return fd;
    }

    /**
     * short型に変換する
     * @param data
     * @return
     */
    public short[] convert(float[] data){
        short[] sd = new short[data.length];
        for(int i = 0;i < data.length; ++i){
            sd[i] = (short) (data[i]*Short.MAX_VALUE);
        }
        return sd;
    }

    public synchronized void add(float[] data){
        if(data.length > packetSize)
            packetSize = data.length;
        packets.add(data);
        length += data.length;
    }


    public synchronized float[] get(int index){
        if(index >= packets.size())
            return null;
        float[] packet = packets.get(index);
        return packet;
    }

    public synchronized int  getPacketSize() {
        return packetSize;
    }

    public float getGain(){
        return gain;
    }

    public void setGain(float gain) {
        this.gain = gain;
    }

    public synchronized int getLength() {
        return length;
    }

    /**
     * デバッグ用にRawファイルを保存する
     */
    public void save(){
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/output.wav");
            //ヘッダ書き込み
            ByteBuffer buffer = ByteBuffer.allocate(44);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            buffer.put("RIFF".getBytes());
            buffer.putInt(44 - 8 + length*2);//これ以降のファイルサイズ
            buffer.put("WAVE".getBytes());
            buffer.put("fmt ".getBytes());
            buffer.putInt(16);//fmtチャンクのバイト数
            buffer.putShort((short) 1);//リニアPCM
            buffer.putShort((short) 1);//モノラル
            buffer.putInt(44100);//サンプリングレート
            buffer.putInt(88200);//データ速度
            buffer.putShort((short) 2);//ブロックサイズ
            buffer.putShort((short) 16);//サンプルあたりビット数
            buffer.put("data".getBytes());//dataチャンク
            buffer.putInt(length * 2);//波形データのバイト数
            fos.write(buffer.array());
            //データ書き込み
            for(float[] packet : packets){
                ByteBuffer data = ByteBuffer.allocate(packet.length*2);
                data.order(ByteOrder.LITTLE_ENDIAN);
                for(int i = 0;i < packet.length;++i) {
                    short s = (short) (packet[i]*Short.MAX_VALUE);
                    data.putShort(s);
                }
                fos.write(data.array());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
