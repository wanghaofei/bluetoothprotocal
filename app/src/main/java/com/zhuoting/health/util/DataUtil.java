package com.zhuoting.health.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;


/**
 * 数据转换工具类
 * 
 * @author Administrator
 * 
 */
public class DataUtil {

	public static DecimalFormat df = new DecimalFormat("0.00");
	public static DecimalFormat df2 = new DecimalFormat("0.0");

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

	public static long getUnsignedIntt(long data) { // 将int数据转换为0~4294967295
		return data & 0x0FFFFFFFFl;
	}

	/**
	 * String 转变为16进制String字符, 每个字节2位, 不足补0
	 */
	public static String getStringByString(String value) {
		String hex = Integer.toHexString(Integer.valueOf(value));
		StringBuilder stringBuilder = new StringBuilder();
		if (hex.length() == 1) {
			hex = '0' + hex;
		}
		stringBuilder.append(hex);
		return stringBuilder.toString();
	}
	public static String toHexString(int i) {
		String value = Integer.toHexString(i);
		if (value.length() == 1) {
			value = "0" + value;
		}
		return value;
	}
	/**
	 * 把16进制String字符转变为byte[]
	 */
	public static byte[] getBytesByString(String data) {
		byte[] bytes = null;
		if (data != null) {
			data = data.toUpperCase();
			int length = data.length() / 2;
			char[] dataChars = data.toCharArray();
			bytes = new byte[length];
			for (int i = 0; i < length; i++) {
				int pos = i * 2;
				bytes[i] = (byte) (charToByte(dataChars[pos]) << 4 | charToByte(dataChars[pos + 1]));
			}
		}
		return bytes;
	}

	/**
	 * 取得在16进制字符串中各char所代表的16进制数
	 */
	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	public static String integerToHexString(int i) {
		String value = Integer.toHexString(i);
		if (value.length() == 1) {
			value = "0" + value;
		}
		return value;
	}

	/**
	 * 根据传入的毫秒数，转换成MM:SS时间串形式返回
	 * 
	 * @param timeMillis
	 * @return
	 */
	public static String getMMSS(long timeMillis) {
		StringBuffer sb = new StringBuffer();
		long mm = (timeMillis % (1000 * 60 * 60)) / 1000 / 60;
		long ss = ((timeMillis % (1000 * 60 * 60)) % (1000 * 60)) / 1000;

		if (mm < 0) {
			sb.append("00:");
		} else if (mm < 10) {
			sb.append("0" + mm + ":");
		} else {
			sb.append(mm + ":");
		}
		if (ss < 0) {
			sb.append("00");
		} else if (ss < 10) {
			sb.append("0" + ss);
		} else {
			sb.append(ss);
		}
		return sb.toString();
	}

	/**
	 * 根据传入的耗秒数, 转换成为HH:MM:SS的字符串返回
	 */
	public static String getHHMMSS(long time) {
		String hhmmss = "00:00:00";
		StringBuffer bf = new StringBuffer();
		if (time == 0) {
			return hhmmss;
		}
		long hh = time / 1000 / 60 / 60;
		long mm = (time % (1000 * 60 * 60)) / 1000 / 60;
		long ss = ((time % (1000 * 60 * 60)) % (1000 * 60)) / 1000;

		if (hh < 0) {
			bf.append("00:");
		} else if (hh < 10) {
			bf.append("0" + hh + ":");
		} else {
			bf.append(hh + ":");
		}
		if (mm < 0) {
			bf.append("00:");
		} else if (mm < 10) {
			bf.append("0" + mm + ":");
		} else {
			bf.append(mm + ":");
		}
		if (ss < 0) {
			bf.append("00");
		} else if (ss < 10) {
			bf.append("0" + ss);
		} else {
			bf.append(ss);
		}
		hhmmss = bf.toString();
		System.out.println(hhmmss);
		return hhmmss;
	}

	/**
	 * 比较2个时间的大小
	 */
	public static boolean compareTime(Date time1, Date time2) {
		return time1.before(time2);
	}

	/**
	 * 根据传入的2个字节2位16进制字符比如FFFF, 计算返回int类型的绝对值
	 */
	public static int hexStringX2bytesToInt(String hexString) {
		return binaryString2int(hexString2binaryString(hexString));
	}

	/**
	 * 16进制转换为2进制
	 */
	public static String hexString2binaryString(String hexString) {
		if (hexString == null || hexString.length() % 2 != 0) {
			return null;
		}
		String bString = "", tmp;
		for (int i = 0; i < hexString.length(); i++) {
			tmp = "0000" + Integer.toBinaryString(Integer.parseInt(hexString.substring(i, i + 1), 16));
			bString += tmp.substring(tmp.length() - 4);
		}
		return bString;
	}

	/**
	 * 二进制转为10进制 返回int
	 */
	public static int binaryString2int(String binarysString) {
		if (binarysString == null || binarysString.length() % 8 != 0) {
			return 0;
		}
		int result = Integer.valueOf(binarysString, 2);
		if ("1".equals(binarysString.substring(0, 1))) {
			System.out.println("这是个负数");
			char[] values = binarysString.toCharArray();
			for (int i = 0; i < values.length; i++) {
				if (values[i] == '1') {
					values[i] = '0';
				} else {
					values[i] = '1';
				}
			}
			binarysString = String.valueOf(values);
			result = Integer.valueOf(binarysString, 2) + 1;
		}

		return result;
	}

	// 二进制转十六进制
	public static String BinaryToHex(String s) {
		return Long.toHexString(Long.parseLong(s, 2));
	}

