package com.kingdee.eas.jc.process;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;

import oracle.sql.TIMESTAMP;

import com.kingdee.eas.jc.bean.EventInfo;
import com.kingdee.eas.jc.exception.EASException;
import com.kingdee.eas.jc.exception.OUTExceptionInfo;
import com.kingdee.eas.jc.services.MaterialReqBillService;
import com.kingdee.eas.jc.services.PurInWarehsBillService;
import com.kingdee.eas.jc.util.Constants;
import com.kingdee.eas.jc.util.DBReadUtil;
import com.kingdee.eas.jc.util.DBWriteUtil;
import com.kingdee.eas.jc.util.LoggerUtil;
import com.kingdee.eas.jc.util.PropertiesUtil;

public class DataProcess {

	/**
	 * �����¼����ж��������ݼ�.
	 * 
	 * @param list
	 */
	public static void processEventList(List list) {
		if (list == null || list.size() <= 0)
			return;
		for (int i = 0; i < list.size(); i++) {
			EventInfo eventinfo = (EventInfo) list.get(i);
			processEventInfo(eventinfo);
		}
	}

	/**
	 * �����¼����е�ÿ����¼
	 * 
	 * @param eventinfo
	 */
	// private static void processEventInfo(EventInfo eventinfo) {
	// if (eventinfo == null)
	// return;
	//
	// Object[] obj = readData(eventinfo);
	//
	// if (Constants.CREATE.equals(eventinfo.getObjectfunction())) {
	// writeData(eventinfo, obj);
	// }else if(Constants.UPDATE.equals(eventinfo.getObjectfunction())){
	// updateData(eventinfo, obj);
	// }else if(Constants.DELETE.equals(eventinfo.getObjectfunction())){
	// deleteData(eventinfo, obj);
	// }
	// }
	/**
	 * �����¼����е�ÿ����¼
	 * 
	 * @param eventinfo
	 */
	private static void processEventInfo(EventInfo eventinfo) {
		if (eventinfo == null)
			return;

		// ��ȡ�¼����Ӧ��ҵ����������.
//		Object[] obj = readData(eventinfo);
		//��Ӷ�ȼ�ͳ����Ĵ�������.
		if ("T_COS_BUNKER_STOCKIN".equals(eventinfo.getObjectname().toUpperCase())) {
			PurInWarehsBillService purInWarehsBillService = new PurInWarehsBillService();
			purInWarehsBillService.doProcess(eventinfo);
		} else if("T_COS_BUNKER_CONSUME".equals(eventinfo.getObjectname().toUpperCase())) {
			MaterialReqBillService materialReqBillService = new MaterialReqBillService();
			materialReqBillService.doProcess(eventinfo);
		} 
		
		/**
	    else if("T_COS_BUNKER_STOCKIN_DETAIL".equals(eventinfo.getObjectname().toUpperCase()) || "T_COS_BUNKER_CONSUME_DETAIL".equals(eventinfo.getObjectname().toUpperCase())){
			//�ӱ��ֱ�Ӳ���.�������ݴ���ʱ,���Զ������ӱ�����.
		} else if ("1".equals(eventinfo.getEventstatus())) {
			writeData(eventinfo, obj);
		} else if ("2".equals(eventinfo.getEventstatus())) {
			updateData(eventinfo, obj);
		} else if ("3".equals(eventinfo.getEventstatus())) {
			deleteData(eventinfo, obj);
		}
		 */
	}

	/**
	 * ��ȡԴ��ҵ������
	 * 
	 * @return
	 */
	private static Object[] readData(EventInfo eventinfo) {
		String table = eventinfo.getObjectname();
		String pkValue = eventinfo.getObjectkey();
		Object[] obj = null;
		String fpk = null;
		int fieldnumber = 0;
		String itemSql = null;
		try {
			PropertiesUtil pu = new PropertiesUtil("resource/" + table.toUpperCase()
					+ ".properties");
			fpk = pu.getValue(Constants.PK);
			fieldnumber = Integer.parseInt(pu.getValue(Constants.FIELD_NUMBER));
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < fieldnumber; i++) {
				sb
						.append(pu.getValue(Constants.COLUMN + i).split(",")[Constants.ZERO]);
				if (i != fieldnumber - 1)
					sb.append(",");
			}
			itemSql = sb.toString();
		} catch (EASException e1) {
			e1.printStackTrace();
			LoggerUtil.logger.error(e1.getMessage());
		}
		Connection readConn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			// ��������
			DBReadUtil dbread = DBReadUtil.getInstance();
			 readConn = dbread.getConnection();
			String readSql = "select " + itemSql + " from " + table.toUpperCase() + " where "
					+ fpk + " ='" + pkValue + "'";
			// System.out.println(readSql);
			stmt = readConn.createStatement();
			rs = stmt.executeQuery(readSql);
			obj = new Object[fieldnumber];
			
