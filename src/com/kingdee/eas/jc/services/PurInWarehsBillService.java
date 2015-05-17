package com.kingdee.eas.jc.services;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;
import java.util.List;

import org.apache.axis.types.Time;

import com.kingdee.eas.jc.bean.EventInfo;
import com.kingdee.eas.jc.bean.PurInWarehsBillInfo;
import com.kingdee.eas.jc.bean.PurInWarehsEntryInfo;
import com.kingdee.eas.jc.exception.EASException;
import com.kingdee.eas.jc.util.DBTools;
import com.kingdee.eas.jc.util.DBWriteUtil;
import com.kingdee.eas.jc.util.LoggerUtil;

public class PurInWarehsBillService {

	
	public void doProcess(EventInfo eventinfo){
		
		try {
			PurInWarehsBillInfo purInWarehsBillInfo = readAndTranslate(eventinfo);
			doInsert(purInWarehsBillInfo);
			updateMiddleTable();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			DBTools.rollback(getWriteConn());
			e.printStackTrace();
		} finally{
			DBTools.close(getWriteConn());
		}
	}
	
	private void updateMiddleTable() {
		// TODO Auto-generated method stub
		
	}

	private PurInWarehsBillInfo readAndTranslate(EventInfo eventinfo) {
		// TODO Auto-generated method stub
		return null;
	}

	private void doInsert(PurInWarehsBillInfo purInWarehsBillInfo) throws Exception {
		Connection writeConn = null;
			writeConn = getWriteConn();
			insertPurInWarehsBillInfo(writeConn, purInWarehsBillInfo);
			insertLsPurInWarehsEntryInfo(writeConn, purInWarehsBillInfo.getFid(), purInWarehsBillInfo.getLspurInWarehsEntryInfos());
			writeConn.commit();
	}

	private void insertPurInWarehsBillInfo(Connection writeConn,
			PurInWarehsBillInfo purInWarehsBillInfo) throws SQLException {
		String fid = DBTools.getUUid(writeConn, purInWarehsBillInfo.getBOStype());
		purInWarehsBillInfo.setFid(fid);
		String insertSql = "insert into T_IM_PurInWarehsBill(fid, FControlUnitID, FCreatorID, FCreateTime, FLastUpdateUserID, FLastUpdateTime, fnumber, FTransactionTypeID, fbizdate, FSupplierID, FStorageOrgUnitID, FBaseStatus, FPaymentTypeID, FCurrencyID, FExchangeRate, fdescription) "
				+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement pst = writeConn.prepareStatement(insertSql);
		//fid
		pst.setString(1, fid);
		//FControlUnitID
		pst.setString(2, purInWarehsBillInfo.getFControlUnitID());
		//FCreatorID
		pst.setString(3, purInWarehsBillInfo.getFCreatorID());
		//FCreateTime
		pst.setTimestamp(4, new Timestamp(Calendar.getInstance().getTimeInMillis()));
		//FLastUpdateUserID
		pst.setString(5, purInWarehsBillInfo.getFLastUpdateUserID());
		//FLastUpdateTime
		pst.setTimestamp(6, new Timestamp(Calendar.getInstance().getTimeInMillis()));
		//fnumber
		pst.setString(7, purInWarehsBillInfo.getFnumber());
		//FTransactionTypeID
		pst.setString(8, purInWarehsBillInfo.getFTransactionTypeID());
		//fbizdate
		pst.setDate(9, purInWarehsBillInfo.getFbizdate());
		//FSupplierID
		pst.setString(10, purInWarehsBillInfo.getFSupplierID());
		//FStorageOrgUnitID
		pst.setString(11, purInWarehsBillInfo.getFStorageOrgUnitID());
		//FBaseStatus
		pst.setString(12, purInWarehsBillInfo.getFBaseStatus());
		//FPaymentTypeID
		pst.setString(13, purInWarehsBillInfo.getFPaymentTypeID());
		//FCurrencyID
		pst.setString(14, purInWarehsBillInfo.getFCurrencyID());
		//FExchangeRate
		pst.setString(15, purInWarehsBillInfo.getFExchangeRate());
		//fdescription
		pst.setString(16, purInWarehsBillInfo.getVoyage());
		pst.execute();
	}

	private void insertLsPurInWarehsEntryInfo(Connection writeConn,String fparentid,
			List<PurInWarehsEntryInfo> lspurInWarehsPurInWarehsEntryInfos) throws Exception {
		String insertSql = "insert into T_IM_PurInWarehsBill(fid, FControlUnitID, FCreatorID, FCreateTime, FLastUpdateUserID, FLastUpdateTime, FParentID, fseq, FMaterialID, FUnitID, FQty, FbaseUnitID, FBaseQty, FWarehouseID, FPrice, Famount, FTaxRate, FPurchaseOrgUnitID) "
				+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement pst = writeConn.prepareStatement(insertSql);
		int seq = 1;
		for (PurInWarehsEntryInfo purInWarehsEntryInfo : lspurInWarehsPurInWarehsEntryInfos) {
			pst.setString(1, DBTools.getUUid(writeConn, purInWarehsEntryInfo.getBOStype()));
			//FControlUnitID
			pst.setString(2, purInWarehsEntryInfo.getFControlUnitID());
			//FCreatorID
			pst.setString(3, purInWarehsEntryInfo.getFCreatorID());
			//FCreateTime
			pst.setTimestamp(4, new Timestamp(Calendar.getInstance().getTimeInMillis()));
			//FLastUpdateUserID
			pst.setString(5, purInWarehsEntryInfo.getFLastUpdateUserID());
			//FLastUpdateTime
			pst.setTimestamp(6, new Timestamp(Calendar.getInstance().getTimeInMillis()));
			//FParentID
			pst.setString(7, fparentid);
			//fseq
			pst.setInt(8, seq++);
			//FMaterialID
			pst.setString(9, purInWarehsEntryInfo.getFMaterialID());
			//FUnitID
			pst.setString(10, purInWarehsEntryInfo.getFUnitID());
			//Fqty
			pst.setDouble(11, purInWarehsEntryInfo.getFQty());
			//FbaseUnitID
			pst.setString(12, purInWarehsEntryInfo.getFbaseUnitID());
			//FBaseQty
			pst.setString(13, purInWarehsEntryInfo.getFBaseQty());
			//FWarehouseID
			pst.setString(14, purInWarehsEntryInfo.getFWarehouseID());
			//FPrice
			pst.setDouble(15, purInWarehsEntryInfo.getFPrice());
			//Famount
			pst.setDouble(16, purInWarehsEntryInfo.getFamount());
			//FTaxRate
			pst.setDouble(17, purInWarehsEntryInfo.getFTaxRate());
			//FPurchaseOrgUnitID
			pst.setString(18, purInWarehsEntryInfo.getFPurchaseOrgUnitID());
			
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
