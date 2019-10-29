package com.zhuoting.health.write;


import android.util.Log;

import com.zhuoting.health.bean.ClockInfo;
import com.zhuoting.health.bean.LongSitInfo;
import com.zhuoting.health.util.DataUtil;
import com.zhuoting.health.util.Tools;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;

/**
 * Created by Hqs on 2018/1/8
 * 写指令，包含各种功能
 * 修改行数1138 开始
 */
public class ProtocolWriter {

    //    private static final boolean QUERY_TIME = true;
//    private static final boolean UN_QUERY_TIME = false;
    private static boolean isQueryTimes = false;
    private static int count = 0;

    /**
     * 以 DFU 的方式升级固件
     *
     * @return
     */
    public static byte[] updateFirmWareByNordicDFU() {
        byte[] update = new byte[]{0x00, 0x00, 0x44, 0x46, 0x55};
        return Tools.makeSend(update);
    }

    /**
     * 固有方式升级
     * code 为 0 ：查询固件信息
     * 为 1 ：删除固件信息
     *
     * @param code
     * @return
     */
    public static byte[] updateFirmWareByLocal(byte code) {
        byte[] update = new byte[]{0x00, 0x01, code};
        return Tools.makeSend(update);
    }

    /**
     * 发送待升级的信息    子版本在前，主版本在后
     *
     * @param subVersion
     * @param mainVersion
     * @param length
     * @return
     */
    // TODO 完善这个协议
    public static byte[] readyToUpdateInfo(byte subVersion, byte mainVersion, int length) {
        byte[] update = new byte[]{0x00, 0x02, subVersion, mainVersion};
        return update;
    }


    /**
     * 同步时间
     *
     * @return
     */
    public static byte[] syncTime() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        if (dayOfWeek == 1) {
            dayOfWeek += 5;
        } else {
            dayOfWeek -= 2;
        }
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        int sec = calendar.get(Calendar.SECOND);

