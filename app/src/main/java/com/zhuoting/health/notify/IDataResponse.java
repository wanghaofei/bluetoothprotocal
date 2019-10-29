package com.zhuoting.health.notify;

import com.zhuoting.health.bean.BloodInfo;
import com.zhuoting.health.bean.ClockInfo;
import com.zhuoting.health.bean.HeartInfo;
import com.zhuoting.health.bean.SleepInfo;
import com.zhuoting.health.bean.SportInfo;

import java.util.List;

/**
 * Created by Hqs on 2018/1/12
 * 设备回传上来的信息
 */
public interface IDataResponse {

    /**
     * 设备固件信息
     *
     * @param argument        同一 CommandId 和 Key 有不同argument
     * @param id              id号
     * @param status          0 支持升级    1 不支持升级
     * @param electricity     电量 0 正常    1 电量低   2 正在充电
     * @param crfwsubversion  当前固件子版本
     * @param crfwmainversion 当前固件主版本
     * @param dldfwsubversion 下载好的固件子版本       无固件版本为 FFFF
     * @param dlfwmainversion 下载好的固件主版本
     * @param dlbytes         已下载的字节数
     */
    void onFirmWareInfoResponse(byte argument, int id, byte status, byte electricity,
                                byte crfwsubversion, byte crfwmainversion, byte dldfwsubversion, byte dlfwmainversion, int dlbytes);


    /**
     * 查询闹钟
     *
     * @param supportAlarmNum 支持的最大闹钟数
     * @param settingAlarmNum 已经设置的闹钟
     * @param alarmSet        闹钟集合
     */
    void onQueryAlarmClock(byte supportAlarmNum, byte settingAlarmNum, List<ClockInfo> alarmSet);


    /**
     * 设备基本信息
     *
     * @param deviceId          设备id
     * @param subVersion        子版本
     * @param mainVersion       主版本
     * @param electricityStatus 电池状态 0正常  1低电量 2充电中 3充满
     * @param electricity       电池电量（0-100）
     * @param bindStatus        绑定状态      0 未绑定 1 已绑定
     * @param synchronizedFlag  同步标志     0 无需同步  1 需同步
     */
    void onDeviceBaseInfo(int deviceId, int subVersion, int mainVersion, byte electricityStatus, byte electricity, byte bindStatus, byte synchronizedFlag);

    /**
     * 设备支持的功能
     *
     * @param function1      计步 、 睡眠监测 、实时数据 、 设备升级 、 心率 、 通知中心 、 多语言 、 血压 -> bit7 到 bit0
     * @param function2      心率告警 、 有氧教练 、 实时心电图上传 、 预留  -> bit7 到 bit0
     * @param alarmNum       闹钟个数
     * @param alarmType      闹钟提醒类型：自定义 会议 聚会 约会 吃药 锻炼 睡觉 起床 -> bit7 到 bit0
     * @param messageNotify1 来电 短信 邮件 QQ 微信 新浪微博 Facebook twitter -> bit7 到 bit0
     * @param messageNotify2 Whatsapp  Messenger Instagram  Linked in   其它  预留 预留 预留 -> bit7 到 bit0
     * @param otherFunction  久坐提醒  防丢提醒   找手机  找手环  出厂设置  预留  勿扰模式   心率监测方式控制  -> bit7到bit0
     */
    void onDeviceSupportFunction(byte function1, byte function2, byte alarmNum, byte alarmType, byte messageNotify1, byte messageNotify2, byte otherFunction);

    /**
     * 低字节在前,MAC=MAC5:MAC4:MAC3:MAC2:MACl:MAC0
     *
     * @param mac Mac Address
     */
    void onDeviceMac(String mac);

    /**
     * 设备名称
     *
     * @param deviceName deviceName
     */
    void onDeviceName(String deviceName);

    /**
     * 心率
     *
     * @param status 0 未在测心率    1 正在测心率
     * @param hr     心率值
     */
    void onCurrentHR(byte status, int hr);

    /**
     * 血压
     *
     * @param status    状态 0 未测血压       1 正在测血压
     * @param systolic  收缩压 （未测时为0）
     * @param diastolic 舒张压（未测时为0）
     */
    void onCurrentBP(byte status, int systolic, int diastolic);


    /**
     * 采样频率
     *
     * @param frequency 如果是 0000 为不支持的类型， 其他就为采样频率
     */
    void onQuerySamplingFreqResponse(int frequency);

    /**
     * 同步当天运动数据
     * 同步通信步骤说明
     * 1、设备收到同步开始后,设备回复请求,如果有数据则开始通过 characteristic3发送1 Block
     * 数据
     * 2、设备通过 characteristic1发送Bock确认信息
     * 3、APP收到Block确认信息后进行判断,并告知设备结果,设备根据结果停止、继续或重
     * 发;
     * 4、以下所有类型数据同步步骤同1~3
     * 5、在同步过程中,如有必要,APP可发送结束同步指令,设备收到此命令后将停止传输数
     * 据
     *
     * @param recordNum      记录条数
     * @param packDataLength 包装字节数
     * @param allDataLength  总字节数
     */
    void onSynchronizedTodaySport(int recordNum, int packDataLength, int allDataLength);

