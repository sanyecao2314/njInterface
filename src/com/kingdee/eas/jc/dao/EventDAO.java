package com.kingdee.eas.jc.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.kingdee.eas.jc.bean.EventInfo;
import com.kingdee.eas.jc.util.LoggerUtil;

public class EventDAO {
	
	/**
	 * 读取事件表
	 * @param conn 
	 * @return 
	 */
	public static List read(Connection conn){
		Statement stmt = null;
		ResultSet rs = null;
		List list = new ArrayList();
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select event_id, object_key, object_name, object_function, EVENT_STATUS from OBJECT_NAME "
//					+ "where object_name in ('T_COS_BUNKER_STOCKIN','T_COS_BUNKER_CONSUME')  order by event_id"); //and object_key='Lymza/ArSUiT3pJif7fOP0cQnlc=' 
					+ "where object_name in ('T_COS_BUNKER_CONSUME')  order by event_id");
			while (rs.next()) {
				EventInfo eventInfo = new EventInfo();
				String obj = null;
				Integer obj1 = 1;
				// ID
				obj = rs.getString("event_id");
				if (obj != null) {
					eventInfo.setEventid(new String(obj.getBytes(),"GBK"));
				}
				obj = rs.getString("object_key");
				if (obj != null) {
					eventInfo.setObjectkey(new String(obj.getBytes(),"GBK"));
				}
				obj = rs.getString("object_name");
				if (obj != null) {
					eventInfo.setObjectname(new String(obj.getBytes(),"GBK"));
				}
				obj = rs.getString("object_function");
				if (obj != null) {
					eventInfo.setObjectfunction(new String(obj.getBytes(),"GBK"));
				}
//				obj = rs.getString("event_priority");
//				if (obj != null) {
//					eventInfo.setEventpriority(new String(obj.getBytes(),"GBK"));
//				}
				obj1 = rs.getInt("event_status");
				if (obj1 != null) {
					eventInfo.setEventstatus(obj1.toString());
				}
//				obj = rs.getString("connector_ID");
//				if (obj != null) {
//					eventInfo.setConnectorID(new String(obj.getBytes(),"GBK"));
//				}
				list.add(eventInfo);
			}
			
		} catch (Exception e) {
			LoggerUtil.logger.error(e.getMessage(),e);
		}finally{
			// 关闭游标
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					// SQL Exception
					LoggerUtil.logger.error(e.getMessage());
//					throw new EASException("ERR998", OUTExceptionInfo.ERR998
//							+ e.getMessage());
				}
			}
			// 关闭结果集合
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// SQL Exception
					LoggerUtil.logger.error(e.getMessage());
//					throw new EASException("ERR998", OUTExceptionInfo.ERR998
//							+ e.getMessage());
				}
			}
		}
		return list;
	}

}
