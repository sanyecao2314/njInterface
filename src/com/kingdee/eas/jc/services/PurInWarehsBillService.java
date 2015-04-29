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

import com.kingdee.eas.jc.bean.PurInWarehsBillInfo;
import com.kingdee.eas.jc.bean.PurInWarehsEntryInfo;
import com.kingdee.eas.jc.exception.EASException;
import com.kingdee.eas.jc.util.DBWriteUtil;
import com.kingdee.eas.jc.util.LoggerUtil;

public class PurInWarehsBillService {

	public void doProcess(PurInWarehsBillInfo purInWarehsBillInfo,
			List<PurInWarehsEntryInfo> lspurInWarehsEntryInfos) {
		Connection writeConn = null;
		try {
			writeConn = getWriteConn();
			insertPurInWarehsBillInfo(writeConn, purInWarehsBillInfo);
			insertLsPurInWarehsEntryInfo(writeConn, purInWarehsBillInfo.getFid(), lspurInWarehsEntryInfos);
			writeConn.commit();
		} catch (Exception e) {
			try {
				writeConn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally{
			try {
				writeConn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private void insertPurInWarehsBillInfo(Connection writeConn,
			PurInWarehsBillInfo purInWarehsBillInfo) throws SQLException {
		String fid = getUUid(writeConn, purInWarehsBillInfo.getBOStype());
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
			pst.setString(1, getUUid(writeConn, purInWarehsEntryInfo.getBOStype()));
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