    /**
     * 今日运动数据
     *
     * @param mlist 该类包含 时间、时间戳、步数、距离、卡路里等
     */
    void onTodaySport(List<SportInfo> mlist);

    /**
     * 同步历史运动
     * 基本说明同 onSynchronizedTodaySport() 方法的说明
     *
     * @param recordNum      记录条数
     * @param packDataLength 封装字节长度
     * @param allDataLength  总字节长度
     */
    void onSynchronizedHistorySport(int recordNum, int packDataLength, int allDataLength);


    /**
     * 历史运动数据
     *
     * @param mlist 该类包含 时间、时间戳、步数、距离、卡路里等
     */
    void onHistorySport(List<SportInfo> mlist);

    /**
     * 同步今天的睡眠数据
     * 基本说明同 onSynchronizedTodaySport() 方法的说明
     *
     * @param recordNum      记录条数
     * @param packDataLength 封装字节长度
     * @param allDataLength  总字节长度
     */
    void onSynchronizedTodaySleep(int recordNum, int packDataLength, int allDataLength);

    /**
     * 今日睡眠
     *
     * @param sleepInfo 包含起始时间，深睡浅睡次数，还有各段睡眠睡眠时间的详细数据等
     */
    void onTodaySleep(SleepInfo sleepInfo);


    /**
     * 同步历史的睡眠数据
     * 基本说明同 onSynchronizedTodaySport() 方法的说明
     *
     * @param recordNum      记录条数
     * @param packDataLength 封装字节长度
     * @param allDataLength  总字节长度
     */
    void onSynchronizedHistorySleep(int recordNum, int packDataLength, int allDataLength);


    /**
     * 同步历史数据
     *
     * @param mlist 包含起始时间，深睡浅睡次数，还有各段睡眠睡眠时间的详细数据等
     */
    void onHistorySleep(List<SleepInfo> mlist);


    /**
     * 同步今天的心率数据
     * 基本说明同 onSynchronizedTodaySport() 方法的说明
     *
     * @param recordNum      记录条数
     * @param packDataLength 封装字节长度
     * @param allDataLength  总字节长度
     */
    void onSynchronizedTodayHeartRate(int recordNum, int packDataLength, int allDataLength);

    /**
     * 今日心率数据
     *
     * @param mlist 包含时间戳、心率值等数据
     */
    void onTodayHeartRate(List<HeartInfo> mlist);

    /**
     * 同步历史的心率数据
     * 基本说明同 onSynchronizedTodaySport() 方法的说明
     *
     * @param recordNum      记录条数
     * @param packDataLength 封装字节长度
     * @param allDataLength  总字节长度
     */
    void onSynchronizedHistoryHeartRate(int recordNum, int packDataLength, int allDataLength);

    /**
     * 同步历史心率数据
     *
     * @param mlist List<HeartInfo>
     */
    void onHistoryHeartRate(List<HeartInfo> mlist);


    /**
     * 同步今天的血压数据
     * 基本说明同 onSynchronizedTodaySport() 方法的说明
     *
     * @param recordNum      记录条数
     * @param packDataLength 封装字节长度
     * @param allDataLength  总字节长度
     */
    void onSynchronizedTodayBloodPressure(int recordNum, int packDataLength, int allDataLength);

    /**
     * 今天的血压数据
     *
     * @param mlist 收缩压/舒张压     测量时间
     */
    void onTodayBloodPressure(List<BloodInfo> mlist);


    /**
     * 同步历史的血压数据
     * 基本说明同 onSynchronizedTodaySport() 方法的说明
     *
     * @param recordNum      记录条数
     * @param packDataLength 封装字节长度
     * @param allDataLength  总字节长度
     */
    void onSynchronizedHistoryBloodPressure(int recordNum, int packDataLength, int allDataLength);

    /**
     * 历史的血压数据
     *
     * @param mlist 收缩压/舒张压     测量时间
     */
    void onHistoryBloodPressure(List<BloodInfo> mlist);


    /**
     * 实时数据
     * 步数、距离（单位：米）、卡路里（单位：千卡）
     *
     * @param steps    步数
     * @param distance 距离
     * @param calorie  卡路里
     */
    void onRealTimeSportData(int steps, int distance, int calorie);

    /**
     * 实时心率
     *
     * @param heartRate 心率
     */
    void onRealTimeHeartRate(int heartRate);

    /**
     * 实时血氧浓度
     *
     * @param oxygen 血氧
     */
    void onRealTimeOxygen(int oxygen);

    /**
     * 实时血压
     *
     * @param systolic  收缩压
     * @param diastolic 舒张压
     * @param heartRate 心率
     */
    void onRealTimeBloodPressure(int systolic, int diastolic, int heartRate);


    /**
     * 心电图的数据
     *
     * @param electrocardiogram 心电数据字节数组
     */
    void onElectrocardiogram(byte[] electrocardiogram);

    /**
     * 光电的数据
     *
     * @param optoelectronic 光电数据字节数组
     */
    void onOptoelectronic(byte[] optoelectronic);

    /**
     * 运动模式数据上传
     */
    void onSportMode(int steps, int instance, int kacl, int sportTime);
}