package com.kingdee.eas.jc.services;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import com.kingdee.eas.jc.bean.MaterialReqBillEntryInfo;
import com.kingdee.eas.jc.bean.MaterialReqBillInfo;
import com.kingdee.eas.jc.exception.EASException;
import com.kingdee.eas.jc.util.DBWriteUtil;
import com.kingdee.eas.jc.util.LoggerUtil;

public class MaterialReqBillService {

	public void doProcess(MaterialReqBillInfo materialReqBillInfo,
			List<MaterialReqBillEntryInfo> lsmaterBillEntryInfos) {
		Connection writeConn = null;
		try {
			writeConn = getWriteConn();
			insertMaterialReqBillInfo(writeConn, materialReqBillInfo);
			insertLsMaterialReqBillEntryInfo(writeConn, lsmaterBillEntryInfos);
			writeConn.commit();
		} catch (Exception e) {
			try {
				writeConn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	private void insertMaterialReqBillInfo(Connection writeConn,
			MaterialReqBillInfo materialReqBillInfo) throws SQLException {
		String insertSql = "";
		PreparedStatement pst = writeConn.prepareStatement(insertSql);
		pst.setString(1,"");
		pst.setString(2,"");
		pst.setString(3,"");
		pst.execute();
	}

	private void insertLsMaterialReqBillEntryInfo(Connection writeConn,
			List<MaterialReqBillEntryInfo> lsmaterBillEntryInfos) throws Exception {
		String insertSql = "";
		PreparedStatement pst = writeConn.prepareStatement(insertSql);
		for (MaterialReqBillEntryInfo materialReqBillEntryInfo : lsmaterBillEntryInfos) {
			pst.setString(1,"");
			pst.setString(2,"");
			pst.setString(3,"");
			pst.addBatch(); 
		}
		pst.executeBatch();
	}

	private Connection getWriteConn() {
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
		
		return writeConn;
	}
	
	private String getUUid(Connection writeConn,String bostype){
		// 获取EAS 用户ID
		CallableStatement cs = null;
		String iobjectPK = null;
		try {
			cs = writeConn.prepareCall("{ ?=call newbosid(?)}");
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
}
