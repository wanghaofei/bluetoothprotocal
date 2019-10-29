package com.zhuoting.health.Core;

/**
 * @author StevenLiu
 * @date 2019/9/28
 * @desc one word for this class
 */
public class CmdType {
    public static int CMD_OTA = 0x00;
    public static int CMD_Setting = 0x01;  // // 设置类命令 1
    public static int CMD_Query = 0x02;   //  // 查询类命令  2
    public static int CMD_APPControl = 0x03;   //// APP控制类命令  3
    public static int CMD_DeviceControl = 0x04;
    public static int CMD_SyncHistoryData = 0x05;  //历史睡眠，运动，心率 5
    public static int CMD_RealTimeDataUpload = 0x06; //同步实时数据类命令 6
    public static int CMD_SyncControlHistory = 0x07;  //同步历史数据ecg,ppg类命令 7


//    public static class CMDSetting {
//        public static int KEY_Time = 0x00;
//        public static int KEY_Alarm = 0x01;
//        public static int KEY_Target = 0x02;
//        public static int KEY_UserInfo = 0x03;
//        public static int KEY_UnitSet = 0x04;
//        public static int KEY_LongSitRemind = 0x05;
//        public static int KEY_AntiLostRemind = 0x06;
//        public static int KEY_AntiLostRemindParams = 0x07;
//        public static int KEY_LeftOrRightHand = 0x08;
//        public static int KEY_MobileOS = 0x09;
//        public static int KEY_Notification = 0x0A;
//        public static int KEY_HeartRateAlarm = 0x0B;
//        public static int KEY_HeartRateMonitor = 0x0C;
//        public static int KEY_FindPhone = 0x0D;
//        public static int KEY_RestoreFactorySettings = 0x0E;
//        public static int KEY_DNDMode = 0x10;
//        public static int KEY_Language = 0x11;
//        public static int KEY_RaiseWristForBrightScreen = 0x12;
//        public static int KEY_SetScreenBrightness = 0x13;
//        public static int KEY_SkinColor = 0x14;
//        public static int KEY_BloodPressureRange = 0x15;
//    }


    // 设置类命令
    public static class CMDSetting {
        public static int KEY_Time = 0x00;                      // 设置时间 （同步时间）0
        public static int KEY_Alarm = 0x01;                     // 设置,修改，删除，查询闹钟 1
        public static int KEY_Target = 0x02;                    // 设置目标 2
        public static int KEY_UserInfo = 0x03;                  // 设置个人信息 3
        public static int KEY_UnitSet = 0x04;                   // 设置单位 4
        public static int KEY_LongSitRemind = 0x05;             // 设置久坐提醒 5
        public static int KEY_AntiLostRemind = 0x06;            // 设置防丢失提醒 6
        public static int KEY_AntiLostRemindParams = 0x07;      // 设置防丢失提醒参数 7
        public static int KEY_LeftOrRightHand = 0x08;           // 设置左右手 8
        public static int KEY_MobileOS = 0x09;                  // 设置手机系统 9
        public static int KEY_Notification = 0x0A;              // 设置通知 10
        public static int KEY_HeartRateAlarm = 0x0B;            // 设置心率提醒 11
        public static int KEY_HeartRateMonitor = 0x0C;          // 设置心率监测模式 12
        public static int KEY_FindPhone = 0x0D;                 // 设置寻找手机 13
        public static int KEY_RestoreFactorySettings = 0x0E;    // 恢复出厂设置 14
        public static int KEY_DNDMode = 0x10;                   // 勿扰模式设置 16
        public static int KEY_Language = 0x11;                  // 设置语言  17
        public static int KEY_RaiseWristForBrightScreen = 0x12; // 抬腕亮屏开关设置 18
        public static int KEY_SetScreenBrightness = 0x13;       // 亮度设置 19
        public static int KEY_SkinColor = 0x14;                 // 肤色设置 20
        public static int KEY_BloodPressureRange = 0x15;        // 血压范围设置 21
    }

//    public static class CMDQuery {
//        public static int KEY_DeviceBaseInfo = 0x00;
//        public static int KEY_MacAddress = 0x01;
//        public static int KEY_NameModel = 0x02;
//        public static int KEY_CurrentHeartRate = 0x03;
//        public static int KEY_CurrentBloodPressure = 0x04;
//        public static int KEY_DeviceSupportFunction = 0x05;
//    }

