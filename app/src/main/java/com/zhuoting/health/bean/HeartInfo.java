package com.zhuoting.health.bean;

import android.database.Cursor;


import com.zhuoting.health.util.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by cowork16 on 2017/8/8.
 */

public class HeartInfo {
    public long rtime;
    public int heartTimes;
    public int hour;
    public String rtimeFormat;
    public String timeStr;

    public HeartInfo(){

    }

    public void fameDate(){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        format.setTimeZone(TimeZone.getDefault());
        rtimeFormat = format.format(new Date(rtime));

        format=new SimpleDateFormat("HH:mm");
        format.setTimeZone(TimeZone.getDefault());

        timeStr = format.format(new Date(rtime));

        System.out.print(timeStr);

        format=new SimpleDateFormat("HH");
        format.setTimeZone(TimeZone.getDefault());

        hour = Integer.parseInt(format.format(new Date(rtime)));
    }

    public void initWithData(byte[] data){


        if (data.length==6){
            byte[] btime = {data[3], data[2], data[1], data[0]};
            rtime = 946684800L + TransUtils.Bytes2Dec(btime);
            rtime = rtime * 1000;
            rtime = rtime-Tools.getTimeOffset();

            byte[] heartTimess = {0x00,0x00,0x00, data[5]};
            heartTimes = TransUtils.Bytes2Dec(heartTimess);


            fameDate();
        }else {
            //心率数据长度不对
            //Tools.writeFile(ErrorUtil.ERROR_3,"MeCareLog.txt");
        }
    }

    public void sqlinster(){
        String sql = "INSERT INTO heart (rtime,rtimeFormat,heartTimes) VALUES (?,?,?)";
       // DBHelper.getInstance(null).execSQL(sql,new Object[]{rtime,rtimeFormat,heartTimes});
    }

    public void setCursor(Cursor cursor){

        rtime = cursor.getLong(cursor.getColumnIndex("rtime"));
        rtimeFormat = cursor.getString(cursor.getColumnIndex("rtimeFormat"));
        heartTimes = cursor.getInt(cursor.getColumnIndex("heartTimes"));

        fameDate();
    }

    public Map<String, Object> objectToDictionary(){

        Map<String, Object> dlist = new HashMap<String, Object>();
        dlist.put("rtime", rtime);
        dlist.put("heartTimes", heartTimes);
        dlist.put("hour", hour);
        return dlist;
    }

    public void setValue(JSONObject dlist){

        try {

            rtime = dlist.getLong("rtime");
            heartTimes = dlist.getInt("heartTimes");
            hour = dlist.getInt("hour");
            fameDate();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "HeartInfo{" +
                "rtime=" + rtime +
                ", heartTimes=" + heartTimes +
                ", hour=" + hour +
                ", rtimeFormat='" + rtimeFormat + '\'' +
                ", timeStr='" + timeStr + '\'' +
                '}';
    }
}
