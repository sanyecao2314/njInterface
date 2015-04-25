package com.kingdee.eas.jc.startup;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.kingdee.eas.jc.dao.EventDAO;
import com.kingdee.eas.jc.exception.EASException;
import com.kingdee.eas.jc.process.DataProcess;
import com.kingdee.eas.jc.util.DBReadUtil;
import com.kingdee.eas.jc.util.LoggerUtil;

public class Startup {

	/** *********************SAB���������ļ�����************************* */

	/**
	 * ������ ��main<BR>
	 * ����˵������ڷ���<BR>
	 * ��ע ����
	 * 
	 * @author xiaolong_fan.
	 * @Date: 2009/02/13
	 * @Version: 1.0
	 * 
	 * @throws EASException
	 */
	public static void main(String[] args) throws EASException {
		
		LoggerUtil.init("resource/log4j.properties", Startup.class.getName());
		
		LoggerUtil.logger.info("start");

		// ��������
		DBReadUtil dbread = DBReadUtil.getInstance();
		Connection readConn = null;

		// ѭ����ȡ�¼���,�м���60s.
		while (true) {
			try {
				readConn = dbread.getConnection();
				EventDAO dao = new EventDAO();
				List list = dao.read(readConn);
				DataProcess dp = new DataProcess();
				dp.processEventList(list);
				try {
					Thread.sleep(6000);
				} catch (InterruptedException e) {
					LoggerUtil.logger.error(e.getMessage());
					e.printStackTrace();
				}
			} catch (Exception e) {
				LoggerUtil.logger.error(e.getMessage());
				e.printStackTrace();
			}finally{
				if (readConn != null) {
					try {
						readConn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
