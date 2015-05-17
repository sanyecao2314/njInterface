package com.kingdee.eas.jc.util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

public class DBTools {

	
	public static String getUUid(Connection conn,String bostype){
		// 获取EAS 用户ID
		CallableStatement cs = null;
		String iobjectPK = null;
		try {
			cs = conn.prepareCall("{ ?=call newbosid(?)}");
		cs.registerOutParameter(1, Types.VARCHAR);
		// Set the value for the IN parameter
		cs.setString(2, bostype);

		// Execute and retrieve the returned value
		cs.execute();
		iobjectPK = cs.getString(1);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				cs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return iobjectPK;
	}
	
	public static void close(Connection conn){
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public static void rollback(Connection conn){
		try {
			conn.rollback();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
}