    // 查询类命令
    public static class CMDQuery {
        public static int KEY_DeviceBaseInfo = 0x00;        // 获取基本信息 0
        public static int KEY_MacAddress = 0x01;            // 获取 Mac 地址 1
        public static int KEY_NameModel = 0x02;             // 获取设备名字或型号 2
        public static int KEY_CurrentHeartRate = 0x03;      // 获取当前心率 3
        public static int KEY_CurrentBloodPressure = 0x04;  // 获取当前血压 4
        public static int KEY_DeviceSupportFunction = 0x05; // 获取手环支持的功能列表 5
    }

    // APP控制类命令
    public static class CMDAPPControl {
        public static int KEY_HeartRateSwitch = 0x00;       // 心率测试开关控制 0
        public static int KEY_BloodPressureSwitch = 0x01;   // 血压测试开关控制 1
        public static int KEY_AppExit = 0x02;               // App退出指令 2
        public static int KEY_FindDevice = 0x03;            // 寻找手环 3
        public static int KEY_Block = 0x04;                 // App对Device的响应 4
        public static int KEY_SportType = 0x05;             // 运动模式 5
        public static int KEY_SamplingRate = 0x06;          // 设置采样率 6
        public static int KEY_SleepRemind = 0x07;           // 设置睡眠提醒时间 7
        public static int KEY_HealthAlarm = 0x08;           // 健康参数、预警信息发送 8
    }

//    public static class CMDAPPControl {
//        public static int KEY_HeartRateSwitch = 0x00;
//        public static int KEY_BloodPressureSwitch = 0x01;
//        public static int KEY_AppExit = 0x02;
//        public static int KEY_FindDevice = 0x03;
//        public static int KEY_Block = 0x04;
//        public static int KEY_SportType = 0x05;
//        public static int KEY_SamplingRate = 0x06;
//        public static int KEY_SleepRemind = 0x07;
//        public static int KEY_HealthAlarm = 0x08;
//    }

    public static class CMDDeviceControl {
        public static int KEY_FIND_PHONE = 0x00;
        public static int KEY_LOST_REMINE = 0x01;
    }

    public static class CMDSyncData {
        public static int KEY_START_SYNC = 0x00;
        public static int KEY_STOP_SYNC = 0x00;
        public static int KEY_SYNC_SPORT_NOW = 0X01;
    }


    // 同步实时数据类命令
    public static class CMDRealTimeData{
        public static int KEY_UPLOAD_CONTROL = 0x00;    // 实时数据上传控制  0
        public static int KEY_WAVE_FORM_UPLOAD = 0x01;  // 波形上传控制 1
        public static int KEY_WEATHER_PUSH = 0x02;      // 天气信息数据传送 2
    }

//    public static class CMDRealTimeData{
//        public static int KEY_UPLOAD_CONTROL = 0x00;
//        public static int KEY_WAVE_FORM_UPLOAD = 0x01;
//        public static int KEY_WEATHER_PUSH = 0x02;
//    }

    // 同步历史数据类命令
    public static class CMDSyncDataHistory {
        public static int KEY_QUERY_DATA = 0x20;                // 获取ECG与PPG历史文件总数  32
        public static int KEY_QUERY_DATA_BY_TIME_STAMP = 0X21;  // 获取ECG与PPG历史文件时间戳集 33
        public static int KEY_UPLOAD_DATA_BY_TIME_STAMP = 0X22; // 上传历史数据 (根据时间戳)  34
        public static int KEY_UPLOAD_DATA_BY_INDEX = 0X23;      // 上传历史数据 (根据序号) 35
        public static int KEY_DELETE_DATA_BY_TIME_STAMP = 0X24; // 删除历史数据 (根据时间戳) 36
        public static int KEY_DELETE_DATA_BY_INDEX = 0X25;      // 删除历史数据 (根据序号) 37
        public static int KEY_COLLECT_DATA_BY_INDEX = 0X26;     // 配置PPG数据定时收集 38
    }

//    public static class CMDSyncDataHistory {
//        public static int KEY_QUERY_DATA = 0x20;
//        public static int KEY_QUERY_DATA_BY_TIME_STAMP = 0X21;
//        public static int KEY_UPLOAD_DATA_BY_TIME_STAMP = 0X22;
//        public static int KEY_UPLOAD_DATA_BY_INDEX = 0X23;
//        public static int KEY_DELETE_DATA_BY_TIME_STAMP = 0X24;
//        public static int KEY_DELETE_DATA_BY_INDEX = 0X25;
//        public static int KEY_COLLECT_DATA_BY_INDEX = 0X26;
//    }


}
