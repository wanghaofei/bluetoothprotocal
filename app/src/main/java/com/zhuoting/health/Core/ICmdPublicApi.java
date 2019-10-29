package com.zhuoting.health.Core;

import com.zhuoting.health.bean.ClockInfo;
import com.zhuoting.health.bean.LongSitInfo;

/**
 * @author StevenLiu
 * @date 2019/9/28
 * @desc one word for this interface ICmdPublicApi
 */
public interface ICmdPublicApi {

    /**
     * 设置时间
     *
     * @param callback IAckCallback
     * @see ClockInfo
     */
    void settingTime(IAckCallback callback);

    /**
     * 设置闹钟
     *
     * @param clockInfo ClockInfo
     * @param callback  IAckCallback
     * @see ClockInfo
     * @see IAckCallback
     */
    void settingAlarm(ClockInfo clockInfo, IAckCallback callback);

    /**
     * 修改闹钟
     *
     * @param oldAlarm 旧闹钟
     * @param newAlarm 新闹钟
     * @param callback IAckCallback
     * @see ClockInfo
     * @see IAckCallback
     */
    void modifyAlarm(ClockInfo oldAlarm, ClockInfo newAlarm, IAckCallback callback);

    /**
     * 删除闹钟
     *
     * @param clockInfo 闹钟
     * @param callback  IAckCallback
     * @see ClockInfo
     * @see IAckCallback
     */
    void deleteAlarm(ClockInfo clockInfo, IAckCallback callback);

    /**
     * 查询闹钟
     *
     * @param callback IAckCallback
     * @see IAckCallback
     */
    void queryAlarm(IAckCallback callback);

    /**
     * 设置目标
     *
     * @param type      0x00:步数 ,0x01:卡路里 ,0x02:距离, 0x03:睡眠
     * @param target    步数、卡路里、距离
     * @param sleepHour 睡眠目标小时
     * @param sleepMin  睡眠目标分钟
     * @param callback  IAckCallback
     * @see IAckCallback
     */
    void setTarget(byte type, int target, byte sleepHour, byte sleepMin, IAckCallback callback);

    /**
     * 设置个人信息
     *
     * @param height   身高
     * @param weight   体重
     * @param sex      性别
     * @param age      年龄
     * @param callback 回调
     */
    void setUserInfo(int height, int weight, byte sex, byte age, IAckCallback callback);

    /**
     * 单位设置
     *
     * @param distanceOnOff   0 km  ，1 mile
     * @param weightOnOff     0 kg，1 lb，2st
     * @param tempOnOff       0 C 摄氏度，1 F 华氏度
     * @param timeFormatOnOff 0 24小时制，1 12小时制
     */
    void setUnit(byte distanceOnOff, byte weightOnOff, byte tempOnOff, byte timeFormatOnOff, IAckCallback callback);

    /**
     * 久坐提醒设置
     *
     * @param info     LongSitInfo
     * @param callback IAckCallback
     */
    void setLongSitRemind(LongSitInfo info, IAckCallback callback);

    /**
     * 防丢失提醒设置
     *
     * @param distanceFlag 0 不防丢, 1 近距离防丢, 2 中距离防丢, 3 远距离防丢
     * @param callback     IAckCallback
     */
    void setAntiLostRemind(byte distanceFlag, IAckCallback callback);

    /**
     * 防丢失提醒参数设置
     *
     * @param mode                 1 近距离防丢 2 中距离防丢 3 远距离防丢
     * @param rssi                 信号强度
     * @param delay                防丢延时
     * @param disconnectDelayOnOff 是否支持断线延时
     * @param repeatOnOff          重复开关
     * @param callback             IAckCallback
     */
    void setAntiLostRemindParams(byte mode, byte rssi, byte delay, byte disconnectDelayOnOff, byte repeatOnOff, IAckCallback callback);

    /**
     * 左手或者右手
     *
     * @param leftOrRightHand 0 左手 ，1 右手
     * @param callback        IAckCallback
     */
    void setLeftOrRightHand(byte leftOrRightHand, IAckCallback callback);

    /**
     * 设置手机系统
     *
     * @param androidOrIOS 0 Android ， 1 IOS
     * @param versionCode  版本号
     * @param callback     IAckCallback
     */
    void setMobileOS(byte androidOrIOS, byte versionCode, IAckCallback callback);

    /**
     * 通知设置
     *
     * @param onOff     总开关 0关 1开
     * @param subOnOff1 来电、短信、邮件、微信、QQ、新浪微博、facebook、twitter 从 Bit7 到 Bit0
     * @param subOnOff2 Messenger、WhatsApp、Linked In、Instagram 、Skype、Line 、预留、预留  从Bit7到Bit0
     * @param callback  IAckCallback
     */
    void setNotification(byte onOff, int subOnOff1, int subOnOff2, IAckCallback callback);

    /**
     * 心率提醒
     *
     * @param onOff    开关: 0 关 1 开
     * @param maxValue 报警阀值(100 - 240)
     * @param callback IAckCallback
     */
    void setHeartRateAlarm(byte onOff, int maxValue, int minValue, IAckCallback callback);

