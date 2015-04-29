package com.kingdee.eas.jc.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.kingdee.eas.jc.exception.EASException;

/**
 * <p>
 * 类名:StringUtil
 * </p>
 * <p>
 * 类说明:字符串帮助类。
 * </p>
 *
 * @version 1.0
 * @since J2SDK1.4.2
 * @author KINGDEE 黎亮
 */

public class StringUtil {

	/**
	 * 方法名 ：isValidJobId<BR>
	 * 方法说明 ：验证JobID<BR>
	 * 备注 ：无
	 *
	 * @author KINGDEE)黎亮
	 * @Date: 2008/12/10
	 * @Version: 1.0
	 *
	 */
	public static boolean isValidJobId(String jobId) {
		return jobId.matches("JOB[0-9]{3}");
	}

	/**
	 * 方法名 ：getBIEOFMessage<BR>
	 * 方法说明 ：得到BI格式结束消息<BR>
	 * 备注 ：无
	 *
	 * @author KINGDEE)黎亮
	 * @Date: 2008/12/10
	 * @Version: 1.0
	 *
	 */
	public static byte[] getBIEOFMessage() {

		byte[] eof = new byte[3];
		eof[0] = 'E';
		eof[1] = 'O';
		eof[2] = 'F';
		return eof;
	}

	/**
	 * 方法名 ：getMQFormatStr<BR>
	 * 方法说明 ：得到OUT文件格式化字符串<BR>
	 * 备注 ：无
	 *
	 * @author KINGDEE)黎亮
	 * @Date: 2008/12/10
	 * @Version: 1.0
	 *
	 */
	public static String getMQFormatStr(String data, int length) {

		if (data.length() >= length) {
			// 如果数据大于指定长度截取数据
			return data.substring(0, length);
		} else {
			// 如果数据小于指定长度补充空格
			StringBuffer buf = new StringBuffer();
			buf.append(data);
			for (int i = 0; i < length - data.length(); i++) {

				buf.append(" ");
			}
			return buf.toString();
		}
	}

	/**
	 * 方法名 ：getOUTFormatStr<BR>
	 * 方法说明 ：得到OUT文件格式化字符串<BR>
	 * 备注 ：无
	 *
	 * @author KINGDEE)黎亮
	 * @Date: 2008/12/10
	 * @Version: 1.0
	 *
	 */
	public static String getOUTFormatStr(String data, List list) {

		// 数据为空返回指定长度的字符串
		if (StringUtil.stringIsEmpty(data)) {
			StringBuffer buf = new StringBuffer();
			for (int i = 0; i < new Integer((String) list.get(0)).intValue(); i++) {
				buf.append(" ");
			}
			return buf.toString();
		}

		// 得到字段类型
		String type = (String) list.get(1);
		if (Constants.DB_CHAR.equalsIgnoreCase(type)) {
			// 字符类型
			return getCharStr(data, new Integer((String) list.get(0))
					.intValue());
		} else if (Constants.DB_NUMERIC.equalsIgnoreCase(type)) {
			// 数字类型
			return getDoubleStr(data, new Integer((String) list.get(0))
					.intValue() - 1, new Integer((String) list.get(2))
					.intValue());
		} else {
			// 其他类型
			return getCharStr(data, new Integer((String) list.get(0))
					.intValue());
		}
	}

	/**
	 * 方法名 ：getIntegerStr<BR>
	 * 方法说明 ：得到OUT文件格式化后的整型字符串<BR>
	 * 备注 ：无
	 *
	 * @author KINGDEE)黎亮
	 * @Date: 2008/12/10
	 * @Version: 1.0
	 *
	 * @param 要格式化的数据字符串
	 * @param 配置文件中的长度
	 *
	 */
	private static String getIntegerStr(String data, int length) {

		// 数据为空返回空字串
		if (StringUtil.stringIsEmpty(data)) {
			return "";
		}

		// 不是数字类型返回空字符串
		if (!StringUtil.isNum(data)) {
			return "";
		}

		StringBuffer buf = new StringBuffer();

		if (!data.startsWith("-")) {
			// 不是负数加上加号
			buf.append("+");
		} else {
			// 负数加上减号
			buf.append("-");
			data = data.replace("-", "");
		}

		if (data.length() >= length) {
			// 数据长度大于配置文件的长度截取数据
			buf.append(data.substring(0, length));
		} else {
			buf.append(data);
			for (int i = 0, size = length - data.length(); i < size; i++) {
				// 数据长度小于配置文件长度填充空格
				buf.append(" ");
			}
		}

		return buf.toString();
	}

