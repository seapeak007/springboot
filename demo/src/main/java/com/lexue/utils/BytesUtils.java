package com.lexue.utils;


import java.nio.charset.Charset;

public class BytesUtils {
	// //////////////////////////////////////////////////////////
	// 静态方法－基础数据类型to二进制。
	// //////////////////////////////////////////////////////////
	/**
	 * 转换基本类型到二进制表示。
	 * 
	 * @param value
	 *            数据。
	 * @return null 系统错误。
	 */
	public static byte[] convertLongToBytes(long value) {
		byte[] b = new byte[8];
		b[0] = (byte) (value >>> 56);
		b[1] = (byte) (value >>> 48);
		b[2] = (byte) (value >>> 40);
		b[3] = (byte) (value >>> 32);
		b[4] = (byte) (value >>> 24);
		b[5] = (byte) (value >>> 16);
		b[6] = (byte) (value >>> 8);
		b[7] = (byte) (value);
		return b;
	}

	/**
	 * 转换基本类型到二进制表示。
	 * 
	 * @param value
	 *            数据。
	 * @param output
	 *            输出缓存。
	 * @param outputOffset
	 *            输出缓存偏移量。
	 * @return 长度。
	 */
	public static int convertLongToBytes(long value, byte[] output, int outputOffset) {
		output[outputOffset] = (byte) (value >>> 56);
		output[outputOffset + 1] = (byte) (value >>> 48);
		output[outputOffset + 2] = (byte) (value >>> 40);
		output[outputOffset + 3] = (byte) (value >>> 32);
		output[outputOffset + 4] = (byte) (value >>> 24);
		output[outputOffset + 5] = (byte) (value >>> 16);
		output[outputOffset + 6] = (byte) (value >>> 8);
		output[outputOffset + 7] = (byte) (value);
		return 8;
	}

	/**
	 * 转换基本类型到二进制表示。
	 * 
	 * @param value
	 *            数据。
	 * @return null 系统错误。
	 */
	public static byte[] convertIntToBytes(int value) {
		byte[] b = new byte[4];
		b[0] = (byte) (value >>> 24);
		b[1] = (byte) (value >>> 16);
		b[2] = (byte) (value >>> 8);
		b[3] = (byte) (value);
		return b;
	}

	/**
	 * 转换基本类型到二进制表示。
	 * 
	 * @param value
	 *            数据。
	 * @param output
	 *            输出缓存。
	 * @param outputOffset
	 *            输出缓存偏移量。
	 * @return 长度。
	 */
	public static int convertIntToBytes(int value, byte[] output, int outputOffset) {
		output[outputOffset] = (byte) (value >>> 24);
		output[outputOffset + 1] = (byte) (value >>> 16);
		output[outputOffset + 2] = (byte) (value >>> 8);
		output[outputOffset + 3] = (byte) (value);
		return 4;
	}

	/**
	 * 转换基本类型到二进制表示。
	 * 
	 * @param value
	 *            数据。
	 * @return null 系统错误。
	 */
	public static byte[] convertShortToBytes(short value) {
		byte[] b = new byte[2];
		b[0] = (byte) (value >>> 8);
		b[1] = (byte) (value);
		return b;
	}

	/**
	 * 转换基本类型到二进制表示。
	 * 
	 * @param value
	 *            数据。
	 * @param output
	 *            输出缓存。
	 * @param outputOffset
	 *            输出缓存偏移量。
	 * @return 长度。
	 */
	public static int convertShortToBytes(short value, byte[] output, int outputOffset) {
		output[outputOffset] = (byte) (value >>> 8);
		output[outputOffset + 1] = (byte) (value);
		return 2;
	}

	/**
	 * 转换基本类型到二进制表示。
	 * 
	 * @param value
	 *            数据。
	 * @return null 系统错误。
	 */
	public static byte[] convertByteToBytes(byte value) {
		byte[] b = new byte[1];
		b[0] = value;
		return b;
	}

	/**
	 * 转换基本类型到二进制表示。
	 * 
	 * @param value
	 *            数据。
	 * @param output
	 *            输出缓存。
	 * @param outputOffset
	 *            输出缓存偏移量。
	 * @return 长度。
	 */
	public static int convertByteToBytes(byte value, byte[] output, int outputOffset) {
		output[outputOffset] = value;
		return 1;
	}

	/**
	 * 转换基本类型到二进制表示。
	 * 
	 * @param v
	 *            数据。
	 * @return null 系统错误。
	 */
	public static byte[] convertDoubleToBytes(double v) {
		long value = Double.doubleToLongBits(v);
		byte[] b = new byte[8];
		b[0] = (byte) (value >>> 56);
		b[1] = (byte) (value >>> 48);
		b[2] = (byte) (value >>> 40);
		b[3] = (byte) (value >>> 32);
		b[4] = (byte) (value >>> 24);
		b[5] = (byte) (value >>> 16);
		b[6] = (byte) (value >>> 8);
		b[7] = (byte) (value);
		return b;
	}

