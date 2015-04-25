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
 * 	����:OUTFileCreator
 * </p>
 * <p>
 * 	��˵��:Properties�����ࡣ
 * </p>
 *
 * @version 1.0
 * @since J2SDK1.4.2
 * @author KINGDEE ����
*/

public class PropertiesUtil {

	/*******************�����ļ�����**********************/
	private Properties properties;

	/**
	 * ������ ��PropertiesUtil<BR>
	 * ����˵�� �����췽��<BR>
	 * ��ע �����췽��
	 *
	 * @author KINGDEE)����
	 * @Date: 2008/12/10
	 * @Version: 1.0
	 *
	 * @throws EASException
	 */
	public PropertiesUtil(String propertiesPath) throws EASException {

		try {

			//���������ļ�
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
	 * ������ ��getValue<BR>
	 * ����˵�� ���õ������ļ��е�����<BR>
	 * ��ע ����
	 *
	 * @author KINGDEE)����
	 * @Date: 2008/12/10
	 * @Version: 1.0
	 *
	 * @param keyֵ
	 *
	 * @throws EASException
	 */
	public String getValue(String key) {

		//��֤keyֵ
		if (StringUtil.stringIsEmpty(key))
			return null;

		String result = properties.getProperty(key);
		if (result == null) {
			return result;
		}
		return result.trim();
	}
}
