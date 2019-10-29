package com.zhuoting.health.bean;

import org.json.JSONException;
import org.json.JSONObject;

public class PhoneModel {
	public String name;
	public String phone;
	
	public void jsonToObj(JSONObject object){
        try {

            if (!object.isNull("name")) {
                name = object.getString("name");
            }

            if (!object.isNull("phone")) {
            	phone = object.getString("phone");
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



    public JSONObject getJsonObj(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", name);
            jsonObject.put("phone", phone);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    public String toString() {
        return "PhoneModel{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
