package com.kingdee.eas.jc.process;

import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

import oracle.sql.TIMESTAMP;

import com.kingdee.eas.jc.bean.EventInfo;
import com.kingdee.eas.jc.exception.EASException;
import com.kingdee.eas.jc.exception.OUTExceptionInfo;
import com.kingdee.eas.jc.util.Constants;
import com.kingdee.eas.jc.util.DBReadUtil;
import com.kingdee.eas.jc.util.DBWriteUtil;
import com.kingdee.eas.jc.util.LoggerUtil;
import com.kingdee.eas.jc.util.PropertiesUtil;

public class DataProcess {

	/**
	 * 处理事件表中读出的数据集.
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
	 * 处理事件表中的每个记录
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
	 * 处理事件表中的每个记录
	 * 
	 * @param eventinfo
	 */
	private static void processEventInfo(EventInfo eventinfo) {
		if (eventinfo == null)
			return;

		// 读取事件表对应的业务表里的数据.
		Object[] obj = readData(eventinfo);

		if ("1".equals(eventinfo.getEventstatus())) {
			writeData(eventinfo, obj);
		} else if ("2".equals(eventinfo.getEventstatus())) {
			updateData(eventinfo, obj);
		} else if ("3".equals(eventinfo.getEventstatus())) {
			deleteData(eventinfo, obj);
		}
	}

	/**
	 * 读取源库业务数据
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
			PropertiesUtil pu = new PropertiesUtil("resource/" + table
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

		DBReadUtil dbread = DBReadUtil.getInstance();
		Connection readConn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			// 读数连接
			readConn = dbread.getConnection();
			String readSql = "select " + itemSql + " from " + table + " where "
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
			// 关闭游标
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
			// 关闭结果集合
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
	 * 向目标库写入业务数据
	 * 
	 * @return
	 */
	private static void writeData(EventInfo eventinfo, Object[] value) {
		// System.out.println("writeData");
		LoggerUtil.logger.info("writedata to " + eventinfo.getObjectname()
				+ ",pk=" + eventinfo.getObjectkey());
		// 读数连接
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

		// 写数连接
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

		String table = eventinfo.getObjectname();
		String pkValue = eventinfo.getObjectkey();
		String fpk = null;
		int fieldnumber = 0;
		String itemSql = null;
		PropertiesUtil pu = null;
		PreparedStatement writePs = null;
		PreparedStatement readPs = null;
		try {
			pu = new PropertiesUtil("resource/" + table + ".properties");
			fpk = pu.getValue(Constants.PK);
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
		String insertSql = "insert into " + pu.getValue(Constants.TRANSFTABLE)
				+ "(" + itemSql + ") values(" + sb.toString() + ")";
		try {
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
				}
				
				
			}
			writePs.execute();

			readPs = readConn
					.prepareStatement("delete from OBJECT_NAME where event_id='"
							+ eventinfo.getEventid() + "'");
			readPs.execute();

			readConn.commit();
			writeConn.commit();
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
	 * 获取F7类型字段在目标库里对应的数据ID
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
	 * 获取F7类型字段在源库里对应的值.
	 * 
	 * @param readConn
	 *            源数据库连接
	 * @param column
	 *            列配置信息
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
	 * 向目标库写入业务数据
	 * 
	 * @return
	 */
	private static void updateData(EventInfo eventinfo, Object[] value) {
		// System.out.println("updateData");
		LoggerUtil.logger.info("updateData to " + eventinfo.getObjectname()
				+ ",pk=" + eventinfo.getObjectkey());
		// 读数连接
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

		// 写数连接
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
			pu = new PropertiesUtil("resource/" + table1 + ".properties");
			String table =pu.getValue(Constants.TRANSFTABLE);
			
			fpk = pu.getValue(Constants.PK);
			fieldnumber = Integer.parseInt(pu.getValue(Constants.FIELD_NUMBER));
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < fieldnumber; i++) {
				sb
						.append(
								pu.getValue(Constants.COLUMN + i).split(",")[Constants.ONE])
						.append("=?");
				if (i != fieldnumber - 1)
					sb.append(",");
			}
			itemSql = sb.toString();

			String deleteSqltrue = "select fid from " + table + " where " + fpk
					+ "='" + pkValue + "'";
			Statement stmt = null;
			ResultSet rs1 = null;
			String biaoji = "0";

			stmt = writeConn.createStatement();
			rs1 = stmt.executeQuery(deleteSqltrue);
			while (rs1.next()) {
				biaoji = "1";
			}

			String updateSql = "update " + table + " set " + itemSql
					+ " where " + fpk + "='" + pkValue + "'";

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

			}
			readPs = readConn
					.prepareStatement("delete from OBJECT_NAME where event_id='"
							+ eventinfo.getEventid() + "'");
			
			writePs.execute();
			readPs.execute();

			writeConn.commit();
			readConn.commit();
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
	 * 向目标库写入业务数据
	 * 
	 * @return
	 */
	private static void deleteData(EventInfo eventinfo, Object[] value) {
		// System.out.println("deleteData");
		LoggerUtil.logger.info("deleteData to " + eventinfo.getObjectname()
				+ ",pk=" + eventinfo.getObjectkey());
		// 读数连接
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

		// 写数连接
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

		String fpk = null;
		PropertiesUtil pu = null;
		try {
			pu = new PropertiesUtil("resource/" + table1 + ".properties");
			fpk = pu.getValue(Constants.PK);
			table =pu.getValue(Constants.TRANSFTABLE);
		} catch (EASException e1) {
			LoggerUtil.logger.error(e1.getMessage());
			e1.printStackTrace();
		}
		String deleteSqltrue = "select fid from " + table + " where " + fpk
				+ "='" + pkValue + "'";
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
		}

		if (biaoji.equals("1")) {

			String deleteSql = "delete from " + table + " where " + fpk + "='"
					+ pkValue + "'";
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
		}
	}
}
