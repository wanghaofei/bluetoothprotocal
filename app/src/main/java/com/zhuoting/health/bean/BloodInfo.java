package com.zhuoting.health.bean;

import android.database.Cursor;


import com.zhuoting.health.util.Tools;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by cowork16 on 2017/8/8.
 */

public class BloodInfo {
    public int SBP;//收缩压
    public int DBP;//舒张压

    public long rtime; //时间戳
    public String rtimeFormat;//时间日期
    public String timeStr;//时间 时-分

    public BloodInfo(){

    }

    public void fameDate(){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        format.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        rtimeFormat = format.format(new Date(rtime));

        format=new SimpleDateFormat("HH:mm");
        format.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        timeStr = format.format(new Date(rtime));
    }

    public void initWithData(byte[] data){



        byte[] btime = {data[3], data[2], data[1], data[0]};
        rtime = 946684800L + TransUtils.Bytes2Dec(btime);
        rtime = rtime * 1000;
        rtime = rtime-Tools.getTimeOffset();

        byte[] SBPs = {0x00,0x00,0x00, data[6]};
        SBP = TransUtils.Bytes2Dec(SBPs);

        byte[] DBPs = {0x00,0x00,0x00, data[5]};
        DBP = TransUtils.Bytes2Dec(DBPs);

        fameDate();
    }

    @Override
    public String toString() {
        return "BloodInfo{" +
                "SBP=" + SBP +
                ", DBP=" + DBP +
                ", rtime=" + rtime +
                ", rtimeFormat='" + rtimeFormat + '\'' +
                ", timeStr='" + timeStr + '\'' +
                '}';
    }

    public void sqlinster(){

        String sql = "INSERT INTO blood (rtime,rtimeFormat,SBP,DBP) VALUES (?,?,?,?)";
//        DBHelper.getInstance(null).execSQL(sql,new Object[]{rtime,rtimeFormat,SBP,DBP});
    }

    public void setCursor(Cursor cursor){

        rtime = cursor.getLong(cursor.getColumnIndex("rtime"));
        rtimeFormat = cursor.getString(cursor.getColumnIndex("rtimeFormat"));
        SBP = cursor.getInt(cursor.getColumnIndex("SBP"));
        DBP = cursor.getInt(cursor.getColumnIndex("DBP"));

        fameDate();
    }
}
