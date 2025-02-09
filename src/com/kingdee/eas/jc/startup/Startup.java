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

	/** *********************SAB处理配置文件名称************************* */

	/**
	 * 方法名 ：main<BR>
	 * 方法说明：入口方法<BR>
	 * 备注 ：无
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

		// 读数连接
		DBReadUtil dbread = DBReadUtil.getInstance();
		Connection readConn = null;

		// 循环读取事件表,中间间隔60s.
		while (true) {
			try {
				readConn = dbread.getConnection();
				EventDAO dao = new EventDAO();
				List list = dao.read(readConn);
				DataProcess dp = new DataProcess();
				dp.processEventList(list);
				try {
					Thread.sleep(600000);
				} catch (InterruptedException e) {
					LoggerUtil.logger.error(e.getMessage());
					e.printStackTrace();
				}
			} catch (Exception e) {
				LoggerUtil.logger.error(e.getMessage(),e);
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
