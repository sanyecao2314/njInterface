package com.kingdee.eas.jc.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 * 类名:TimeUtil
 * </p>
 * <p>
 * 类说明:时间帮助类。
 * </p>
 *
 * @version 1.0
 * @since J2SDK1.4.2
 * @author KINGDEE 黎亮
 */
public class TimeUtil {

	/**
	 * 方法名 ：get14TransDate<BR>
	 * 方法说明 ：得到14位日期<BR>
	 * 备注 ：无
	 *
	 * @author KINGDEE)黎亮
	 * @Date: 2008/12/10
	 * @Version: 1.0
	 *
	 * @exception ParseException
	 *
	 * @return 注意是java.sql.Date
	 */
	public java.sql.Date get14TransDate(String dataStr) throws ParseException {

		return new java.sql.Date(new SimpleDateFormat("yyyyMMddHHmmss").parse(
				dataStr).getTime());
	}

	/**
	 * 方法名 ：getCurrentTime<BR>
	 * 方法说明 ：得到当前时间<BR>
	 * 备注 ：无
	 *
	 * @author KINGDEE)黎亮
	 * @Date: 2008/12/10
	 * @Version: 1.0
	 *
	 */
	public String getCurrentTime() {
		return new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date());
	}

	/**
	 * 方法名 ：get8TransDate<BR>
	 * 方法说明 ：得到8位的日期<BR>
	 * 备注 ：无
	 *
	 * @author KINGDEE)黎亮
	 * @Date: 2008/12/10
	 * @Version: 1.0
	 *
	 */
	public String get8TransDate() {
		return new SimpleDateFormat("yyyyMMdd").format(new Date());
	}

	/**
	 * 方法名 ：getTransTime<BR>
	 * 方法说明 ：得到传输时间<BR>
	 * 备注 ：无
	 *
	 * @author KINGDEE)黎亮
	 * @Date: 2008/12/10
	 * @Version: 1.0
	 *
	 */
	public String getTransTime() {
		return new SimpleDateFormat("HHmmss").format(new Date());
	}

	/**
	 * 方法名 ：get14TransDate<BR>
	 * 方法说明 ：得到传输日期<BR>
	 * 备注 ：无
	 *
	 * @author KINGDEE)黎亮
	 * @Date: 2008/12/10
	 * @Version: 1.0
	 *
	 */
	public String get14TransDate() {
		return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	}
}
