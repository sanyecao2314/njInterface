package com.kingdee.eas.jc.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DPUtil {

	/**
	 * ��ò���ϵͳ��Ӧ��ID
	 * @param readConn
	 * @param fid ��Ӫϵͳ��Ӧ��ID
	 * @param writeConn
	 * @return
	 */
	public static String getSupplierID(Connection readConn ,String fid, Connection writeConn){
		String fnumberQuery = "select fnumber from T_BD_Supplier where fid = ?";
		String fidQuery = "select fid from T_BD_Supplier where fnumber= ?";
		return getFid(readConn, fid, writeConn, fnumberQuery, fidQuery);
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
	public static String getWarehouseFid(Connection readConn ,String fid, Connection writeConn){
		String fnumberQuery = "select fnumber from T_COS_Vessel where fid = ?";
		String fidQuery = "select fid from T_BD_Currency where fnumber= ?";
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
		String res = "";
		String fidQuery = "select fid from t_bd_material where fnumber = ?";
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
			e.printStackTrace();
		}finally{
			close(null, writePs, null, writeRS);
		}
		return res;
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
