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
	 * ��ò���ϵͳ��Ӧ��ID
	 * @param readConn
	 * @param fid ��Ӫϵͳ��Ӧ��ID
	 * @param writeConn
	 * @return
	 */
	public static String getSupplierID(Connection readConn ,String fnumber, Connection writeConn){
//		String fnumberQuery = "select fnumber from T_BD_Supplier where fnumber = ?";
		String fidQuery = "select fid from T_BD_Supplier where fnumber= ?";
		return getFidByFnumber(fnumber, writeConn, fidQuery);
	}
	
	/**
	 * ��ò���ϵͳ����ID
	 * @param readConn
	 * @param fid ��Ӫϵͳ��Ӧ��ID
	 * @param writeConn
	 * @return
	 */
	public static String getCurrencyFid(Connection readConn ,String fid, Connection writeConn){
		String fnumberQuery = "select fnumber from T_BD_Currency where fid = ?";
		String fidQuery = "select fid from T_BD_Currency where fnumber= ?";
		return getFid(readConn, fid, writeConn, fnumberQuery, fidQuery);
	}
	
	/**
	 * ��ò���ϵͳ�Ĳֿ�Fid.
	 * @param readConn
	 * @param fid ��Ӫϵͳ��������.
	 * @param writeConn
	 * @return
	 */
	public static String getWarehouseFid(Connection readConn ,String fid, Connection writeConn){
		String fnumberQuery = "select fnumber from T_COS_Vessel where fid = ?";
		String fidQuery = "select fid from T_DB_WAREHOUSE where fnumber= ?";
		return getFid(readConn, fid, writeConn, fnumberQuery, fidQuery); 
	}
	
	/**
	 * ��ò���ϵͳ�Ĳֿ�Fid.
	 * @param readConn
	 * @param fid ��Ӫϵͳ��������.
	 * @param writeConn
	 * @return
	 */
	public static String getStorageOrgUnitID(Connection readConn ,String fid, Connection writeConn){
		String fnumberQuery = "select fnumber from T_COS_Vessel where fid = ?";
		String fidQuery = "select fid from T_DB_WAREHOUSE where fnumber= ?";
		return getFid(readConn, fid, writeConn, fnumberQuery, fidQuery); 
	}

	
	
	/**
	 * ��óɱ���������Fid
	 * @param readConn
	 * @param fid ��Ӫϵͳ����Fid.
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
	 * ��ò���ϵͳ��ػ�������ID
	 * @param readConn
	 * @param fid ԭʼfid.
	 * @param writeConn
	 * @param fnumberQuery ��ѯ��Ӧ�Ļ������ϱ����sql���.
	 * @param fidQuery  ��ѯ������Ҫ��fid��sql���.
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
	 * �������ϱ���,������ϵ�fid
	 * @param readConn
	 * @param fnumber ���ϱ���
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
	 * ���ݴ��������ȡ�ֿ�Fid
	 * @param readConn
	 * @param fnumber �������� 
	 * @param writeConn
	 * @return
	 */
	public static String getWarehouseFidByfnumber(Connection readConn, String fnumber, Connection writeConn) {
		String fidQuery = "select fid from T_DB_WAREHOUSE where fnumber= ?";
		return getFidByFnumber(fnumber, writeConn, fidQuery);
	}
	
	/**
	 * ���ݴ��������ȡ�ֿ�Fid
	 * @param readConn
	 * @param fnumber �������� 
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
	 * ɾ���¼��������Ϣ.
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
	 * ��ȡ���ϵĻ���������λ.
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

}