	/**
	 * 方法名 ：getDoubleStr<BR>
	 * 方法说明 ：得到OUT文件格式化后的小数类型字符串<BR>
	 * 备注 ：无
	 *
	 * @author KINGDEE)黎亮
	 * @Date: 2008/12/10
	 * @Version: 1.0
	 *
	 * @param data
	 *            要格式化的数据字符串
	 * @param length
	 *            配置文件中的长度
	 * @param pre
	 *            精度
	 *
	 */
	public static String getDoubleStr(String data, int length, int pre) {

		// 数据为空返回空字串
		if (StringUtil.stringIsEmpty(data)) {
			StringBuffer buf = new StringBuffer();
			for (int i = 0; i <= length; i++) {
				buf.append(" ");
			}
			return buf.toString();
		}

		// 不是小数类型返回空字串
		if (!StringUtil.isBigDecimal(data)) {
			StringBuffer buf = new StringBuffer();
			for (int i = 0; i <= length; i++) {
				buf.append(" ");
			}
			return buf.toString();
		}

		BigDecimal bg = new BigDecimal(data);
		// 设置精度
		bg = bg.setScale(pre, BigDecimal.ROUND_HALF_UP);
		data = bg.toString();
		StringBuffer buf = new StringBuffer();

		if (!data.startsWith("-")) {
			// 不是负数加上加号
			buf.append("+");
			data = data.replace(".", "");
		} else {
			// 负数加上减号
			buf.append("-");
			data = data.replace("-", "");
			data = data.replace(".", "");
		}

		if (data.length() >= length) {
			// 数据长度大于配置文件的长度截取数据
			buf.append(data.substring(0, length));
		} else {
			// 数据长度小于配置文件长度填充0
			for (int i = 0, size = length - data.length(); i < size; i++) {
				buf.append("0");
			}
			buf.append(data);
		}

		return buf.toString();
	}

	/**
	 * 方法名 ：getCharStr<BR>
	 * 方法说明 ：得到OUT文件格式化后的字符类型字符串<BR>
	 * 备注 ：无
	 *
	 * @author KINGDEE)黎亮
	 * @Date: 2008/12/10
	 * @Version: 1.0
	 *
	 * @param data
	 *            要格式化的数据字符串
	 * @param length
	 *            配置文件中的长度
	 *
	 */
	private static String getCharStr(String data, int length) {

		// 数据为空返回空字串
		if (StringUtil.stringIsEmpty(data)) {
			return "";
		}

		StringBuffer buf = new StringBuffer();
		byte[] strs = data.getBytes();
		if (strs.length >= length) {
			// 数据长度大于配置文件的长度截取数据
			buf.append(new String(StringUtil.getStringValue(strs, 0, length)));
		} else {
			// 数据长度小于配置文件长度填充空格
			buf.append(data);
			for (int i = 0, size = length - strs.length; i < size; i++) {
				buf.append(" ");
			}
		}
		return buf.toString();
	}