	/**
	 * 转换基本类型到二进制表示。
	 * 
	 * @param v
	 *            数据。
	 * @param output
	 *            输出缓存。
	 * @param outputOffset
	 *            输出缓存偏移量。
	 * @return 长度。
	 */
	public static int convertDoubleToBytes(double v, byte[] output, int outputOffset) {
		long value = Double.doubleToLongBits(v);
		output[outputOffset] = (byte) (value >>> 56);
		output[outputOffset + 1] = (byte) (value >>> 48);
		output[outputOffset + 2] = (byte) (value >>> 40);
		output[outputOffset + 3] = (byte) (value >>> 32);
		output[outputOffset + 4] = (byte) (value >>> 24);
		output[outputOffset + 5] = (byte) (value >>> 16);
		output[outputOffset + 6] = (byte) (value >>> 8);
		output[outputOffset + 7] = (byte) (value);
		return 8;
	}

	/**
	 * 转换基本类型到二进制表示。
	 * 
	 * @param v
	 *            数据。
	 * @return null 系统错误。
	 */
	public static byte[] convertFloatToBytes(float v) {
		int value = Float.floatToIntBits(v);
		byte[] b = new byte[4];
		b[0] = (byte) (value >>> 24);
		b[1] = (byte) (value >>> 16);
		b[2] = (byte) (value >>> 8);
		b[3] = (byte) (value);
		return b;
	}

	/**
	 * 转换基本类型到二进制表示。
	 * 
	 * @param v
	 *            数据。
	 * @param output
	 *            输出缓存。
	 * @param outputOffset
	 *            输出缓存偏移量。
	 * @return 长度。
	 */
	public static int convertFloatToBytes(float v, byte[] output, int outputOffset) {
		int value = Float.floatToIntBits(v);
		output[outputOffset] = (byte) (value >>> 24);
		output[outputOffset + 1] = (byte) (value >>> 16);
		output[outputOffset + 2] = (byte) (value >>> 8);
		output[outputOffset + 3] = (byte) (value);
		return 4;
	}

	/**
	 * 转换基本类型到二进制表示。
	 * 
	 * @param value
	 *            数据。
	 * @return null 系统错误。
	 */
	public static byte[] convertBooleanToBytes(boolean value) {
		byte[] b = new byte[1];
		if (value) {
			b[0] = 1;
		} else {
			b[0] = 0;
		}
		return b;
	}

	/**
	 * 转换基本类型到二进制表示。
	 * 
	 * @param value
	 *            数据。
	 * @param output
	 *            输出缓存。
	 * @param outputOffset
	 *            输出缓存偏移量。
	 * @return 长度。
	 */
	public static int convertBooleanToBytes(boolean value, byte[] output, int outputOffset) {
		if (value) {
			output[outputOffset] = 1;
		} else {
			output[outputOffset] = 0;
		}
		return 1;
	}

	/**
	 * 转换字符串到二进制表示。
	 * 
	 * @param value
	 *            数据。
	 * @return null 系统错误。
	 */
	public static byte[] convertStringToBytes(String value) {
		return value.getBytes(CommonSet.CHAR_SET);
	}

	/**
	 * 转换字符串到二进制表示。
	 * 
	 * @param value
	 *            数据。
	 * @param charset
	 *            字符集。
	 * @return null 系统错误。
	 */
	public static byte[] convertStringToBytes(String value, Charset charset) {
		return value.getBytes(charset);
	}

	/**
	 * 转换字符串到二进制表示。使用UTF-8默认编码。
	 * 
	 * @param value
	 *            数据。
	 * @param charset
	 *            字符集。
	 * @param output
	 *            输出缓存。
	 * @param outputOffset
	 *            输出缓存偏移量。
	 * @return 长度。
	 */
	public static int convertStringToBytes(String value, byte[] output, int outputOffset) {
		byte[] b = value.getBytes(CommonSet.CHAR_SET);
		for (int i = 0; i < b.length; i++) {
			output[outputOffset + i] = b[i];
		}
		return b.length;
	}

	/**
	 * 转换字符串到二进制表示。
	 * 
	 * @param value
	 *            数据。
	 * @param charset
	 *            字符集。
	 * @param output
	 *            输出缓存。
	 * @param outputOffset
	 *            输出缓存偏移量。
	 * @return 长度。
	 */
	public static int convertStringToBytes(String value, Charset charset, byte[] output, int outputOffset) {
		byte[] b = value.getBytes(charset);
		for (int i = 0; i < b.length; i++) {
			output[outputOffset + i] = b[i];
		}
		return b.length;
	}

