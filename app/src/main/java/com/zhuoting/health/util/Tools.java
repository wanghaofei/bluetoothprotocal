package com.zhuoting.health.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import com.mycj.protocal.R;
import com.zhuoting.health.bean.PhoneModel;
import com.zhuoting.health.bean.RunInfo;
import com.zhuoting.health.jni.JNITools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

public class Tools {

    public static String username;
    public static String dev_id = "";

    public static String ip = null;


    public static List<Object> scenelist = new ArrayList<Object>();

    public static final String BasePath = Environment.getExternalStorageDirectory().getPath() + "/SmartAM/";

    public static final String IMAGE_FILE_LOCATION = Environment.getExternalStorageDirectory() + "/temp.jpg";

    public static final String filepath = "hi-light";

    public static final int timeout = 5;

    /**
     * 数据库
     *
     * @param context
     * @return
     */
    public static SQLiteDatabase db;

//	public static SQLiteDatabase getDb(Context context){
//
//		DBHelper dbHelper = new DBHelper(context);//这段代码放到Activity类中才用this
//		SQLiteDatabase db = null;
//		db = dbHelper.getReadableDatabase();
//
//		return db;
//	}

    public static String timeFormat(Date date, String sformat) {
        SimpleDateFormat format = new SimpleDateFormat(sformat);
        return format.format(date);
    }

    //	public static void showAlert(Context context,String msg,DialogInterface.OnClickListener listener){
//		CustomDialog.Builder ibuilder = new CustomDialog.Builder(context);
//		ibuilder.setTitle(context.getString(R.string.prompt));
//		ibuilder.setMessage(msg);
//		ibuilder.setPositiveButton(context.getString(R.string.confirm), listener);
//		ibuilder.create().show();
//	}
    public static void showAlert3(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }


//	public static void showAlert2(Context context,String msg,DialogInterface.OnClickListener listener){
//		CustomDialog.Builder ibuilder = new CustomDialog.Builder(context);
//		ibuilder.setTitle(context.getString(R.string.prompt));
//		ibuilder.setMessage(msg);
//		ibuilder.setPositiveButton(context.getString(R.string.confirm), listener);
//		ibuilder.setNegativeButton(context.getString(R.string.cancel),null);
//		ibuilder.create().show();
//	}

    public static Bitmap small(Bitmap bitmap, float value) {
        Matrix matrix = new Matrix();
        matrix.postScale(value, value); //长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizeBmp;
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int getwindowwidth(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static int getwindowheight(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 图片合成
     *
     * @return
     */
    public static Bitmap createBitmap(Bitmap src, Bitmap watermark, int size, int type) {
        if (src == null) {
            return null;
        }
        if (watermark.getWidth() < size) {
            for (int i = 2; i < 10; i++) {
                watermark = Tools.small(watermark, i);
                if (watermark.getWidth() > size) {
                    break;
                }
            }
        }

        int w = src.getWidth();
        int h = src.getHeight();
        int ww = watermark.getWidth();
        int wh = watermark.getHeight();
        //create the new blank bitmap
        Bitmap newb = Bitmap.createBitmap(size, size, Config.ARGB_8888);//创建一个新的和SRC长度宽度一样的位图
        Canvas cv = new Canvas(newb);
        //draw watermark into
        cv.drawBitmap(watermark, 0, 0, null);//在src的右下角画入水印
        //draw src into
        switch (type) {
            case 1:
                cv.drawBitmap(src, 0, 0, null);//在 0，0坐标开始画入src
                break;
            case 2:
                cv.drawBitmap(src, size - w, 0, null);//在 0，0坐标开始画入src
                break;
            case 3:
                cv.drawBitmap(src, 0, size - h, null);//在 0，0坐标开始画入src
                break;
            case 4:
                cv.drawBitmap(src, size - w, size - h, null);//在 0，0坐标开始画入src
                break;
            default:
                cv.drawBitmap(src, 0, 0, null);//在 0，0坐标开始画入src
                break;
        }

        //save all clip
        cv.save(Canvas.ALL_SAVE_FLAG);//保存
        //store
        cv.restore();//存储

        newb = Tools.GetRoundedCornerBitmap(newb, size / 2);
        return newb;
    }

    /**
     * 1身高，2体重，3路程
     */
    public static void saveUnit(int key, int value, Context context) {
        //value 1开始
        SharedPreferences sp = context.getSharedPreferences("smartam", context.MODE_PRIVATE);
        //存入数据
        Editor editor = sp.edit();
        editor.putInt("unit" + key, value);
        editor.commit();
    }

    public static int readUnit(int key, Context context) {
        //value 1开始
        SharedPreferences sp = context.getSharedPreferences("smartam", context.MODE_PRIVATE);
        int value = sp.getInt("unit" + key, -1);
        if (value == -1) {
            if (key == 1) {
                value = 0;
            } else if (key == 2) {
                value = 0;
            } else if (key == 3) {
                value = 0;
            }
        }
        return value;
    }

    //生成圆角图片
    public static Bitmap GetRoundedCornerBitmap(Bitmap bitmap, int roundPx) {
        try {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                    bitmap.getHeight(), Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight());
            final RectF rectF = new RectF(new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight()));
            //			final float roundPx = 14;
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(Color.BLACK);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

            final Rect src = new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight());

            canvas.drawBitmap(bitmap, src, rect, paint);
            return output;
        } catch (Exception e) {
            return bitmap;
        }
    }

