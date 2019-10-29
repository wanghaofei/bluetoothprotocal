package com.zhuoting.health.parser;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.zhuoting.health.ProtocolTag;
import com.zhuoting.health.bean.BloodInfo;
import com.zhuoting.health.bean.ClockInfo;
import com.zhuoting.health.bean.HeartInfo;
import com.zhuoting.health.bean.SleepInfo;
import com.zhuoting.health.bean.SportInfo;
import com.zhuoting.health.notify.IDataReceiveComplete;
import com.zhuoting.health.notify.IDataResponse;
import com.zhuoting.health.notify.IErrorCommand;
import com.zhuoting.health.notify.IRequestResponse;
import com.zhuoting.health.util.DataUtil;
import com.zhuoting.health.util.Tools;
import com.zhuoting.health.util.TransUtils;
import com.zhuoting.health.write.ProtocolWriter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Hqs on 2018/1/4
 */
public class DataParser {


    public static boolean isSyncing = false;

    public IRequestResponse mIRequestResponse;
    public IErrorCommand mIErrorCommand;
    public IDataResponse mIDataResponse;
    public IOperation mIOperation;
    public IDataReceiveComplete mIDataReceiveComplete;
    private byte[] bytes;
    private byte[] sportsModeBytes;
    byte syncType;                                          // 同步类型数据，2运动
    int pageSize;                                               // 保存包大小
    int byteSize;                                                // 保存字节大小
    byte[] ravData = new byte[0];
    boolean isFront = false;
    int msgSize;

    //    static int ecg_dataCnt = 0;
    int sportNum;
    int sleepNum;
    int heartNum;
    int bloodNum;
    List<SportInfo> sportlist = new ArrayList<>();
    List<HeartInfo> heartlist = new ArrayList<>();
    List<BloodInfo> bloodlist = new ArrayList<>();
    List<SleepInfo> sleeplist = new ArrayList<>();
    List<Byte> ppgList = new ArrayList<>();
    List<Byte> ecgList = new ArrayList<>();
    List<Long> times = new ArrayList<>();
    private boolean isQueryTimes;
    private int type = 0;
    private int incrementCount = 1;

    private static boolean isStopUploadData = false;

//    public Boolean isSendingCollectData = false;

    public static final String TAG = "DataParser";

