package com.zhuoting.health;

/**
 * Created by Hqs on 2018/1/4
 * 各个协议的标志头
 * 使用HashMap存储
 */
public class ProtocolTag {

    /**
     *  手环传过来的信息
     */
    public static final String FIRMWARE_UPDATE = "00";                              // 固件升级
    public static final String FIRMWARE_UPDATE_THIRD = "0000";          // 固件升级第三方 比如 Nordic 的 DFU(Device Firmware Update)
    public static final String FIRMWARE_UPDATE_LOCAL = "0001";          // 本协议的升级方式

    public static final String TIME_SETTING = "0100";                                       // 时间设置
    public static final String ALARM_CLOCK_SETTING = "0101";                  // 闹钟提醒
    public static final String TARGET_SETTING = "0102";                                 // 目标设置
    public static final String USER_INFO_SETTING = "0103";                          // 用户设置
    public static final String UNIT_SETTING = "0104";                                        // 单位设置
    public static final String LONG_SIT_SETTING = "0105";                              // 久坐提醒
    public static final String PREVENT_LOST_SETTING = "0106";                 // 防丢提醒
    public static final String PREVENT_LOST_PARAMS_SETTING = "0107";                // 防丢参数设置
    public static final String LEFT_OR_RIGHT_HAND_SETTING = "0108";                     // 左手右手设置
    public static final String MOBILE_OS_SETTING = "0109";                          // 手机操作系统设置 （Android 或 IOS）
    public static final String NOTIFYCATION_ONOFF_SETTING = "010A"; // 通知提醒开关设置
    public static final String HEART_RATE_REMIND_SETTING = "010B";  // 心率提醒设置
    public static final String HEART_RATE_MONITOR = "010C";                    // 心率监测
    public static final String FIND_MOBILE_ONOFF = "010D";                         // 寻找手机开关
    public static final String RECOVER_TO_DEFAULT = "010E";                     // 恢复出厂设置
    public static final String DONOT_DISTURB = "010F";                                    // 免打扰
    public static final String ANCS_ONOFF = "0110";                 // Apple Notification Center Service 苹果通知中心服务
    public static final String AEROBIC_EXERCISE_ONOFF = "0111";                             // 有氧运动开关
    public static final String LANGUAGE_SETTING = "0112";                           // 语言设置 中文1和英文0
    public static final String LEFT_THE_WRIST_TO_BRIGHT = "0113";       // 抬腕亮屏
    public static final String BRIGHTNESS_CONTROL = "0114";                      // 显示屏亮度设置 0 1 2 低中高
    public static final String SKIN_COLOR = "0115";                  //肤色设置
    public static final String BLOODPRESSURE = "0116";    //血压范围设置
    public static final String SLEEPTOREMINDSEETING = "011A";


    public static final String DEVICE_INFO = "0200" ;               // 设备信息，包括版本号等
    public static final String SUPPORT_LIST = "0201";              // 支持列表
    public static final String DEVICE_MAC = "0202";                 // MAC地址
    public static final String DEVICE_NAME = "0203";              // 设备名称
    public static final String CURRENT_HEART_RATE = "0205";                 // 实时心率
    public static final String CURRENT_BLOOD_PRESSURE = "0206";      // 实时血压

    public static final String FINDBAND = "0300";                       // 寻找手环
    public static final String HR_MEASUREMENT_ONOFF_CONTROL = "0301";       // 心率测试开关控制
    public static final String BP_MEASUREMENT_ONOFF_CONTROL = "0302";       // 血压测试开关控制
    public static final String BLOOD_PRESSURE_CALIBRATION = "0303";                 // 血压校准
    public static final String APP_EXIT = "0304";                                                                     // App退出
    public static final String AEROBICS_COACH = "0305";                                                  // 有氧教练
    public static final String BIND_DEVICE = "0306";                                                            // 绑定设备
    public static final String UNBIND_DEVICE = "0307";                                                      // 解除绑定
    public static final String MESSAGE_NOTIFICATION = "0308";                                   // 信息提醒命令
    public static final String DATA_POST_COMMAND_RESPONSE = "0309";     // 数据上传设置响应
    public static final String BLOOD_OXYGEN_ONOFF_CONTROL = "0310";//血氧测试开光控制
    public static final String RESPIRATORY_RATE_ONOFF_CONTROL = "0311"; //呼吸率测试开关控制
    public static final String WEATHER = "0312";
    public static final String REAL_TIME_UPLOAD_DATA_DEVICE_STATUS = "0314";
    public static final String HEALTHPARAMANDWARNINGINFORMATION = "0315";