	/**
	 * 方法名 ：isBIDateFormat<BR>
	 * 方法说明 ：判断字符串是否是YYYYMMDDHHMISS的14位日期<BR>
	 * 备注 ：无
	 *
	 * @author KINGDEE)黎亮
	 * @Date: 2008/12/12
	 * @Version: 1.0
	 *
	 * @param str
	 *            要格式化的数据字符串
	 *
	 */
	public static boolean isBIDateFormat(String str) {

		// 不是数字类型返回错误
		if (!StringUtil.isNum(str)) {
			return false;
		}

		// 不是14未返回错误
		if (str.length() != 14) {
			return false;
		}

		// 得到月份，日期，小时，分钟，秒
		int month = new Integer(str.substring(4, 6)).intValue();
		int day = new Integer(str.substring(6, 8)).intValue();
		int h = new Integer(str.substring(8, 10)).intValue();
		int m = new Integer(str.substring(10, 12)).intValue();
		int s = new Integer(str.substring(12, 14)).intValue();

		if (month > 12) {
			// 月份大于12，非法日期
			return false;
		}

		if (day > 31) {
			// 日期大于31，非法日期
			return false;
		}

		if (h > 23) {
			// 小时大于23，非法日期
			return false;
		}

		if (m > 59) {
			// 分钟大于59，非法日期
			return false;
		}

		if (s > 59) {
			// 秒大于59
			return false;
		}

		try {
			// 格式化14位数据
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
			Date date = format.parse(str);
			// 验证是否有11月31日这种非法日期
			int newMonth = date.getMonth() + 1;
			if (newMonth != month) {
				return false;
			}
		} catch (ParseException e) {
			e.printStackTrace();
			LoggerUtil.logger.error("StringUtil/isBIDateFormat():"
					+ e.getMessage() + "#" + e.getCause());
			return false;
		}

		return true;
	}

	/**
	 * 方法名 ：isMSTDateFormat<BR>
	 * 方法说明 ：判断字符串是否是YYYYMMDD的8位日期<BR>
	 * 备注 ：无
	 *
	 * @author KINGDEE)黎亮
	 * @Date: 2008/12/12
	 * @Version: 1.0
	 *
	 * @param str
	 *            要格式化的数据字符串
	 *
	 */
	public static boolean isMSTDateFormat(String str) {

		// 如果不是数字类型返回错误，包含是Null的情况
		if (!StringUtil.isNum(str)) {
			return false;
		}

		// 99999999是正确的Master格式，在EAS系统中为99991231
		if ("99999999".equals(str)) {
			return true;
		}

		// 不是八位的数字返回错误
		if (str.length() != 8) {
			return false;
		}

		// 得到月份和日期
		int month = new Integer(str.substring(4, 6)).intValue();
		int day = new Integer(str.substring(6, 8)).intValue();

		if (month > 12) {
			// 月份大于12，非法日期
			return false;
		}

		if (day > 31) {
			// 日期大于31，非法日期
			return false;
		}

		try {
			// 格式化8位数据
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			Date date = format.parse(str);
			// 验证是否有11月31日这种非法日期
			int newMonth = date.getMonth() + 1;
			if (newMonth != month) {
				return false;
			}
		} catch (ParseException e) {
			e.printStackTrace();
			LoggerUtil.logger.error("StringUtil/isMSTDateFormat():"
					+ e.getMessage() + "#" + e.getCause());
			return false;
		}

		return true;
	}

	/**
	 * 方法名 ：getBigDecimal<BR>
	 * 方法说明 ：根据字符串和小数点位数返回BigDecimal<BR>
	 * 备注 ：无
	 *
	 * @author KINGDEE)黎亮
	 * @Date: 2008/12/12
	 * @Version: 1.0
	 *
	 * @param str
	 *            要格式化的数据字符串
	 * @param point
	 *            小数点位数
	 *
	 */
	public static String getBigDecimal(String str, int point) {

		if (point < str.length()) {
			return str.substring(0, str.length() - point) + "."
					+ str.substring(str.length() - point);
		} else {
			int zeroNum = point - str.length();
			StringBuffer buf = new StringBuffer();
			buf.append("0.");
			for (int i = 0; i < zeroNum; i++) {
				buf.append("0");
			}
			return buf.append(str).toString();
		}

	}

