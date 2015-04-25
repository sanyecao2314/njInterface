package com.kingdee.eas.jc.util;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * <p>
 * 	����:LoggerUtil
 * </p>
 * <p>
 * 	��˵��:Log4j�����ࡣ
 * </p>
 *
 * @version 1.0
 * @since J2SDK1.4.2
 * @author KINGDEE ����
*/
public class LoggerUtil {

	/****************��־ʵ��********************/
	public static Logger logger;

	/**
	 * ������ ��init<BR>
	 * ����˵������ʼ����־ʵ��<BR>
	 * ��ע ����
	 *
	 * @author KINGDEE)����
	 * @Date: 2008/12/03
	 * @Version: 1.0
	 *
	 * @param propPath log4j�����ļ�
	 * @param clazz ����
	 *
	 */
	public static void init(String propPath, String clazz) {

		//����log4j�����ļ�
		PropertyConfigurator.configure(propPath);
		logger = Logger.getLogger(clazz);
	}
}
