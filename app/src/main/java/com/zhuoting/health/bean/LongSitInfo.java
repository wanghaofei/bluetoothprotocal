package com.zhuoting.health.bean;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import org.json.JSONException;
import org.json.JSONObject;

public class LongSitInfo {
	public int s_hour1;//开始时间1：时
	public int s_min1; //开始时间1：分
	public int e_hour1;//结束时间1：时
	public int e_min1; //结束时间1：分

	public int s_hour2;//开始时间2：时
	public int s_min2; //开始时间2：分
	public int e_hour2;//结束时间2：时
	public int e_min2;//结束时间2：分

	public int remindGap;//1,15分钟，2，30分钟，3，45分钟，4，60分钟
	public boolean open; //是否开启
	public String valueArray;//周一至周日的值


	public LongSitInfo(){
		//周一，周二，周三，周四，周五
		valueArray = "1,2,3,4,5";
		s_hour1 = 8;
		e_hour1 = 12;
		s_hour2 = 13;
		s_min2 = 30;
		e_hour2 = 15;
		e_min2 = 30;

		open = false;
		remindGap = 1;
	}
	public void setValue(Context context){	
		SharedPreferences sp = context.getSharedPreferences("smartam", context.MODE_PRIVATE);
		String msg = sp.getString("longsit", null);
		
		if (msg == null) {
			return;
		}
		
		try {
			JSONObject list = new JSONObject(msg);
			
			s_hour1 = list.getInt("s_hour1");
			s_min1 = list.getInt("e_min1");

			e_hour1 = list.getInt("e_hour1");
			e_min1 = list.getInt("e_min1");

			s_hour2 = list.getInt("s_hour2");
			s_min2 = list.getInt("e_min2");

			e_hour2 = list.getInt("e_hour2");
			e_min2 = list.getInt("e_min2");

			remindGap = list.getInt("remindGap");
			open = list.getBoolean("open");

			valueArray = list.getString("valueArray"); 

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void saveValue(Context context){
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("s_hour1", s_hour1);
			jsonObject.put("s_min1", s_min1);
			jsonObject.put("e_hour1", e_hour1);
			jsonObject.put("e_min1", e_min1);
			jsonObject.put("s_hour2", s_hour2);
			jsonObject.put("s_min2", s_min2);
			jsonObject.put("e_hour2", e_hour2);
			jsonObject.put("e_min2", e_min2);
			jsonObject.put("remindGap", remindGap);
			jsonObject.put("open", open);
			jsonObject.put("valueArray", valueArray);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		SharedPreferences sp = context.getSharedPreferences("smartam", context.MODE_PRIVATE);
		//存入数据
		Editor editor = sp.edit();
		editor.putString("longsit", jsonObject.toString());
		editor.commit();
	}
	
	public String getTime1(){
		String str = "";
		
		if (s_hour1<10) {
			str = str+"0"+s_hour1;
		}else{
			str = str+s_hour1;
		}
		str = str+":";
		if (s_min1<10) {
			str = str+"0"+s_min1;
		}else{
			str = str+s_min1;
		}
		
		return str;
	}

	public String getTime2(){
		String str = "";
		
		if (e_hour1<10) {
			str = str+"0"+e_hour1;
		}else{
			str = str+e_hour1;
		}
		str = str+":";
		if (e_min1<10) {
			str = str+"0"+e_min1;
		}else{
			str = str+e_min1;
		}
		
		return str;
	}
	
	public String getTime3(){
		String str = "";
		
		if (s_hour2<10) {
			str = str+"0"+s_hour2;
		}else{
			str = str+s_hour2;
		}
		str = str+":";
		if (s_min2<10) {
			str = str+"0"+s_min2;
		}else{
			str = str+s_min2;
		}
		
		return str;
	}

	public String getTime4(){
		String str = "";
		
		if (e_hour2<10) {
			str = str+"0"+e_hour2;
		}else{
			str = str+e_hour2;
		}
		str = str+":";
		if (e_min2<10) {
			str = str+"0"+e_min2;
		}else{
			str = str+e_min2;
		}
		
		return str;
	}

	public byte[] makeSendByte(){
		String[] str = valueArray.split(",");
		String val = "";
		for (int i = 0; i<8; i++) {
			boolean on = false;
			for (String msg : str) {
				if (i == Integer.parseInt(msg)-1) {
					on = true;
					break;
				}
			}

			if (i == 7 && open){
				on = true;
			}


			if (on) {
				val = "1" + val;
			}else{
				val = "0" + val;
			}
		}


		int week = Integer.parseInt(val, 2);



		int shour1 = s_hour1;
		int smin1 = s_min1;

		int ehour1 = e_hour1;
		int emin1 = e_min1;

		int shour2 = s_hour2;
		int smin2 = s_min2;

		int ehour2 = e_hour2;
		int emin2 = e_min2;

		int rra = 0;

		if (remindGap == 1) {
			rra = 15;
		}else if (remindGap == 2) {
			rra = 30;
		}else if (remindGap == 3) {
			rra = 45;
		}else if (remindGap == 4) {
			rra = 60;
		}



		byte[] smsg = {0x01,0x05,(byte) shour1,(byte) smin1,(byte) ehour1,(byte) emin1,
				(byte) shour2,(byte) smin2,(byte) ehour2,(byte) emin2,(byte) rra,(byte) week};

		return  smsg;
	}
	@Override
	public String toString() {
		return "LongSitInfo{" +
				"s_hour1=" + s_hour1 +
				", s_min1=" + s_min1 +
				", e_hour1=" + e_hour1 +
				", e_min1=" + e_min1 +
				", s_hour2=" + s_hour2 +
				", s_min2=" + s_min2 +
				", e_hour2=" + e_hour2 +
				", e_min2=" + e_min2 +
				", remindGap=" + remindGap +
				", open=" + open +
				", valueArray='" + valueArray + '\'' +
				'}';
	}
}