    /**
     * 心率监测模式设置
     *
     * @param mode     0 手动 1 自动
     * @param interval 时间间隔 (1 - 60)
     * @param callback IAckCallback
     */
    void setHeartRateMonitor(byte mode, byte interval, IAckCallback callback);

    /**
     * 寻找手机
     *
     * @param onOff    0关 1开
     * @param callback IAckCallback
     */
    void setFindPhone(byte onOff, IAckCallback callback);

    /**
     * 恢复出厂设置
     *
     * @param callback IAckCallback
     */
    void restoreFactorySettings(IAckCallback callback);

    /**
     * 勿扰模式设置
     *
     * @param onOff     开关
     * @param startHour 起始时间 小时
     * @param startMin  起始时间 分钟
     * @param endHour   结束时间 小时
     * @param endMin    结束时间 分钟
     * @param callback  IAckCallback
     */
    void setDNDMode(byte onOff, byte startHour, byte startMin, byte endHour, byte endMin, IAckCallback callback);

    /**
     * 设置语言
     *
     * @param language 0 为英语 1 为中文
     * @param callback IAckCallback
     */
    void setLanguage(int language, IAckCallback callback);

    /**
     * 抬腕亮屏开关设置
     *
     * @param onOff    0 关闭 1 打开
     * @param callback IAckCallback
     */
    void raiseWristForBrightScreen(byte onOff, IAckCallback callback);

    /**
     * 亮度设置
     *
     * @param level    0 低 1 中 2 高
     * @param callback IAckCallback
     */
    void setScreenBrightness(byte level, IAckCallback callback);

    /**
     * 肤色设置
     *
     * @param skinColor 0-白 、1-白间黄、 2-黄、3-棕色、4-褐色、5-黑
     * @param callback  IAckCallback
     */
    void setSkinColor(byte skinColor, IAckCallback callback);

    /**
     * 血压范围设置
     *
     * @param bloodPressure 0-偏低 1-正常 2-轻微偏高 3-中度偏高 4-重度高
     * @param callback      IAckCallback
     */
    void setBloodPressureRange(byte bloodPressure, IAckCallback callback);

    /**
     * 获取基本信息
     * 固件版本号、电池电量等
     *
     * @param callback IAckCallback
     */
    void getBaseInfo(IAckCallback callback);

    /**
     * 获取 Mac 地址
     *
     * @param callback IAckCallback
     */
    void getMacAddress(IAckCallback callback);

    /**
     * 获取设备名字或型号
     *
     * @param callback IAckCallback
     */
    void getDeviceNameAndModel(IAckCallback callback);

    /**
     * 获取当前心率
     *
     * @param callback IAckCallback
     */
    void getCurrentHeartRate(IAckCallback callback);

    /**
     * 获取当前血压
     *
     * @param callback IAckCallback
     */
    void getCurrentBloodPressure(IAckCallback callback);

    /**
     * 获取手环支持的功能列表
     *
     * @param callback IAckCallback
     */
    void getDeviceSupportFunction(IAckCallback callback);

    /**
     * 心率测试开关控制
     *
     * @param mode     0 关闭，1 单次测量，2 监测模式
     * @param callback IAckCallback
     */
    void setHeartRateSwitch(byte mode, IAckCallback callback);

    /**
     * 血压测试开关控制
     *
     * @param mode     0 关闭，1 单次测量，2 监测模式
     * @param callback IAckCallback
     */
    void setBloodPressureSwitch(byte mode, IAckCallback callback);

    /**
     * App退出指令
     * 此命令是为了通知设备关闭蓝牙，有的手机在退出app后蓝牙仍然会和设备进行短暂的连接
     *
     * @param callback IAckCallback
     */
    void setAppExit(IAckCallback callback);

    /**
     * 实时数据上传控制
     *
     * @param onOff    0 关  ， 1 开
     * @param type     0 步数 1 心率 2 血氧 3 血压 4 HRV
     * @param interval 间隔秒（1 - 240）如果 onOff为0，这个填0就可以了
     * @param callback IAckCallback
     */
    void setRealTimeDataUploadControl(byte onOff, byte type, byte interval, IAckCallback callback);

    /**
     * 波形上传控制
     *
     * @param onOff    0 停止上传 ， 1 开始上传
     * @param type     0 光电       1 心电
     * @param callback IAckCallback
     */
    void setWaveFormUploadControl(byte onOff, byte type, IAckCallback callback);

    /**
     * 天气信息数据传送
     *
     * @param minTemp  最低温度
     * @param maxTemp  最高温度
     * @param realTemp 实时温度
     * @param code     code
     * @param callback IAckCallback
     */
    void setWeatherPush(String minTemp, String maxTemp, String realTemp, int code, IAckCallback callback);

