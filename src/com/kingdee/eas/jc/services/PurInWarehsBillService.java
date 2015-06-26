package com.kingdee.eas.jc.services;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.kingdee.eas.jc.bean.EventInfo;
import com.kingdee.eas.jc.bean.PurInWarehsBillInfo;
import com.kingdee.eas.jc.bean.PurInWarehsEntryInfo;
import com.kingdee.eas.jc.exception.EASException;
import com.kingdee.eas.jc.util.DBReadUtil;
import com.kingdee.eas.jc.util.DBTools;
import com.kingdee.eas.jc.util.DBWriteUtil;
import com.kingdee.eas.jc.util.DPUtil;
import com.kingdee.eas.jc.util.LoggerUtil;

/**
 * 采购入库处理
 * @author fans.fan
 *
 */
public class PurInWarehsBillService {

	
	public void doProcess(EventInfo eventinfo){
		
		try {
			PurInWarehsBillInfo purInWarehsBillInfo = readAndTranslate(eventinfo);
			doInsert(purInWarehsBillInfo);
			DPUtil.updateMiddleTable(getReadConn(), "T_COS_BUNKER_STOCKIN", eventinfo.getEventid());
			DPUtil.updateMiddleTable(getReadConn(), "T_COS_BUNKER_STOCKIN_DETAIL", eventinfo.getEventid());
		} catch (Exception e) {
			DBTools.rollback(getWriteConn());
			e.printStackTrace();
		} finally{
			DBTools.close(getWriteConn());
		}
	}
	
	private PurInWarehsBillInfo readAndTranslate(EventInfo eventinfo) throws SQLException {
		String querySql = "select * from T_COS_BUNKER_STOCKIN where fid='" + eventinfo.getEventid() + "'";
		ResultSet rs = getReadConn().createStatement().executeQuery(querySql);
		
		PurInWarehsBillInfo purInWarehsBillInfo = new PurInWarehsBillInfo();
		while (rs.next()) {
			//单据编号
			String str = rs.getString("FID");
			purInWarehsBillInfo.setFnumber(str);
//			//事务类型
//			String str = rs.getString("TRANSACTION_TYPE");
			//燃油供应商
			str = rs.getString("SUPPLIERS_ID");
			String supplierID = DPUtil.getSupplierID(getReadConn(), str, getWriteConn());
			purInWarehsBillInfo.setFSupplierID(supplierID);
			//业务日期
			Timestamp timestamp = rs.getTimestamp("BIZ_DATE");
			Date date = new Date(timestamp.getYear(), timestamp.getMonth(), timestamp.getDate());
			purInWarehsBillInfo.setFbizdate(date);
//			//库存组织
//			String str = rs.getString("STOCK_ORG");
//			//部门
//			String str = rs.getString("DEPARTMENT");
//			//单据状态
//			String str = rs.getString("BIZ_STATUS");
			//币种 CURRENCY_ID
			str = rs.getString("CURRENCY_ID");
			purInWarehsBillInfo.setFCurrencyID(DPUtil.getCurrencyFid(getReadConn(), str, getWriteConn()));
//			//成本中心
//			String str = rs.getString("COST_CENTER");
//			//业务类型
//			String str = rs.getString("FID");
			//摘要
			//源单据类型
			//航次
			str = rs.getString("VOY_NO");
			purInWarehsBillInfo.setFDescription(str);

		}
		
		String queryEntrySql =  "select * from T_COS_BUNKER_STOCKIN_DETAIL where fid='" + eventinfo.getEventid() + "'";
		rs = getReadConn().createStatement().executeQuery(querySql);
		List<PurInWarehsEntryInfo> lspurEntryInfos = new ArrayList<PurInWarehsEntryInfo>();
		while (rs.next()) {
			PurInWarehsEntryInfo purInWarehsEntryInfo = new PurInWarehsEntryInfo();
			
			String str = rs.getString("CODE");
			purInWarehsEntryInfo.setFMaterialID(DPUtil.getMaterialFid(str, getWriteConn()));
			purInWarehsEntryInfo.setFQty(rs.getDouble("QUANTITY"));
			purInWarehsEntryInfo.setFBaseQty(rs.getString("BASE_QUANTITY"));
			str = rs.getString("WAREHOUSE");
			purInWarehsEntryInfo.setWarehouse(DPUtil.getWarehouseFid(getReadConn(), str, getWriteConn()));
			purInWarehsEntryInfo.setFPrice(rs.getDouble("PRICE"));
			purInWarehsEntryInfo.setFamount(rs.getDouble("AMOUNT"));
			
			lspurEntryInfos.add(purInWarehsEntryInfo);
		}
		
		purInWarehsBillInfo.setLspurInWarehsEntryInfos(lspurEntryInfos);
		
		return purInWarehsBillInfo;
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
		pst.setString(16, purInWarehsBillInfo.getFDescription());
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
				// 写数连接
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
			e.printStackTrace();
		}
		
		return writeConn;
	}
	
	Connection readConn = null;
	private Connection getReadConn(){
		try {
			if (readConn == null || readConn.isClosed()) {
				DBReadUtil dbread = DBReadUtil.getInstance();
				readConn = dbread.getConnection();
				readConn.getAutoCommit();
				readConn.setAutoCommit(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LoggerUtil.logger.error(e.getMessage());
		}
		
		return readConn;
	}
	
}
