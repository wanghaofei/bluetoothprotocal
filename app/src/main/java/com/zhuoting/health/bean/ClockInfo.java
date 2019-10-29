package com.zhuoting.health.bean;


import android.util.Log;

import com.zhuoting.health.util.Tools;

import java.io.Serializable;


public class ClockInfo implements Serializable{
	public boolean t_open; //是否打开闹钟
	public int c_hour;  //小时
	public int c_min;   //分钟
	//第一位开关，后面7位，周一至周日重复
	public String valueArray;
	//0x00:起床 0x01:睡觉 0x02:锻炼 0x03:吃药 0x04:约会 0x05:聚会 0x06:会议 0x07:自定义
	public int type;

	//贪睡0-59;
	public int uplater;

	public ClockInfo(){
		valueArray = "0";
	}

	public ClockInfo(byte []smsg){
		System.out.println(Tools.logbyte(smsg));

		int week =  (smsg[3] & 0xff);

		String str = Integer.toBinaryString(week);
		System.out.println(str.length());
		if (str.length() < 8) {
			int lg = 8-str.length();
			for (int i = 0; i < lg; i++) {
				str = "0"+str;
			}
		}

//		if (week == 0){
//			str = "0";
//		}
		

		
		String val = "";
		for (int i = 0; i<8; i++) {
		     int ts = Integer.parseInt(str.substring(i, i+1));
		     if (i == 0) {
				if (ts == 0) {
					t_open = false;
				}else{
					t_open = true;
				}
			}else{
				if (ts == 1) {
					if (!val.equals("")) {
						val = (8-i) + "," + val;
					}else{
						val = (8-i) + val;
					}
				}
			}
		}
		if (val.equals("")) {
			val = "0";
		}
		
		valueArray = val;


		byte[] rates2 = { 0x00, smsg[1] };
		c_hour = smsg[1] & 0xff;//Tools.BCD_TO_TEN(TransUtils.bytes2short(rates2));
		
		byte[] rates3 = { 0x00, smsg[2] };
		c_min = smsg[2] & 0xff;//Tools.BCD_TO_TEN(TransUtils.bytes2short(rates3));

//		byte[] rates4 = { 0x00, smsg[0] };
		type = smsg[0] & 0xff;

		uplater = smsg[4] & 0xff;
	}
	
	public int getweek(){
		//"11111101";
		String[] str = valueArray.split(",");
		String val = "";
		for (int i = 0; i<8; i++) {
			if (i == 7) {
				if (t_open) {
					val = "1" + val;
				}else{
					val = "0" + val;
				}
				break;
			}
			boolean on = false;
			for (String msg : str) {
				if (i == Integer.parseInt(msg)-1) {
					on = true;
					break;
				}
			}
			if (on) {
				val = "1" + val;
			}else{
				val = "0" + val;
			}
		}

		int week = Integer.parseInt(Tools.BinaryToHex(val), 16);
		return week;
	}


	@Override
	public String toString() {
		return "ClockInfo{" +
				"t_open=" + t_open +
				", c_hour=" + c_hour +
				", c_min=" + c_min +
				", valueArray='" + valueArray + '\'' +
				'}';
	}
}