        byte aa1 = (byte) (year % 0x100);
        byte aa2 = (byte) (year / 0x100);
        byte[] syncTimes = {(byte) 0x01, 0x00, aa1, aa2, (byte) month,
                (byte) dayOfMonth, (byte) hour, (byte) min, (byte) sec, (byte) dayOfWeek};
        byte[] smsg = Tools.makeSend(syncTimes);
        Log.e("write", Tools.logbyte(smsg));
        return smsg;
    }

    /**
     * 闹铃
     * clockInfotype 0起床、1睡觉、2锻炼、3吃药、4约会、5聚会、6会议、7、自定义
     * hour (0-23)
     * min   (0-59)
     * onOff 二进制八位 由高到低位数的功能 bit 7:总开关 0关1开，bit 6 周日 0不重复1重复 ，bit5 周六 0不重复1重复……bit0周一 0不重复1重复
     * lazySleep (0 - 59)分钟
     */
    public static byte[] writeForAlarmSetting(ClockInfo clockInfo) {
        byte[] alarm = new byte[]{0x01, 0x01, 0x01, (byte) clockInfo.type, (byte) clockInfo.c_hour, (byte) clockInfo.c_min, (byte) clockInfo.getweek(), (byte) clockInfo.uplater};
        Log.d("chen", Tools.logbyte(Tools.makeSend(alarm)));
        return Tools.makeSend(alarm);
    }

    /**
     * 删除指定闹钟
     * 如果该指定时间的闹钟不存在则返回一个错误码
     * hour
     * min
     *
     * @return
     */
    public static byte[] writeForDeleteAlarm(ClockInfo clockInfo) {
        byte[] deleteAlarm = new byte[]{0x01, 0x01, 0x02, (byte) clockInfo.c_hour, (byte) clockInfo.c_min};
        return Tools.makeSend(deleteAlarm);
    }


    /**
     * 修改指定闹钟
     * 如果要修改的闹钟不存在会返回错误码
     * lastHour 要修改的闹钟的小时
     * lastMin   要修改的闹钟的分钟
     * type      0 - 7 ：起床、睡觉、锻炼、吃药、约会、聚会、会议、自定义
     * hour      新的闹钟的小时
     * min       新的闹钟的分钟
     * lazySleep     懒睡时间
     */
    public static byte[] writeForModifyAlarm(ClockInfo oclockInfo, ClockInfo nclockInfo) {
        byte[] modifyAlarm = new byte[]{0x01, 0x01, 0x03, (byte) oclockInfo.c_hour, (byte) oclockInfo.c_min, (byte) nclockInfo.type, (byte) nclockInfo.c_hour, (byte) nclockInfo.c_min, (byte) nclockInfo.getweek(), (byte) nclockInfo.uplater};
        return Tools.makeSend(modifyAlarm);
    }


    /**
     * 设置目标
     *
     * @param type
     * @param target
     * @param sleepHour
     * @param sleepMin
     * @return
     */
    // TODO 仍待确定，根据协议这个长度似乎会变
    public static byte[] writeForTargetSetting(byte type, int target, byte sleepHour, byte sleepMin) {
        int byte1 = (0x000000ff & target);
        int byte2 = (0x0000ff00 & target);
        int byte3 = (0x00ff0000 & target);
        int byte4 = (0xff000000 & target);
        String byte1Str = Integer.toHexString(byte1);
        String byte2Str = Integer.toHexString(byte2);
        String byte3Str = Integer.toHexString(byte3);
        String byte4Str = Integer.toHexString(byte4);
        if (byte1Str.length() < 2) {
            byte1Str = "0" + byte1Str;
        }
        if (byte2Str.length() < 2) {
            byte2Str = "0" + byte2Str;
        }
        if (byte3Str.length() < 2) {
            byte3Str = "0" + byte3Str;
        }
        if (byte4Str.length() < 2) {
            byte4Str = "0" + byte4Str;
        }
        byte[] bytesByString = DataUtil.getBytesByString(byte1Str + byte2Str + byte3Str + byte4Str);
        byte[] setTarget = new byte[]{0x01, 0x02, type, bytesByString[0], bytesByString[1], bytesByString[2], bytesByString[3], sleepHour, sleepMin};
        return Tools.makeSend(setTarget);
    }


    /**
     * 查询闹钟
     *
     * @return
     */
    public static byte[] writeForQueryAlarmClock() {
        byte[] alarm = new byte[]{0x01, 0x01, 0x00};
        return Tools.makeSend(alarm);
    }


    /**
     * 用户信息设置
     *
     * @param height
     * @param weight
     * @param sex
     * @param age
     * @return
     */
    public static byte[] writeForUserInfoSetting(int height, int weight, byte sex, byte age) {
        if (height < 100 || height > 250) {
            Log.e("write", "身高不在100到250内，身高值：" + height);
            return null;
        }
        if (weight < 30 || weight > 200) {
            Log.e("write", "体重不在30到200内，体重值：" + weight);
            return null;
        }
        if (age < 6 || age > 120) {
            Log.e("write", "年龄不在6到120内，体重值：" + age);
        }
        String heightStr = Integer.toHexString(height);
        String weightStr = Integer.toHexString(weight);
        if (heightStr.length() < 2) {
            heightStr = "0" + heightStr;
        }
        if (weightStr.length() < 2) {
            weightStr = "0" + weightStr;
        }
        byte[] bytesByString = DataUtil.getBytesByString(heightStr + weightStr);
        byte[] userInfo = new byte[]{0x01, 0x03, bytesByString[0], bytesByString[1], sex, age};
        return Tools.makeSend(userInfo);
    }

    /**
     * 单位设置
     *
     * @param distanceOnOff   0 km  ，1 mile
     * @param weightOnOff     0 kg，1 lb，2st
     * @param tempOnOff       0 C 摄氏度，1 F 华氏度
     * @param timeFormatOnOff 0 24小时制，1 12小时制
     * @return
     */
    public static byte[] writeForSettingUnit(byte distanceOnOff, byte weightOnOff, byte tempOnOff, byte timeFormatOnOff) {
        byte[] unitSetting = new byte[]{0x01, 0x04, distanceOnOff, weightOnOff, tempOnOff, timeFormatOnOff};
        return Tools.makeSend(unitSetting);
    }


    /**
     * 久坐提醒设置
     *
     * @return
     */
    public static byte[] writeForLongSitSetting(LongSitInfo longSitInfo) {

        byte[] longSitSetting = longSitInfo.makeSendByte();
        return Tools.makeSend(longSitSetting);
    }


    /**
     * 防丢失设置
     *
     * @param distanceFlag 0 不防丢
     *                     1 近距离防丢
     *                     2 中距离防丢
     *                     3 远距离防丢
     * @return
     */
    public static byte[] writeForPreventLostSetting(byte distanceFlag) {
        byte[] preventLost = new byte[]{0x01, 0x06, distanceFlag};
        return Tools.makeSend(preventLost);
    }

    /**
     * @param mode                 1 近距离防丢 2 中距离防丢 3 远距离防丢
     * @param rssi                 信号强度
     * @param delay                防丢延时
     * @param disconnectDelayOnOff 是否支持断线延时
     * @param repeatOnOff          重复开关
     * @return
     */
    public static byte[] writeForPreventLostParamSetting(byte mode, byte rssi, byte delay, byte disconnectDelayOnOff, byte repeatOnOff) {
        byte[] preventLostParam = new byte[]{0x01, 0x07, mode, rssi, delay, disconnectDelayOnOff, repeatOnOff};
        return Tools.makeSend(preventLostParam);
    }


    /**
     * 左手或者右手
     *
     * @param leftOrRight 0 左手 ，1 右手
     * @return
     */
    public static byte[] writeForLeftOrRightHand(byte leftOrRight) {
        byte[] leftRightOnOff = new byte[]{0x01, 0x08, leftOrRight};
        return Tools.makeSend(leftRightOnOff);
    }


    /**
     * 设置手机系统
     *
     * @param androidOrIOS 0 Android ， 1 IOS
     * @param versionCode  版本号
     * @return
     */
    public static byte[] writeForMobileOSSetting(byte androidOrIOS, byte versionCode) {
        byte[] mobileOS = new byte[]{0x01, 0x09, androidOrIOS, versionCode};
        return Tools.makeSend(mobileOS);
    }


    /**
     * 通知设置
     *
     * @param onOff     总开关 0关 1开
     * @param subOnOff1 来电、短信、邮件、微信、QQ、新浪微博、facebook、twitter 从 Bit7 到 Bit0
     * @param subOnOff2 Messenger、WhatsApp、Linked In、Instagram 、Skype、Line 、预留、预留  从Bit7到Bit0
     * @return
     */
    public static byte[] writeForNotification(byte onOff, int subOnOff1, int subOnOff2) {
        byte[] notification = new byte[]{0x01, 0x0A, onOff, (byte) subOnOff1, (byte) subOnOff2};
        Log.d("iiiii", Tools.logbyte(Tools.makeSend(notification)));
        return Tools.makeSend(notification);
    }


    /**
     * 心率提醒
     *
     * @param onOff    开关                  0 关 1开
     * @param maxValue 报警阀值(100 - 240)
     * @return
     */
    public static byte[] writeForHeartRateAlarm(byte onOff, int maxValue, int minValue) {
//        String valueString = Integer.toHexString(maxValue);
//        if (valueString.length() < 2) {
//            valueString = "0" + valueString;
//        }
//        String onOffstr = "0" + onOff;
//        byte[] bytesByString = DataUtil.getBytesByString(onOffstr + valueString);
        byte[] heartRateAlarm = new byte[]{0x01, 0x0B, onOff, (byte) maxValue, (byte) minValue};
        return Tools.makeSend(heartRateAlarm);
    }


    /**
     * @param mode     0 手动 1 自动
     * @param interval 时间间隔 (1 - 60)
     * @return
     */
    public static byte[] writeForHeartRateMonitor(byte mode, byte interval) {
        byte[] heartRateMonitor = new byte[]{0x01, 0x0C, mode, interval};
        return Tools.makeSend(heartRateMonitor);
    }

    /**
     * 寻找手机
     *
     * @param onOff 0关 1开
     * @return
     */
    public static byte[] writeForFindPhoneSetting(byte onOff) {
        byte[] findPhone = new byte[]{0x01, 0x0D, onOff};
        return Tools.makeSend(findPhone);
    }

    /**
     * 恢复出厂设置
     *
     * @return
     */
    public static byte[] writeForRecoverToDefault() {
        byte[] recoverToDefault = new byte[]{0x01, 0x0E, 0x52, 0x53, 0x59, 0x53};
        return Tools.makeSend(recoverToDefault);
    }


    /**
     * 勿扰模式设置
     *
     * @param onOff     开关
     * @param starrHour 起始时间 小时
     * @param startMin  起始时间 分钟
     * @param endHour   结束时间 小时
     * @param endMin    结束时间 分钟
     * @return
     */
    public static byte[] writeForDonotDisturbe(byte onOff, byte starrHour, byte startMin, byte endHour, byte endMin) {
        byte[] donotDisturbe = new byte[]{0x01, 0x0F, onOff, starrHour, startMin, endHour, endMin};
        return Tools.makeSend(donotDisturbe);
    }


    /**
     * ANCS开关设置
     *
     * @param onOff
     * @return
     */
    public static byte[] writeForANCS(byte onOff) {
        byte[] ancs = new byte[]{0x01, 0x10, onOff};
        return Tools.makeSend(ancs);
    }

    //TODO 有氧运动

    /**
     * 设置语言
     *
     * @param chOrEn 0为英语 1为中文
     * @return
     */
    public static byte[] writeForLauguageSetting(byte chOrEn) {
        byte[] lauguage = new byte[]{0x01, 0x12, chOrEn};
        return Tools.makeSend(lauguage);
    }


    /**
     * 抬腕亮屏开关设置
     *
     * @param onOff 0 关闭        1 打开
     * @return
     */
    public static byte[] writeForLeftTheWristToBright(byte onOff) {
        byte[] brightSetting = new byte[]{0x01, 0x13, onOff};
        return Tools.makeSend(brightSetting);
    }

    /**
     * 亮度设置
     *
     * @param level 0低 1中 2高
     * @return
     */
    public static byte[] writeForBrightnessSetting(byte level) {
        byte[] brightnessSetting = new byte[]{0x01, 0x14, level};
        return Tools.makeSend(brightnessSetting);
    }


    /**
     * ******************************************  02 开头的 *************************************************
     */

    /**
     * 获取基本信息
     * 固件版本号、电池电量等
     *
     * @return
     */
    public static byte[] getBaseInfo() {
        byte[] smsg = {0x02, 0x00, 0x47, 0x43};
        smsg = Tools.makeSend(smsg);
        return smsg;
    }


    /**
     * 获取手环支持的功能列表
     *
     * @return
     */
    public static byte[] writeForGetSupportFunction() {
        byte[] support = new byte[]{0x02, 0x01, 0x47, 0x46};
        return Tools.makeSend(support);
    }


    /**
     * 获取 Mac 地址
     *
     * @return
     */
    public static byte[] writeForGetMac() {
        byte[] getMac = new byte[]{0x02, 0x02, 0x47, 0x4D};
        return Tools.makeSend(getMac);
    }

    /**
     * 获取设备名字或型号
     *
     * @return
     */
    public static byte[] writeForGetDeviceNameOrVersion() {
        byte[] getDeviceName = new byte[]{0x02, 0x03, 0x47, 0x50};
        return Tools.makeSend(getDeviceName);
    }

    /**
     * 获取各种通知的开关
     * 微信、Instagram 等等
     *
     * @return
     */
    public static byte[] writeForGetDeviceNotificationStatus() {
        byte[] getOnOffStatus = new byte[]{0x02, 0x04, 0x47, 0x53};
        return Tools.makeSend(getOnOffStatus);
    }

    /**
     * 获取当前心率
     *
     * @return
     */
    public static byte[] writeForGetCurrentHeartRate() {
        byte[] getCurrentHr = new byte[]{0x02, 0x05, 0x48, 0x52};
        return Tools.makeSend(getCurrentHr);
    }


    /**
     * 获取当前血压
     *
     * @return
     */
    public static byte[] writeForGetCurrentBP() {
        byte[] getBP = new byte[]{0x02, 0x06, 0x42, 0x50};
        return Tools.makeSend(getBP);
    }

    /**
     * ***********************************************************************************************
     */

    /**
     * 寻找手环
     *
     * @param mode        1 开始寻找， 0 停止寻找
     * @param remindTimes 设备提醒次数（1-10），如果App不支持写0
     * @param interval    提醒间隔秒（1-3）
     * @return
     */
    public static byte[] writeForFindBand(byte mode, byte remindTimes, byte interval) {
        byte[] findBand = new byte[]{0x03, 0x00, mode, remindTimes, interval};
        return Tools.makeSend(findBand);
    }


    /**
     * 心率测试开关控制
     *
     * @param mode 0 关闭，1 单次测量，2 监测模式
     * @return
     */
    public static byte[] writeForHRMeasure(byte mode) {
        byte[] hrMeasure = new byte[]{0x03, 0x01, mode};
        Log.e("chen33", Tools.logbyte(Tools.makeSend(hrMeasure)));
        return Tools.makeSend(hrMeasure);
    }

    /**
     * 血压测试开关控制
     *
     * @param mode 0 关闭，1 单次，2 监测
     * @return
     */
    public static byte[] writeForBPMeasure(byte mode) {
        byte[] bpMeasure = new byte[]{0x03, 0x02, mode};
        return Tools.makeSend(bpMeasure);
    }

    /**
     * ppg频率
     *
     * @param mode 0x00: 关闭心率测量
     *             0x01: 采样率由设备自定义
     *             0x02: 22HZ
     *             0x03: 46HZ
     *             0x04: 68HZ
     *             0x05: 84HZ
     *             0x06: 105HZ
     *             0x07: 120HZ
     * @return
     */
    public static byte[] writeForPPGHz(byte mode, byte min) {
        byte[] bpMeasure = new byte[]{0x03, 0x0f, mode, min};
        return Tools.makeSend(bpMeasure);
    }

    /**
     * 血压校准
     *
     * @param systolic  收缩压
     * @param diastolic 舒张压
     * @return
     */
    public static byte[] writeForCorrectBP(int systolic, int diastolic) {
        String systolicStr = Integer.toHexString(systolic);
        if (systolicStr.length() < 2) {
            systolicStr = "0" + systolicStr;
        }
        String diastolicStr = Integer.toHexString(diastolic);
        if (diastolicStr.length() < 2) {
            diastolicStr = "0" + diastolicStr;
        }
        byte[] bytesByString = DataUtil.getBytesByString(systolicStr + diastolicStr);
        byte[] correctBp = new byte[]{0x03, 0x03, bytesByString[0], bytesByString[1]};
        return Tools.makeSend(correctBp);
    }

    /**
     * App退出指令
     * 此命令是为了通知设备关闭蓝牙，有的手机在退出app后蓝牙仍然会和设备进行短暂的连接
     *
     * @return
     */
    public static byte[] writeForAppExit() {
        byte[] appExit = new byte[]{0x03, 0x04, 0x00};
        return Tools.makeSend(appExit);
    }

    /**
     * 有氧教练模式开关控制
     *
     * @param onOff 0 停止， 1 启动
     * @return
     */
    public static byte[] writeForAerobicCoachOnOff(byte onOff) {
        byte[] aerobicCoach = new byte[]{0x03, 0x05, onOff};
        return Tools.makeSend(aerobicCoach);
    }


    /**
     * 绑定设备
     *
     * @param type    0 Android ， 1 IOS
     * @param version 系统版本号
     * @return
     */
    public static byte[] bindDevice(byte type, byte version) {
        byte[] bindDevice = new byte[]{0x03, 0x06, type, version};
        return Tools.makeSend(bindDevice);
    }


    /**
     * 解除绑定
     *
     * @return
     */
    public static byte[] unBindDevice() {
        byte[] unBind = new byte[]{0x03, 0x07, (byte) 0xAA, 0x55};
        return Tools.makeSend(unBind);
    }

    // TODO 信息提醒命令

    public static byte[] writeForInformationNotification(byte type, byte[] title, byte[] content) {
        byte[] title_content = Tools.byteMerger(title, content);
        byte[] notification = new byte[]{0x03, 0x08, type};
        byte[] inforSend = Tools.byteMerger(notification, title_content);
        return Tools.makeSend(inforSend);
    }

    //天医提醒命令
    public static byte[] writeForTYInformationNotification(byte type, byte[] title, byte[] content) {

        byte[] title_content = Tools.byteMerger(title, content);
        byte[] notification = new byte[]{0x03, 0x70, type};
        byte[] inforSend = Tools.byteMerger(notification, title_content);
        return Tools.makeSend(inforSend);
    }


    /**
     * 实时数据上传控制
     *
     * @param onOff    0 关  ， 1 开
     * @param type     0 步数 1 心率 2 血氧 3 血压 4 HRV
     * @param interval 间隔秒（1 - 240）如果 onOff为0，这个填0就可以了
     */
    public static byte[] writeForDataUpload(byte onOff, byte type, byte interval) {
        byte[] upload = new byte[]{0x03, 0x09, onOff, type, interval};
        return Tools.makeSend(upload);
    }


    /**
     * 设置采样率
     *
     * @param type 0 光电波形  ， 1 心电波形
     * @return 查询采样率
     */
    public static byte[] writeForQuerySamplingFreq(byte type) {
        byte[] samplingFreq = new byte[]{0x03, 0x0A, type};
        return Tools.makeSend(samplingFreq);
    }

    /**
     * 波形上传控制
     *
     * @param onOff 0 停止上传 ， 1 开始上传
     * @param type  0 光电       1 心电
     * @return
     */
    public static byte[] writeForWaveUploadControl(byte onOff, byte type) {
        byte[] waveUpload = new byte[]{0x03, 0x0B, onOff, type};
        return Tools.makeSend(waveUpload);
    }


    /**
     * ************************************************************************************************
     */

    /**
     * 手环发出命令，寻找手机，App 返回操作成功与否
     *
     * @param startOrEnd 0 成功 ， 1 失败-不支持
     * @return
     */
    public static byte[] writeForFindPhone(byte startOrEnd) {
        byte[] findPhone = new byte[]{0x04, 0x00, startOrEnd};
        return Tools.makeSend(findPhone);
    }

    /**
     * 防丢提醒
     *
     * @param starOrEnd 0 结束 1 开始
     * @return
     */
    public static byte[] writeForPreventLost(byte starOrEnd) {
        byte[] preventLost = new byte[]{0x04, 0x01, starOrEnd};
        return Tools.makeSend(preventLost);
    }

    /**
     * 接听或者拒接
     * @param answerOrDeny
     * @return
     */