    public static final String SAMPLING_FREQ = "030A";         // 采样频率 (16 - 1000)
    public static final String WAVE_POST_COMMAND_RESPONSE = "030B";   // 波形上传响应

    public static final String FIND_PHONE_RESPONSE = "0400";                // 寻找手机响应
    public static final String PREVENT_LOST_RESPONSE = "0401";          // 防丢提醒响应
    public static final String ANSWER_OR_REJECT_PHONE  = "0402";     // 接收（拒接）电话
    public static final String CONTROL_THE_CAMERA = "0403";     // 控制相机照相
    public static final String CONTROL_THE_MUSIC = "0404";     // 控制音乐照相

    public static final String SYNCHRO_ALL_SWITCH_RESPONSE = "0500";     // 同步所有数据开关响应
    public static final String BLOCK_COMFIRM_RESPONSE = "0580";      // BLOCK 确认信息响应
    public static final String SYNCHRO_TODAY_SPORT_DATA = "0501";  // 同步当天运动数据
    public static final String SYNCHRO_HISTORY_SPORT_DATA = "0502";         // 同步历史运动数据
    public static final String SYNCHRO_TODAY_SLEEP_DATA = "0503";  // 同步当天睡眠数据
    public static final String SYNCHRO_HISTORY_SLEEP_DATA = "0504";         // 同步历史睡眠数据
    public static final String SYNCHRO_TODAY_HEART_RATE_DATA = "0505";       // 同步当天心率数据
    public static final String SYNCHRO_HISTORY_HEART_RATE_DATA = "0506";   // 同步历史心率数据
    public static final String SYNCHRO_TODAY_BLOOD_PRESSURE_DATA = "0507";          // 同步当天血压
    public static final String SYNCHRO_HISTORY_BLOOD_PRESSURE_DATA = "0508";     // 同步历史血压
    public static final String DELETE_SPORT_DATA = "0540";               // 删除运动数据
    public static final String DELETE_SLEEP_DATA = "0541";               // 删除睡眠数据
    public static final String DELETE_HEART_RATE_DATA = "0542";// 删除心率数据
    public static final String DELETE_BLOOD_PRESSURE_DATA = "0543"; //删除血压数据

    // 快速同步，不需要回应，在数据量大的时候使用这种方式，比如心电图等
    public static final String SYNCHRO_STEP_DISTANCE_CALORIE_WITHOUT_RES = "0600";  // 步数路程卡路里
    public static final String SYNCHRO_HEART_RATE_WITHOUT_RES = "0601";                              // 心率
    public static final String SYNCHRO_BLOOD_OXYGEN_WITHOUT_RES = "0602";                        // 血氧
    public static final String SYNCHRO_PRESSURE_HEART_RATE_WITHOUT_RES = "0603";     // 血压心率
    public static final String OPTOELECTRONIC_WAVEFORM = "0604";                                               // 光电波形
    public static final String ELECTROCARDIOGRAM = "0605";                                                           // 心电波形
    public static final String SPORTS_MODE = "0606";


    //血压ppg
    public static final String TestBlood = "0302";
    public static final String UpdateWaveData = "030b";
    public static final String SamplingRate = "030a";
    public static final String UpdateOptoelectronicWaveform = "0604";
    public static final String upppg = "030f";

    //运动模式
    public static final String SportType = "030c";


    //历史采集数据同步、获取记录条数
    public static final String QueryHistoryData = "0700";
    //获取历史采集数据
    public static final String GetHistoryData = "0701";
    //按时间获取历史采集数据
    public static final String GetHistoryDataTime = "0702";
    //手环往APP发送数据
    public static final String ByHistoryData = "0710";
    //设备上传历史采集数据
    public static final String replyECGData = "0720";
    //删除历史采集数据
    public static final String deleteECGData = "0730";
    //删除历史采集数据根据时间
    public static final String deleteECGDataWithTime = "0731";
    //设备上传历史采集数据
    public static final String replyPPGata = "0720";
    //删除历史采集数据
    public static final String deletePPGData = "0730";
}