    /**
     * 二进制转十六进制
     *
     * @param s
     * @return
     */
    public static String BinaryToHex(String s) {
        if (s.equals("")) {
            return "0";
        }
        return Long.toHexString(Long.parseLong(s, 2));
    }

    /**
     * 获取uuid
     */
    public static String getUUID() {
        UUID uuid = UUID.randomUUID();
        String uniqueId = uuid.toString();
        return uniqueId;

    }

    /**
     * Json 转成 Map<>
     *
     * @param jsonStr
     * @return
     */
    public static Map<String, Object> getMapForJson(String jsonStr) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonStr);

            Iterator<String> keyIter = jsonObject.keys();
            String key;
            Object value;
            Map<String, Object> valueMap = new HashMap<String, Object>();
            while (keyIter.hasNext()) {
                key = keyIter.next();
                value = jsonObject.get(key);
                valueMap.put(key, value);
            }
            return valueMap;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            //			Log.e(HttpClientUtils.TAG, e.toString());
        }
        return null;
    }

    /**
     * Json 转成 List<Map<>>
     *
     * @param jsonStr
     * @return
     */
    public static List<Map<String, Object>> getlistForJson(String jsonStr) {
        List<Map<String, Object>> list = null;
        try {
            JSONArray jsonArray = new JSONArray(jsonStr);
            JSONObject jsonObj;
            list = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObj = (JSONObject) jsonArray.get(i);
                list.add(getMapForJson(jsonObj.toString()));
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return list;
    }


    public static Bitmap decodeUriAsBitmap(Uri uri, Context context) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;

    }


    public static Map<String, String> urlToMap(String url) {
        Map<String, String> list = new HashMap();

        String[] sta = url.split("&");
        for (int i = 0; i < sta.length; i++) {
            String[] ccc = sta[i].split("=");
            list.put(ccc[0], ccc[1]);
        }

        return list;
    }


    public static void showLogin(final Context context) {

        //    	LoginPop loginPop = new LoginPop((Activity)context);
        //    	loginPop.showPopupWindow(((Activity)context).getWindow().getDecorView());

        //    	CustomDialog.Builder ibuilder = new CustomDialog.Builder(context);
        //		ibuilder.setTitle("提示");
        //		ibuilder.setMessage("请登录");
        //		ibuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
        //
        //			@Override
        //			public void onClick(DialogInterface dialog, int which) {
        //				// TODO Auto-generated method stub
        //				Intent intent = new Intent(context, LoginActivity.class);
        //				context.startActivity(intent);
        //			}
        //		});
        //		ibuilder.setNegativeButton("注册", new DialogInterface.OnClickListener() {
        //
        //			@Override
        //			public void onClick(DialogInterface dialog, int which) {
        //				// TODO Auto-generated method stub
        //				Intent intent = new Intent(context, LoginRegisterActivity.class);
        //				context.startActivity(intent);
        //			}
        //		});
        //		ibuilder.create().show();
    }

    public static int getDpi(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        int height = 0;
        @SuppressWarnings("rawtypes")
        Class c;
        try {
            c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            height = dm.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return height;
    }

    public static int[] getScreenWH(Context poCotext) {
        WindowManager wm = (WindowManager) poCotext
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        return new int[]{width, height};
    }

    public static int getVrtualBtnHeight(Context poCotext) {
        int location[] = getScreenWH(poCotext);
        int realHeiht = getDpi((Activity) poCotext);
        int virvalHeight = realHeiht - location[1];
        return virvalHeight;
    }


    public static byte[] makeSend(byte[] test) {
        byte[] testc = new byte[test.length + 2];
        int count = 0;
        for (int i = 0; i < test.length; i++) {
            testc[i + count] = test[i];
            if (i == 1) {
                int length = test.length + 4;
                testc[2] = (byte) (length % 0x100);
                testc[3] = (byte) (length / 0x100);
                count = 2;
            }
        }
        return makeCRC16(testc);
    }

    public static byte[] makeCRC16(byte[] test) {
        byte[] crc = JNITools.newInstance().makeCRC(test);
        byte[] bytes = Crc16Util.crcTable(test);
        Log.e("Crc16Util", DataUtil.byteToHexString(bytes));
        Log.e("Crc16", Crc16Util.getCrc(test));
        byte[] testc = new byte[test.length + 2];
        for (int i = 0; i < test.length; i++) {
            testc[i] = test[i];
        }
        System.out.println(test.length);

        testc[test.length] = crc[0];
        testc[test.length + 1] = crc[1];
        Log.e("crc16", DataUtil.byteToHexString(testc));
        return testc;
    }

    //====数据拆分
    //====数据拆分
    public static List<byte[]> makeSendMsg(byte[] data, int datasize) {
        List<byte[]> msglist = new ArrayList<byte[]>();
        int index = data.length / datasize;
        if (data.length % datasize > 0) {
            index = index + 1;
        }
        for (int i = 0; i < index; i++) {
            int lenght = datasize;
            if (i * datasize + datasize > data.length) {
                lenght = (int) data.length - i * datasize;
            }
            byte[] msg = new byte[lenght];

            System.arraycopy(data, i * datasize, msg, 0, lenght);

            msglist.add(msg);
        }

        return msglist;
    }

    public static List<byte[]> makeSendMsg(byte[] data, int datasize, int type) {
        List<byte[]> msglist = new ArrayList<byte[]>();
        int index = data.length / datasize;
        if (data.length % datasize > 0) {
            index = index + 1;
        }
        for (int i = 0; i < index; i++) {
            int lenght = datasize;
            if (i * datasize + datasize > data.length) {
                lenght = (int) data.length - i * datasize;
            }
            byte[] msg = new byte[lenght];

            System.arraycopy(data, i * datasize, msg, 0, lenght);

            byte[] smsg = perdata(msg, i, type);

            msglist.add(smsg);
        }

        return msglist;
    }

    public static byte[] perdata(byte[] data, int index, int type) {
        byte[] msg = new byte[data.length + 3];
        if (type == 1) {
            msg[0] = (byte) 0xc4;
        } else {
            msg[0] = (byte) 0xc3;
        }

        msg[1] = (byte) (0xff & (data.length + 3));
        msg[2] = (byte) (0xff & index);
        System.arraycopy(data, 0, msg, 3, data.length);
        byte[] cmsg = makeCRC16(msg);
        return cmsg;
    }


    public static byte[] finishmsg() {
        byte[] test = new byte[]{(byte) 0xc4, 0x02};
        byte[] testc = makeCRC16(test);

        return testc;
    }

    public static byte[] finishmsg2() {
        byte[] test = new byte[]{(byte) 0xc3, 0x03, 0x02};
        byte[] testc = makeCRC16(test);

        return testc;
    }

    public static String logbyte(byte[] data) {
        if (data == null) {
            return "null";
        }
        String revmsg = "";
        for (int n = 0; n < data.length; n++) {
            String aa = Integer.toHexString(data[n]);
            if (aa.length() == 1) {
                revmsg = revmsg + "0" + aa + ",";
            } else {
                aa = aa.replace("ffffff", "");
                revmsg = revmsg + aa + ",";
            }
        }
        return revmsg;
    }

//	public static String weekname(String str,Context context){
//		if (str.equals("0")) {
//			return context.getString(R.string.never);
//		}else if(str.equals("1,2,3,4,5")){
//			return context.getString(R.string.working_day);
//		}else if(str.equals("1,2,3,4,5,6,7")){
//			return context.getString(R.string.every_day);
//		}else{
//			String[] wlist = str.split(",");
//			String weekstr = "";
//
//			for (String msg : wlist) {
//				if (weekstr.equals("")) {
//					weekstr = weekstr + Tools.weekOne(msg,context);
//				}else{
//					weekstr = weekstr + "," + Tools.weekOne(msg,context);
//				}
//			}
//			return weekstr;
//		}
//	}
//	public static String weekOne(String msg,Context context){
//		if (msg.equals("1")) {
//			return context.getString(R.string.mon);
//		}else if (msg.equals("2")) {
//			return context.getString(R.string.tue);
//		}else if (msg.equals("3")) {
//			return context.getString(R.string.wed);
//		}else if (msg.equals("4")) {
//			return context.getString(R.string.thu);
//		}else if (msg.equals("5")) {
//			return context.getString(R.string.fri);
//		}else if (msg.equals("6")) {
//			return context.getString(R.string.sat);
//		}else if (msg.equals("7")) {
//			return context.getString(R.string.sun);
//		}
//		return "";
//	}

    public static int BCD_CO(int x) {
        x = (x / 10) * 16 + (x % 10);
        return x;
    }

    public static int BCD_TO_TEN(int x) {
        x = ((x / 16) * 10 + x % 16);
        return x;
    }

    public static void saveUpTime(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("smartam", context.MODE_PRIVATE);
        //存入数据
        Editor editor = sp.edit();
        editor.putLong(key, System.currentTimeMillis());
        editor.commit();
    }

    public static String getUpTime(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("smartam", context.MODE_PRIVATE);
        long ltime = sp.getLong(key, 0);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm");
        if (ltime == 0) {
            return format.format(new Date());
        }
        return format.format(new Date(ltime));
    }

    //=====================
    //身高1feet ＝ 30.48cm,1cm = 0.0328feet
    public static float changeHeight(int type, int value) {
        float height = value;

        if (type == 1) {
            height = height * 0.0328f;
            height = (float) (Math.round(height * 10)) / 10;//(这里的100就是2位小数点,如果要其它
        }

        return height;
    }


    //1KG=2.2046226(lb)
    //1KG=0.157473(st)
    public static float changeWidth(int type, int value) {
        float width = value;

        if (type == 2) {
            width = width * 2.2046226f;
            width = (float) (Math.round(width * 10)) / 10;//(这里的100就是2位小数点,如果要其它
        } else if (type == 3) {
            width = width * 0.157473f;
            width = (float) (Math.round(width * 10)) / 10;//(这里的100就是2位小数点,如果要其它
        }

        return width;
    }

    //1Km=0.621Mlie
    public static float changelength(int type, int value) {
        float length = value / 1000.0f;

        if (type == 2) {
            length = length * 0.621f;
        }

        length = (float) ((int) (length * 100)) / 100.0f;//(这里的100就是2位小数点,如果要其它

        return length;
    }

    public static void saveColckTitle(Context context, int key, String value) {
        SharedPreferences sp = context.getSharedPreferences("smartam", context.MODE_PRIVATE);
        //存入数据
        Editor editor = sp.edit();
        editor.putString("clockt" + key, value);
        editor.commit();
    }

    //	public static String readColckTitle(Context context,int key){
//		SharedPreferences sp = context.getSharedPreferences("smartam", context.MODE_PRIVATE);
//		String str = sp.getString("clockt"+key, context.getString(R.string.alarm_clock));
//		return str;
//	}
    //========SD卡判断
    private boolean ExistSDCard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else
            return false;
    }


