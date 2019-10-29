package com.zhuoting.health.util;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @ClassName: DigitalTransUtils
 * @Description: TODO
 * @author KrisRay
 * @date Dec 27, 2012 11:31:57 AM
 */

/**
 * @ClassName: TransUtils
 * @Description: TODO
 * @author KrisRay
 * @date 2013年7月25日 下午5:02:56
 */
public class TransUtils {

	private final static byte[] mHex = "0123456789ABCDEF".getBytes();
	private final static String mDigits = "0123456789ABCDEF";
	public static final String encode = "UTF-8";

	/**
	 * @Title: Bytes2Hex
	 * @Description: Convert length many bytes of the byte array to a hex
	 *               string.
	 * @param data
	 * @param length
	 * @return String
	 * @throws
	 */
	public static String Bytes2Hex(byte[] data, int length) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i != length; i++) {
			int v = data[i] & 0xff;
			buf.append(mDigits.charAt(v >> 4));
			buf.append(mDigits.charAt(v & 0xf));
		}
		return buf.toString();
	}

	/**
	 * @Title: Bytes2Hex
	 * @Description: Convert byte array to a hex string.
	 * @param data
	 * @return String
	 * @throws
	 */
	public static String Bytes2Hex(byte[] data) {
		return Bytes2Hex(data, data.length);
	}

	/**
	 * @Title: Bytes2HexString
	 * @Description: Another method to convert byte array to a hex string.
	 * @param b
	 * @return String
	 * @throws
	 */
	public static String Bytes2HexString(byte[] b) {
		byte[] buff = new byte[2 * b.length];
		for (int i = 0; i < b.length; i++) {
			buff[2 * i] = mHex[(b[i] >> 4) & 0x0f];
			buff[2 * i + 1] = mHex[b[i] & 0x0f];
		}
		return new String(buff);
	}

	/**
	 * @Title: appendSpace
	 * @Description: Append space into a String.
	 * @param str
	 * @return String
	 * @throws
	 */
	public static String appendSpace(String str) {
		return str.replaceAll(".{2}(?!$)", "$0 ");
	}

	/**
	 * @Title: Char2Byte
	 * @Description: Convert char to a byte array.
	 * @param c
	 * @return byte[]
	 * @throws
	 */
	public static byte[] Char2Byte(char c) {
		byte[] b = new byte[2];
		b[0] = (byte) ((c & 0xFF00) >> 8);
		b[1] = (byte) (c & 0xFF);
		return b;
	}

	/**
	 * @Title: Bytes2Bin
	 * @Description: Convert byte array to a bin string.
	 * @param b
	 * @return String
	 * @throws
	 */
	public static String Bytes2Bin(byte[] b) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i != b.length; i++) {
			for (int j = 7; j >= 0; --j) {
				if (((1 << j) & b[i]) == 0) {
					buf.append(0);
				} else {
					buf.append(1);
				}
			}
			buf.append(" ");
		}
		return buf.toString();
	}

	/**
	 * @Title: Bytes2Dec
	 * @Description: Convert byte array to a dec int.
	 * @param bytes
	 * @return int
	 * @throws
	 */
	public static int Bytes2Dec(byte[] bytes) {

		int intValue = 0;
		// 由高位到低位
		for (int i = 0; i < bytes.length; i++) {

			int shift = (4 - 1 - i) * 8;
			intValue += (bytes[i] & 0x000000FF) << shift; // 往高位游

		}

		return intValue;
	}

	public static int bytes2int(byte[] bytes) {
		int num = bytes[0] & 0xFF;
		num |= ((bytes[1] << 8) & 0xFF00);
		num |= ((bytes[2] << 16) & 0xFF0000);
		num |= ((bytes[3] << 24) & 0xFF000000);
		return num;
	}

	/**
	 * @Title: isByteArrayEmpty
	 * @Description: charge byte array whether is all zero
	 * @param b
	 * @return boolean
	 * @throws
	 */
	public static boolean isByteArrayEmpty(byte[] b) {
		int flag = 0;
		for (int i = 0; i < b.length; i++) {
			if (b[i] == 0)
				flag++;
		}
		if (flag == 8)
			return true;
		else
			return false;
	}

	/**
	 * 数字字符串转ASCII码字符串
	 * 
	 * @param content
	 *            字符串
	 * @return ASCII字符串
	 */
	public static String StringToAsciiString(String content) {
		String result = "";
		int max = content.length();
		for (int i = 0; i < max; i++) {
			char c = content.charAt(i);
			String b = Integer.toHexString(c);
			result = result + b;
		}
		return result;
	}

	/**
	 * 十六进制转字符串
	 * 
	 * @param hexString
	 *            十六进制字符串
	 * @param encodeType
	 *            编码类型4：Unicode，2：普通编码
	 * @return 字符串
	 */
	public static String hexStringToString(String hexString, int encodeType) {
		String result = "";
		int max = hexString.length() / encodeType;
		for (int i = 0; i < max; i++) {
			char c = (char) hexStringToAlgorism(hexString.substring(i
					* encodeType, (i + 1) * encodeType));
			result += c;
		}
		return result;
	}

	/**
	 * 十六进制字符串装十进制
	 * 
	 * @param hex
	 *            十六进制字符串
	 * @return 十进制数值
	 */
	public static int hexStringToAlgorism(String hex) {
		hex = hex.toUpperCase();
		int max = hex.length();
		int result = 0;
		for (int i = max; i > 0; i--) {
			char c = hex.charAt(i - 1);
			int algorism = 0;
			if (c >= '0' && c <= '9') {
				algorism = c - '0';
			} else {
				algorism = c - 55;
			}
			result += Math.pow(16, max - i) * algorism;
		}
		return result;
	}

	/**
	 * 十六转二进制
	 * 
	 * @param hex
	 *            十六进制字符串
	 * @return 二进制字符串
	 */
	public static String hexStringToBinary(String hex) {
		hex = hex.toUpperCase();
		String result = "";
		int max = hex.length();
		for (int i = 0; i < max; i++) {
			char c = hex.charAt(i);
			switch (c) {
			case '0':
				result += "0000";
				break;
			case '1':
				result += "0001";
				break;
			case '2':
				result += "0010";
				break;
			case '3':
				result += "0011";
				break;
			case '4':
				result += "0100";
				break;
			case '5':
				result += "0101";
				break;
			case '6':
				result += "0110";
				break;
			case '7':
				result += "0111";
				break;
			case '8':
				result += "1000";
				break;
			case '9':
				result += "1001";
				break;
			case 'A':
				result += "1010";
				break;
			case 'B':
				result += "1011";
				break;
			case 'C':
				result += "1100";
				break;
			case 'D':
				result += "1101";
				break;
			case 'E':
				result += "1110";
				break;
			case 'F':
				result += "1111";
				break;
			}
		}
		return result;
	}

	/**
	 * ASCII码字符串转数字字符串
	 * 
	 * @param content
	 *            ASCII字符串
	 * @return 字符串
	 */
	public static String AsciiStringToString(String content) {
		String result = "";
		int length = content.length() / 2;
		for (int i = 0; i < length; i++) {
			String c = content.substring(i * 2, i * 2 + 2);
			int a = hexStringToAlgorism(c);
			char b = (char) a;
			String d = String.valueOf(b);
			result += d;
		}
		return result;
	}

	public static long bytes2long(byte[] datas) {

		return Long.parseLong(bytes2hex(datas), 16);

	}

	/**
	 * 将十进制转换为指定长度的十六进制字符串
	 * 
	 * @param algorism
	 *            int 十进制数字
	 * @param maxLength
	 *            int 转换后的十六进制字符串长度
	 * @return String 转换后的十六进制字符串
	 */
	public static String algorismToHEXString(int algorism, int maxLength) {
		String result = "";
		result = Integer.toHexString(algorism);

		if (result.length() % 2 == 1) {
			result = "0" + result;
		}
		return patchHexString(result.toUpperCase(), maxLength);
	}

	/**
	 * 字节数组转为普通字符串（ASCII对应的字符）
	 * 
	 * @param bytearray
	 *            byte[]
	 * @return String
	 */
	public static String bytestoString(byte[] bytearray) {
		String result = "";
		char temp;

		int length = bytearray.length;
		for (int i = 0; i < length; i++) {
			temp = (char) bytearray[i];
			result += temp;
		}
		return result;
	}

	/**
	 * 二进制字符串转十进制
	 * 
	 * @param binary
	 *            二进制字符串
	 * @return 十进制数值
	 */
	public static int binaryToAlgorism(String binary) {
		int max = binary.length();
		int result = 0;
		for (int i = max; i > 0; i--) {
			char c = binary.charAt(i - 1);
			int algorism = c - '0';
			result += Math.pow(2, max - i) * algorism;
		}
		return result;
	}

	/**
	 * 十进制转换为十六进制字符串
	 * 
	 * @param algorism
	 *            int 十进制的数字
	 * @return String 对应的十六进制字符串
	 */
	public static String algorismToHEXString(int algorism) {
		String result = "";
		result = Integer.toHexString(algorism);

		if (result.length() % 2 == 1) {
			result = "0" + result;

		}
		result = result.toUpperCase();

		return result;
	}

	/**
	 * HEX字符串前补0，主要用于长度位数不足。
	 * 
	 * @param str
	 *            String 需要补充长度的十六进制字符串
	 * @param maxLength
	 *            int 补充后十六进制字符串的长度
	 * @return 补充结果
	 */
	static public String patchHexString(String str, int maxLength) {
		String temp = "";
		for (int i = 0; i < maxLength - str.length(); i++) {
			temp = "0" + temp;
		}
		str = (temp + str).substring(0, maxLength);
		return str;
	}

	/**
	 * 将一个字符串转换为int
	 * 
	 * @param s
	 *            String 要转换的字符串
	 * @param defaultInt
	 *            int 如果出现异常,默认返回的数字
	 * @param radix
	 *            int 要转换的字符串是什么进制的,如16 8 10.
	 * @return int 转换后的数字
	 */
	public static int parseToInt(String s, int defaultInt, int radix) {
		int i = 0;
		try {
			i = Integer.parseInt(s, radix);
		} catch (NumberFormatException ex) {
			i = defaultInt;
		}
		return i;
	}

	/**
	 * 将一个十进制形式的数字字符串转换为int
	 * 
	 * @param s
	 *            String 要转换的字符串
	 * @param defaultInt
	 *            int 如果出现异常,默认返回的数字
	 * @return int 转换后的数字
	 */
	public static int parseToInt(String s, int defaultInt) {
		int i = 0;
		try {
			i = Integer.parseInt(s);
		} catch (NumberFormatException ex) {
			i = defaultInt;
		}
		return i;
	}

	/**
	 * 十六进制字符串转为Byte数组,每两个十六进制字符转为一个Byte
	 * 
	 * @param hexString
	 *            十六进制字符串
	 * @return byte 转换结果
	 */
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		int remainder = hexString.length() % 2;
		if (remainder != 0) {

			hexString = hexString.substring(0, hexString.length() - 1) + "0"
					+ hexString.substring(hexString.length() - 1);
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	/**
	 * Convert char to byte
	 * 
	 * @param c
	 *            char
	 * @return byte
	 */
	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	/**
	 * 十六进制串转化为byte数组
	 * 
	 * @return the array of byte
	 */
	public static final byte[] hex2bytes(String hex)
			throws IllegalArgumentException {
		if (hex.length() % 2 != 0) {
			throw new IllegalArgumentException();
		}
		char[] arr = hex.toCharArray();
		byte[] b = new byte[hex.length() / 2];
		for (int i = 0, j = 0, l = hex.length(); i < l; i++, j++) {
			String swap = "" + arr[i++] + arr[i];
			int byteint = Integer.parseInt(swap, 16) & 0xFF;
			b[j] = new Integer(byteint).byteValue();
		}
		return b;
	}
	
	public static String binaryString2hexString(String bString)  
    {  
        if (bString == null || bString.equals("") || bString.length() % 8 != 0)  
            return null;  
        StringBuffer tmp = new StringBuffer();  
        int iTmp = 0;  
        for (int i = 0; i < bString.length(); i += 4)  
        {  
            iTmp = 0;  
            for (int j = 0; j < 4; j++)  
            {  
                iTmp += Integer.parseInt(bString.substring(i + j, i + j + 1)) << (4 - j - 1);  
            }  
            tmp.append(Integer.toHexString(iTmp));  
        }  
        return tmp.toString();  
    } 

	/**
	 * 字节数组转换为十六进制字符串
	 * 
	 * @param b
	 *            byte[] 需要转换的字节数组
	 * @return String 十六进制字符串
	 */
	public static final String bytes2hex(byte b[]) {
		if (b == null) {
			throw new IllegalArgumentException(
					"Argument b ( byte array ) is null! ");
		}
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = Integer.toHexString(b[n] & 0xff);
			if (stmp.length() == 1) {
				hs = hs + "0" + stmp;
			} else {
				hs = hs + stmp;
			}
		}
		return hs.toUpperCase();
	}

	/**
	 * @Title: int2bytes32
	 * @Description: int 转32位 byte 数组
	 * @param res
	 * @return byte[]
	 * @throws
	 */
	public static byte[] int2bytes(int res) {
		byte[] b = new byte[4];
		for (int i = 0; i < 4; i++) {
			b[i] = (byte) (res >> 8 * (3 - i) & 0xFF);
		}
		return b;
	}

	/**
	 * @Title: int2bytes16
	 * @Description: int 转16位 byte 数组
	 * @param res
	 * @return byte[]
	 * @throws
	 */
	public static byte[] short2bytes(short res) {
		byte[] b = new byte[2];
		b[1] = (byte) (res & 0xff);
		b[0] = (byte) (res >> 8 & 0xff);

		return b;
	}

	public static short bytes2short(byte[] b) {
		return (short) (b[1] & 0xff | (b[0] & 0xff) << 8);
	}

	/**
	 * @Title: byte2hex
	 * @Description: TODO
	 * @param b
	 * @return String
	 * @throws
	 */
	public static final String byte2hex(byte b) {
		StringBuffer buf = new StringBuffer();
		char[] hexChars = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F' };
		int high = ((b & 0xf0) >> 4);
		int low = (b & 0x0f);
		buf.append(hexChars[high]);
		buf.append(hexChars[low]);
		return buf.toString();
	}

	public static final int byte2int(byte b) {
		return b & 0xff;
	}

	/**
	 * @Title: checkSum
	 * @Description: 校验和
	 * @param b
	 * @return byte[]
	 * @throws
	 */
	public static byte[] checkSum(int[] b) {

		byte sum = (byte) 0;

		byte[] command = new byte[b.length + 1];

		for (int i = 0; i < b.length; i++) {

			command[i] = (byte) (b[i] & 0xFF);

		}

		for (int i = 0; i < command.length - 1; i++) {

			sum += command[i];
		}

		sum = (byte) ((~sum) + 1);

		command[command.length - 1] = sum;

		return command;

	}

	/**
	 * @Title: IntToByteArray
	 * @Description: 将int转为低字节在前，高字节在后的byte数组
	 * @param n
	 * @return byte[]
	 * @throws
	 */
	// 封包int算法
	public static byte[] IntToByteArray(int n) {
		byte[] b = new byte[4];
		b[0] = (byte) (n & 0xff);
		b[1] = (byte) (n >> 8 & 0xff);
		b[2] = (byte) (n >> 16 & 0xff);
		b[3] = (byte) (n >> 24 & 0xff);
		return b;
	}

	/**
	 * byte数组转化为int 将低字节在前，高字节在后的byte数组转为int
	 */
	// 解包为int算法
	public static int ByteArrayToInt(byte[] bArr) {
		if (bArr.length != 4) {
			return -1;
		}
		return (int) ((((bArr[3] & 0xff) << 24) | ((bArr[2] & 0xff) << 16)
				| ((bArr[1] & 0xff) << 8) | ((bArr[0] & 0xff) << 0)));
	}

	/**
	 * 将short转为低字节在前，高字节在后的byte数组(网络字节)
	 */
	// 封包short算法
	public static byte[] ShorttoByteArray(short n) {
		byte[] b = new byte[2];
		b[0] = (byte) (n & 0xff);
		b[1] = (byte) (n >> 8 & 0xff);
		return b;
	}

	// 解包为short算法
	public static short ByteArraytoShort(byte[] b) {
		short iOutcome = 0;
		byte bLoop;
		for (int i = 0; i < 2; i++) {
			bLoop = b[i];
			iOutcome += (bLoop & 0xff) << (8 * i);
		}
		return iOutcome;
	}

	/**
	 * 将byte数组转化成String,为了支持中文，转化时用UTF-8编码方式
	 */
	public static String ByteArraytoString(byte[] valArr, int maxLen) {
		String result = null;
		int index = 0;
		while (index < valArr.length && index < maxLen) {
			if (valArr[index] == 0) {
				break;
			}
			index++;
		}
		byte[] temp = new byte[index];
		System.arraycopy(valArr, 0, temp, 0, index);
		try {
			result = new String(temp, encode);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 将String转化为byte,为了支持中文，转化时用UTF-8编码方式
	 */
	public static byte[] StringToByteArray(String str) {
		byte[] temp = null;
		try {
			temp = str.getBytes(encode);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return temp;
	}

	public static byte[] getCRC16(byte[] source) {

		byte[] pcrc = new byte[2];
		int buflen = source.length;
		int CRC = 0x0000ffff;
		int POLYNOMIAL = 0x0000a001;
		int i, j;

		for (i = 0; i < buflen; i++) {
			CRC ^= ((int) source[i] & 0x000000ff);
			for (j = 0; j < 8; j++) {
				if ((CRC & 0x00000001) != 0) {
					CRC >>= 1;
					CRC ^= POLYNOMIAL;
				} else {
					CRC >>= 1;
				}
			}
		}

		// System.out.println(Integer.toHexString(CRC));
		pcrc[0] = (byte) (CRC & 0x00ff);
		pcrc[1] = (byte) (CRC >> 8);

		return pcrc;
	}

	public static byte[] getSendDatas(byte[] datas) {

		byte[] crcs = getCRC16(datas);

		byte[] sends = new byte[datas.length + crcs.length];

		System.arraycopy(datas, 0, sends, 0, datas.length);
		System.arraycopy(crcs, 0, sends, datas.length, crcs.length);

		return sends;
	}

	public static byte[] toPrimitive(List<Byte> byteArray) {

		Byte[] array = (Byte[]) byteArray.toArray(new Byte[byteArray.size()]);

		byte[] result = new byte[array.length];
		for (int i = 0; i < array.length; ++i) {
			result[i] = array[i].byteValue();
		}
		return result;
	}

	public static String formatNum(int num) {
		if (num < 10) {
			return "0" + num;
		} else {
			return Integer.toString(num);
		}
	}

	public static byte[] get32Bytes(byte[] data) {

		int sLen = 32;

		int len = data.length;
		if (len > sLen) {

			byte[] tmp1 = new byte[sLen];
			System.arraycopy(data, 0, tmp1, 0, sLen);
			return tmp1;

		} else if (len < sLen) {

			byte[] tmp2 = new byte[sLen];

			System.arraycopy(data, 0, tmp2, 0, len);

			return tmp2;

		} else {

			return data;
		}
	}
}