    public int historyType = 0;  //0 ECG, 1 PPG

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {  // 第一次进入app 如果 连接完成就开始同步
                isSyncing = true;
            }
            if (msg.what == 1) {
                isSyncing = true;
                sendMsg1(); //同步历史运动数据
            } else if (msg.what == 2) {
                isSyncing = true;
                sendMsg0();
            } else if (msg.what == 3) {
                isSyncing = true;
                sendMsg2(); //同步历史睡眠数据
            } else if (msg.what == 4) {
                isSyncing = true;
                sendMsg3(); //同步历史心率数据
            } else if (msg.what == 5) {
                isSyncing = true;
                sendMsg4();  //同步历史血压数据
            } else if (msg.what == 6) {
                if (isSyncing == false) {
                    return;
                }

                if (isFront == false) {
                    isSyncing = false;
                    return;
                }
                sendMsgOpen2();
                isSyncing = false;

            }
        }

    };
    private int num;
    private int record = 1;
    private int dataPacketNum = 0;
    byte[] ecgData = new byte[0];
    private int ecgDataLength;
    private int ecgPackageLength;


    public void resetEcgData() {
        isStopUploadData = false;
        ecgData = new byte[0];
        ecgDataLength = 0;
        record = 1;
        num = 0;
        sportNum = 0;
        sleepNum = 0;
        heartNum = 0;
        bloodNum = 0;
        historyType = 0;
        incrementCount = 1;
    }


    public void setRequestResponseListener(IRequestResponse iRequestResponse) {
        this.mIRequestResponse = iRequestResponse;
    }

    public void setDataResponseListener(IDataResponse iDataResponse) {
        this.mIDataResponse = iDataResponse;
    }

    public void setErrorCommandListener(IErrorCommand iErrorCommand) {
        this.mIErrorCommand = iErrorCommand;
    }

    public void setOperation(IOperation iOperation) {
        this.mIOperation = iOperation;
    }

    public void setDataReceiveComplete(IDataReceiveComplete mIDataReceiveComplete) {
        this.mIDataReceiveComplete = mIDataReceiveComplete;
    }

    private DataParser() {
    }

    private static DataParser mDataParser;

    public static DataParser newInstance() {                     // 单例模式，双重锁
        if (mDataParser == null) {
            synchronized (DataParser.class) {
                if (mDataParser == null) {
                    mDataParser = new DataParser();
                }
            }
        }
        return mDataParser;
    }

    public void parseData(byte[] data) {
        String dataStr = TransUtils.bytes2hex(data);
        String tagHead = dataStr.substring(0, 2);
        String tagStr = dataStr.substring(0, 4);

//        Log.e("DataParser2",Tools.logbyte(data));


//        Log.d("chen44", Tools.logbyte(data));
        if (tagHead.equals("05")) {
            if (tagStr.equals(ProtocolTag.SYNCHRO_ALL_SWITCH_RESPONSE)) {    // 同步所有开关
                mIRequestResponse.onSynchronizdAllSwitchResponse(data[4]);

            } else if (tagStr.equals(ProtocolTag.BLOCK_COMFIRM_RESPONSE)) {         // BLOCK 确认信息响应
                mIRequestResponse.onBlockConfirmResponse(data[4]);

            } else if (data[0] == 0x05 && (data[1] == 0x02 || data[1] == 0x04 || data[1] == 0x06 || data[1] == 0x08)) {
                syncType = data[1];     // 02 历史运动        04 历史睡眠       06 历史心率         08 历史血压
                if (data[2] == 0x08) {
                    // 长度为 8 ，表示同步完成
                    if (syncType == 0x02) {//处理运动数据
                        sportlist.clear();
                        mIDataResponse.onHistorySport(sportlist);

                    } else if (syncType == 0x04) {//处理睡眠数据
                        Log.d("chen44", "处理睡眠数据111");
                        sleeplist.clear();
                        mIDataResponse.onHistorySleep(sleeplist);
                        //无睡眠时回调

                    } else if (syncType == 0x06) {
                        //处理心率数据
                        heartlist.clear();
                        Log.d("chen66", "处理心率1");
                        mIDataResponse.onHistoryHeartRate(heartlist);

                    } else if (syncType == 0x08) {                          //处理血压数据
                        bloodlist.clear();
                        mIDataResponse.onHistoryBloodPressure(bloodlist);

                    }
                    return;
                }
                pageSize = 0;
                byte[] pageSizeB = {0x00, 0x00, data[5], data[4]};
                pageSize = TransUtils.Bytes2Dec(pageSizeB);
                byteSize = 0;
                byte[] bytebyte = {data[13], data[12], data[11], data[10]};
                Log.d("mmm", Tools.logbyte(bytebyte));
                byteSize = TransUtils.Bytes2Dec(bytebyte);
                Log.d("mmm", byteSize + "");
                if (pageSize == 0 && byteSize == 0) {               // pageSize 和 byteSize
                    return;
                }
            } else if (data[0] == 0x05 && (data[1] == 0x11 || data[1] == 0x13 || data[1] == 0x15 || data[1] == 0x17)) {//同步运动数据
                if (data.length > 6) {
                    int count = data.length - 6;
                    byte command[] = new byte[count];
                    for (int i = 0; i < count; i++) {
                        command[i] = data[i + 4];
                    }
                    ravData = Tools.byteMerger(ravData, command);
                    if (ravData.length == byteSize) {
                        mIOperation.onDoReceiveAllComplete();
                        perData();
                        initData();
                    }
                }
            } else if (tagStr.equals(ProtocolTag.SYNCHRO_TODAY_SPORT_DATA)) {     // 同步今天的运动数据
                if (data.length >= 20) {               // 如果字节数大于或者等于12个字节，则这条数据是运动的数据
                    byte[] todaySport = new byte[]{data[4], data[5], data[6], data[7], data[8], data[9], data[10], data[11], data[12], data[13], data[14], data[15], data[16], data[17]};
                    SportInfo sportInfo = new SportInfo();
                    sportInfo.initWithData(todaySport);
                    sportlist.add(sportInfo);
                    if (sportlist.size() == sportNum) {
                        mIDataResponse.onTodaySport(sportlist);
                    }
                } else {                                             // 如果字节数小于12就说明这是一条数据数目的数据
                    byte[] bytesRecord = new byte[]{data[5], data[4]};
                    int recordNum = Integer.valueOf(DataUtil.byteToHexString(bytesRecord), 16);
                    if (recordNum == 0) {
                        mIDataResponse.onSynchronizedTodaySport(recordNum, 0, 0);
                        mIDataResponse.onTodaySport(new ArrayList<SportInfo>());
                    } else {
                        sportNum = recordNum;
                        sportlist.clear();
                        byte[] bytesPack = new byte[]{data[9], data[8], data[7], data[6]};
                        byte[] bytesAll = new byte[]{data[13], data[12], data[11], data[10]};
                        int packNum = Integer.valueOf(DataUtil.byteToHexString(bytesPack), 16);  // 封包数
                        int allNum = Integer.valueOf(DataUtil.byteToHexString(bytesAll), 16);         // 总包数
                        mIDataResponse.onSynchronizedTodaySport(recordNum, packNum, allNum);
                    }
                }

            } else if (tagStr.equals(ProtocolTag.SYNCHRO_HISTORY_SPORT_DATA)) { // 同步历史的运动数据
                Log.e("sport", dataStr);
                if (data.length >= 20) {               // 如果字节数大于或者等于12个字节，则这条数据是运动的数据
                    byte[] todaySport = new byte[]{data[4], data[5], data[6], data[7], data[8], data[9], data[10], data[11], data[12], data[13], data[14], data[15], data[16], data[17]};
                    SportInfo sportInfo = new SportInfo();
                    sportInfo.initWithData(todaySport);
                    sportlist.add(sportInfo);
                    if (sportlist.size() == sportNum) {
                        mIDataResponse.onHistorySport(sportlist);
                    }
                } else {                                             // 如果字节数小于12就说明这是一条数据数目的数据
                    byte[] bytesRecord = new byte[]{data[5], data[4]};
                    int recordNum = Integer.valueOf(DataUtil.byteToHexString(bytesRecord), 16);
                    if (recordNum == 0) {
                        mIDataResponse.onSynchronizedHistorySport(recordNum, 0, 0);
                        mIDataResponse.onHistorySport(new ArrayList<SportInfo>());
                    } else {
                        sportNum = recordNum;
                        sportlist.clear();
                        byte[] bytesPack = new byte[]{data[9], data[8], data[7], data[6]};
                        byte[] bytesAll = new byte[]{data[13], data[12], data[11], data[10]};
                        int packNum = Integer.valueOf(DataUtil.byteToHexString(bytesPack), 16);  // 封包数
                        int allNum = Integer.valueOf(DataUtil.byteToHexString(bytesAll), 16);         // 总包数
                        mIDataResponse.onSynchronizedHistorySport(recordNum, packNum, allNum);
                    }
                }

            } else if (tagStr.equals(ProtocolTag.SYNCHRO_TODAY_SLEEP_DATA)) {   // 同步今天的睡眠数据
                if (data.length == 16) {              // 如果只有16个字节，那说明这条数据是记录条数的数据
                    byte[] bytesRecord = new byte[]{data[5], data[4]};
                    int recordNum = Integer.valueOf(DataUtil.byteToHexString(bytesRecord), 16);
                    if (recordNum == 0) {
                        mIDataResponse.onSynchronizedTodaySleep(recordNum, 0, 0);
                    } else {
                        byte[] bytesPack = new byte[]{data[9], data[8], data[7], data[6]};
                        byte[] bytesAll = new byte[]{data[13], data[12], data[11], data[10]};
                        int packNum = Integer.valueOf(DataUtil.byteToHexString(bytesPack), 16);  // 封包数
                        int allNum = Integer.valueOf(DataUtil.byteToHexString(bytesAll), 16);         // 总包数
                        mIDataResponse.onSynchronizedTodaySleep(recordNum, packNum, allNum);
                    }
                } else {
                    Log.e("sleepInfo", "Today : " + dataStr);
                }

            } else if (tagStr.equals(ProtocolTag.SYNCHRO_HISTORY_SLEEP_DATA)) { // 同步历史睡眠数据
                Log.d("chen55", "我经过了这里");
                if (data.length == 16) {              // 如果只有16个字节，那说明这条数据是记录条数的数据
                    byte[] bytesRecord = new byte[]{data[5], data[4]};
                    int recordNum = Integer.valueOf(DataUtil.byteToHexString(bytesRecord), 16);
                    if (recordNum == 0) {
                        mIDataResponse.onSynchronizedHistorySleep(recordNum, 0, 0);
                    } else {
                        byte[] bytesPack = new byte[]{data[9], data[8], data[7], data[6]};
                        byte[] bytesAll = new byte[]{data[13], data[12], data[11], data[10]};
                        int packNum = Integer.valueOf(DataUtil.byteToHexString(bytesPack), 16);  // 封包数
                        int allNum = Integer.valueOf(DataUtil.byteToHexString(bytesAll), 16);         // 总包数
                        mIDataResponse.onSynchronizedHistorySleep(recordNum, packNum, allNum);
                    }
                } else {
                    Log.e("sleepInfo", dataStr);
                }

            } else if (tagStr.equals(ProtocolTag.SYNCHRO_TODAY_HEART_RATE_DATA)) {    // 同步今天的心率数据
                Log.d("chen22", data.length + "");
                if (data.length == 16) {              // 如果只有16个字节，那说明这条数据是记录条数的数据
                    byte[] bytesRecord = new byte[]{data[5], data[4]};
                    int recordNum = Integer.valueOf(DataUtil.byteToHexString(bytesRecord), 16);
                    if (recordNum == 0) {
                        mIDataResponse.onSynchronizedTodayHeartRate(recordNum, 0, 0);
                        mIDataResponse.onTodayHeartRate(new ArrayList<HeartInfo>());
                    } else {
                        heartNum = recordNum;
                        heartlist.clear();
                        byte[] bytesPack = new byte[]{data[9], data[8], data[7], data[6]};
                        byte[] bytesAll = new byte[]{data[13], data[12], data[11], data[10]};
                        int packNum = Integer.valueOf(DataUtil.byteToHexString(bytesPack), 16);  // 封包数
                        int allNum = Integer.valueOf(DataUtil.byteToHexString(bytesAll), 16);         // 总包数
                        mIDataResponse.onSynchronizedTodayHeartRate(recordNum, packNum, allNum);
                    }
                } else {
                    HeartInfo heartInfo = new HeartInfo();
                    byte[] heartRateToday = new byte[data.length - 6];
                    for (int i = 0; i < heartRateToday.length; i++) {
                        heartRateToday[i] = data[i + 4];
                    }
                    heartInfo.initWithData(heartRateToday);
                    heartlist.add(heartInfo);
                    if (heartlist.size() == heartNum) {
                        mIDataResponse.onTodayHeartRate(heartlist);
                    }
                }

            } else if (tagStr.equals(ProtocolTag.SYNCHRO_HISTORY_HEART_RATE_DATA)) { // 同步历史心率数据
                Log.d("chen33", data.length + "");
                if (data.length == 16) {              // 如果只有16个字节，那说明这条数据是记录条数的数据
                    byte[] bytesRecord = new byte[]{data[5], data[4]};
                    int recordNum = Integer.valueOf(DataUtil.byteToHexString(bytesRecord), 16);
                    if (recordNum == 0) {
                        Log.d("chen66", "处理心率2");
                        mIDataResponse.onSynchronizedHistoryHeartRate(recordNum, 0, 0);
                        mIDataResponse.onHistoryHeartRate(new ArrayList<HeartInfo>());
                    } else {
                        heartNum = recordNum;
                        heartlist.clear();
                        byte[] bytesPack = new byte[]{data[9], data[8], data[7], data[6]};
                        byte[] bytesAll = new byte[]{data[13], data[12], data[11], data[10]};
                        int packNum = Integer.valueOf(DataUtil.byteToHexString(bytesPack), 16);  // 封包数
                        int allNum = Integer.valueOf(DataUtil.byteToHexString(bytesAll), 16);         // 总包数
                        mIDataResponse.onSynchronizedHistoryHeartRate(recordNum, packNum, allNum);
                    }
                } else {
                    HeartInfo heartInfo = new HeartInfo();
                    byte[] heartRateToday = new byte[data.length - 6];
                    for (int i = 0; i < heartRateToday.length; i++) {
                        heartRateToday[i] = data[i + 4];
                    }
                    heartInfo.initWithData(heartRateToday);
                    heartlist.add(heartInfo);
                    if (heartlist.size() == heartNum) {
                        Log.d("chen66", "处理心率3");
                        mIDataResponse.onHistoryHeartRate(heartlist);
                    }
                }

            } else if (tagStr.equals(ProtocolTag.SYNCHRO_TODAY_BLOOD_PRESSURE_DATA)) {    // 同步今天的血压数据
                if (data.length == 16) {              // 如果只有16个字节，那说明这条数据是记录条数的数据
                    byte[] bytesRecord = new byte[]{data[5], data[4]};
                    int recordNum = Integer.valueOf(DataUtil.byteToHexString(bytesRecord), 16);
                    if (recordNum == 0) {
                        mIDataResponse.onSynchronizedTodayBloodPressure(recordNum, 0, 0);
                        mIDataResponse.onTodayBloodPressure(new ArrayList<BloodInfo>());
                    } else {
                        bloodNum = recordNum;
                        bloodlist.clear();
                        byte[] bytesPack = new byte[]{data[9], data[8], data[7], data[6]};
                        byte[] bytesAll = new byte[]{data[13], data[12], data[11], data[10]};
                        int packNum = Integer.valueOf(DataUtil.byteToHexString(bytesPack), 16);  // 封包数
                        int allNum = Integer.valueOf(DataUtil.byteToHexString(bytesAll), 16);         // 总包数
                        mIDataResponse.onSynchronizedTodayBloodPressure(recordNum, packNum, allNum);
                    }
                } else {
                    BloodInfo bloodInfo = new BloodInfo();
                    byte[] heartRateToday = new byte[data.length - 6];
                    for (int i = 0; i < heartRateToday.length; i++) {
                        heartRateToday[i] = data[i + 4];
                    }
                    bloodInfo.initWithData(heartRateToday);
                    bloodlist.add(bloodInfo);
                    if (bloodlist.size() == heartNum) {
                        mIDataResponse.onTodayBloodPressure(bloodlist);
                    }
                }

            } else if (tagStr.equals(ProtocolTag.SYNCHRO_HISTORY_BLOOD_PRESSURE_DATA)) { // 同步历史血压数据
                if (data.length == 16) {              // 如果只有16个字节，那说明这条数据是记录条数的数据
                    byte[] bytesRecord = new byte[]{data[5], data[4]};
                    int recordNum = Integer.valueOf(DataUtil.byteToHexString(bytesRecord), 16);
                    if (recordNum == 0) {
                        mIDataResponse.onSynchronizedHistoryBloodPressure(recordNum, 0, 0);
                        mIDataResponse.onHistoryBloodPressure(new ArrayList<BloodInfo>());
                    } else {
                        bloodNum = recordNum;
                        bloodlist.clear();
                        byte[] bytesPack = new byte[]{data[9], data[8], data[7], data[6]};
                        byte[] bytesAll = new byte[]{data[13], data[12], data[11], data[10]};
                        int packNum = Integer.valueOf(DataUtil.byteToHexString(bytesPack), 16);  // 封包数
                        int allNum = Integer.valueOf(DataUtil.byteToHexString(bytesAll), 16);         // 总包数
                        mIDataResponse.onSynchronizedHistoryBloodPressure(recordNum, packNum, allNum);
                    }
                } else {
                    BloodInfo bloodInfo = new BloodInfo();
                    byte[] heartRateToday = new byte[data.length - 6];
                    for (int i = 0; i < heartRateToday.length; i++) {
                        heartRateToday[i] = data[i + 4];
                    }
                    bloodInfo.initWithData(heartRateToday);
                    bloodlist.add(bloodInfo);
                    if (bloodlist.size() == heartNum) {
                        mIDataResponse.onHistoryBloodPressure(bloodlist);
                    }
                }

            } else if (tagStr.equals(ProtocolTag.DELETE_SPORT_DATA)) {                    // 删除运动数据
                mIRequestResponse.onDeleteSportData(data[4]);

            } else if (tagStr.equals(ProtocolTag.DELETE_SLEEP_DATA)) {                    // 删除睡眠数据
                mIRequestResponse.onDeleteSleepData(data[4]);

            } else if (tagStr.equals(ProtocolTag.DELETE_HEART_RATE_DATA)) {     // 删除心率数据
                mIRequestResponse.onDeleteHeartRateData(data[4]);

            } else if (tagStr.equals(ProtocolTag.DELETE_BLOOD_PRESSURE_DATA)) {   //删除血压数据
                mIRequestResponse.onDeleteBloodPressureData(data[4]);
            }

        } else if (tagHead.equals("06")) {            // 不断上传，不需要回应
            Log.d("chenka", Tools.logbyte(bytes));
            // 同步步数、距离、卡路里
            if (tagStr.equals(ProtocolTag.SYNCHRO_STEP_DISTANCE_CALORIE_WITHOUT_RES)) {
                if (bytes == null) ;
                bytes = new byte[2];
                bytes[0] = data[5];
                bytes[1] = data[4];
                int steps = Integer.valueOf(DataUtil.byteToHexString(bytes), 16);
                bytes[0] = data[7];
                bytes[1] = data[6];
                int distance = Integer.valueOf(DataUtil.byteToHexString(bytes), 16);
                bytes[0] = data[9];
                bytes[1] = data[8];
                int calories = Integer.valueOf(DataUtil.byteToHexString(bytes), 16);
                mIDataResponse.onRealTimeSportData(steps, distance, calories);

            } else if (tagStr.equals(ProtocolTag.SYNCHRO_HEART_RATE_WITHOUT_RES)) {        // 心率
                int heartRate = Integer.valueOf(dataStr.substring(8, 10), 16);
                mIDataResponse.onRealTimeHeartRate(heartRate);

            } else if (tagStr.equals(ProtocolTag.SYNCHRO_BLOOD_OXYGEN_WITHOUT_RES)) {  // 血氧
                int bloodOxygen = Integer.valueOf(dataStr.substring(8, 10), 16);
                mIDataResponse.onRealTimeOxygen(bloodOxygen);

            } else if (tagStr.equals(ProtocolTag.SYNCHRO_PRESSURE_HEART_RATE_WITHOUT_RES)) { // 同步血压和心率
                int systolic = Integer.valueOf(dataStr.substring(8, 10), 16);
                int diastolic = Integer.valueOf(dataStr.substring(10, 12), 16);
                int heartRate = Integer.valueOf(dataStr.substring(12, 14), 16);
                mIDataResponse.onRealTimeBloodPressure(systolic, diastolic, heartRate);

            } else if (tagStr.equals(ProtocolTag.OPTOELECTRONIC_WAVEFORM)) {              //  光电波形

//                Log.e("dataRes","...接收数据..ppg....");
                perPPGMsg(data);

            } else if (tagStr.equals(ProtocolTag.ELECTROCARDIOGRAM)) {
                // 心电波形
//                Log.e("dataRes","...接收数据..ecg....");
                perGcdMsg(data);

            } else if (tagStr.equals(ProtocolTag.UpdateOptoelectronicWaveform)) {                                // 心电波形
                mIRequestResponse.onUpdateOptoelectronicWaveform(data);

            } else if (tagStr.equals(ProtocolTag.SPORTS_MODE)) {                            //运动模式数据上传

                if (sportsModeBytes == null) {
                    sportsModeBytes = new byte[2];
                }
                sportsModeBytes[0] = data[5];
                sportsModeBytes[1] = data[4];

                int steps = Integer.valueOf(DataUtil.byteToHexString(sportsModeBytes), 16);

                sportsModeBytes[0] = data[7];
                sportsModeBytes[1] = data[6];

                int instance = Integer.valueOf(DataUtil.byteToHexString(sportsModeBytes), 16);

                sportsModeBytes[0] = data[9];
                sportsModeBytes[1] = data[8];

                int kcal = Integer.valueOf(DataUtil.byteToHexString(sportsModeBytes), 16);

                sportsModeBytes[0] = data[11];
                sportsModeBytes[1] = data[10];
                int sportTime = Integer.valueOf(DataUtil.byteToHexString(sportsModeBytes), 16);

                mIDataResponse.onSportMode(steps, instance, kcal, sportTime);
            }

        } else if (tagHead.equals("01")) {            // 01 开头 各种基础设置
            if (tagStr.equalsIgnoreCase(ProtocolTag.TIME_SETTING)) {                                   // 时间设置
                if (isErrorType(data)) {                     // 校验错误，估计不会执行到这里
                    mIErrorCommand.onErrorCommand(ProtocolTag.TIME_SETTING, data[4]);
                } else {                                                    // 时间设置
                    mIRequestResponse.onTimeSettingResponse(data[4]);
                }

            } else if (tagStr.equalsIgnoreCase(ProtocolTag.ALARM_CLOCK_SETTING)) {    // 闹钟提醒
                Log.d("yyyy", Tools.logbyte(data));
                if (isErrorType(data)) {             //如果发送命令发生错误
                    mIErrorCommand.onErrorCommand(ProtocolTag.ALARM_CLOCK_SETTING, data[4]);
                } else {
                    if (data[4] == 0) {              // 操作码为0
                        if (data[6] == 0) {          // 已设置的闹钟,闹钟数为0
                            mIDataResponse.onQueryAlarmClock(data[5], data[6], null);
                        } else {                              // 闹钟数1个或一个以上
                            int settingNum = data[6];
                            ArrayList<ClockInfo> alarmList = new ArrayList<>();
                            for (int i = 0; i < settingNum; i++) {
                                byte[] msga = new byte[]{data[7 + i * 5], data[8 + i * 5], data[9 + i * 5], data[10 + i * 5], data[11 + i * 5]};
                                ClockInfo clockInfo = new ClockInfo(msga);
                                alarmList.add(clockInfo);
                            }
                            mIDataResponse.onQueryAlarmClock(data[5], data[6], alarmList);
                        }
                    } else if (data[4] == 1) {    // 操作码为1
                        mIRequestResponse.onAlarmSettingResponse(data[5]);

                    } else if (data[4] == 2) {   // 操作码为2
                        mIRequestResponse.onDeleteAlarmSetting(data[5]);

                    } else if (data[4] == 3) {   // 操作码为3
                        mIRequestResponse.onModifyAlarmResponse(data[5]);
                    }
                }

            } else if (tagStr.equalsIgnoreCase(ProtocolTag.TARGET_SETTING)) {                   // 目标设置
                if (isErrorType(data)) {
                    mIErrorCommand.onErrorCommand(ProtocolTag.TARGET_SETTING, data[4]);
                } else {
                    mIRequestResponse.onTargetSettingResponse(data[4]);
                }

            } else if (tagStr.equalsIgnoreCase(ProtocolTag.USER_INFO_SETTING)) {            // 用户设置
                if (isErrorType(data)) {                 // 发送命令错误
                    mIErrorCommand.onErrorCommand(ProtocolTag.USER_INFO_SETTING, data[4]);
                } else {
                    mIRequestResponse.onUserInfoSettingResponse(data[4]);
                }

            } else if (tagStr.equalsIgnoreCase(ProtocolTag.UNIT_SETTING)) {                          // 单位设置
                if (isErrorType(data)) {
                    mIErrorCommand.onErrorCommand(ProtocolTag.UNIT_SETTING, data[4]);
                } else {
                    mIRequestResponse.onUnitSettingResponse(data[4]);
                }

            } else if (tagStr.equalsIgnoreCase(ProtocolTag.LONG_SIT_SETTING)) {                // 久坐设置
                if (isErrorType(data)) {
                    mIErrorCommand.onErrorCommand(ProtocolTag.LONG_SIT_SETTING, data[4]);
                } else {
                    mIRequestResponse.onLongsitSettingResponse(data[4]);
                }

            } else if (tagStr.equalsIgnoreCase(ProtocolTag.PREVENT_LOST_SETTING)) {    // 防丢失开关设置
                if (isErrorType(data)) {
                    mIErrorCommand.onErrorCommand(ProtocolTag.PREVENT_LOST_SETTING, data[4]);
                } else {
                    mIRequestResponse.onPreventLostOnOffResponse(data[4]);
                }

            } else if (tagStr.equalsIgnoreCase(ProtocolTag.PREVENT_LOST_PARAMS_SETTING)) {    // 防丢参数设置
                if (isErrorType(data)) {
                    mIErrorCommand.onErrorCommand(ProtocolTag.PREVENT_LOST_PARAMS_SETTING, data[4]);
                } else {
                    mIRequestResponse.onPreventLostParamSettingResponse(data[4]);
                }

            } else if (tagStr.equalsIgnoreCase(ProtocolTag.LEFT_OR_RIGHT_HAND_SETTING)) { // 左手和右手设置
                if (isErrorType(data)) {
                    mIErrorCommand.onErrorCommand(ProtocolTag.LEFT_OR_RIGHT_HAND_SETTING, data[4]);
                } else {
                    mIRequestResponse.onLeftOrRightHandSettingResponse(data[4]);
                }

            } else if (tagStr.equalsIgnoreCase(ProtocolTag.MOBILE_OS_SETTING)) {              // 手机系统设置（Android or IOS）
                if (isErrorType(data)) {
                    mIErrorCommand.onErrorCommand(ProtocolTag.MOBILE_OS_SETTING, data[4]);
                } else {
                    mIRequestResponse.onMobileOSSettingResponse(data[4]);
                }

            } else if (tagStr.equalsIgnoreCase(ProtocolTag.NOTIFYCATION_ONOFF_SETTING)) {     // 通知提醒开关设置
                if (isErrorType(data)) {
                    mIErrorCommand.onErrorCommand(ProtocolTag.NOTIFYCATION_ONOFF_SETTING, data[4]);
                } else {
                    mIRequestResponse.onNotificationSettingResponse(data[4]);
                }

            } else if (tagStr.equalsIgnoreCase(ProtocolTag.HEART_RATE_REMIND_SETTING)) {       // 心率提醒设置
                if (isErrorType(data)) {
                    mIErrorCommand.onErrorCommand(ProtocolTag.HEART_RATE_REMIND_SETTING, data[4]);
                } else {
                    mIRequestResponse.onHeartRateAlarmSettingResponse(data[4]);
                }

            } else if (tagStr.equalsIgnoreCase(ProtocolTag.HEART_RATE_MONITOR)) {                          // 心率监测
                if (isErrorType(data)) {
                    mIErrorCommand.onErrorCommand(ProtocolTag.HEART_RATE_MONITOR, data[4]);
                } else {
                    mIRequestResponse.onHeartRateMonitorResponse(data[4]);
                }

            } else if (tagStr.equalsIgnoreCase(ProtocolTag.FIND_MOBILE_ONOFF)) {                              // 寻找手机开关
                if (isErrorType(data)) {
                    mIErrorCommand.onErrorCommand(ProtocolTag.FIND_MOBILE_ONOFF, data[4]);
                } else {
                    mIRequestResponse.onFindMobileOnOffResponse(data[4]);
                }

            } else if (tagStr.equalsIgnoreCase(ProtocolTag.RECOVER_TO_DEFAULT)) {       // 恢复出厂设置
                if (isErrorType(data)) {
                    mIErrorCommand.onErrorCommand(ProtocolTag.RECOVER_TO_DEFAULT, data[4]);
                } else {
                    mIRequestResponse.onRecoverToDefaultSettingResponse(data[4]);
                }

            } else if (tagStr.equalsIgnoreCase(ProtocolTag.DONOT_DISTURB)) {        // 免打扰设置
                if (isErrorType(data)) {
                    mIErrorCommand.onErrorCommand(ProtocolTag.DONOT_DISTURB, data[4]);
                } else {
                    mIRequestResponse.onDisturbeSettingResponse(data[4]);
                }

            } else if (tagStr.equalsIgnoreCase(ProtocolTag.ANCS_ONOFF)) {               // ANCS 用不了，这是安卓系统


            } else if (tagStr.equalsIgnoreCase(ProtocolTag.AEROBIC_EXERCISE_ONOFF)) {   // 有氧运动开关

                if (isErrorType(data)) {
                    mIErrorCommand.onErrorCommand(ProtocolTag.AEROBIC_EXERCISE_ONOFF, data[4]);
                } else {
                    mIRequestResponse.onAerobicExerciseResponse(data[4]);
                }

            } else if (tagStr.equalsIgnoreCase(ProtocolTag.LANGUAGE_SETTING)) {             // 语言设置
                if (isErrorType(data)) {
                    mIErrorCommand.onErrorCommand(ProtocolTag.LANGUAGE_SETTING, data[4]);
                } else {
                    mIRequestResponse.onLanguageSettingResponse(data[4]);
                }

            } else if (tagStr.equalsIgnoreCase(ProtocolTag.LEFT_THE_WRIST_TO_BRIGHT)) {     // 抬腕亮屏
                if (isErrorType(data)) {
                    mIErrorCommand.onErrorCommand(ProtocolTag.LEFT_THE_WRIST_TO_BRIGHT, data[4]);
                } else {
                    mIRequestResponse.onLeftTheWristToBrightResponse(data[4]);
                }

            } else if (tagStr.equalsIgnoreCase(ProtocolTag.BRIGHTNESS_CONTROL)) {   // 亮度设置
                if (isErrorType(data)) {
                    mIErrorCommand.onErrorCommand(ProtocolTag.BRIGHTNESS_CONTROL, data[4]);
                } else {
                    mIRequestResponse.onBrightnessSettingResponse(data[4]);
                }

            } else if (tagStr.equalsIgnoreCase(ProtocolTag.SKIN_COLOR)) {       //肤色设置
                if (isErrorType(data)) {
                    mIErrorCommand.onErrorCommand(ProtocolTag.SKIN_COLOR, data[4]);
                } else {
                    mIRequestResponse.onSetSkinColor(data[4]);
                }

            } else if (tagStr.equals(ProtocolTag.BLOODPRESSURE)) {
                mIRequestResponse.onBloodPressureResponse(data[4]);

            } else if (tagStr.equalsIgnoreCase(ProtocolTag.SLEEPTOREMINDSEETING)) {
                mIRequestResponse.onSleepToRemindResponse(data[4]);

            } else if (tagStr.equals("011B")) {
                int resultCode = data[4];
                Log.e("resultCode", resultCode + "");
                mIRequestResponse.onPPGDataCollectSettingResult(resultCode);
            }

        } else if (tagHead.equals("03")) {            // 03 开头 APP控制命令
            if (tagStr.equalsIgnoreCase(ProtocolTag.TestBlood)) {               // 血压测试开关
                if (isErrorType(data)) {
                    mIErrorCommand.onErrorCommand(ProtocolTag.TestBlood, data[4]);
                } else {
                    mIRequestResponse.onTestBlood(data[4]);
                }
            } else if (tagStr.equalsIgnoreCase(ProtocolTag.UpdateWaveData)) {   // 波形上传控制
                if (isErrorType(data)) {
                    mIErrorCommand.onErrorCommand(ProtocolTag.UpdateWaveData, data[4]);
                } else {
                    mIRequestResponse.onUpdateWaveData(data[4]);
                }

            } else if (tagStr.equals(ProtocolTag.HR_MEASUREMENT_ONOFF_CONTROL)) { // 心率测试开关控制
                mIRequestResponse.onHRMeasurementOnoffControl(data[4]);

            } else if (tagStr.equalsIgnoreCase(ProtocolTag.SAMPLING_FREQ)) {        // 采样频率
                byte[] freqBytes = new byte[]{data[5], data[4]};
                int freq = Integer.valueOf(DataUtil.byteToHexString(freqBytes), 16);
                mIDataResponse.onQuerySamplingFreqResponse(freq);

            } else if (tagStr.equalsIgnoreCase(ProtocolTag.SportType)) {            // 运动模式启动
                if (isErrorType(data)) {
                    mIErrorCommand.onErrorCommand(ProtocolTag.SportType, data[4]);
                } else {
                    mIRequestResponse.onsetSportTypeResponse(data[4]);
                }

            } else if (tagStr.equalsIgnoreCase(ProtocolTag.upppg)) {                // ppg
                if (isErrorType(data)) {
                    mIErrorCommand.onErrorCommand(ProtocolTag.UpdateWaveData, data[4]);
                } else {
                    mIRequestResponse.onsetPPGHZResponse(data[4]);
                }

            } else if (tagStr.equals(ProtocolTag.WEATHER)) {        // 天气预报
                mIRequestResponse.onWeatherResponse(data[4]);

            } else if (tagStr.equals(ProtocolTag.FINDBAND)) {       // 寻找手环
                mIRequestResponse.onFindBandResponse(data[4]);

            } else if (tagStr.equals(ProtocolTag.BP_MEASUREMENT_ONOFF_CONTROL)) {   // 血压测试开关控制
                mIRequestResponse.onBPMeasurementOnoffControl(data[4]);

            } else if (tagStr.equals(ProtocolTag.BLOOD_PRESSURE_CALIBRATION)) {     // 血压校准
                mIRequestResponse.onBloodPressureCalibration(data[4]);

            } else if (tagStr.equals(ProtocolTag.APP_EXIT)) {       // App退出
                mIRequestResponse.onAppExitResponse(data[4]);

            } else if (tagStr.equals(ProtocolTag.AEROBICS_COACH)) { // 有氧教练开关控制
                mIRequestResponse.onAerobicExerciseOnOffResponse(data[4]);

            } else if (tagStr.equals(ProtocolTag.BIND_DEVICE)) {    // 绑定设备
                mIRequestResponse.onBindDeviceResponse(data[4]);

            } else if (tagStr.equals(ProtocolTag.UNBIND_DEVICE)) {  // 解除绑定
                mIRequestResponse.onUnBindDeviceResponse(data[4]);

            } else if (tagStr.equals(ProtocolTag.MESSAGE_NOTIFICATION)) {           // 信息提醒
                mIRequestResponse.onMessageNotificationResponse(data[4]);

            } else if (tagStr.equals(ProtocolTag.DATA_POST_COMMAND_RESPONSE)) {     // 数据实时上传回应
                mIRequestResponse.onRealTimeDataResponse(data[4]);

            } else if (tagStr.equals(ProtocolTag.BLOOD_OXYGEN_ONOFF_CONTROL)) {     // 血氧开关控制
                mIRequestResponse.onBloodOxygenMeasure(data[4]);

            } else if (tagStr.equals(ProtocolTag.RESPIRATORY_RATE_ONOFF_CONTROL)) { // 呼吸率开关控制
                mIRequestResponse.onRespiratoryRateMeasure(data[4]);

            } else if (tagStr.equals(ProtocolTag.HEALTHPARAMANDWARNINGINFORMATION)) {
                mIRequestResponse.onHealthParamAndWarningInformation(data[4]);
            } else if (tagStr.equals(ProtocolTag.REAL_TIME_UPLOAD_DATA_DEVICE_STATUS)) {
                mIDataReceiveComplete.onRealTimeUploadDataDeviceState(data[4], data[5]);
            }

        } else if (tagHead.equals("07")) {
            if (tagStr.equals(ProtocolTag.QueryHistoryData)) {   //0700
                int collectType = data[4];
                if (data[4] != -5) {
                    byte[] countBytes = {data[6], data[5]};
                    num = Integer.valueOf(Tools.byteToHexString(countBytes), 16);
                    // 查询数据时间集
                    if (isQueryTimes = ProtocolWriter.isQueryTime()) {
                        times.clear();
                        mIDataReceiveComplete.onQueryDataProfile("", collectType, 0, 0);
                    } else {
                        mIDataReceiveComplete.onQueryDataCount(num);
                    }
                }
//            if (tagStr.equals(ProtocolTag.QueryHistoryData)) {   //0700
//                int collectType = data[4];
//                if (data[4] != -5) {
//                    byte[] countBytes = {data[6], data[5]};
//                    num = Integer.valueOf(Tools.byteToHexString(countBytes), 16);
//                    if (num > 0) {
//                        mIDataReceiveComplete.onQueryDataCount(num);
//                        // 查询数据时间集
//                        if (isQueryTimes = ProtocolWriter.isQueryTime()) {
//                            times.clear();
//                            mIDataReceiveComplete.onQueryDataProfile("", collectType, 0, num);
//                        }
//                    } else {
//                        mIDataReceiveComplete.onQueryDataCount(0);
//                    }
//                }
            } else if (tagStr.equals(ProtocolTag.GetHistoryData)) {  //0701

                Log.e("=====DataParser===== ", Tools.logbyte(data));
                if (data.length > 7) {
                    int collectType = data[4];
                    byte[] timeBytes = {data[10], data[9], data[8], data[7]};
                    long time = 946684800L + TransUtils.Bytes2Dec(timeBytes);
                    if (time != 946684800L) {
                        times.add(time);
                    }
                    time = time * 1000;
                    time = time - Tools.getTimeOffset();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String timeStr = format.format(new Date(time));
                    //上传数据总字节数
                    byte[] dataBytes = {data[17], data[16], data[15], data[14]};
                    ecgDataLength = TransUtils.Bytes2Dec(dataBytes);
                    //上传数据总包数
                    byte[] dataPackagebyte = {data[19], data[18]};
                    int ecgPackageLength = TransUtils.Bytes2Dec(dataPackagebyte);
                    // 添加时间集
                    if (isQueryTimes) {
                        if (incrementCount >= num) {
                            mIDataReceiveComplete.onQueryDataTimes(times);
                            incrementCount = 1;
                            isQueryTimes = false;
                            ProtocolWriter.setQueryTime(false);
                        } else {
                            mIDataReceiveComplete.onQueryDataProfile(timeStr, collectType, incrementCount++, num);
                        }
                    }
                }

//                if (data.length > 7) {
//                    int collectType = data[4];
//                    byte[] timeBytes = {data[10], data[9], data[8], data[7]};
//                    long time = 946684800L + TransUtils.Bytes2Dec(timeBytes);
//                    times.add(time);
//                    time = time * 1000;
//                    time = time - Tools.getTimeOffset();
//                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    String timeStr = format.format(new Date(time));
//                    //上传数据总字节数
//                    byte[] dataBytes = {data[17], data[16], data[15], data[14]};
//                    ecgDataLength = TransUtils.Bytes2Dec(dataBytes);
//                    //上传数据总包数
//                    byte[] dataPackagebyte = {data[19], data[18]};
//                    int ecgPackageLength = TransUtils.Bytes2Dec(dataPackagebyte);
//                    // 添加时间集
//                    if (isQueryTimes) {
//                        if (incrementCount >= num) {
//
//                            if (times.size() > 0) {
//                                times.remove(times.size() - 1);
//                            }
//
//                            mIDataReceiveComplete.onQueryDataTimes(times);
//                            incrementCount = 1;
//                            isQueryTimes = false;
//                            ProtocolWriter.setQueryTime(false);
//                        } else {
//                            mIDataReceiveComplete.onQueryDataProfile(timeStr, collectType, ++incrementCount, num);
//                        }
//                    }
//                }
            } else if (tagStr.equals(ProtocolTag.GetHistoryDataTime)) { //0702
                if (data.length > 7) {
                    //上传数据总字节数
                    byte[] dataBytes = {data[17], data[16], data[15], data[14]};
                    ecgDataLength = TransUtils.Bytes2Dec(dataBytes);
                    //上传数据总包数
                    byte[] dataPackagebyte = {data[19], data[18]};
                    ecgPackageLength = TransUtils.Bytes2Dec(dataPackagebyte);

                    mIDataReceiveComplete.onHistoryCollectData0702Finish();
                }
            } else if (tagStr.equals(ProtocolTag.ByHistoryData)) { //0710
//                isSendingCollectData = true;
                int sequenceNumber = data[5] & 0xff;
                if (sequenceNumber != record) {
                    record = sequenceNumber;
                    int quantity = data.length - 8;
                    byte command[] = new byte[quantity];
                    for (int i = 0; i < quantity; i++) {
                        command[i] = data[i + 6];
                    }
                    dataPacketNum += quantity;
                    ecgData = Tools.byteMerger(ecgData, command);
                }
            } else if (tagStr.equals(ProtocolTag.replyECGData)) { //0720
                if (data.length > 8) {
                    // 如果总字节数相等，则继续往下发另一条
                    byte[] dataBytes = {data[10], data[9], data[8], data[7]};
                    int uploadDataLength = TransUtils.Bytes2Dec(dataBytes);
                    // 是否接收完成
                    if (uploadDataLength != ecgDataLength) {
                        // 是否停止上传数据
                        if (isStopUploadData()) {
//                            isSendingCollectData = false;
                            mIDataReceiveComplete.onStopUploadData(data[4]);
                        } else {
//                            int collectType = data[4];
//                            mIDataReceiveComplete.onDataCountCompare(ecgData.length, uploadDataLength, ecgDataLength, collectType);
//
                            int collectType = data[4];
                            if (ecgData.length != uploadDataLength) {
                                ecgData = Tools.byteSplit(ecgData, dataPacketNum);
                            }
                            Log.e(TAG, "已接收的包 == " + ecgData.length);
                            Log.e(TAG, "每十包长度 == " + uploadDataLength);
                            dataPacketNum = 0;

                            mIDataReceiveComplete.onDataCountCompare(ecgData.length, uploadDataLength, ecgDataLength, collectType);

//                            mIDataReceiveComplete.onDataCountCompare(ecgData.length, uploadDataLength, collectType);

                        }
                    } else {
                        // 0,ECG;1,PPG
                        historyType = data[4];
                        perHistoryData(ecgData);
//                        isSendingCollectData = false;
                        record = 1;
                        ecgDataLength = 0;
                    }
                } else {
                    mIDataReceiveComplete.onStopUploadDataSuccess(data[5]);
                    isStopUploadData = false;
                    ecgData = new byte[0];
                    ecgDataLength = 0;
                    record = 1;
                }
            } else if (tagStr.equals(ProtocolTag.deleteECGData)) { //0730
                ecgData = new byte[0];
                ecgDataLength = 0;
                record = 1;
                mIDataReceiveComplete.onDataDeleteSuccess(data[7]);

            } else if (tagStr.equals(ProtocolTag.deleteECGDataWithTime)) { //0731
                ecgData = new byte[0];
                ecgDataLength = 0;
                record = 1;
                mIDataReceiveComplete.onDataDeleteSuccess(data[9]);
            }
        } else {
            if (tagStr.equals(ProtocolTag.FIRMWARE_UPDATE_THIRD)) {                          // 固件升级 第三方 Nordic 的 DFU
                if (isErrorType(data)) {            //  错误码
                    mIErrorCommand.onErrorCommand(ProtocolTag.FIRMWARE_UPDATE_THIRD, data[4]);
                } else {                                            // 数据响应
                    mIRequestResponse.onFirmWareUpdateResponse(data[4]);
                }
            } else if (tagStr.equals(ProtocolTag.FIRMWARE_UPDATE_LOCAL)) {               // 固件升级 使用自己定义的
                if (data[4] == 0) {                         // argument 为 0
                    if ((data[11]) == -1 && (data[12]) == -1) {                // 无下载固件
                        mIDataResponse.onFirmWareInfoResponse(data[4], data[5] + data[6] * 0x100, data[7], data[8],
                                data[9], data[10], data[11], data[12], 0);
                    } else {
                        byte[] length = new byte[]{data[16], data[15], data[14], data[13]};
                        int bytesLength = Integer.valueOf(DataUtil.byteToHexString(length), 16);
                        mIDataResponse.onFirmWareInfoResponse(data[4], data[5] + data[6] * 0x100, data[7], data[8],
                                data[9], data[10], data[11], data[12], bytesLength);
                    }
                } else if (data[4] == 1) {
                    mIRequestResponse.onDeleteDownloadedFirmWare(data[5]);

                } else if (data[4] == 2) {
                    mIRequestResponse.onUpdateFWStatusResponse(data[5]);

                } else if (data[4] == 3) {
                    mIRequestResponse.onFirmWareBlockResponse(data[5]);
                }
            }

            if (tagStr.equals(ProtocolTag.DEVICE_INFO)) {           // 设备信息
                mIDataResponse.onDeviceBaseInfo(data[4] + data[5] * 0x100, data[6], data[7], data[8], data[9], data[10], data[11]);

            } else if (tagStr.equals(ProtocolTag.SUPPORT_LIST)) {   // 支持列表
                mIDataResponse.onDeviceSupportFunction(data[4], data[5], data[6], data[7], data[8], data[9], data[10]);

            } else if (tagStr.equals(ProtocolTag.DEVICE_MAC)) {     //  设备MAC
                String mac = String.format("%2x:%2x:%2x:%2x:%2x:%2x", (byte) (data[9] & 0xff), (byte) (data[8] & 0x0ff), (byte) (data[7] & 0x0ff), (byte) (data[6] & 0x0ff), (byte) (data[5] & 0x0ff), (byte) (data[4] & 0x0ff));
                mIDataResponse.onDeviceMac(mac);

            } else if (tagStr.equals(ProtocolTag.DEVICE_NAME)) {    // 设备名称
                byte[] nameBytes = new byte[data.length - 6];
                for (int i = 0; i < nameBytes.length; i++) {
                    nameBytes[i] = data[4 + i];
                }
                int len = data.length - 6;
                StringBuilder name = new StringBuilder();
                for (int i = 0; i < len - 1; i++) {
                    name.append(String.format("%c", nameBytes[i]));
                }
                mIDataResponse.onDeviceName(name.toString().trim());

            } else if (tagStr.equals(ProtocolTag.CURRENT_HEART_RATE)) {     // 实时心率
                mIDataResponse.onCurrentHR(data[4], Integer.valueOf(DataUtil.byteToHexString(new byte[]{data[5]}), 16));

            } else if (tagStr.equals(ProtocolTag.CURRENT_BLOOD_PRESSURE)) {     // 当前血压
                byte[] bpData = new byte[]{data[5], data[6]};
                String bpDataStr = DataUtil.byteToHexString(bpData);
                int systolic = Integer.valueOf(bpDataStr.substring(0, 2), 16);
                int diastolic = Integer.valueOf(bpDataStr.substring(2, 4), 16);
                mIDataResponse.onCurrentBP(data[4], systolic, diastolic);

            } else if (tagStr.equals(ProtocolTag.FIND_PHONE_RESPONSE)) {  // 寻找手机
                mIRequestResponse.onFindPhoneResponse(data[4]);

            } else if (tagStr.equals(ProtocolTag.PREVENT_LOST_RESPONSE)) {  // 防丢失
                mIRequestResponse.onPreventLostResponse(data[4]);

            } else if (tagStr.equals(ProtocolTag.ANSWER_OR_REJECT_PHONE)) {  // 接听（拒接）电话
                mIRequestResponse.onAnswerOrRejectPhoneResponse(data[4]);

            } else if (tagStr.equals(ProtocolTag.CONTROL_THE_CAMERA)) { // 控制相机
                mIRequestResponse.onControlTheCamera(data[4]);

            } else if (tagStr.equals(ProtocolTag.CONTROL_THE_MUSIC)) {  // 控制音乐
                mIRequestResponse.onControlTheMusic(data[4]);
            }
        }
    }

    /**
     * 是否为错误类型
     * 0xFB 到 0xFF
     *
     * @param bytes
     * @return
     */
    public boolean isErrorType(byte[] bytes) {
        if (bytes[4] < 0) {
            return true;
        }
        return false;
    }

    public void sendMsg0() {
//        byte[] smsg = {0x05,0x00,0x01};
//        smsg = Tools.makeSend(smsg);
//        BleHandler.getInstance(getApplication()).sendMsg(smsg);
    }

    public void sendMsg1() { // 开始同步
        byte[] smsg = {0x05, 0x02, 0x01};
        smsg = Tools.makeSend(smsg);
        mIOperation.onDoSynchronizedHistorySport();
    }

    public void sendMsg2() {
        byte[] smsg = {0x05, 0x04, 0x01};
        smsg = Tools.makeSend(smsg);
        mIOperation.onDoSynchronizedHistorySleep();
    }

    public void sendMsg3() {
        byte[] smsg = {0x05, 0x06, 0x01};
        smsg = Tools.makeSend(smsg);
        mIOperation.onDoSynchronizedHistoryHeartRate();
    }

    public void sendMsg4() {
        byte[] smsg = {0x05, 0x08, 0x01};
        smsg = Tools.makeSend(smsg);
        mIOperation.onDoSynchronizedHistoryBloodPressure();
    }

    public void nextSend(boolean isdel) {
        if (syncType == 0x02) {
            if (isdel) {
                sendMsgDel(1);
            } else {
                mHandler.sendEmptyMessage(3);
            }
            Log.i("aa==", "运动完成");
        } else if (syncType == 0x04) {
            if (isdel) {
                sendMsgDel(2);
            } else {
                mHandler.sendEmptyMessage(4);
            }
            Log.i("aa==", "睡眠完成");
        } else if (syncType == 0x06) {
            if (isdel) {
                sendMsgDel(3);
            } else {
                mHandler.sendEmptyMessage(5);
            }
        } else {
            if (isSyncing) {
                sendMsgDel(4);
            }
        }
    }

    /**
     * @param index 1 表示   删除运动数据
     *              2 表示   删除睡眠数据
     *              3 表示   删除心率数据
     *              4 表示   删除血压数据
     */
    public void sendMsgDel(int index) {
        if (index == 1) {                                   // index 为 1 表示   删除运动数据
            byte[] smsg = {0x05, 0x40, 0x02};
            smsg = Tools.makeSend(smsg);
            mIOperation.onDeleteSport();
        } else if (index == 2) {                        // index 为 2 表示   删除睡眠数据
            byte[] smsg = {0x05, 0x41, 0x02};
            smsg = Tools.makeSend(smsg);
            mIOperation.onDeleteSleep();
        } else if (index == 3) {                        // index 为 3 表示   删除心率数据
            byte[] smsg = {0x05, 0x42, 0x02};
            smsg = Tools.makeSend(smsg);
            mIOperation.onDeleteHeartRate();
        } else if (index == 4) {                        // index 为 4 表示   删除血压数据
            byte[] smsg = {0x05, 0x43, 0x02};
            smsg = Tools.makeSend(smsg);
            mIOperation.onDeleteBloodPressure();
        }
    }

    // 打开开关实时步数
    public void sendMsgOpen2() {  //  03,09,09,00,01,00,02,a0,de
        byte[] smsg = {0x03, 0x09, 0x01, 0x00, 0x02};
        smsg = Tools.makeSend(smsg);
    }

    public void initData() {
        ravData = new byte[0];
    }

    public void perData() {
        if (syncType == 0x02) {//处理运动数据
            List<byte[]> blist = Tools.makeSendMsg(ravData, 14);
            sportlist.clear();
            for (byte[] smsg : blist) {
                SportInfo sportInfo = new SportInfo();
                sportInfo.initWithData(smsg);
                sportlist.add(sportInfo);
            }
            mIDataResponse.onHistorySport(sportlist);

        } else if (syncType == 0x04) {//处理睡眠数据
            List<byte[]> dlist = new ArrayList<>();
            int head1 = 0;
            int tlenght = 10000;
            byte[] mppData = null;
            int index = -1;
            for (int i = 0; i < ravData.length; i++) {
                int head = (ravData[i] & 0xff);
                if (head1 == 0) {
                    head1 = (head & 0xff);
                } else {
                    if (head1 == (0xaf & 0xff) && head == (0xfa & 0xff) && index == -1) {
                        byte[] stepbyte = {0x00, 0x00, ravData[i + 2], ravData[i + 1]};
                        tlenght = TransUtils.Bytes2Dec(stepbyte);
                        mppData = new byte[tlenght];
                        index = 0;
                        mppData[0] = (byte) head1;
                    }
                }
                if (index > -1 && mppData != null) {
                    index = index + 1;
                    if (index < mppData.length) {
                        mppData[index] = (byte) head;
                    }
                }
                if (index + 1 == tlenght) {
                    dlist.add(mppData);
                    head1 = 0;
                    index = -1;
                }
            }
            sleeplist.clear();
            for (byte[] obj : dlist) {
                if (obj == null) {
                    return;
                }
                SleepInfo sleepInfo = new SleepInfo();
                sleepInfo.initWithData(obj);
                sleeplist.add(sleepInfo);
            }
            for (int i = 0; i < sleeplist.size(); i++) {
                Log.d("iiiiiiiiiiiiiiiii", sleeplist.get(i).endFormet + "----" + sleeplist.get(i).endTime);
            }
            mIDataResponse.onHistorySleep(sleeplist);

        } else if (syncType == 0x06) {                  //处理心率数据
            List<byte[]> blist = Tools.makeSendMsg(ravData, 6);
            heartlist.clear();
            for (byte[] smsg : blist) {
                HeartInfo heartInfo = new HeartInfo();
                heartInfo.initWithData(smsg);
                heartlist.add(heartInfo);
            }
            mIDataResponse.onHistoryHeartRate(heartlist);

        } else if (syncType == 0x08) {                          //处理血压数据
            List<byte[]> blist = Tools.makeSendMsg(ravData, 8);
            bloodlist.clear();
            for (byte[] smsg : blist) {
                BloodInfo bloodInfo = new BloodInfo();
                bloodInfo.initWithData(smsg);
                bloodlist.add(bloodInfo);
            }
            mIDataResponse.onHistoryBloodPressure(bloodlist);

        }
    }

    /**
     * 心电波形数据
     */
    public void perGcdMsg(byte[] data) {
        int quantity = data.length - 6;
        byte ecgData[] = new byte[quantity];
        for (int i = 0; i < quantity; i++) {
//            ecgData[i] = data[i + 6];
            ecgData[i] = data[i + 4];
        }
        int offset = 0;
        int len = ecgData.length;
        int dataCnt = len / 3;
        int cur_cnt = 0;
        while (cur_cnt < dataCnt) {
            byte[] outData = {ecgData[offset], ecgData[offset + 1], ecgData[offset + 2]};
            offset += 3;
            mIDataResponse.onElectrocardiogram(outData);
            cur_cnt++;
        }
    }

    /**
     * 光电波形数据
     */
    public void perPPGMsg(byte[] data) {

//        Log.e("dataRes","光电波形="+data.toString());

        int quantity = data.length - 6;
        byte ppgData[] = new byte[quantity];
        for (int i = 0; i < quantity; i++) {
//            ppgData[i] = data[i + 6];
            ppgData[i] = data[i + 4];
        }
        int offset = 0;
        int len = ppgData.length;
        int dataCnt = len / 3;
        int cur_cnt = 0;
        while (cur_cnt < dataCnt) {
            byte[] outData = {ppgData[offset], ppgData[offset + 1], ppgData[offset + 2]};
            offset += 3;
            mIDataResponse.onOptoelectronic(outData);
            cur_cnt++;
        }
    }

    /**
     * 历史数据
     */
    public void perHistoryData(byte[] readData) {
        int offset = 0;
        int len = readData.length;
        int dataCnt = 0;
        int cur_cnt = 0;
        if (historyType == 0) {
            dataCnt = len / 3;
            cur_cnt = 0;
            while (cur_cnt < dataCnt) {
                byte[] outData = {readData[offset], readData[offset + 1], readData[offset + 2]};
                offset += 3;
                int count = outData.length;
                for (int i = 0; i < count; i++) {
                    ecgList.add(outData[i]);
                }
                cur_cnt++;
            }
            ecgData = new byte[0];
            mIDataReceiveComplete.onHistoryECGData(ecgList);
            if (ecgList != null) {
                ecgList.clear();
            }
        } else {
            dataCnt = len / 2;
            cur_cnt = 0;
            while (cur_cnt < dataCnt) {
                byte[] outData = {readData[offset], readData[offset + 1], 0x00};
                offset += 2;
                int count = outData.length;
                for (int i = 0; i < count; i++) {
                    ppgList.add(outData[i]);
                }
                cur_cnt++;
            }
            ecgData = new byte[0];
            mIDataReceiveComplete.onHistoryPPGData(ppgList);
            if (ppgList != null) {
                ppgList.clear();
            }
        }
    }

    public static void setStopUploadData(boolean isStop) {
        isStopUploadData = isStop;
    }

    public static boolean isStopUploadData() {
        return isStopUploadData;
    }
}
