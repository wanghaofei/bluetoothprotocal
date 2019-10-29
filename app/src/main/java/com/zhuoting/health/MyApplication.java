//package com.zhuoting.health;
//
//import android.app.Activity;
//import android.app.Application;
//import android.os.Bundle;
//import android.os.Environment;
//import android.support.multidex.MultiDexApplication;
//import android.util.Log;
//
////import com.crashlytics.android.Crashlytics;
////import com.zhuoting.health.observer.SubObserver;
////import com.zhuoting.health.service.MyBleService;
////import com.zhuoting.health.service.NotificationMonitor;
////import com.zhuoting.health.tools.BleHandler;
//
//import com.inuker.bluetooth.library.BluetoothContext;
//import com.zhuoting.health.util.DataUtil;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//
////import io.fabric.sdk.android.Fabric;
//
//public class MyApplication extends MultiDexApplication {
//
//
//    private static final String TAG = "MyApplication";
//    public List<String> devList = new ArrayList<>();
//
//    private static MyApplication instance;
//    public static boolean isBackground = false;
//
//    public static boolean isSyncing = false;
//
//    public static MyApplication getInstance() {
//        return instance;
//    }
//
//    int mFinalCount;
//
//    public static String fileName = Environment.getExternalStorageDirectory() + "/MecareMsg.txt";
//    String fileName2 = Environment.getExternalStorageDirectory() + "/Noticetest.txt";
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        Log.e("Heal","onCreate");
////        SubObserver.getInstance();
////        BleHandler.getInstance(this);
//        instance = this;
//        BluetoothContext.set(this);
//        File file = new File(fileName);
//        if (file.exists()) {
//     //       file.delete();
//
//        }
////        file = new File(fileName2);
////        if (file.exists()) {
////            file.delete();
////        }
//
////		Tools.saveBleAddress(null,this);
////		BleHandler.getInstance(this).disBle();
//
////        Fabric.with(this, new Crashlytics());
////        Intent intent = new Intent(this, MyBleService.class);
////        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////        startService(intent);
//
////		int a = initHeart(150);
////		System.out.println("xxxx=="+a);
//   //     toggleNotificationListenerService();
//
//        instance = this;
//
////		byte[] test = new byte[] {0x03,0x01};
////		int xx = crc16JNI(test);
////		byte[] testaa = new byte[2];
////		testaa[0] = (byte)xx;
////		testaa[1] = (byte)(xx >> 8);
//        //byte[] crc = Tools.makeCRC16(test);
////		Log.i("xxxzz==",Tools.logbyte(testaa));
//
//
//        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
//            @Override
//            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
//
//            }
//
//            @Override
//            public void onActivityStarted(Activity activity) {
//                mFinalCount++;
//                //如果mFinalCount ==1，说明是从后台到前台
//                Log.e("onActivityStarted", mFinalCount + "");
//                if (mFinalCount == 1) {
//                    //说明从后台回到了前台
////					Tools.saveBack(false,getApplicationContext());
////                    BleHandler.getInstance(getApplicationContext()).changeBack(false);
////                    BleHandler.getInstance(getApplicationContext()).clearCache();
//                    Log.i(TAG, " 返回到了 前台");
//                    MyApplication.isBackground = false;
//                }
//            }
//
//            @Override
//            public void onActivityResumed(Activity activity) {
//
//            }
//
//            @Override
//            public void onActivityPaused(Activity activity) {
//
//            }
//
//            @Override
//            public void onActivityStopped(Activity activity) {
//                mFinalCount--;
//                //如果mFinalCount ==0，说明是前台到后台
//
//                Log.i("onActivityStopped", mFinalCount + "");
//                if (mFinalCount == 0) {
//                    //说明从前台回到了后台
////					Tools.saveBack(true,getApplicationContext());
//                    Log.i(TAG, " 切换到了 后台");
////                    BleHandler.getInstance(getApplicationContext()).changeBack(true);
//                    MyApplication.isBackground = true;
//                }
//            }
//
//            @Override
//            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
//
//            }
//
//            @Override
//            public void onActivityDestroyed(Activity activity) {
//
//            }
//        });
//
//    }
//
//    //重新开启NotificationMonitor
////    private void toggleNotificationListenerService() {
////        ComponentName thisComponent = new ComponentName(this, NotificationMonitor.class);
////        PackageManager pm = getPackageManager();
////        pm.setComponentEnabledSetting(thisComponent, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
////        pm.setComponentEnabledSetting(thisComponent, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
////
////    }
//
//    private static String getCrc(byte[] data) {
//        int high;
//        int flag;
//
//        // 16位寄存器，所有数位均为1
//        int wcrc = 0xffff;
//        for (int i = 0; i < data.length; i++) {
//            // 16 位寄存器的高位字节
//            high = wcrc >> 8;
//            // 取被校验串的一个字节与 16 位寄存器的高位字节进行“异或”运算
//            wcrc = high ^ data[i];
//
//            for (int j = 0; j < 8; j++) {
//                flag = wcrc & 0x0001;
//                // 把这个 16 寄存器向右移一位
//                wcrc = wcrc >> 1;
//                // 若向右(标记位)移出的数位是 1,则生成多项式 1010 0000 0000 0001 和这个寄存器进行“异或”运算
//                if (flag == 1)
//                    wcrc ^= 0xa001;
//            }
//        }
//
//        return Integer.toHexString(wcrc);
//    }
//
//    @Override
//    public void onTerminate() {
//        // TODO Auto-generated method stub
//        super.onTerminate();
//
////   		intent22.putExtra("type", 30020);
////		sendBroadcast(intent22);
//
//
//    }
//
//    public void uncaughtException(Thread thread, Throwable ex) {
//        System.out.println("uncaughtException");
////        intent22.putExtra("type", 30020);
//// 		sendBroadcast(intent22);
//    }
//
////    public byte[] makeCRC(byte[] msg) {
////        Log.e("MyApp","msg : "+ DataUtil.byteToHexString(msg));
////        int xx = crc16JNI(msg);
////
////        byte[] testaa = new byte[2];
////        testaa[0] = (byte) xx;
////        testaa[1] = (byte) (xx >> 8);
////
////        return testaa;
////    }
////
////
////    public void initHeartHz(int val,boolean val2) {
////        initHeart(val,val2);
////    }
////
////
////    public int makeHeartVal(int val) {
////        return makeValue(val);
////    }
//
//    /**
//     * A native method that is implemented by the 'native-lib' native library,
//     * which is packaged with this application.
//     */
////    public native int crc16JNI(byte[] msg);
////
////    public native int initHeart(int a,boolean b);
////
////    public native int makeValue(int a);
//
//    // Used to load the 'native-lib' library on application startup.
//    static {
//        System.loadLibrary("native_lib");//crc16JNI
//    }
//}
