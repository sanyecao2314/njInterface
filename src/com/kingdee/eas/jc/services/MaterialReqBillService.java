package com.kingdee.eas.jc.services;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;
import java.util.List;

import com.kingdee.eas.jc.bean.EventInfo;
import com.kingdee.eas.jc.bean.MaterialReqBillEntryInfo;
import com.kingdee.eas.jc.bean.MaterialReqBillInfo;
import com.kingdee.eas.jc.bean.PurInWarehsBillInfo;
import com.kingdee.eas.jc.bean.PurInWarehsEntryInfo;
import com.kingdee.eas.jc.exception.EASException;
import com.kingdee.eas.jc.util.DBTools;
import com.kingdee.eas.jc.util.DBWriteUtil;
import com.kingdee.eas.jc.util.LoggerUtil;

public class MaterialReqBillService {
	
	
	public void doProcess(EventInfo eventinfo){
		try {
			MaterialReqBillInfo materialReqBillInfo = readAndTranslate(eventinfo);
			doInsert(materialReqBillInfo);
			updateMiddleTable();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			DBTools.rollback(getWriteConn());
			e.printStackTrace();
		}finally{
			DBTools.close(getWriteConn());
		}
	}
	
	private void updateMiddleTable() {
		// TODO Auto-generated method stub
		
	}

	private MaterialReqBillInfo readAndTranslate(EventInfo eventinfo) {
		// TODO Auto-generated method stub
		return null;
	}

	private void doInsert(MaterialReqBillInfo materialReqBillInfo) throws Exception {
		Connection writeConn = null;
			writeConn = getWriteConn();
			insertMaterialReqBillInfo(writeConn, materialReqBillInfo);
			insertLsMaterialReqBillEntryInfo(writeConn, materialReqBillInfo.getFid(), materialReqBillInfo.getLsmaterBillEntryInfos());
			writeConn.commit();
	}

	private void insertMaterialReqBillInfo(Connection writeConn,
			MaterialReqBillInfo materialReqBillInfo) throws SQLException {
		String fid = DBTools.getUUid(writeConn, materialReqBillInfo.getBOStype());
		materialReqBillInfo.setFid(fid);
		String insertSql = "insert into T_IM_MaterialReqBill(fid, FControlUnitID, FCreatorID, FCreateTime, FLastUpdateUserID, FLastUpdateTime, fnumber, FTransactionTypeID, fbizdate, FStorageOrgUnitID, FBaseStatus, FCostCenterOrgUnitID, FBizTypeID, fdescription) "
				+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement pst = writeConn.prepareStatement(insertSql);
		//fid
		pst.setString(1, fid);
		//FControlUnitID
		pst.setString(2, materialReqBillInfo.getFControlUnitID());
		//FCreatorID
		pst.setString(3, materialReqBillInfo.getFCreatorID());
		//FCreateTime
		pst.setTimestamp(4, new Timestamp(Calendar.getInstance().getTimeInMillis()));
		//FLastUpdateUserID
		pst.setString(5, materialReqBillInfo.getFLastUpdateUserID());
		//FLastUpdateTime
		pst.setTimestamp(6, new Timestamp(Calendar.getInstance().getTimeInMillis()));
		//fnumber
		pst.setString(7, materialReqBillInfo.getFnumber());
		//FTransactionTypeID
		pst.setString(8, materialReqBillInfo.getFTransactionTypeID());
		//fbizdate
		pst.setDate(9, materialReqBillInfo.getFbizdate());
		//FStorageOrgUnitID
		pst.setString(10, materialReqBillInfo.getFStorageOrgUnitID());
		//FBaseStatus
		pst.setString(11, materialReqBillInfo.getFBaseStatus());
		//FCostCenterOrgUnitID;
		pst.setString(12, materialReqBillInfo.getFCostCenterOrgUnitID());
		//FBizTypeID;
		pst.setString(13, materialReqBillInfo.getFBizTypeID());
		//fdescription
		pst.setString(14, materialReqBillInfo.getVoyage());
		pst.execute();
	}

	private void insertLsMaterialReqBillEntryInfo(Connection writeConn, String fparentid,
			List<MaterialReqBillEntryInfo> lsmaterBillEntryInfos) throws Exception {
		String insertSql = "insert into T_IM_MaterialReqBillEntry(fid, FControlUnitID, FCreatorID, FCreateTime, FLastUpdateUserID, FLastUpdateTime, FParentID, fseq, FMaterialID, FUnitID, FQty, FAssistUnitID, FAssistQty, FWarehouseID) " 
				+" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement pst = writeConn.prepareStatement(insertSql);
		int seq = 1;
		for (MaterialReqBillEntryInfo materialReqBillEntryInfo : lsmaterBillEntryInfos) {
			pst.setString(1, DBTools.getUUid(writeConn, materialReqBillEntryInfo.getBOStype()));
			//FControlUnitID
			pst.setString(2, materialReqBillEntryInfo.getFControlUnitID());
			//FCreatorID
			pst.setString(3, materialReqBillEntryInfo.getFCreatorID());
			//FCreateTime
			pst.setTimestamp(4, new Timestamp(Calendar.getInstance().getTimeInMillis()));
			//FLastUpdateUserID
			pst.setString(5, materialReqBillEntryInfo.getFLastUpdateUserID());
			//FLastUpdateTime
			pst.setTimestamp(6, new Timestamp(Calendar.getInstance().getTimeInMillis()));
			//FParentID
			pst.setString(7, fparentid);
			//fseq
			pst.setInt(8, seq++);
			//FMaterialID
			pst.setString(9, materialReqBillEntryInfo.getFMaterialID());
			//FUnitID
			pst.setString(10, materialReqBillEntryInfo.getFUnitID());
			//Fqty
			pst.setDouble(11, materialReqBillEntryInfo.getFQty());
			//FAssistUnitID
			pst.setString(12, materialReqBillEntryInfo.getFAssistUnitID());
			//FAssistQty;
			pst.setDouble(13, materialReqBillEntryInfo.getFAssistQty());
			//FWarehouseID
			pst.setString(14, materialReqBillEntryInfo.getFWarehouseID());
			
			pst.addBatch(); 
		}
		pst.executeBatch();
	}

	Connection writeConn = null;
	private Connection getWriteConn() {
		try {
			if (writeConn == null || writeConn.isClosed()) {
				// Ð´ÊýÁ¬½Ó
				DBWriteUtil dbwrite = DBWriteUtil.getInstance();
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
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return writeConn;
	}
	
	
}