			while (rs.next()) {
				for (int i = 1; i <= fieldnumber; i++) {
					obj[i - 1] = rs.getObject(i);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			LoggerUtil.logger.error(e.getMessage());
		} finally {
			// �ر��α�
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					// SQL Exception
					LoggerUtil.logger.error(e.getMessage());
					// throw new EASException("ERR998", OUTExceptionInfo.ERR998
					// + e.getMessage());
				}
			}
			// �رս������
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// SQL Exception
					LoggerUtil.logger.error(e.getMessage());
					// throw new EASException("ERR998", OUTExceptionInfo.ERR998
					// + e.getMessage());
				}
			}
			if (readConn != null) {
				try {
					readConn.close();
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		}
		return obj;
	}

	/**
	 * ��Ŀ���д��ҵ������
	 * 
	 * @return
	 */
	private static void writeData(EventInfo eventinfo, Object[] value) {
		// System.out.println("writeData");
		
		// ��������
		DBReadUtil dbread = DBReadUtil.getInstance();
		Connection readConn = null;
		try {
			readConn = dbread.getConnection();
			readConn.getAutoCommit();
			readConn.setAutoCommit(false);
		} catch (EASException e) {
			e.printStackTrace();
			LoggerUtil.logger.error(e.getMessage());
		} catch (SQLException e) {
			e.printStackTrace();
			LoggerUtil.logger.error(e.getMessage());
		}

		// д������
		DBWriteUtil dbwrite = DBWriteUtil.getInstance();
		Connection writeConn = null;
		try {
			writeConn = dbwrite.getConnection();
			writeConn.getAutoCommit();
			writeConn.setAutoCommit(false);
			

			
		} catch (EASException e) {
			e.printStackTrace();
			LoggerUtil.logger.error(e.getMessage());
		} catch (SQLException e) {
			e.printStackTrace();
			LoggerUtil.logger.error(e.getMessage());
		}

		String table1 = eventinfo.getObjectname();
		String pkValue = eventinfo.getObjectkey();
		String tabletype =null;
		String tablenumber =null;
		String fpk = null;
		int fieldnumber = 0;
		String itemSql = null;
		PropertiesUtil pu = null;
		PreparedStatement writePs = null;
		PreparedStatement readPs = null;
		String table =null;
		String parenttable =null;
		String iobjectPK=null;
		try {
			pu = new PropertiesUtil("resource/" + table1.toUpperCase() + ".properties");
			fpk = pu.getValue(Constants.PK);
			table=pu.getValue(Constants.TRANSFTABLE);
			
			tabletype=pu.getValue(Constants.TABLETYPE);
			if(tabletype.equals("1")||tabletype.equals("2")){
				tablenumber=pu.getValue(Constants.TABLENUMBER);
				parenttable=pu.getValue(Constants.PARENTTABLE);
				// ��ȡEAS �û�ID
				CallableStatement cs;
				try {
					cs = writeConn
					.prepareCall("{ ?=call newbosid(?)}");


				cs.registerOutParameter(1, Types.VARCHAR);
				// Set the value for the IN parameter
				cs.setString(2, tablenumber);

				// Execute and retrieve the returned value
				cs.execute();
				iobjectPK = cs.getString(1);
				cs.close();
				
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			fieldnumber = Integer.parseInt(pu.getValue(Constants.FIELD_NUMBER));
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < fieldnumber; i++) {
				sb
						.append(pu.getValue(Constants.COLUMN + i).split(",")[Constants.ONE]);
				if (i != fieldnumber - 1)
					sb.append(",");
			}
			itemSql = sb.toString();
		} catch (EASException e1) {
			e1.printStackTrace();
			LoggerUtil.logger.error(e1.getMessage());
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < fieldnumber; i++) {
			sb.append("?");
			if (i != fieldnumber - 1)
				sb.append(",");
		}

		
		String insertSql = "insert into " + table
				+ "(" + itemSql + ") values(" + sb.toString() + ")";
		//-----------------------
		String inSqltrue = "select fid from " + table.toUpperCase() + " where " + fpk
				+ "='" + pkValue + "'";
		if(tabletype.equals("1")||tabletype.equals("2")){
			inSqltrue= "select fid from " + table.toUpperCase() + " where oldfid='" + pkValue + "'";
		}
		

		
		Statement stmt = null;
		ResultSet rs1 = null;
		String biaoji = "0";
		
		

		try {
			 stmt = writeConn.createStatement();
		     rs1 = stmt.executeQuery(inSqltrue);
		
		while (rs1.next()) {
			biaoji = "1";
		}
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
					LoggerUtil.logger.error(e.getMessage());
				}
			}
			if (rs1 != null) {
				try {
					rs1.close();
				} catch (SQLException e) {
					e.printStackTrace();
					LoggerUtil.logger.error(e.getMessage());
				}
			}
		}
		
		