//    // TODO 这是应该是手环发送过来 由手环决定接听还是拒接
////    public static byte[] writeForAnswerOrDenyPhone(byte answerOrDeny){
////        byte[] aOd = new byte[]{0x04,0x02,answerOrDeny};
////        return Tools.makeSend(aOd);
////    }

    /**
     * 1、发送同步指令前APP可发送开始命令；
     * 2、APP端根据实际情况可发送运动、睡眠、心率、血压等所有类型的同么
     * 3、所有数据传输完后APP发送结束指令。
     * 4、APP端如果只同步某一项数据，可无需发送‘風涉厫书鹭篆 只需单独发送某一项数据的同步指令即可f设备也可以板  瘙 輸完后再增大。
     *
     * @param onOff 0 结束 1开始
     * @return
     */
    public static byte[] writeForAllDataOnOff(byte onOff) {
        byte[] allDataOnOff = new byte[]{0x05, 0x00, onOff};
        return Tools.makeSend(allDataOnOff);
    }


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
     * @param command 0 接收成功 1 所有数据接收完毕 2 包数不符 3 字节数不符 4 CRC16错误
     * @return
     */
    public static byte[] writeForAppResponseBlock(byte command) {
        byte[] bytesByString = DataUtil.getBytesByString("0508");
        byte[] response = new byte[]{bytesByString[0], bytesByString[1], command};
        return Tools.makeSend(response);
    }

    /**
     * 同步当天运动数据
     *
     * @param command 0 结束 ， 1 开始
     * @return
     */
    public static byte[] writeForSychronTodaySportData(byte command) {
        byte[] today = new byte[]{0x05, 0x01, command};
        return Tools.makeSend(today);
    }

    /**
     * 同步历史运动数据
     *
     * @param command 0 结束 ， 1 开始
     * @return
     */
    public static byte[] writeForSychronHistorySportData(byte command) {
        byte[] today = new byte[]{0x05, 0x02, command};
        Log.e(" ", Tools.makeSend(today) + "");
        return Tools.makeSend(today);
    }


    /**
     * 同步当天睡眠数据
     *
     * @param command 0 结束 ， 1 开始
     * @return
     */
    public static byte[] writeForSychronTodaySleepData(byte command) {
        byte[] today = new byte[]{0x05, 0x03, command};
        return Tools.makeSend(today);
    }

    /**
     * 同步历史睡眠数据
     *
     * @param command 0 结束 ， 1 开始
     * @return
     */
    public static byte[] writeForSychronHistorySleepData(byte command) {
        byte[] today = new byte[]{0x05, 0x04, command};
        return Tools.makeSend(today);
    }


    /**
     * 同步当天心率数据
     *
     * @param command 0 结束 ， 1 开始
     * @return
     */
    public static byte[] writeForSychronTodayHRData(byte command) {
        byte[] today = new byte[]{0x05, 0x05, command};
        return Tools.makeSend(today);
    }


    /**
     * 同步历史心率数据
     *
     * @param command 0 结束 ， 1 开始
     * @return
     */
    public static byte[] writeForSychronHistoryHRData(byte command) {
        byte[] today = new byte[]{0x05, 0x06, command};
        return Tools.makeSend(today);
    }


    /**
     * 同步当天血压数据
     *
     * @param command 0 结束 ， 1 开始
     * @return
     */
    public static byte[] writeForSychronTodayBPData(byte command) {
        byte[] today = new byte[]{0x05, 0x07, command};
        return Tools.makeSend(today);
    }

    public static byte[] writeForSychronHistoryData(byte dataType) {
        byte[] today = new byte[]{0x05, dataType, 0x01};
        return Tools.makeSend(today);
    }

    public static byte[] writeForDeleteHistoryData(byte dataType) {
        byte[] today = new byte[]{0x05, dataType, 0x02};
        return Tools.makeSend(today);
    }

    /**
     * 同步历史血压数据
     *
     * @param command 0 结束 ， 1 开始
     * @return
     */
    public static byte[] writeForSychronHistoryBPData(byte command) {
        byte[] today = new byte[]{0x05, 0x08, command};
        return Tools.makeSend(today);
    }

    /**
     * 删除运动数据
     *
     * @param command 0 当天  1 历史  2 所有
     * @return
     */
    public static byte[] writeForDeleteSportData(byte command) {
        byte[] deleteSport = new byte[]{0x05, 0x40, command};
        return Tools.makeSend(deleteSport);
    }


    /**
     * 删除睡眠数据
     *
     * @param command 0 当天  1 历史  2 所有
     * @return
     */
    public static byte[] writeForDeleteSleepData(byte command) {
        byte[] deleteSport = new byte[]{0x05, 0x41, command};
        return Tools.makeSend(deleteSport);
    }


    /**
     * 删除心率数据
     *
     * @param command 0 当天  1 历史  2 所有
     * @return
     */
    public static byte[] writeForDeleteHRData(byte command) {
        byte[] deleteSport = new byte[]{0x05, 0x42, command};
        return Tools.makeSend(deleteSport);
    }


    /**
     * 删除血压数据
     *
     * @param command 0 当天  1 历史  2 所有
     * @return
     */
    public static byte[] writeForDeleteBPData(byte command) {
        byte[] deleteSport = new byte[]{0x05, 0x43, command};
        return Tools.makeSend(deleteSport);
    }


    /**
     * 测试血压
     *
     * @param command 0关闭，2开始
     * @return
     */
    public static byte[] writeForTestBPData(byte command) {
        byte[] deleteSport = new byte[]{0x03, 0x02, command};
        return Tools.makeSend(deleteSport);
    }

    /**
     * 上传波形控制
     *
     * @return
     */
    public static byte[] writeForUpdateWaveData() {
        byte[] deleteSport = new byte[]{0x03, 0x0b, 0x01, 0x01};
        return Tools.makeSend(deleteSport);
    }

    /**
     * 查询采样率
     *
     * @return
     */
    public static byte[] writeForSamplingRate() {
        byte[] deleteSport = new byte[]{0x03, 0x0a, 0x00};
        return Tools.makeSend(deleteSport);
    }


    /**
     * 运动模式
     *
     * @param type 0 走路，1 跑步，2 骑行，3 健身
     * @param open 0 停止，1 开始
     */
    public static byte[] setSportType(int type, int open) {

        if (type == 0) {
            //        lal4.text = @"走路";
            byte[] deleteSport = new byte[]{0x03, 0x0C, (byte) open, 0x00};
            return Tools.makeSend(deleteSport);
        } else if (type == 1) {
            //        lal4.text = @"跑步";
            byte[] deleteSport = new byte[]{0x03, 0x0C, (byte) open, 0x01};
            return Tools.makeSend(deleteSport);
        } else if (type == 2) {
            //        lal4.text = @"骑行";
            byte[] deleteSport = new byte[]{0x03, 0x0C, (byte) open, 0x03};
            return Tools.makeSend(deleteSport);
        } else if (type == 3) {
            //        lal4.text = @"健身";
            byte[] deleteSport = new byte[]{0x03, 0x0C, (byte) open, 0x04};
            return Tools.makeSend(deleteSport);
        }
        byte[] deleteSport = new byte[]{0x03, 0x0C, (byte) open, 0x04};
        return Tools.makeSend(deleteSport);
    }

    /**
     * 肤色设置
     * 0-白 、1-白间黄、 2-黄、3-棕色、4-褐色、5-黑
     */
    public static byte[] setSkinColor(byte type) {
        byte[] skinColor = new byte[]{0x01, 0x15, type};
        return Tools.makeSend(skinColor);
    }

    /**
     * 血氧测试开关控制
     * 0-关闭 1-单次测量 2-监测模式
     *
     * @return
     */
    public static byte[] writeForBloodOxygenMeasure(byte type) {
        byte[] bloodOxygen = new byte[]{0x01, 0x10, type};
        return Tools.makeSend(bloodOxygen);
    }

    /**
     * 呼吸率测试开关控制
     * 0-关闭 1-单次测量 2-监测模式
     *
     * @return
     */

    public static byte[] writeForRespiratory_Rate(byte type) {
        byte[] RespiratoryRate = new byte[]{0x01, 0x10, type};
        return Tools.makeSend(RespiratoryRate);
    }

    /**
     * 天气信息数据传送
     *
     * @return
     */
    // TODO
    public static byte[] writeForWeather(String minTemp, String maxTemp, String realTemp, int code) {
        byte[] low;
        byte[] high;
        byte[] temp;
        byte[] strCode;
        byte[] weather = new byte[]{0x03, 0x12};
        try {
            low = minTemp.getBytes("utf-8");
            high = maxTemp.getBytes("utf-8");
            temp = realTemp.getBytes("utf-8");
            strCode = new byte[]{(byte) code, 0x00};


            //最低温度
            byte[] minTempByteHead = new byte[]{0x00, (byte) (minTemp.length() & 0xff), (byte) ((minTemp.length() >> 8) & 0xff)};
            byte[] minTempByte = Tools.byteMerger(minTempByteHead, low);

            //最高温度
            byte[] maxTempByteHead = new byte[]{0x01, (byte) (maxTemp.length() & 0xff), (byte) ((maxTemp.length() >> 8) & 0xff)};
            byte[] maxTempByte = Tools.byteMerger(maxTempByteHead, high);

            //实时温度
            byte[] realTempByteHead = new byte[]{0x02, (byte) (realTemp.length() & 0xff), (byte) ((realTemp.length() >> 8) & 0xff)};
            byte[] realTempByte = Tools.byteMerger(realTempByteHead, temp);

            //天气编码类型
            byte[] codeByteHead = new byte[]{0x04, 0x02, 0x00};
            byte[] codeByte = Tools.byteMerger(codeByteHead, strCode);

            byte[] minAndMax = Tools.byteMerger(minTempByte, maxTempByte);

            byte[] minAndMaxAndReal = Tools.byteMerger(minAndMax, realTempByte);

            byte[] all = Tools.byteMerger(minAndMaxAndReal, codeByte);

            weather = Tools.byteMerger(weather, all);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return Tools.makeSend(weather);
    }


    /**
     * 血压范围设置 0-偏低 1-正常 2-轻微偏高 3-中度偏高 4-重度高
     *
     * @return
     */

    public static byte[] writeForBloodPressure(byte cammand) {
        byte[] RespiratoryRate = new byte[]{0x01, 0x16, cammand};
        return Tools.makeSend(RespiratoryRate);
    }

    /**
     * 设置睡眠提醒时间
     *
     * @param hour    小时（0-23）
     * @param minutes 分钟 (0-59)
     * @param onOff   true 开 false关
     * @param weekStr (首位开关、其他为周一到周日)
     */
    public static byte[] writeForSleepToRemindSetting(int hour, int minutes, boolean onOff, String weekStr) {
        int week = getweek(onOff, weekStr);
        byte[] sleepToRemindByte = new byte[]{0x01, 0x1A, (byte) hour, (byte) minutes, (byte) week};
        return Tools.makeSend(sleepToRemindByte);
    }


    public static int getweek(boolean onOrOff, String week) {
        String[] str = week.split(",");
        String val = "";
        for (int i = 0; i < 8; i++) {
            if (i == 7) {
                if (onOrOff) {
                    val = "1" + val;
                } else {
                    val = "0" + val;
                }
                break;
            }
            boolean on = false;
            for (String msg : str) {
                if (i == Integer.parseInt(msg) - 1) {
                    on = true;
                    break;
                }
            }
            if (on) {
                val = "1" + val;
            } else {
                val = "0" + val;
            }
        }

        int weekRemind = Integer.parseInt(Tools.BinaryToHex(val), 16);
        return weekRemind;
    }

    /**
     * 配置PPG数据定时收集
     *
     * @param isOpen       是否打开
     * @param timeLen      时长 单位秒
     * @param timeInterval 时间间隔 单位分
     */
    public static byte[] writeForPpgDataCollectSetting(int isOpen, int timeLen, int timeInterval) {
        byte[] ppgDataSetting = new byte[]{0x01, 0x1b, (byte) (isOpen & 0xff), 0x00, (byte) (timeLen & 0xff), (byte) ((timeLen >> 8) & 0xff), (byte) (timeInterval & 0xff), (byte) ((timeInterval >> 8) & 0xff)};
        return Tools.makeSend(ppgDataSetting);
    }

    /**
     * 健康参数、预警信息发送
     *
     * @param warningState  预警状态（0-无预警，1-预警生效中、0xff-本字段无效）
     * @param healthState   健康状态(0-未知、1-优秀、2-良好、3-一般、4-较差、5-生病、0xff-本字段无效)
     * @param healthCount   健康指数（0-120  0xff-本字段无效）
     * @param friendWarning 亲友预警（0-无预警、1-预警生效中）
     */
    public static byte[] writeForHealthParamAndWarningInformation(int warningState, int healthState, int healthCount, int friendWarning) {
        byte[] healthParamAndWarningInformationByte = new byte[]{0x03, 0x15, (byte) warningState, (byte) healthState, (byte) healthCount, (byte) friendWarning
                , 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,};
        return Tools.makeSend(healthParamAndWarningInformationByte);
    }

    /**
     * 查询历史采集数据记录条数
     *
     * @param type 数据类型
     */
    public static byte[] writeForQueryHistoryData(int type) {
        byte[] queryECGHistory = {0x07, 0x00, (byte) type};
        return Tools.makeSend(queryECGHistory);
    }

    /**
     * 获取历史采集数据
     *
     * @param type     数据类型
     * @param index    记录序号
     * @param isUpload 是否开始数据上传
     */
    public static byte[] writeForGetHistortData(int type, int index, int isUpload) {
        byte[] getHistortData = {0x07, 0x01, (byte) type, (byte) index, 0x00, (byte) isUpload};
        return Tools.makeSend(getHistortData);
    }

    /**
     * 获取历史采集数据时间
     *
     * @param type 数据类型
     */
    public static byte[] getHistoryDataTimes(int type, boolean isQueryTime) {
        isQueryTimes = isQueryTime;
        return writeForQueryHistoryData(type);
    }

    public static boolean isQueryTime() {
        return isQueryTimes;
    }

    public static void setQueryTime(boolean queryTimes) {
        isQueryTimes = queryTimes;
    }

    /**
     * 获取历史采集数据
     *
     * @param type      数据类型
     * @param timeStamp 时间戳
     * @return 数据集 byte[]
     */
    public static byte[] getHistoryDataWithTime(int type, long timeStamp) {
        long time = timeStamp - 946684800L;
        byte[] getHistoryData = {0x07, 0x02, (byte) type,
                (byte) (time & 0xff), (byte) ((time >> 8) & 0xff), (byte) ((time >> 16) & 0xff), (byte) ((time >> 24) & 0xff), 0x01};
        return Tools.makeSend(getHistoryData);
    }

    /**
     * @param dataType  数据类型
     * @param replyType 数据是否接收成功
     */
    //每十包数据回复
    public static byte[] replyECGData(int dataType, int replyType) {
        byte[] smsg = {0x07, 0x20, (byte) dataType, (byte) replyType};
        return Tools.makeSend(smsg);
    }

    /**
     * 停止上传数据
     *
     * @param dataType 数据类型
     */
    //每十包数据回复
    public static byte[] replyData(int dataType) {
        byte[] smsg = {0x07, 0x20, (byte) dataType, (byte) 0x04};
        return Tools.makeSend(smsg);
    }

//    /**
//     * @param dataType    数据类型
//     * @param deleteIndex 记录编号
//     */
//    // //删除历史采集数据
//    public static byte[] deleteECGData(int dataType, int deleteIndex) {
//        byte[] smsg = {0x07, 0x30, (byte) dataType, (byte) deleteIndex, 0x00};
//        return Tools.makeSend(smsg);
//    }

    /**
     * @param dataType    数据类型
     * @param deleteIndex 记录编号
     */
    // //删除历史采集数据
    public static byte[] deleteECGData(int dataType, int deleteIndex) {
        byte[] smsg = {0x07, 0x30, (byte) dataType, (byte) deleteIndex, (byte) ((deleteIndex >> 8) & 0xff)};
        return Tools.makeSend(smsg);
    }

    /**
     * @param dataType  数据类型
     * @param timeStamp 时间戳
     */
    // //删除历史采集数据
    public static byte[] deleteDataWithTime(int dataType, long timeStamp) {
        long time = timeStamp - 946684800L;
        byte[] smsg = {0x07, 0x31, (byte) dataType,
                (byte) (time & 0xff), (byte) ((time >> 8) & 0xff), (byte) ((time >> 16) & 0xff), (byte) ((time >> 24) & 0xff)};
        return Tools.makeSend(smsg);
    }


//    /**
//     * ecg,0关闭，2打开
//     * @return
//     */
//    public static byte[] startECG(int open){
//
//       if (open == 0){
//           return closeEcg();
//       }else{
//           return openEcg2();
//       }
//    }


    //    //打开开关
//    public static byte[] openEcg1() {
//
//        byte[] smsg = {0x03, 0x02, 0x02};
//        return Tools.makeSend(smsg);
//
//    }
//
//    //打开开关
//    public static byte[] openEcg2() {
//        byte[] smsg = {0x03,0x0b,0x01,0x01};
//        return Tools.makeSend(smsg);
//
//
//    }
//
    public static byte[] closeEcg() {
        byte[] smsg = {0x03, 0x02, 0x00};
        return Tools.makeSend(smsg);
    }
}
