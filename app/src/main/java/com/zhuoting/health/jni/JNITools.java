package com.zhuoting.health.jni;

import com.zhuoting.health.write.ProtocolAIWriter;

public class JNITools {


    private static JNITools jniTools;

    public static JNITools newInstance() {                     // 单例模式，双重锁
        if (jniTools == null) {
            synchronized (ProtocolAIWriter.class) {
                if (jniTools == null) {
                    jniTools = new JNITools();
                }
            }
        }
        return jniTools;
    }

    static {
        System.loadLibrary("native_lib");
    }

    public native int crc16JNI(byte[] msg);

    public native int initHeart(int a,boolean b);

    public native int makeValue(int a);

    public int makeHeartVal(int val) {
        return makeValue(val);
    }

    public byte[] makeCRC(byte[] msg) {
        int xx = crc16JNI(msg);

        byte[] testaa = new byte[2];
        testaa[0] = (byte) xx;
        testaa[1] = (byte) (xx >> 8);

        return testaa;
    }
    public void initHeartHz(int val,boolean val2) {
        initHeart(val,val2);
    }

}