//	public static Dialog createLoadingDialog(Context context, String msg) {
//
//		LayoutInflater inflater = LayoutInflater.from(context);
//		View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
//		RelativeLayout layout = (RelativeLayout) v.findViewById(R.id.dialog_view);// 加载布局
//
//		//        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
//		//        tipTextView.setText(msg);// 设置加载信息
//
//		Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog
//
//		loadingDialog.setCancelable(false);// 不可以用“返回键”取消
//		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
//				LinearLayout.LayoutParams.MATCH_PARENT,
//				LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
//		return loadingDialog;
//	}

    /**
     * 滤波算法
     *
     * @param data，新产生的数据
     * @param ratio，分子越小值越平滑但会滞后，0.1，1.3
     * @param Yn_1，初始化数据，计算出来后的数据
     * @return
     */
    public static float LowPassFilter0(float data, float ratio, float Yn_1) {
        Yn_1 = ratio * data + (1 - ratio) * (Yn_1);
        return Yn_1;
    }


    public static String creatFile(String fileName) {
        String path = Tools.BasePath + fileName;
        File dirFirstFolder = new File(path);//方法二：通过变量文件来获取需要创建的文件夹名字
        if (!dirFirstFolder.exists()) { //如果该文件夹不存在，则进行创建
            dirFirstFolder.mkdirs();//创建文件夹
        }

        return path;
    }

    public static void writeToFile(String filePath, String msg) {

        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(msg.getBytes());
            raf.close();
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }


    /**
     * @param path 注意的是并不是所有的文件夹都可以进行读取的，权限问题
     */
    public static List<String> getFileList(String path) {
        File file = new File(path);

        List<String> mlist = new ArrayList<String>();
        //如果是文件夹的话
        if (file.isDirectory()) {
            //返回文件夹中有的数据
            File[] files = file.listFiles();
            //先判断下有没有权限，如果没有权限的话，就不执行了
            if (null == files)
                return mlist;

            for (int i = 0; i < files.length; i++) {
                String filePath = files[i].getAbsolutePath();
                if (filePath.indexOf(".txt") != -1) {
                    mlist.add(filePath);
                }
            }
        }

        return mlist;
    }


    public static String readFile(String path) {
        try {
            File urlFile = new File(path);
            InputStreamReader isr = new InputStreamReader(new FileInputStream(urlFile), "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String str = "";
            String mimeTypeLine = null;
            while ((mimeTypeLine = br.readLine()) != null) {
                str = str + mimeTypeLine;
            }
            return str;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    //================================================
    //========scene处理
//		public static void addPhone(Context context, PhoneModel phoneModel){
//			SharedPreferences sp = context.getSharedPreferences("smartam", Context.MODE_PRIVATE);
//			String groupStr = sp.getString("phonelist", null);
//
//			JSONArray alist = null;
//			if (groupStr != null) {
//				try {
//					alist = new JSONArray(groupStr);
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//			}else{
//				alist = new JSONArray();
//			}
//
//			alist.put(phoneModel.getJsonObj());
//
//			Editor editor = sp.edit();
//			editor.putString("phonelist", alist.toString());
//			editor.commit();
//		}
//
//		public static List<PhoneModel> getPhone(Context context){
//			SharedPreferences sp = context.getSharedPreferences("smartam", Context.MODE_PRIVATE);
//			String groupStr = sp.getString("phonelist", null);
//
//			JSONArray alist = null;
//			if (groupStr != null) {
//				try {
//					alist = new JSONArray(groupStr);
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//			}else{
//				alist = new JSONArray();
//			}
//
//			List<PhoneModel> slist = new ArrayList();
//			for (int i=0;i<alist.length();i++){
//				PhoneModel phoneModel = new PhoneModel();
//				try {
//					phoneModel.jsonToObj(alist.getJSONObject(i));
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//				slist.add(phoneModel);
//			}
//
//			return slist;
//		}
//
//
//		public static void savePhonelist(Context context, List<PhoneModel> glist){
//			SharedPreferences sp = context.getSharedPreferences("smartam", Context.MODE_PRIVATE);
//			String groupStr = sp.getString("phonelist", null);
//
//			JSONArray alist = new JSONArray();
//
//			for (int i=0;i<glist.size();i++){
//				PhoneModel phoneModel = glist.get(i);
//				alist.put(phoneModel.getJsonObj());
//			}
//
//			Editor editor = sp.edit();
//			editor.putString("phonelist", alist.toString());
//			editor.commit();
//		}

    public static String readColckTitle(Context context, int key) {
        SharedPreferences sp = context.getSharedPreferences("smartam", context.MODE_PRIVATE);
        String str = sp.getString("clockt" + key, context.getString(R.string.alarm_clock));
        return str;
    }

    public static String weekname(String str, Context context) {
        if (str.equals("0")) {
            return context.getString(R.string.never);
        } else if (str.equals("1,2,3,4,5")) {
            return context.getString(R.string.working_day);
        } else if (str.equals("1,2,3,4,5,6,7")) {
            return context.getString(R.string.every_day);
        } else {
            String[] wlist = str.split(",");
            String weekstr = "";

            for (String msg : wlist) {
                if (weekstr.equals("")) {
                    weekstr = weekstr + Tools.weekOne(msg, context);
                } else {
                    weekstr = weekstr + "," + Tools.weekOne(msg, context);
                }
            }
            return weekstr;
        }
    }

    public static String weekOne(String msg, Context context) {
        if (msg.equals("1")) {
            return context.getString(R.string.mon);
        } else if (msg.equals("2")) {
            return context.getString(R.string.tue);
        } else if (msg.equals("3")) {
            return context.getString(R.string.wed);
        } else if (msg.equals("4")) {
            return context.getString(R.string.thu);
        } else if (msg.equals("5")) {
            return context.getString(R.string.fri);
        } else if (msg.equals("6")) {
            return context.getString(R.string.sat);
        } else if (msg.equals("7")) {
            return context.getString(R.string.sun);
        }
        return "";
    }


    public static void addPhone(Context context, PhoneModel phoneModel) {
        SharedPreferences sp = context.getSharedPreferences("smartam", Context.MODE_PRIVATE);
        String groupStr = sp.getString("phonelist", null);

        JSONArray alist = null;
        if (groupStr != null) {
            try {
                alist = new JSONArray(groupStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            alist = new JSONArray();
        }

        alist.put(phoneModel.getJsonObj());

        Editor editor = sp.edit();
        editor.putString("phonelist", alist.toString());
        editor.commit();
    }


    public static List<PhoneModel> getPhone(Context context) {
        SharedPreferences sp = context.getSharedPreferences("smartam", Context.MODE_PRIVATE);
        String groupStr = sp.getString("phonelist", null);

        JSONArray alist = null;
        if (groupStr != null) {
            try {
                alist = new JSONArray(groupStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            alist = new JSONArray();
        }

        List<PhoneModel> slist = new ArrayList();
        for (int i = 0; i < alist.length(); i++) {
            PhoneModel phoneModel = new PhoneModel();
            try {
                phoneModel.jsonToObj(alist.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            slist.add(phoneModel);
        }

        return slist;
    }


    public static void savePhonelist(Context context, List<PhoneModel> glist) {
        SharedPreferences sp = context.getSharedPreferences("smartam", Context.MODE_PRIVATE);
        String groupStr = sp.getString("phonelist", null);

        JSONArray alist = new JSONArray();

        for (int i = 0; i < glist.size(); i++) {
            PhoneModel phoneModel = glist.get(i);
            alist.put(phoneModel.getJsonObj());
        }

        Editor editor = sp.edit();
        editor.putString("phonelist", alist.toString());
        editor.commit();
    }


    //合并byte[]
    public static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
        byte[] byte_3 = new byte[byte_1.length + byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }


    // 拆分byte[]
    public static byte[] byteSplit(byte[] byte_1, int index) {
        byte[] byte_3 = new byte[byte_1.length - index];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length - index);
        return byte_3;
    }

    //=====
    public static void saveStep(int value, Context context) {
        //value 1开始
        SharedPreferences sp = context.getSharedPreferences("smartam", context.MODE_PRIVATE);
        //存入数据
        Editor editor = sp.edit();
        editor.putInt("saveStep", value);
        editor.commit();
    }

    public static int readStep(Context context) {
        //value 1开始
        SharedPreferences sp = context.getSharedPreferences("smartam", context.MODE_PRIVATE);
        int value = sp.getInt("saveStep", 10000);
        return value;
    }


    //=====
    public static void saveSleep(int value, Context context) {
        //value 1开始
        SharedPreferences sp = context.getSharedPreferences("smartam", context.MODE_PRIVATE);
        //存入数据
        Editor editor = sp.edit();
        editor.putInt("saveSleep", value);
        editor.commit();
    }

    public static int readSleep(Context context) {
        //value 1开始
        SharedPreferences sp = context.getSharedPreferences("smartam", context.MODE_PRIVATE);
        int value = sp.getInt("saveSleep", 8);
        return value;
    }


    //=====
    public static void saveAge(int value, Context context) {
        //value 1开始
        SharedPreferences sp = context.getSharedPreferences("smartam", context.MODE_PRIVATE);
        //存入数据
        Editor editor = sp.edit();
        editor.putInt("saveAge", value);
        editor.commit();
    }

    public static int readAge(Context context) {
        //value 1开始
        SharedPreferences sp = context.getSharedPreferences("smartam", context.MODE_PRIVATE);
        int value = sp.getInt("saveAge", 18);
        return value;
    }


    public static int readInitAge(Context context) {
        //value 1开始
        SharedPreferences sp = context.getSharedPreferences("smartam", context.MODE_PRIVATE);
        int value = sp.getInt("saveAge", 0);
        return value;
    }


    //=====
    public static void saveSex(int value, Context context) {
        //value 1开始
        SharedPreferences sp = context.getSharedPreferences("smartam", context.MODE_PRIVATE);
        //存入数据
        Editor editor = sp.edit();
        editor.putInt("saveSex", value);
        editor.commit();
    }

    public static int readSex(Context context) {
        //value 1开始
        SharedPreferences sp = context.getSharedPreferences("smartam", context.MODE_PRIVATE);
        int value = sp.getInt("saveSex", 0);
        return value;
    }


    public static void saveBack(boolean value, Context context) {
        //value 1开始
        SharedPreferences sp = context.getSharedPreferences("smartam", context.MODE_PRIVATE);
        //存入数据
        Editor editor = sp.edit();
        editor.putBoolean("saveBack", value);
        editor.commit();
    }

    public static boolean readBack(Context context) {
        //value 1开始
        SharedPreferences sp = context.getSharedPreferences("smartam", context.MODE_PRIVATE);
        boolean value = sp.getBoolean("saveBack", false);
        return value;
    }

    //=====
    public static void saveKg(int value, Context context) {
        //value 1开始
        SharedPreferences sp = context.getSharedPreferences("smartam", context.MODE_PRIVATE);
        //存入数据
        Editor editor = sp.edit();
        editor.putInt("saveKg", value);
        editor.commit();
    }

    public static int readKg(Context context) {
        //value 1开始
        SharedPreferences sp = context.getSharedPreferences("smartam", context.MODE_PRIVATE);
        int value = sp.getInt("saveKg", 60);
        return value;
    }


    //=====
    public static void saveCm(int value, Context context) {
        //value 1开始
        SharedPreferences sp = context.getSharedPreferences("smartam", context.MODE_PRIVATE);
        //存入数据
        Editor editor = sp.edit();
        editor.putInt("saveCm", value);
        editor.commit();
    }

    public static int readCm(Context context) {
        //value 1开始
        SharedPreferences sp = context.getSharedPreferences("smartam", context.MODE_PRIVATE);
        int value = sp.getInt("saveCm", 170);
        return value;
    }


    //=====
    public static void saveNoti(List<Integer> value, Context context) {

        String str = "";
        for (int i = 0; i < value.size(); i++) {
            if (str.equals("")) {
                str = str + value.get(i);
            } else {
                str = str + "," + value.get(i);
            }
        }

        //value 1开始
        SharedPreferences sp = context.getSharedPreferences("smartam", context.MODE_PRIVATE);
        //存入数据
        Editor editor = sp.edit();
        editor.putString("saveNoti", str);
        editor.commit();
    }

    public static List<Integer> readNoti(Context context) {
        //value 1开始
        SharedPreferences sp = context.getSharedPreferences("smartam", context.MODE_PRIVATE);
        String value = sp.getString("saveNoti", "1,1,1,1,1,1,1,1,1,1,1,1,1");

        if (value.length() < 24) {
            value = value + ",1";
        }

        String str[] = value.split(",");

        List<Integer> aa = new ArrayList<>();
        for (String strVal : str) {
            aa.add(Integer.parseInt(strVal));
        }

        return aa;
    }


    //=====
    public static void saveAuto(int value, Context context) {
        //value 1开始
        SharedPreferences sp = context.getSharedPreferences("smartam", context.MODE_PRIVATE);
        //存入数据
        Editor editor = sp.edit();
        editor.putInt("saveAuto", value);
        editor.commit();
    }

    public static int readAuto(Context context) {
        //value 1开始
        SharedPreferences sp = context.getSharedPreferences("smartam", context.MODE_PRIVATE);
        int value = sp.getInt("saveAuto", 0);
        return value;
    }


    //=====
    public static void saveDv(int value, Context context) {
        //value 1开始
        SharedPreferences sp = context.getSharedPreferences("smartam", context.MODE_PRIVATE);
        //存入数据
        Editor editor = sp.edit();
        editor.putInt("saveDv", value);
        editor.commit();
    }

    public static int readDv(Context context) {
        //value 1开始
        SharedPreferences sp = context.getSharedPreferences("smartam", context.MODE_PRIVATE);
        int value = sp.getInt("saveDv", 150);
        return value;
    }

    //=====
    public static void saveDvo(Boolean value, Context context) {
        //value 1开始
        SharedPreferences sp = context.getSharedPreferences("smartam", context.MODE_PRIVATE);
        //存入数据
        Editor editor = sp.edit();
        editor.putBoolean("saveDvo", value);
        editor.commit();
    }

    public static boolean readDvo(Context context) {
        //value 1开始
        SharedPreferences sp = context.getSharedPreferences("smartam", context.MODE_PRIVATE);
        boolean value = sp.getBoolean("saveDvo", false);
        return value;
    }


    //=====
    public static void saveFw(Boolean value, Context context) {
        //value 1开始
        SharedPreferences sp = context.getSharedPreferences("smartam", context.MODE_PRIVATE);
        //存入数据
        Editor editor = sp.edit();
        editor.putBoolean("saveFw", value);
        editor.commit();
    }

    public static boolean readFw(Context context) {
        //value 1开始
        SharedPreferences sp = context.getSharedPreferences("smartam", context.MODE_PRIVATE);
        boolean value = sp.getBoolean("saveFw", true);
        return value;
    }


    //=====
    public static void saveHan(int value, Context context) {
        //value 1开始
        SharedPreferences sp = context.getSharedPreferences("smartam", context.MODE_PRIVATE);
        //存入数据
        Editor editor = sp.edit();
        editor.putInt("saveHan", value);
        editor.commit();
    }

    public static int readHan(Context context) {
        //value 1开始
        SharedPreferences sp = context.getSharedPreferences("smartam", context.MODE_PRIVATE);
        int value = sp.getInt("saveHan", 1);
        return value;
    }


    //=====
    public static void addRun(RunInfo runInfo, Context context) {
        //value 1开始
        SharedPreferences sp = context.getSharedPreferences("smartam", context.MODE_PRIVATE);
        String strValue = sp.getString("runList", null);
        JSONArray array = new JSONArray();
        if (strValue != null) {
            try {
                array = new JSONArray(strValue);
            } catch (JSONException e) {

            }
        }


        JSONArray aaaa = new JSONArray();
        aaaa.put(runInfo.objectToDictionary());

        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject object = array.getJSONObject(i);
                aaaa.put(object);
            } catch (JSONException e) {

            }
        }

        //存入数据
        Editor editor = sp.edit();
        editor.putString("runList", aaaa.toString());
        editor.commit();
    }


    public static void saveRunList(List<RunInfo> rlist, Context context) {
        //value 1开始
        SharedPreferences sp = context.getSharedPreferences("smartam", context.MODE_PRIVATE);

        JSONArray array = new JSONArray();


        for (RunInfo rinfo : rlist) {
            array.put(rinfo.objectToDictionary());
        }

        //存入数据
        Editor editor = sp.edit();
        editor.putString("runList", array.toString());
        editor.commit();
    }


    public static List<RunInfo> getRunList(Context context) {
        //value 1开始
        SharedPreferences sp = context.getSharedPreferences("smartam", context.MODE_PRIVATE);
        String strValue = sp.getString("runList", null);
        JSONArray array = new JSONArray();
        if (strValue != null) {
            try {
                array = new JSONArray(strValue);
            } catch (JSONException e) {

            }
        }

        List<RunInfo> rlist = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject object = array.getJSONObject(i);
                RunInfo rinfo = new RunInfo();
                rinfo.setValue(object);

                rlist.add(rinfo);
            } catch (JSONException e) {

            }
        }

        return rlist;
    }

    //=====
//	public static void addRunMsg(List<LatLng> plist,String uuid, Context context){
//		//value 1开始
//
//		JSONArray alist = new JSONArray();
//		for (LatLng point : plist){
//			String spoint = point.latitude+":"+point.longitude;
//			alist.put(spoint);
//		}
//
//		SharedPreferences sp = context.getSharedPreferences("smartam", context.MODE_PRIVATE);
//		//String strValue = sp.getString("runMsg",null);
//		//存入数据
//		Editor editor = sp.edit();
//		editor.putString(uuid, alist.toString());
//		editor.commit();
//	}

    //=====
    public static void delRunMsg(String uuid, Context context) {
        //value 1开始
        SharedPreferences sp = context.getSharedPreferences("smartam", context.MODE_PRIVATE);
        //存入数据
        Editor editor = sp.edit();
        editor.remove(uuid);
        editor.commit();
    }

//	public static List<LatLng> readRunMsg(String uuid, Context context){
//		//value 1开始
//
//		SharedPreferences sp = context.getSharedPreferences("smartam", context.MODE_PRIVATE);
//
//
//		String strValue = sp.getString(uuid,null);
//		JSONArray array = new JSONArray();
//		if (strValue != null){
//			try {
//				array = new JSONArray(strValue);
//			}catch (JSONException e){
//
//			}
//		}
//
//		List<LatLng> plist = new ArrayList<>();
//
//		for (int i=0;i<array.length();i++){
//			try {
//
//				String object = array.getString(i);
//				String p[] = object.split(":");
//				LatLng latLng = new LatLng(Double.parseDouble(p[0]),Double.parseDouble(p[1]));
//				plist.add(latLng);
//
//			}catch (JSONException e){
//
//			}
//		}
//
//		return plist;
//	}


    //=====
    public static void saveHrList(List<Integer> plist, String timeStr, Context context) {

        SharedPreferences sp = context.getSharedPreferences("smartam", context.MODE_PRIVATE);

        String strValue = sp.getString("saveHrList", null);

        JSONArray array = new JSONArray();
        if (strValue != null) {
            try {
                array = new JSONArray(strValue);
            } catch (JSONException e) {

            }
        }

        JSONArray aaaa = new JSONArray();
        aaaa.put(timeStr);

        for (int i = 0; i < array.length(); i++) {
            try {
                String object = array.getString(i);
                aaaa.put(object);
            } catch (JSONException e) {

            }
        }


        //存入数据
        JSONArray aaa = new JSONArray(plist);

        Editor editor = sp.edit();
        editor.putString("saveHrList", aaaa.toString());
        editor.putString(timeStr, aaa.toString());
        editor.commit();
    }

    //=====
    public static List<String> readHrList(Context context) {
        //value 1开始
        SharedPreferences sp = context.getSharedPreferences("smartam", context.MODE_PRIVATE);

        String strValue = sp.getString("saveHrList", null);

        JSONArray array = new JSONArray();
        if (strValue != null) {
            try {
                array = new JSONArray(strValue);
            } catch (JSONException e) {

            }
        }

        List<String> timeList = new ArrayList<>();


        for (int i = 0; i < array.length(); i++) {
            try {
                String object = array.getString(i);
                timeList.add(object);

            } catch (JSONException e) {

            }
        }

        return timeList;
    }


    public static List<Integer> readHrListMsg(String timeStr, Context context) {
        //value 1开始
        SharedPreferences sp = context.getSharedPreferences("smartam", context.MODE_PRIVATE);

        String strValue = sp.getString(timeStr, null);

        JSONArray array = new JSONArray();
        if (strValue != null) {
            try {
                array = new JSONArray(strValue);
            } catch (JSONException e) {

            }
        }

        List<Integer> timeList = new ArrayList<>();


        for (int i = 0; i < array.length(); i++) {
            try {
                Integer object = array.getInt(i);
                timeList.add(object);

            } catch (JSONException e) {

            }
        }

        return timeList;
    }


    public static void delHearMsg(String timeStr, Context context) {
        SharedPreferences sp = context.getSharedPreferences("smartam", context.MODE_PRIVATE);

        String strValue = sp.getString("saveHrList", null);

        JSONArray array = new JSONArray();
        if (strValue != null) {
            try {
                array = new JSONArray(strValue);
            } catch (JSONException e) {

            }
        }


        for (int i = 0; i < array.length(); i++) {
            try {
                String object = array.getString(i);
                if (object.equals(timeStr)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        array.remove(i);
                    }
                }
            } catch (JSONException e) {

            }
        }


        //存入数据

        Editor editor = sp.edit();
        editor.putString("saveHrList", array.toString());
        editor.remove(timeStr);
        editor.commit();
    }


    //=====
    public static void saveBleAddress(String value, Context context) {
        //value 1开始
        SharedPreferences sp = context.getSharedPreferences("smartam", context.MODE_PRIVATE);
        //存入数据
        Editor editor = sp.edit();
        if (value == null) {
            editor.remove("saveBleAddress");
        } else {
            editor.putString("saveBleAddress", value);
        }

        editor.commit();
    }

    public static String readBleAddress(Context context) {
        //value 1开始
        SharedPreferences sp = context.getSharedPreferences("smartam", context.MODE_PRIVATE);
        String value = sp.getString("saveBleAddress", null);
        return value;
    }


    //==========
    public static void saveLogin(boolean value, Context context) {
        //value 1开始
        SharedPreferences sp = context.getSharedPreferences("smartam", context.MODE_PRIVATE);
        //存入数据
        Editor editor = sp.edit();
        editor.putBoolean("saveLogin", value);
        editor.commit();
    }

    public static boolean readLogin(Context context) {
        //value 1开始
        SharedPreferences sp = context.getSharedPreferences("smartam", context.MODE_PRIVATE);
        boolean value = sp.getBoolean("saveLogin", false);
        return value;
    }

    public static int getTimeOffset() {
        int offset = TimeZone.getDefault().getOffset(System.currentTimeMillis());
        return offset;
    }

    /**
     * byte[]转变为16进制String字符, 每个字节2位, 不足补0
     */
    public static String byteToHexString(byte[] bytes) {
        String result = null;
        String hex = null;
        if (bytes != null && bytes.length > 0) {
            final StringBuilder stringBuilder = new StringBuilder(bytes.length);
            for (byte byteChar : bytes) {
                hex = Integer.toHexString(byteChar & 0xFF);
                if (hex.length() == 1) {
                    hex = '0' + hex;
                }
                stringBuilder.append(hex.toUpperCase());
            }
            result = stringBuilder.toString();
        }
        return result;
    }

    /**
     * String 类型转化为 Long 类型
     *
     * @param strTime String 类型时间
     * @return Date 类型时间，转化后保存时分秒
     */
    public static long String2Long(String strTime){
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date;
            date = formatter.parse(strTime);
            return date.getTime();
        } catch (ParseException e){
            Log.e("String 类型转化为 Long 类型","时间解析异常，请检查传入的时间是否正确");
        }
        return 0;
    }
}
