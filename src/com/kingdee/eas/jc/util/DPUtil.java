package com.kingdee.eas.jc.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mchange.v2.log.LogUtils;


/**
 * @author fans.fan
 *
 */
public class DPUtil {

	/**
	 * 获得财务系统供应商ID
	 * @param readConn
	 * @param fid 经营系统供应商ID
	 * @param writeConn
	 * @return
	 */
	public static String getSupplierID(Connection readConn ,String fid, Connection writeConn){
		String fnumberQuery = "select fnumber from T_BD_Supplier where fid = ?";
		String fidQuery = "select fid from T_BD_Supplier where fnumber= ?";
		return getFid(readConn, fid, writeConn, fnumberQuery, fidQuery);
	}
	
	/**
	 * 获得财务系统币种ID
	 * @param readConn
	 * @param fid 经营系统供应商ID
	 * @param writeConn
	 * @return
	 */
	public static String getCurrencyFid(Connection readConn ,String fid, Connection writeConn){
		String fnumberQuery = "select fnumber from T_BD_Currency where fid = ?";
		String fidQuery = "select fid from T_DB_WAREHOUSE where fnumber= ?";
		return getFid(readConn, fid, writeConn, fnumberQuery, fidQuery);
	}
	
	/**
	 * 获得财务系统的仓库Fid.
	 * @param readConn
	 * @param fid 经营系统船舶主键.
	 * @param writeConn
	 * @return
	 */
	public static String getWarehouseFid(Connection readConn ,String fid, Connection writeConn){
		String fnumberQuery = "select fnumber from T_COS_Vessel where fid = ?";
		String fidQuery = "select fid from T_DB_WAREHOUSE where fnumber= ?";
		return getFid(readConn, fid, writeConn, fnumberQuery, fidQuery); 
	}

	/**
	 * 获得成本中心主键Fid
	 * @param readConn
	 * @param fid 经营系统船舶Fid.
	 * @param writeConn
	 * @return
	 */
	public static String getCostCenterFid(Connection readConn, String fid, Connection writeConn){
		String fnumberQuery = "select fnumber from T_COS_Vessel where fid = ?";
		String fidQuery = "select fid from T_ORG_CostCenter where fnumber= ?";
		return getFid(readConn, fid, writeConn, fnumberQuery, fidQuery); 
	}
	
	/**
	 * 获得财务系统相关基础资料ID
	 * @param readConn
	 * @param fid 原始fid.
	 * @param writeConn
	 * @param fnumberQuery 查询对应的基础资料编码的sql语句.
	 * @param fidQuery  查询最终需要的fid的sql语句.
	 * @return
	 */
	public static String getFid(Connection readConn ,String fid, Connection writeConn, String fnumberQuery, String fidQuery){
		String res = "";
		PreparedStatement readPs = null;
		PreparedStatement writePs = null;
		ResultSet readRs = null;
		ResultSet writeRS = null;
		try {
			readPs = readConn.prepareStatement(fnumberQuery);
			readPs.setString(1, fid);
			readRs = readPs.executeQuery();
			if (readRs.next()) {
				writePs = writeConn.prepareStatement(fidQuery);
				writePs.setString(1, readRs.getString("fnumber"));
				writeRS = writePs.executeQuery();
				if(writeRS.next()){
					res = writeRS.getString("fid");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			close(readPs, writePs, readRs, writeRS);
		}
		return res;
	}
	
	/**
	 * 根据物料编码,获得物料的fid
	 * @param readConn
	 * @param fnumber 物料编码
	 * @param writeConn
	 * @return
	 */
	public static String getMaterialFid(String fnumber, Connection writeConn){
		String fidQuery = "select fid from t_bd_material where fnumber = ?";
		return getFidByFnumber(fnumber, writeConn, fidQuery);
	}
	
	/**
	 * 根据船舶编码获取仓库Fid
	 * @param readConn
	 * @param fnumber 船舶编码 
	 * @param writeConn
	 * @return
	 */
	public static String getWarehouseFidByfnumber(Connection readConn, String fnumber, Connection writeConn) {
		String fidQuery = "select fid from T_DB_WAREHOUSE where fnumber= ?";
		return getFidByFnumber(fnumber, writeConn, fidQuery);
	}
	
	private static String getFidByFnumber(String fnumber, Connection writeConn, String fidQuery){
		String res = "";
		PreparedStatement writePs = null;
		ResultSet writeRS = null;
		try {
				writePs = writeConn.prepareStatement(fidQuery);
				writePs.setString(1, fnumber);
				writeRS = writePs.executeQuery();
				if(writeRS.next()){
					res = writeRS.getString("fid");
				}
		} catch (SQLException e) {
			LoggerUtil.logger.error("getFidByFnumber error.", e);
		}finally{
			close(null, writePs, null, writeRS);
		}
		return res;
	}
	
	public static void updateMiddleTable(Connection readConn, String tableName, String fid) {
		String deleteSql = "delete from  " + tableName + "  where fid = ?";
		PreparedStatement ps = null;
		try {
			ps = readConn.prepareStatement(deleteSql);
			ps.setString(1, fid);
			ps.execute();
		} catch (SQLException e) {
			LoggerUtil.logger.error("update middleTable error.", e);
		}finally{
			close(ps, null, null, null);
		}
	}

	private static void close(PreparedStatement readPs, PreparedStatement writePs, ResultSet readRs, ResultSet writeRS) {

		if (readPs != null) {
			try {
				readPs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (writePs != null) {
			try {
				writePs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (readRs != null) {
			try {
				readRs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (writeRS != null) {
			try {
				writeRS.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
