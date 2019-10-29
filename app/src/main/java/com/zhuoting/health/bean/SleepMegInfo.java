package com.zhuoting.health.bean;

import com.zhuoting.health.util.Tools;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by cowork16 on 2017/8/8.
 */

public class SleepMegInfo {
//    public static int sleepType;//1深睡,2浅睡
//    public static long stime;
//    public static int sleepLong;

    private int sleepType;//1深睡,2浅睡
    private long stime;
    private int sleepLong;


    public int getSleepType() {
        return sleepType;
    }

    public void setSleepType(int sleepType) {
        this.sleepType = sleepType;
    }

    public long getStime() {
        return stime;
    }

    public void setStime(long stime) {
        this.stime = stime;
    }

    public int getSleepLong() {
        return sleepLong;
    }

    public void setSleepLong(int sleepLong) {
        this.sleepLong = sleepLong;
    }

    @Override
    public String toString() {
        return "SleepMegInfo{" +
                "sleepType=" + sleepType +
                ", stime=" + stime +
                ", sleepLong=" + sleepLong +
                '}';
    }

    public SleepMegInfo() {

    }


    public void initWithData(byte[] data) {

        sleepType = (data[0] & 0xff);

        if (sleepType == 241) {
            sleepType = 1;
        } else {
            sleepType = 2;
        }


        byte[] btime = {data[4], data[3], data[2], data[1]};
        stime = 946684800L + TransUtils.Bytes2Dec(btime);
        stime = stime * 1000;
        stime = stime - Tools.getTimeOffset();
        byte[] etime = {0x00, data[7], data[6], data[5]};
        sleepLong = TransUtils.Bytes2Dec(etime);


    }


//    public static JSONObject objectToDictionary() {
//
//        JSONObject jsonObject = new JSONObject();
////    NSInteger sleepType;//1深睡,2浅睡
////    NSDate *stime;
////    NSInteger sleepLong;
//
//        try {
//            jsonObject.put("stime", stime);
//            jsonObject.put("sleepType", sleepType);
//            jsonObject.put("sleepLong", sleepLong);
//        } catch (JSONException e) {
//
//        }
//
//
//        return jsonObject;
//    }

    public void setValue(JSONObject jsonObject) {
        try {
            stime = jsonObject.getLong("stime");
            sleepType = jsonObject.getInt("sleepType");                 //总步数  3字节  步
            sleepLong = jsonObject.getInt("sleepLong");                  //运动距离  3字节 M
        } catch (JSONException e) {

        }
    }




}