	// //////////////////////////////////////////////////////////
	// 静态方法－二进制to基础数据类型。
	// //////////////////////////////////////////////////////////
	/**
	 * 转换二进制表示到基础类型。
	 * 
	 * @param value
	 *            二进制表示。
	 * @param offset
	 *            偏移量。
	 * @return
	 */
	public static long convertBytesToLong(byte[] value, int offset) {
		long l = 0;
		l |= 0xFFL & value[offset];
		l = l << 8;
		l |= 0xFFL & value[offset + 1];
		l = l << 8;
		l |= 0xFFL & value[offset + 2];
		l = l << 8;
		l |= 0xFFL & value[offset + 3];
		l = l << 8;
		l |= 0xFFL & value[offset + 4];
		l = l << 8;
		l |= 0xFFL & value[offset + 5];
		l = l << 8;
		l |= 0xFFL & value[offset + 6];
		l = l << 8;
		l |= 0xFFL & value[offset + 7];
		return l;
	}

	/**
	 * 转换二进制表示到基础类型。
	 * 
	 * @param value
	 *            二进制表示。
	 * @param offset
	 *            偏移量。
	 * @return
	 */
	public static int convertBytesToInt(byte[] value, int offset) {
		int i = 0;
		i |= 0xFF & value[offset];
		i = i << 8;
		i |= 0xFF & value[offset + 1];
		i = i << 8;
		i |= 0xFF & value[offset + 2];
		i = i << 8;
		i |= 0xFF & value[offset + 3];
		return i;
	}

	/**
	 * 转换二进制表示到基础类型。
	 * 
	 * @param value
	 *            二进制表示。
	 * @param offset
	 *            偏移量。
	 * @return
	 */
	public static short convertBytesToShort(byte[] value, int offset) {
		int i = 0;
		i |= 0xFF & value[offset];
		i = i << 8;
		i |= 0xFF & value[offset + 1];
		return (short) i;
	}

	/**
	 * 转换二进制表示到基础类型。
	 * 
	 * @param value
	 *            二进制表示。
	 * @param offset
	 *            偏移量。
	 * @return
	 */
	public static byte convertBytesToByte(byte[] value, int offset) {
		return value[offset];
	}

	/**
	 * 转换二进制表示到基础类型。
	 * 
	 * @param value
	 *            二进制表示。
	 * @param offset
	 *            偏移量。
	 * @return
	 */
	public static boolean convertBytesToBoolean(byte[] value, int offset) {
		if (value[offset] == 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 转换二进制表示到基础类型。
	 * 
	 * @param value
	 *            二进制表示。
	 * @param offset
	 *            偏移量。
	 * @return
	 */
	public static double convertBytesToDouble(byte[] value, int offset) {
		long l = 0;
		l |= 0xFFL & value[offset];
		l = l << 8;
		l |= 0xFFL & value[offset + 1];
		l = l << 8;
		l |= 0xFFL & value[offset + 2];
		l = l << 8;
		l |= 0xFFL & value[offset + 3];
		l = l << 8;
		l |= 0xFFL & value[offset + 4];
		l = l << 8;
		l |= 0xFFL & value[offset + 5];
		l = l << 8;
		l |= 0xFFL & value[offset + 6];
		l = l << 8;
		l |= 0xFFL & value[offset + 7];
		return Double.longBitsToDouble(l);
	}

	/**
	 * 转换二进制表示到基础类型。
	 * 
	 * @param value
	 *            二进制表示。
	 * @param offset
	 *            偏移量。
	 * @return
	 */
	public static float convertBytesToFloat(byte[] value, int offset) {
		int i = 0;
		i |= 0xFF & value[offset];
		i = i << 8;
		i |= 0xFF & value[offset + 1];
		i = i << 8;
		i |= 0xFF & value[offset + 2];
		i = i << 8;
		i |= 0xFF & value[offset + 3];
		return Float.intBitsToFloat(i);
	}

	/**
	 * 转换二进制表示到基础类型。采用默认UTF-8字符集。
	 * 
	 * @param value
	 *            二进制表示。
	 * @param offset
	 *            偏移量。
	 * @return
	 */
	public static String convertBytesToString(byte[] value, int offset, int length) {
		return new String(value, offset, length, CommonSet.CHAR_SET);
	}

	/**
	 * 转换二进制表示到基础类型。
	 * 
	 * @param value
	 *            二进制表示。
	 * @param offset
	 *            偏移量。
	 * @param charset
	 *            字符集。
	 * @return
	 */
	public static String convertBytesToString(byte[] value, int offset, int length, Charset charset) {
		return new String(value, offset, length, charset);
	}
}