    /**
     * 寻找手环
     *
     * @param mode        1 开始寻找， 0 停止寻找
     * @param remindTimes 设备提醒次数（1-10），如果App不支持写0
     * @param interval    提醒间隔秒（1-3）
     * @param callback    IAckCallback
     */
    void setFindDevice(byte mode, byte remindTimes, byte interval, IAckCallback callback);

    /**
     * App对Device的响应
     * 同步通信步骤说明
     * 1、设备收到同步开始后,设备回复请求,如果有数据则开始通过 characteristic3发送1 Block
     * 数据
     * 2、设备通过 characteristic1发送 Block确认信息
     * 3、APP收到Block确认信息后进行判断,并告知设备结果,设备根据结果停止、继续或重
     * 发;
     * 4、以下所有类型数据同步步骤同1-3
     * 5、在同步过程中,如有必要,APP可发送结束同步指令,设备收到此命令后将停止传输数
     * 据
     * 6、当天运动数据记录格式如3564(C)
     *
     * @param command  0 接收成功 1 所有数据接收完毕 2 包数不符 3 字节数不符 4 CRC16错误
     * @param callback IAckCallback
     */
    void setBlockInfo(byte command, IAckCallback callback);

    /**
     * 运动模式
     *
     * @param type     0 走路，1 跑步，2 骑行，3 健身
     * @param onOff    0 停止，1 开始
     * @param callback IAckCallback
     */
    void setSportTypeSwitch(int type, int onOff, IAckCallback callback);

    /**
     * 设置采样率
     *
     * @param type     0 光电波形，1 心电波形
     * @param callback IAckCallback
     */
    void setSamplingRate(byte type, IAckCallback callback);

    /**
     * 设置睡眠提醒时间
     *
     * @param hour     小时（0-23）
     * @param minutes  分钟 (0-59)
     * @param onOff    true 开 false关
     * @param weekStr  (首位开关、其他为周一到周日)
     * @param callback IAckCallback
     */
    void setRemindSleep(int hour, int minutes, boolean onOff, String weekStr, IAckCallback callback);

    /**
     * 健康参数、预警信息发送
     *
     * @param warningState  预警状态（0-无预警，1-预警生效中、0xff-本字段无效）
     * @param healthState   健康状态(0-未知、1-优秀、2-良好、3-一般、4-较差、5-生病、0xff-本字段无效)
     * @param healthCount   健康指数（0-120  0xff-本字段无效）
     * @param friendWarning 亲友预警（0-无预警、1-预警生效中）
     * @param callback      IAckCallback
     */
    void setHealthAlarm(int warningState, int healthState, int healthCount, int friendWarning, IAckCallback callback);

    /**
     * 同步历史数据(不包括ECG与PPG)
     *
     * @param dataType 0x02 运动 0x04 睡眠 0x06 心率 0x08 血压
     * @param callback IAckCallback
     * @see IAckCallback
     */
    void queryHistoryData(int dataType, IAckCallback callback);

    /**
     * 删除历史数据(不包括ECG与PPG)
     *
     * @param dataType 0x40 运动 0x41 睡眠 0x42 心率 0x43 血压
     * @param callback IAckCallback
     * @see IAckCallback
     */
    void deleteHistoryData(int dataType, IAckCallback callback);

    /**
     * 获取ECG与PPG历史文件总数
     *
     * @param collectType 0x00 ECG 0x01 PPG
     * @param callback    IAckCallback
     */
    void queryHistoryCollectDataTotalNum(int collectType, IAckCallback callback);

    /**
     * 获取ECG与PPG历史文件时间戳集
     *
     * @param collectType 0x00 ECG 0x01 PPG
     * @param callback    IAckCallback
     */
    void queryHistoryCollectDataTimestamps(int collectType, IAckCallback callback);

    /**
     * 上传历史数据
     *
     * @param collectType 0x00 ECG 0x01 PPG
     * @param timestamps  时间戳
     * @param callback    IAckCallback
     */
    void getHistoryCollectData(int collectType, long timestamps, IAckCallback callback);

    /**
     * 上传历史数据
     *
     * @param collectType 0x00 ECG 0x01 PPG
     * @param index       序号
     * @param callback    IAckCallback
     */
    void getHistoryCollectData(int collectType, int index, IAckCallback callback);

    /**
     * 删除历史数据
     *
     * @param collectType 0x00 ECG 0x01 PPG
     * @param index       序号
     * @param callback    IAckCallback
     */
    void deleteHistoryCollectData(int collectType, int index, IAckCallback callback);

    /**
     * 删除历史数据
     *
     * @param collectType 0x00 ECG 0x01 PPG
     * @param timestamps  时间戳
     * @param callback    IAckCallback
     */
    void deleteHistoryCollectData(int collectType, long timestamps, IAckCallback callback);

    /**
     * 配置PPG数据定时收集
     *
     * @param isOpen       是否打开
     * @param timeLen      时长 单位秒
     * @param timeInterval 时间间隔 单位分
     * @param callback     IAckCallback
     */
    void setHistoryDataRegularCollection(int isOpen, int timeLen, int timeInterval, IAckCallback callback);
}