	/**
	 * 方法名 ：isValidUpdateFlag<BR>
	 * 方法说明 ：验证是否正确的MasterUpdateFlag<BR>
	 * 备注 ：无
	 *
	 * @author KINGDEE)黎亮
	 * @Date: 2008/12/12
	 * @Version: 1.0
	 *
	 * @param str
	 *            MasterUpdateFlag
	 *
	 */
	public static boolean isValidUpdateFlag(String str) {

		// 合法格式x，X，1，2，3，4，5，6，7，8，9，0
		if ("x".equalsIgnoreCase(str) || StringUtil.isNum(str)) {
			return true;
		}
		return false;
	}

	/**
	 * 方法名 ：loginResult<BR>
	 * 方法说明 ：验证WebService登陆是否成功<BR>
	 * 备注 ：无
	 *
	 * @author KINGDEE)黎亮
	 * @Date: 2008/12/12
	 * @Version: 1.0
	 *
	 * @param str
	 *            WebService登陆后得到的Session信息
	 *
	 */
	public static boolean loginResult(String sessionId) {
		// 例如2989ed16-011b-1000-e000-092cc0a80165
		return sessionId
				.matches("[a-zA-Z_0-9]{8}-[a-zA-Z_0-9]{4}-[a-zA-Z_0-9]{4}-[a-zA-Z_0-9]{4}-[a-zA-Z_0-9]{12}");
	}

	/**
	 * 方法名 ：changeNull2Str<BR>
	 * 方法说明 ：把Null替换为空字串<BR>
	 * 备注 ：无
	 *
	 * @author KINGDEE)黎亮
	 * @Date: 2008/12/12
	 * @Version: 1.0
	 *
	 * @param str
	 *            字符串
	 *
	 * @return 过滤后的字符串
	 */
	public static String changeNull2Str(String str) {
		if (str == null) {
			return "";
		}
		return str;
	}

	/**
	 * 方法名 ：isNum<BR>
	 * 方法说明 ：判断字符串是否是整型格式<BR>
	 * 备注 ：无
	 *
	 * @author KINGDEE)黎亮
	 * @Date: 2008/12/12
	 * @Version: 1.0
	 *
	 * @param str
	 *            字符串
	 *
	 */
	public static boolean isNum(String str) {

		if (str == null) {
			return false;
		}
		return str.matches("[0-9]+");
	}

	/**
	 * 方法名 ：isBigDecimal<BR>
	 * 方法说明 ：判断字符串是否是小数格式<BR>
	 * 备注 ：无
	 *
	 * @author KINGDEE)黎亮
	 * @Date: 2008/12/12
	 * @Version: 1.0
	 *
	 * @param str
	 *            字符串
	 *
	 */
	public static boolean isBigDecimal(String str) {

		if (str == null) {
			return false;
		}
		return str.trim().matches("[0-9.-]+");
	}

	/**
	 * 方法名 ：stringIsEmpty<BR>
	 * 方法说明 ：判断字符串是否是NUll或者空字串<BR>
	 * 备注 ：无
	 *
	 * @author KINGDEE)黎亮
	 * @Date: 2008/12/12
	 * @Version: 1.0
	 *
	 * @param str
	 *            字符串
	 *
	 */
	public static boolean stringIsEmpty(String str) {
		if (str == null || "".equals(str.trim())) {
			return true;
		}
		return false;
	}

	/**
	 * 方法名 ：isEmailFormat<BR>
	 * 方法说明 ：判断字符串是否是电子邮件格式<BR>
	 * 备注 ：无
	 *
	 * @author KINGDEE)黎亮
	 * @Date: 2008/12/12
	 * @Version: 1.0
	 *
	 * @param str
	 *            字符串
	 *
	 */
	public static boolean isEmailFormat(String str) {

		String compile = ".*?@.*?[.]";
		Pattern pattern = Pattern.compile(compile);
		Matcher matcher = pattern.matcher(str);
		if (matcher.find()) {
			return true;
		}

		return false;
	}