	// 十六进制转二进制
	public static String HexToBinary(String s) {
		return Long.toBinaryString(Long.parseLong(s, 16));
	}

	/**
	 * 根据步数得到卡洛里 并且格式化
	 * 
	 * @param step
	 * @return
	 */
	public static String getKal(int step) {
		DecimalFormat df = new DecimalFormat("0.00");
		String kael = df.format(((step / 1000) * 18.842));
		return kael;

	}
	

	/**
	 * 根据步数得到距离 并且格式化
	 * 
	 * @param step
	 * @return
	 */
	public static float getDistanceValue(int step) {
		return (float) ((step / 1000f) * 0.416);
	}
	public static String getDistanceStrValue(int step) {
//		return format1((float) ((step / 1000f) * 0.416));
		return format((float) ((step / 1000f) * 0.416));
	}
	
	/**
	 * 根据完成步数/目标步数 得到百分比
	 * 
	 * @return
	 */
	public static String getPercent(int x, int total) {
		// NumberFormat num = NumberFormat.getPercentInstance();
		// NumberFormat nf = NumberFormat.getInstance(Locale.FRENCH);
		// nf .setMaximumIntegerDigits(2);
		// nf .setMaximumFractionDigits(2);
		// return nf.format(b);

		String result = "";// 接受百分比的值
		double x_double = x * 1.0;
		double tempresult = x_double / total;
		// NumberFormat nf = NumberFormat.getPercentInstance(); 注释掉的也是一种方法
		// nf.setMinimumFractionDigits( 2 ); 保留到小数点后几位
		DecimalFormat df1 = new DecimalFormat("0.00%"); // ##.00%
														// 百分比格式，后面不足2位的用0补齐
		// result=nf.format(tempresult);
		result = df1.format(tempresult);
		return result;
	}

	/**
	 * 十进制的字符串 --> 十六进制的字符串
	 * 
	 * @param value
	 * @return
	 */
	public static String getHexFormOctString(String value) {
		return Integer.toHexString(Integer.valueOf(value));
	}

	/**
	 * int n1 = 14; //十进制转成十六进制： Integer.toHexString(n1); //十进制转成八进制
	 * Integer.toOctalString(n1); //十进制转成二进制 Integer.toBinaryString(12);
	 * 
	 * //十六进制转成十进制 Integer.valueOf("FFFF",16).toString(); //十六进制转成二进制
	 * Integer.toBinaryString(Integer.valueOf("FFFF",16)); //十六进制转成八进制
	 * Integer.toOctalString(Integer.valueOf("FFFF",16));
	 * 
	 * //八进制转成十进制 Integer.valueOf("576",8).toString(); //八进制转成二进制
	 * Integer.toBinaryString(Integer.valueOf("23",8)); //八进制转成十六进制
	 * Integer.toHexString(Integer.valueOf("23",8));
	 * 
	 * 
	 * //二进制转十进制 Integer.valueOf("0101",2).toString(); //二进制转八进制
	 * Integer.toOctalString(Integer.parseInt("0101", 2)); //二进制转十六进制
	 * Integer.toHexString(Integer.parseInt("0101", 2));
	 */
	/**
	 * 格式化日期
	 * 
	 * @param time
	 * @return
	 */
	public static String formatDateTime(long time) {
		if (0 == time) {
			return "";
		}
		SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
		return mDateFormat.format(new Date(time));
	}


	public static String format(double value) {
		DecimalFormat df = new DecimalFormat("0.00");
		return df.format(value);
	}
	
	public static String format1(double value) {
		DecimalFormat df = new DecimalFormat("0.0");
		return df.format(value);
	}

	public static String getAscii(String str) {
		String tmp;
		StringBuffer sb = new StringBuffer(1000);
		char c;
		int i, j;
		sb.setLength(0);
		for (i = 0; i < str.length(); i++) {
			c = str.charAt(i);
			if (c > 255) {
				sb.append("//u");
				j = (c >>> 8);
				tmp = Integer.toHexString(j);
				if (tmp.length() == 1)
					sb.append("0");
				sb.append(tmp);
				j = (c & 0xFF);
				tmp = Integer.toHexString(j);
				if (tmp.length() == 1)
					sb.append("0");
				sb.append(tmp);
			} else {
				sb.append(c);
			}
		}
		return (new String(sb));
	}
	
	//java 合并两个byte数组  
	   public static byte[] byteMerger(byte[] byte_1, byte[] byte_2){  
	        byte[] byte_3 = new byte[byte_1.length+byte_2.length];  
	        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);  
	        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);  
	        return byte_3;  
	    }
	   
	private static final double WEIGHT_DEFF =    2.2046226;
	private static final double HEIGHT_DEFF =    0.3937008;
	   
	public static int kgToLb(int kg) {
		//1千克(kg)=2.2046226磅(lb)
		//1厘米(cm)=0.3937008英寸(in)
		double result = kg * WEIGHT_DEFF;
		Log.e("", "kg="+kg +"转为lb ："+ result);
		return (int) Math.round(result);
	}

	public static int cmToInch(int cm) {
		double result = cm * HEIGHT_DEFF;
		Log.e("", "cm="+cm +"转为inch ："+ result);
		return (int) Math.round(result);
	} 
	
	public static int lbToKg(int lb) {
		double result = lb / WEIGHT_DEFF;
		return (int) Math.round(result);
	}
	
	public static int inchToCm(int inch) {
		double result = inch / HEIGHT_DEFF;
		return (int) Math.round(result);
	}
	

}
