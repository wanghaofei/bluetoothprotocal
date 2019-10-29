package com.zhuoting.health.bean;

//import com.zhuoting.health.tools.Tools;

import com.zhuoting.health.util.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by cowork16 on 2017/4/11.
 */

public class RunInfo implements Serializable{

    public float desSize;//距离单位km
    public String listUuid;
    public int runTime;

    public float kmh;//时速
    public String ps;//配速
    public long beginDate;

    public String timeFormat;
    public String timeStr;

    public int heartTimes;

    public RunInfo(){
        beginDate = System.currentTimeMillis();
        listUuid = Tools.getUUID();
        ps = 0+"'"+0+"\"";
    }

    public void makeKmh(){
        //时速
        if (runTime > 0) {
            kmh = desSize/(runTime/3600.0f);
        }

        //配速
        int pst = 0;
        if (desSize > 0) {
            pst = (int)(1/desSize * runTime);
        }

        makeTimePs(pst);
    }

    public void makeTimePs(int time){


        int ss = time % 60;
        int mm = (time-ss)/60 % 60;

        //    0'0''km

        ps = mm+"'"+ss+"\"";
    }

    public JSONObject objectToDictionary(){

        JSONObject dlist = new JSONObject();
        try {
            dlist.put("desSize", desSize);
            dlist.put("listUuid", listUuid);
            dlist.put("runTime", runTime);
            dlist.put("kmh", kmh);
            dlist.put("ps", ps);
            dlist.put("beginDate", beginDate);
            dlist.put("heartTimes", heartTimes);
        }catch (JSONException e){

        }

        return dlist;
    }

    public void setValue(JSONObject dlist){

        try {

            desSize = (float) dlist.getDouble("desSize");
            listUuid = dlist.getString("listUuid");
            runTime = dlist.getInt("runTime");
            kmh = (float) dlist.getDouble("kmh");
            ps = dlist.getString("ps");
            beginDate = dlist.getLong("beginDate");
            heartTimes = dlist.getInt("heartTimes");

            System.out.println(beginDate+"");

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));

            Date date = new Date(beginDate);
            timeFormat = simpleDateFormat.format(date);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    @Override
    public String toString() {
        return "RunInfo{" +
                "desSize=" + desSize +
                ", listUuid='" + listUuid + '\'' +
                ", runTime=" + runTime +
                ", kmh=" + kmh +
                ", ps='" + ps + '\'' +
                ", beginDate=" + beginDate +
                ", timeFormat='" + timeFormat + '\'' +
                ", timeStr='" + timeStr + '\'' +
                ", heartTimes=" + heartTimes +
                '}';
    }
}