	/**
	 * 方法名 ：isEmailFormat<BR>
	 * 方法说明 ：判断字符串是否是决裁系统可接收的Master消息<BR>
	 * 备注 ：无
	 *
	 * @author KINGDEE)黎亮
	 * @Date: 2008/12/12
	 * @Version: 1.0
	 *
	 * @param str
	 *            字符串
	 *
	 */
	public static boolean isValidMessageName(String name) {

		if ("M01".equalsIgnoreCase(name) || "M02".equalsIgnoreCase(name)
				|| "M03".equalsIgnoreCase(name) || "M15".equalsIgnoreCase(name)
				|| "M21".equalsIgnoreCase(name))
			return true;
		return false;
	}

	/**
	 * 方法名 ：getStringValue<BR>
	 * 方法说明 ：字符串截取<BR>
	 * 备注 ：无
	 *
	 * @author KINGDEE)黎亮
	 * @Date: 2008/12/12
	 * @Version: 1.0
	 *
	 * @param str
	 *            字符串
	 *
	 */
	public static String getStringValue(byte[] record, int offset, int length) {
		return new String(record, offset, length);
	}

	/**
	 * 方法名 ：getMSTProcessStringValue<BR>
	 * 方法说明 ：字符串截取<BR>
	 * 备注 ：无
	 *
	 * @author KINGDEE)黎亮
	 * @Date: 2008/12/12
	 * @Version: 1.0
	 *
	 * @param str
	 *            字符串
	 *
	 */
	public static String getMSTProcessStringValue(byte[] record, int offset,
			int length) throws UnsupportedEncodingException {

		/** **For AIX*** */
		// return new String(new String(record, offset,
		// length).getBytes("ISO8859-1"),"GBK");
		/** *For Windows***** */
		return new String(new String(record, offset, length));
	}

	/**
	 * 方法名 ：getByteVale<BR>
	 * 方法说明 ：字符串截取<BR>
	 * 备注 ：无
	 *
	 * @author KINGDEE)黎亮
	 * @Date: 2008/12/12
	 * @Version: 1.0
	 *
	 * @param str
	 *            字符串
	 * @return 返回byte[]
	 */
	public static byte[] getByteVale(byte[] record, int offset, int length) {
		byte[] result = new byte[length];
		for (int i = 0; i < length; i++) {
			result[i] = record[offset + i];
		}
		return result;
	}

	/**
	 * 方法名 ：initMap<BR>
	 * 方法说明 ：初始化文件配置<BR>
	 * 备注 ：无
	 *
	 * @author KINGDEE)黎亮
	 * @Date: 2008/12/12
	 * @Version: 1.0
	 *
	 * @param file
	 *            配置文件
	 * @return Map 装载配置数据的Map
	 *
	 * @exception EASException
	 */
	public static Map initMap(File file) throws EASException {

		Map map = new HashMap();
		BufferedReader reader = null;
		String line = null;
		// 配置文件中的field列名称
		Pattern pattern = Pattern.compile("COLUMN[0-9]+=(.+)");

		try {
			reader = new BufferedReader(new FileReader(file));
			// 循环读取配置文件取数
			while ((line = reader.readLine()) != null) {
				// 匹配出配置参数
				Matcher matcher = pattern.matcher(line);
				boolean flag = matcher.matches();
				if (flag) {
					String[] cfgs = matcher.group(1).split(",");
					List list = new ArrayList();
					list.add(cfgs[1]);
					list.add(cfgs[2]);
					if (cfgs.length == 4) {
						list.add(cfgs[3]);
					}
					map.put(cfgs[0], list);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			LoggerUtil.logger.error(e.getMessage());
			throw new EASException("ERR999", e.getMessage() + "#" + e.getCause());
		} finally {
			// 关闭文件流
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
					LoggerUtil.logger.error(e.getMessage());
					throw new EASException("ERR998",  e.getMessage() + "#" + e.getCause());
				}
			}
		}

		return map;
	}
}
