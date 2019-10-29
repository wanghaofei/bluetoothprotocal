package com.zhuoting.health.write;

import android.os.Message;

import com.zhuoting.health.notify.IAIDataResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProtocolAIWriter {

    public static String AiLogin = "http://www.kangyuanai.com/api/login/thirdLogin";
    public static String puclicKey = "e6411Z54MmxxzmFEaRlSuTcO3bCmE78U75QFHUedZw";
    public static String ecgUrl = "http://www.kangyuanai.com/api/ecg_report/userEcgReportThirdTest";
    private static Request request;
    private static Call call;

    public static IAIDataResponse iaiDataResponse;

    private ProtocolAIWriter() {
    }

    private static ProtocolAIWriter mProtocolAIWriter;

    public static ProtocolAIWriter newInstance() {                     // 单例模式，双重锁
        if (mProtocolAIWriter == null) {
            synchronized (ProtocolAIWriter.class) {
                if (mProtocolAIWriter == null) {
                    mProtocolAIWriter = new ProtocolAIWriter();
                }
            }
        }
        return mProtocolAIWriter;
    }


    public void setIaiDataResponse(IAIDataResponse iaiDataResponse) {
        this.iaiDataResponse = iaiDataResponse;
    }

    /**
     * AI诊断
     */
    public static void writeForAI(final List<Integer> testlist, final String deviceName, final int age, final int sex, final String phone, final int heart, final int maxBlood, final int minBlood) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
        RequestBody requestBodyPost = new FormBody.Builder()
                .add("merchant_name", "szyc0808")
                .add("merchant_password", "c3p5YzA4MDg=")
                .build();
        Request requestPost = new Request.Builder()
                .url(AiLogin)
                .addHeader("Content-Type", "application/json")
                .post(requestBodyPost)
                .build();
        client.newCall(requestPost).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String string = response.body().string();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(string);
                            int code = jsonObject.getInt("code");
                            if (code == 200) {
                                testzd(testlist, deviceName, age, sex, phone, heart, maxBlood, minBlood);
                            } else {

                            }
                        } catch (JSONException e) {

                        }
                    }
                }).start();
            }
        });
    }


    public static void testzd(List<Integer> testlist, String deviceName, int age, int sex, String phone, int heart, int maxBlood, int minBlood) {
        String lan = Locale.getDefault().getLanguage();
        String language = "";
        if (lan.toUpperCase().equals("ZH")) {
            language = "CH";
        } else {
            language = "EN";
        }
        JSONObject object = new JSONObject();
        try {
            object.put("device_sn", deviceName);
            object.put("age", age);
            object.put("gender", sex);
            object.put("cellphone", phone);
            object.put("lead_name", "1");
            object.put("lead_data", new JSONArray(testlist).toString());
            object.put("scale_value", 70);
            object.put("sample_base", 83);
            object.put("language", language);
            object.put("past_illness", "-");
            object.put("heart_beat", heart);
            object.put("maxBP", maxBlood);
            object.put("minBP", minBlood);
            //0代表芭乐。1代表通用
            object.put("show_type", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        OkHttpClient mOkHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS).build();


        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), object.toString());
        String token = "c3p5YzA4MDg=";
        request = new Request.Builder()
                .url(ecgUrl)
                .post(requestBody)
                .addHeader("Content-Type", "application/json;charset:utf-8")
                .addHeader("merchantname", token)
                .addHeader("publickey", puclicKey)
                .build();
        call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Message msg = new Message();
                msg.obj = "";
                if (e.getMessage() == null || e.getMessage().toString().equals("Socket closed")) {
                    msg.what = -1;
                } else {
                    msg.what = -2;
                }

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                iaiDataResponse.onAIUrlResponse(str);
            }
        });
    }
}