//---------	�ӱ����⴦�� 	
		String inSqltrue2=null;
		Statement stmt2 = null;
		ResultSet rs2 = null;
		String biaoji2 = "0";
		String newfid=null;
		String pfid=null;
		if(tabletype.equals("2")){
			for (int i = 0; i < value.length; i++) {
				String[] column = pu.getValue(Constants.COLUMN + i).split(",");
		 if ("NSF".equals(column[Constants.TWO])) {
			          pfid=(String) value[i];
				}
				
				
			}
			
			inSqltrue2= "select fid from " + parenttable.toUpperCase() + " where oldfid='" + pfid + "'";
		

		try {
			 stmt2 = writeConn.createStatement();
		     rs2 = stmt2.executeQuery(inSqltrue2);

		while (rs2.next()) {
			biaoji2 = "1"; 
			newfid=(String)rs2.getString("FID");
		}

		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} finally {
			if (stmt2 != null) {
				try {
					stmt2.close();
				} catch (SQLException e) {
					e.printStackTrace();
					LoggerUtil.logger.error(e.getMessage());
				}
			}
			if (rs2 != null) {
				try {
					rs2.close();
				} catch (SQLException e) {
					e.printStackTrace();
					LoggerUtil.logger.error(e.getMessage());
				}
			}
		}
		
		}
		//---------			
		
		try {
		
if(tabletype.equals("2")){
//�ӱ��������	
	
if(biaoji2.equals("0")){
	//��������¼û�в��룬���Ȳ�����	
	
}else{
	//��������м�¼�������⴦�� 
	writePs = writeConn.prepareStatement(insertSql);
	for (int i = 0; i < value.length; i++) {
		String[] column = pu.getValue(Constants.COLUMN + i).split(",");
		if ("C".equals(column[Constants.TWO])) {
			if (value[i] == null) {
				writePs.setString(i + 1, null);
			} else {
				writePs.setString(i + 1, (String) value[i]);
			}
		} else if ("FVC".equals(column[Constants.TWO])) {
			writePs.setString(i + 1, column[Constants.THREE]);
		} else if ("N".equals(column[Constants.TWO])) {
			if (value[i] == null) {
				writePs.setBigDecimal(i + 1, null);
			} else {
				writePs.setBigDecimal(i + 1, (BigDecimal) value[i]);
			}
		} else if ("TS".equals(column[Constants.TWO])) {
			if (value[i] == null) {
				writePs.setTimestamp(i + 1, null);
			} else {
				writePs.setTimestamp(i + 1, ((TIMESTAMP) value[i])
						.timestampValue());
			}
		} else if ("D".equals(column[Constants.TWO])) {
			if (value[i] == null) {
				writePs.setDate(i + 1, null);
			} else {
				writePs.setDate(i + 1, (Date) value[i]);
			}
		} else if ("F71".equals(column[Constants.TWO])) {
			String str = getF71Value(readConn, column, value[i]);
			writePs.setString(i + 1, str);
		} else if ("F72".equals(column[Constants.TWO])) {
			String str = getF72Value(readConn, writeConn, column,
					value[i]);
			writePs.setString(i + 1, str);
		} else if ("CL".equals(column[Constants.TWO])) {
			if (value[i] == null) {
				writePs.setClob(i + 1, (Clob) null);

			} else {
				writePs.setClob(i + 1, (Clob) value[i]);
			}
		}else if ("TS1".equals(column[Constants.TWO])) {
			if (value[i] == null) {
				writePs.setTimestamp(i + 1, null);
			} else {
				//Date a= new Date(i);
				
				//writePs.setTimestamp(i + 1,new Timestamp(new Date().getTime()));

			}
		}else if ("NF".equals(column[Constants.TWO])) {
			if (value[i] == null) {
				writePs.setString(i + 1, (String)iobjectPK);
				//setTimestamp(i + 1, null);
			} else {
				writePs.setString(i + 1, (String)iobjectPK);

			}
		}
		else if ("NSF".equals(column[Constants.TWO])) {
			if (value[i] == null) {
				writePs.setString(i + 1, (String)newfid);
				//setTimestamp(i + 1, null);
			} else {
				writePs.setString(i + 1, (String)newfid);

			}
		}
		
		
	}
	
	
	writePs.execute();
	LoggerUtil.logger.info("writedata to " + eventinfo.getObjectname()
			+ ",pk=" + eventinfo.getObjectkey());
	readPs = readConn
			.prepareStatement("delete from OBJECT_NAME where event_id='"
					+ eventinfo.getEventid() + "'");
	readPs.execute();

	readConn.commit();
	writeConn.commit();
	
}	
	
	
	
	
	
	
}else{
			
			//--------------------------------�����ӱ���ĳ���
			
		if(biaoji.equals("1")){	
			readPs = readConn
					.prepareStatement("delete from OBJECT_NAME where event_id='"
							+ eventinfo.getEventid() + "'");
			readPs.execute();
			readConn.commit();
			LoggerUtil.logger.error("############---------insert  error repeat  in jingying tablename " + table1.toUpperCase()+"---in caiwu tablename"+table.toUpperCase()
					+ ",pk=" + eventinfo.getObjectkey());
			
		}else{
			
			writePs = writeConn.prepareStatement(insertSql);
			for (int i = 0; i < value.length; i++) {
				String[] column = pu.getValue(Constants.COLUMN + i).split(",");
				if ("C".equals(column[Constants.TWO])) {
					if (value[i] == null) {
						writePs.setString(i + 1, null);
					} else {
						writePs.setString(i + 1, (String) value[i]);
					}
				} else if ("FVC".equals(column[Constants.TWO])) {
					writePs.setString(i + 1, column[Constants.THREE]);
				} else if ("N".equals(column[Constants.TWO])) {
					if (value[i] == null) {
						writePs.setBigDecimal(i + 1, null);
					} else {
						writePs.setBigDecimal(i + 1, (BigDecimal) value[i]);
					}
				} else if ("TS".equals(column[Constants.TWO])) {
					if (value[i] == null) {
						writePs.setTimestamp(i + 1, null);
					} else {
						writePs.setTimestamp(i + 1, ((TIMESTAMP) value[i])
								.timestampValue());
					}
				} else if ("D".equals(column[Constants.TWO])) {
					if (value[i] == null) {
						writePs.setDate(i + 1, null);
					} else {
						writePs.setDate(i + 1, (Date) value[i]);
					}
				} else if ("F71".equals(column[Constants.TWO])) {
					String str = getF71Value(readConn, column, value[i]);
					writePs.setString(i + 1, str);
				} else if ("F72".equals(column[Constants.TWO])) {
					String str = getF72Value(readConn, writeConn, column,
							value[i]);
					writePs.setString(i + 1, str);
				} else if ("CL".equals(column[Constants.TWO])) {
					if (value[i] == null) {
						writePs.setClob(i + 1, (Clob) null);

					} else {
						writePs.setClob(i + 1, (Clob) value[i]);
					}
				}else if ("TS1".equals(column[Constants.TWO])) {
					if (value[i] == null) {
						writePs.setTimestamp(i + 1, null);
					} else {
						//Date a= new Date(i);
						
						//writePs.setTimestamp(i + 1,new Timestamp(new Date().getTime()));

					}
				}else if ("NF".equals(column[Constants.TWO])) {
					if (value[i] == null) {
						writePs.setString(i + 1, (String)iobjectPK);
						//setTimestamp(i + 1, null);
					} else {
						writePs.setString(i + 1, (String)iobjectPK);

					}
				}
				
				
			}
			
			
			writePs.execute();
			LoggerUtil.logger.info("writedata to " + eventinfo.getObjectname()
					+ ",pk=" + eventinfo.getObjectkey());
			readPs = readConn
					.prepareStatement("delete from OBJECT_NAME where event_id='"
							+ eventinfo.getEventid() + "'");
			readPs.execute();

			readConn.commit();
			writeConn.commit();

			
		}}
			
		} catch (SQLException e) {
			e.printStackTrace();
			LoggerUtil.logger.error(e.getMessage());
			
			if (readConn != null) {
				try {
					readConn.rollback();
				} catch (SQLException e1) {
					LoggerUtil.logger.error(OUTExceptionInfo.ERR002
							+ e1.getMessage());
				}
			}
			if (writeConn != null) {
				try {
					writeConn.rollback();
				} catch (SQLException e1) {
					LoggerUtil.logger.error(OUTExceptionInfo.ERR002
							+ e1.getMessage());
				}
			}
		} finally {
			if (writePs != null) {
				try {
					writePs.close();
				} catch (SQLException e) {
					e.printStackTrace();
					LoggerUtil.logger.error(e.getMessage());
				}
			}
			if (readPs != null) {
				try {
					readPs.close();
				} catch (SQLException e) {
					e.printStackTrace();
					LoggerUtil.logger.error(e.getMessage());
				}
			}
			if (readConn != null) {
				try {
					readConn.close();
				} catch (SQLException e) {
					LoggerUtil.logger.error(OUTExceptionInfo.ERR270 + " : "
							+ e.getMessage());
				}
			}
			if (writeConn != null) {
				try {
					writeConn.close();
				} catch (SQLException e) {
					LoggerUtil.logger.error(OUTExceptionInfo.ERR270 + " : "
							+ e.getMessage());
				}
			}
		}
	}

	/**
	 * ��ȡF7�����ֶ���Ŀ������Ӧ������ID
	 * 
	 * @param readConn
	 * @param writeConn
	 * @param column
	 * @param object
	 * @return
	 */
	private static String getF72Value(Connection readConn,
			Connection writeConn, String[] column, Object object) {
		if (readConn == null || writeConn == null || column == null
				|| object == null) {
			return null;
		}
		String readSql = "select " + column[Constants.THREE] + " from " + column[Constants.FOUR]
				+ " where " + column[Constants.FIVE] + "='" + object.toString() + "'";
		Statement stmt = null;
		ResultSet rs = null;
		Object obj = null;
		try {
			stmt = readConn.createStatement();
			rs = stmt.executeQuery(readSql);
			while (rs.next()) {
				obj = rs.getObject(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			LoggerUtil.logger.error(e.getMessage());
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
					LoggerUtil.logger.error(e.getMessage());
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
					LoggerUtil.logger.error(e.getMessage());
				}
			}
		}
		if (obj == null) {
			return null;
		}
		String read2Sql = "select " + column[Constants.SIX] + " from " + column[Constants.SEVEN]
				+ " where " + column[Constants.EIGHT] + "='" + obj.toString() + "'";
		try {
			stmt = writeConn.createStatement();
			rs = stmt.executeQuery(read2Sql);
			obj=null;
			while (rs.next()) {
				obj = rs.getObject(1);
			}
		} catch (SQLException e) {
			LoggerUtil.logger.error(e.getMessage());
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
					LoggerUtil.logger.error(e.getMessage());
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
					LoggerUtil.logger.error(e.getMessage());
				}
			}
			// if (readConn != null) {
			// try {
			// readConn.close();
			// } catch (SQLException e) {
			// LoggerUtil.logger.error(OUTExceptionInfo.ERR270 + " : "
			// + e.getMessage());
			// }
			// }
			// if (writeConn != null) {
			// try {
			// writeConn.close();
			// } catch (SQLException e) {
			// LoggerUtil.logger.error(OUTExceptionInfo.ERR270 + " : "
			// + e.getMessage());
			// }
			// }
		}
		if (obj == null) {
			return null;
		}

		return String.valueOf(obj);
	}

	/**
	 * ��ȡF7�����ֶ���Դ�����Ӧ��ֵ.
	 * 
	 * @param readConn
	 *            Դ���ݿ�����
	 * @param column
	 *            ��������Ϣ
	 * @param object
	 *            value
	 * @return
	 */
	private static String getF71Value(Connection readConn, String[] column,
			Object object) {
		if (readConn == null || column == null || object == null) {
			return null;
		}
		// String[] strs = column.split(",");
		String sql = "select " + column[Constants.THREE] + " from " + column[Constants.FOUR] + " where "
				+ column[Constants.FIVE] + "='" + object.toString() + "'";
		Statement stmt = null;
		ResultSet rs = null;
		Object obj = null;
		try {
			stmt = readConn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				obj = rs.getObject(1);
			}
		} catch (SQLException e) {
			LoggerUtil.logger.error(e.getMessage());
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					LoggerUtil.logger.error(e.getMessage());
					e.printStackTrace();
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					LoggerUtil.logger.error(e.getMessage());
					e.printStackTrace();
				}
			}
			// if (readConn != null) {
			// try {
			// readConn.close();
			// } catch (SQLException e) {
			// LoggerUtil.logger.error(OUTExceptionInfo.ERR270 + " : "
			// + e.getMessage());
			// }
			// }
		}
		return String.valueOf(obj);
	}

	/**
	 * ��Ŀ���д��ҵ������
	 * 
	 * @return
	 */
	private static void updateData(EventInfo eventinfo, Object[] value) {
		// System.out.println("updateData");
		LoggerUtil.logger.info("updateData to " + eventinfo.getObjectname()
				+ ",pk=" + eventinfo.getObjectkey());
		// ��������
		DBReadUtil dbread = DBReadUtil.getInstance();
		Connection readConn = null;
		PreparedStatement writePs = null;
		PreparedStatement readPs = null;
		try {
			readConn = dbread.getConnection();
			readConn.getAutoCommit();
			readConn.setAutoCommit(false);
		} catch (EASException e) {
			LoggerUtil.logger.error(e.getMessage());
			e.printStackTrace();
		} catch (SQLException e) {
			LoggerUtil.logger.error(e.getMessage());
			e.printStackTrace();
		}

		// д������
		DBWriteUtil dbwrite = DBWriteUtil.getInstance();
		Connection writeConn = null;
		try {
			writeConn = dbwrite.getConnection();
			writeConn.getAutoCommit();
			writeConn.setAutoCommit(false);

			String table1 = eventinfo.getObjectname();
			String pkValue = eventinfo.getObjectkey();
			String fpk = null;
			int fieldnumber = 0;
			String itemSql = null;
			PropertiesUtil pu = null;
			pu = new PropertiesUtil("resource/" + table1.toUpperCase() + ".properties");
			String table =pu.getValue(Constants.TRANSFTABLE);
			String tabletype=pu.getValue(Constants.TABLETYPE);
			
			fpk = pu.getValue(Constants.PK);
			fieldnumber = Integer.parseInt(pu.getValue(Constants.FIELD_NUMBER));
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < fieldnumber; i++) {
				//
				if(tabletype.equals("1")||tabletype.equals("2")){
					if(!pu.getValue(Constants.COLUMN + i).split(",")[Constants.ONE].equals("FID")){
						if(!pu.getValue(Constants.COLUMN + i).split(",")[Constants.ONE].equals("FParentID")){
							
							
							sb.append(pu.getValue(Constants.COLUMN + i).split(",")[Constants.ONE]).append("=?");	
						}	
					}
						
				}else{
				sb.append(pu.getValue(Constants.COLUMN + i).split(",")[Constants.ONE]).append("=?");
				}
				if (i != fieldnumber - 1)
					sb.append(",");
			}
			itemSql = sb.toString();

			String deleteSqltrue = "select fid from " + table.toUpperCase() + " where " + fpk
					+ "='" + pkValue + "'";
			
			 if(tabletype.equals("1")||tabletype.equals("2")){
				 deleteSqltrue = "select fid from " + table.toUpperCase() + " where oldfid='" + pkValue + "'";
			 }
			Statement stmt = null;
			ResultSet rs1 = null;
			String biaoji = "0";

			stmt = writeConn.createStatement();
			rs1 = stmt.executeQuery(deleteSqltrue);
			while (rs1.next()) {
				biaoji = "1";
			}
			stmt.close();
			rs1.close();
if (biaoji.equals("1")){
			String updateSql = "update " + table.toUpperCase() + " set " + itemSql+ " where " + fpk + "='" + pkValue + "'";
			 if(tabletype.equals("1")||tabletype.equals("2")){
				 updateSql = "update " + table.toUpperCase() + " set " + itemSql+ " where oldfid ='" + pkValue + "'";
			 }

			writePs = writeConn.prepareStatement(updateSql);
			for (int i = 0; i < value.length; i++) {
				String[] column = pu.getValue(Constants.COLUMN + i).split(",");
				if ("C".equals(column[Constants.TWO])) {
					if (value[i] == null) {
						writePs.setString(i + 1, null);
					} else {
						writePs.setString(i + 1, (String) value[i]);
					}
				} else if ("FVC".equals(column[Constants.TWO])) {
					writePs.setString(i + 1, column[Constants.THREE]);
				} else if ("N".equals(column[Constants.TWO])) {
					if (value[i] == null) {
						writePs.setBigDecimal(i + 1, null);
					} else {
						writePs.setBigDecimal(i + 1, (BigDecimal) value[i]);
					}
				} else if ("TS".equals(column[Constants.TWO])) {
					if (value[i] == null) {
						writePs.setTimestamp(i + 1, null);
					} else {
						writePs.setTimestamp(i + 1, ((TIMESTAMP) value[i])
								.timestampValue());
					}
				} else if ("D".equals(column[Constants.TWO])) {
					if (value[i] == null) {
						writePs.setDate(i + 1, null);
					} else {
						writePs.setDate(i + 1, (Date) value[i]);
					}
				} else if ("F71".equals(column[Constants.TWO])) {
					String str = getF71Value(readConn, column, value[i]);
					writePs.setString(i + 1, str);
				} else if ("F72".equals(column[Constants.TWO])) {
					String str = getF72Value(readConn, writeConn, column,
							value[i]);
					writePs.setString(i + 1, str);
				} else if ("CL".equals(column[Constants.TWO])) {
					if (value[i] == null) {
						writePs.setClob(i + 1, (Clob) null);

					} else {
						writePs.setClob(i + 1, (Clob) value[i]);
					}
				}
				else if ("NF".equals(column[Constants.TWO])) {
					if (value[i] == null) {
						

					} else {
						
					}
				}else if ("NSF".equals(column[Constants.TWO])) {
					if (value[i] == null) {
						

					} else {
						
					}
				}

			}
			readPs = readConn
					.prepareStatement("delete from OBJECT_NAME where event_id='"
							+ eventinfo.getEventid() + "'");
			
			writePs.execute();
			readPs.execute();

			writeConn.commit();
			readConn.commit();
         }else{
        	 readPs = readConn
 					.prepareStatement("delete from OBJECT_NAME where event_id='"
 							+ eventinfo.getEventid() + "'"); 
        		readPs.execute();
        		readConn.commit();
        		LoggerUtil.logger.error("############---------UpdateData not find in jingying tablename " + table1.toUpperCase()+"---in caiwu tablename"+table.toUpperCase()
						+ ",pk=" + eventinfo.getObjectkey());
         }
		} catch (Exception e) {
			LoggerUtil.logger.error(e.getMessage());
			e.printStackTrace();
			if (readConn != null) {
				try {
					readConn.rollback();
				} catch (SQLException e1) {
					LoggerUtil.logger.error(OUTExceptionInfo.ERR002
							+ e1.getMessage());
				}
			}
			if (writeConn != null) {
				try {
					writeConn.rollback();
				} catch (SQLException e1) {
					LoggerUtil.logger.error(OUTExceptionInfo.ERR002
							+ e1.getMessage());
				}
			}
		} finally {
			if (writePs != null) {
				try {
					writePs.close();
				} catch (SQLException e) {
					LoggerUtil.logger.error(e.getMessage());
					e.printStackTrace();
				}
			}
			if (readPs != null) {
				try {
					readPs.close();
				} catch (SQLException e) {
					LoggerUtil.logger.error(e.getMessage());
					e.printStackTrace();
				}
			}
			if (readConn != null) {
				try {
					readConn.close();
				} catch (SQLException e) {
					LoggerUtil.logger.error(OUTExceptionInfo.ERR270 + " : "
							+ e.getMessage());
				}
			}
			if (writeConn != null) {
				try {
					writeConn.close();
				} catch (SQLException e) {
					LoggerUtil.logger.error(OUTExceptionInfo.ERR270 + " : "
							+ e.getMessage());
				}
			}
		}
	}

	/**
	 * ��Ŀ���д��ҵ������
	 * 
	 * @return
	 */
	private static void deleteData(EventInfo eventinfo, Object[] value) {
		// System.out.println("deleteData");
		LoggerUtil.logger.info("deleteData to " + eventinfo.getObjectname()
				+ ",pk=" + eventinfo.getObjectkey());
		// ��������
		DBReadUtil dbread = DBReadUtil.getInstance();
		Connection readConn = null;
		try {
			readConn = dbread.getConnection();
			readConn.getAutoCommit();
			readConn.setAutoCommit(false);
		} catch (EASException e) {
			LoggerUtil.logger.error(e.getMessage());
			e.printStackTrace();
		} catch (SQLException e) {
			LoggerUtil.logger.error(e.getMessage());
			e.printStackTrace();
		}

		// д������
		DBWriteUtil dbwrite = DBWriteUtil.getInstance();
		Connection writeConn = null;
		try {
			writeConn = dbwrite.getConnection();
			writeConn.getAutoCommit();
			writeConn.setAutoCommit(false);
		} catch (EASException e) {
			LoggerUtil.logger.error(e.getMessage());
			e.printStackTrace();
		} catch (SQLException e) {
			LoggerUtil.logger.error(e.getMessage());
			e.printStackTrace();
		}

		String table1 = eventinfo.getObjectname();
		String pkValue = eventinfo.getObjectkey();
		String table=null;
		String tabletype =null;
		String fpk = null;
		PropertiesUtil pu = null;
		try {
			pu = new PropertiesUtil("resource/" + table1.toUpperCase() + ".properties");
			fpk = pu.getValue(Constants.PK);
			table =pu.getValue(Constants.TRANSFTABLE);
			tabletype=pu.getValue(Constants.TABLETYPE);
		} catch (EASException e1) {
			LoggerUtil.logger.error(e1.getMessage());
			e1.printStackTrace();
		}
		String deleteSqltrue = "select fid from " + table.toUpperCase() + " where " + fpk
				+ "='" + pkValue + "'";
		if(tabletype.equals("1")||tabletype.equals("2")){
			deleteSqltrue = "select fid from " + table.toUpperCase() + " where oldfid='" + pkValue + "'";
		}
		
		Statement stmt = null;
		ResultSet rs1 = null;
		String biaoji = "0";

		try {
			stmt = writeConn.createStatement();
			rs1 = stmt.executeQuery(deleteSqltrue);
			while (rs1.next()) {
				biaoji = "1";
			}
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					LoggerUtil.logger.error(e.getMessage());
					e.printStackTrace();
				}
			}
			if (rs1 != null) {
				try {
					rs1.close();
				} catch (SQLException e) {
					LoggerUtil.logger.error(e.getMessage());
					e.printStackTrace();
				}
			}
		
		}

		if (biaoji.equals("1")) {

			String deleteSql = "delete from " + table.toUpperCase() + " where " + fpk + "='"
					+ pkValue + "'";
			if(tabletype.equals("1")||tabletype.equals("2")){
				deleteSql = "delete from " + table.toUpperCase() + " where oldfid='"
						+ pkValue + "'";
			}
			PreparedStatement writePs = null;
			PreparedStatement readPs = null;
			try {
				writePs = writeConn.prepareStatement(deleteSql);
				writePs.execute();

				readPs = readConn
						.prepareStatement("delete from OBJECT_NAME where event_id='"
								+ eventinfo.getEventid() + "'");
				readPs.execute();

				readConn.commit();
				writeConn.commit();
			} catch (SQLException e) {
				LoggerUtil.logger.error(e.getMessage());
				e.printStackTrace();
				if (readConn != null) {
					try {
						readConn.rollback();
					} catch (SQLException e1) {
						LoggerUtil.logger.error(OUTExceptionInfo.ERR002
								+ e1.getMessage());
					}
				}
				if (writeConn != null) {
					try {
						writeConn.rollback();
					} catch (SQLException e1) {
						LoggerUtil.logger.error(OUTExceptionInfo.ERR002
								+ e1.getMessage());
					}
				}
			} finally {
				if (writePs != null) {
					try {
						writePs.close();
					} catch (SQLException e) {
						LoggerUtil.logger.error(e.getMessage());
						e.printStackTrace();
					}
				}
				if (readPs != null) {
					try {
						readPs.close();
					} catch (SQLException e) {
						LoggerUtil.logger.error(e.getMessage());
						e.printStackTrace();
					}
				}
				if (readConn != null) {
					try {
						readConn.close();
					} catch (SQLException e) {
						LoggerUtil.logger.error(OUTExceptionInfo.ERR270 + " : "
								+ e.getMessage());
					}
				}
				if (writeConn != null) {
					try {
						writeConn.close();
					} catch (SQLException e) {
						LoggerUtil.logger.error(OUTExceptionInfo.ERR270 + " : "
								+ e.getMessage());
					}
				}
			}
		}else{


			PreparedStatement readPs = null;
			try {
				
				readPs = readConn
						.prepareStatement("delete from OBJECT_NAME where event_id='"
								+ eventinfo.getEventid() + "'");
				readPs.execute();

				readConn.commit();
				LoggerUtil.logger.error("############--------- deleteData not find in jingying tablename " + table1.toUpperCase()+"---in caiwu tablename"+table.toUpperCase()
						+ ",pk=" + eventinfo.getObjectkey());
				
			} catch (SQLException e) {
				LoggerUtil.logger.error(e.getMessage());
				e.printStackTrace();
				if (readConn != null) {
					try {
						readConn.rollback();
					} catch (SQLException e1) {
						LoggerUtil.logger.error(OUTExceptionInfo.ERR002
								+ e1.getMessage());
					}
				}
				if (writeConn != null) {
					try {
						writeConn.rollback();
					} catch (SQLException e1) {
						LoggerUtil.logger.error(OUTExceptionInfo.ERR002
								+ e1.getMessage());
					}
				}
			} finally {
				
				if (readPs != null) {
					try {
						readPs.close();
					} catch (SQLException e) {
						LoggerUtil.logger.error(e.getMessage());
						e.printStackTrace();
					}
				}
				if (readConn != null) {
					try {
						readConn.close();
					} catch (SQLException e) {
						LoggerUtil.logger.error(OUTExceptionInfo.ERR270 + " : "
								+ e.getMessage());
					}
				}
				if (writeConn != null) {
					try {
						writeConn.close();
					} catch (SQLException e) {
						LoggerUtil.logger.error(OUTExceptionInfo.ERR270 + " : "
								+ e.getMessage());
					}
				}
			}
		
			
		}
	}
}
