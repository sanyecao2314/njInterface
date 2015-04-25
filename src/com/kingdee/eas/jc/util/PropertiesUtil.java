package com.kingdee.eas.jc.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.kingdee.eas.jc.exception.EASException;
/**
 * <p>
 * 	类名:OUTFileCreator
 * </p>
 * <p>
 * 	类说明:Properties帮助类。
 * </p>
 *
 * @version 1.0
 * @since J2SDK1.4.2
 * @author KINGDEE 黎亮
*/

public class PropertiesUtil {

	/*******************配置文件变量**********************/
	private Properties properties;

	/**
	 * 方法名 ：PropertiesUtil<BR>
	 * 方法说明 ：构造方法<BR>
	 * 备注 ：构造方法
	 *
	 * @author KINGDEE)黎亮
	 * @Date: 2008/12/10
	 * @Version: 1.0
	 *
	 * @throws EASException
	 */
	public PropertiesUtil(String propertiesPath) throws EASException {

		try {

			//加载配置文件
			InputStream in = new BufferedInputStream(new FileInputStream(
					propertiesPath));
			properties = new Properties();
			properties.load(in);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new EASException("ERR102", e.getMessage()+"#"+e.getCause());
		} catch (IOException e) {
			e.printStackTrace();
			throw new EASException("ERR103", e.getMessage()+"#"+e.getCause());
		}

	}

	/**
	 * 方法名 ：getValue<BR>
	 * 方法说明 ：得到配置文件中的数据<BR>
	 * 备注 ：无
	 *
	 * @author KINGDEE)黎亮
	 * @Date: 2008/12/10
	 * @Version: 1.0
	 *
	 * @param key值
	 *
	 * @throws EASException
	 */
	public String getValue(String key) {

		//验证key值
		if (StringUtil.stringIsEmpty(key))
			return null;

		String result = properties.getProperty(key);
		if (result == null) {
			return result;
		}
		return result.trim();
	}
}
