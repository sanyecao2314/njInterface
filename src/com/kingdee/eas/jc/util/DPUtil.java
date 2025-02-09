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
	public static String getSupplierID(Connection readConn ,String fnumber, Connection writeConn){
//		String fnumberQuery = "select fnumber from T_BD_Supplier where fnumber = ?";
		String fidQuery = "select fid from T_BD_Supplier where fnumber= ?";
		return getFidByFnumber(fnumber, writeConn, fidQuery);
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
		String fidQuery = "select fid from T_BD_Currency where fnumber= ?";
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
	 * 获得财务系统的仓库Fid.
	 * @param readConn
	 * @param fid 经营系统船舶主键.
	 * @param writeConn
	 * @return
	 */
	public static String getStorageOrgUnitID(Connection readConn ,String fid, Connection writeConn){
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
//		String fnumberQuery = "select fnumber from T_ORG_CostCenter where fid = ?";
		String fidQuery = "select fid from T_ORG_CostCenter where fnumber= ?";
		return getFid(readConn, fid, writeConn, fnumberQuery, fidQuery); 
	}
	
	/**
	 * 获得计量单位主键
	 * @param fid	经营系统 计量单位主键
	 * @param readConn
	 * @param writeConn
	 * @return
	 */
	public static String getFUnitID(String fid, Connection readConn, Connection writeConn) {
		String fnumberQuery = "select fnumber from t_bd_MeasureUnit where fid = ?";
		String fidQuery = "select fid from t_bd_MeasureUnit where fnumber= ?";
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
		String fid = getFidByFnumber(fnumber, writeConn, fidQuery);
		if (StringUtil.stringIsEmpty(fid)) {
			LoggerUtil.logger.error("material code is null .add test code " );
			fid = "14JKoW+4S8moPw0iY178n0QJ5/A=";
		}
		
		return fid;
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
	
	/**
	 * 根据船舶编码获取仓库Fid
	 * @param readConn
	 * @param fnumber 船舶编码 
	 * @param writeConn
	 * @return
	 */
	public static String getStorageOrgUnitIDByfnumber(Connection readConn, String fnumber, Connection writeConn) {
		String fidQuery = "select fid from t_org_storage where fnumber= ?";
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
		if(StringUtil.stringIsEmpty(res)){
			LoggerUtil.logger.error("data error.fnumber=" + fnumber + "|query=" + fidQuery);
		}
		return res;
	}
	
	/**
	 * 删除事件表里的信息.
	 * @param readConn
	 * @param tableName
	 * @param fid
	 */
	public static void updateMiddleTable(Connection readConn, String tableName, String fid) {
		String deleteSql = "delete from  " + tableName + "  where object_key = ?";
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

	/**
	 * 获取物料的基本计量单位.
	 * @param fMaterialID
	 * @return
	 */
	public static String getMaterialBaseUtil(String fMaterialID, Connection conn) {
		String res = "";
		PreparedStatement readPs = null;
		ResultSet readRs = null;
		try {
			readPs = conn.prepareStatement("select FBaseUnit from t_bd_material where fid=? ");
			readPs.setString(1, fMaterialID);
			readRs = readPs.executeQuery();
			if (readRs.next()) {
				res = readRs.getString("FBaseUnit");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			close(readPs, null, readRs, null);
		}
		return res;
	}

	/**
	 * 获得部门
	 * @param shipNumber
	 * @param writeConn
	 * @return
	 */
	public static String getAdminOrgUnitId(String shipNumber, Connection writeConn) {
		String fidQuery = "select  fid from t_org_admin where fnumber=?";
		return getFidByFnumber(shipNumber, writeConn, fidQuery);
	}

	/**
	 * 获取汇率
	 * @param fid
	 * @param writeConn
	 * @return
	 */
	public static double getExchange(String fid, Connection writeConn){
		String querySql = "select a.fconvertRate from T_BD_ExchangeRate a "
				+ "inner join T_BD_ExchangeAux b on a. fexchangeauxid = b.fid "
				+ "where b.fsourcecurrencyid=? and b.ftargetcurrencyid='dfd38d11-00fd-1000-e000-1ebdc0a8100dDEB58FDC' and a.fisusedbg = 0 "
				+ "order by a.favailtime desc ";
		double res = 1d;
		PreparedStatement writePs = null;
		ResultSet writeRS = null;
		try {
				writePs = writeConn.prepareStatement(querySql);
				writePs.setString(1, fid);
				writeRS = writePs.executeQuery();
				if(writeRS.next()){
					res = writeRS.getDouble("fconvertRate");
				}
		} catch (SQLException e) {
			LoggerUtil.logger.error("getFidByFnumber error.", e);
		}finally{
			close(null, writePs, null, writeRS);
		}
		return res;
	}


}
