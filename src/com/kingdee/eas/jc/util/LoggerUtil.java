package com.kingdee.eas.jc.util;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * <p>
 * 	类名:LoggerUtil
 * </p>
 * <p>
 * 	类说明:Log4j帮助类。
 * </p>
 *
 * @version 1.0
 * @since J2SDK1.4.2
 * @author KINGDEE 黎亮
*/
public class LoggerUtil {

	/****************日志实例********************/
	public static Logger logger;

	/**
	 * 方法名 ：init<BR>
	 * 方法说明：初始化日志实例<BR>
	 * 备注 ：无
	 *
	 * @author KINGDEE)黎亮
	 * @Date: 2008/12/03
	 * @Version: 1.0
	 *
	 * @param propPath log4j配置文件
	 * @param clazz 类名
	 *
	 */
	public static void init(String propPath, String clazz) {

		//加载log4j配置文件
		PropertyConfigurator.configure(propPath);
		logger = Logger.getLogger(clazz);
	}
}
