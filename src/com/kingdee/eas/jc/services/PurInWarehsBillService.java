package com.kingdee.eas.jc.services;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kingdee.eas.jc.bean.EventInfo;
import com.kingdee.eas.jc.bean.PurInWarehsBillInfo;
import com.kingdee.eas.jc.bean.PurInWarehsEntryInfo;
import com.kingdee.eas.jc.exception.EASException;
import com.kingdee.eas.jc.util.DBReadUtil;
import com.kingdee.eas.jc.util.DBTools;
import com.kingdee.eas.jc.util.DBWriteUtil;
import com.kingdee.eas.jc.util.DPUtil;
import com.kingdee.eas.jc.util.LoggerUtil;
import com.kingdee.eas.jc.util.StringUtil;

/**
 * 采购入库处理
 * @author fans.fan
 *
 */
public class PurInWarehsBillService {

	private static Map<String, Integer> seqNo = new HashMap<String, Integer>();
	
	public void doProcess(EventInfo eventinfo){
		
		try {
			PurInWarehsBillInfo purInWarehsBillInfo = readAndTranslate(eventinfo);
			doInsert(purInWarehsBillInfo);
			DPUtil.updateMiddleTable(getReadConn(), "OBJECT_NAME", eventinfo.getObjectkey());
			DPUtil.updateMiddleTable(getReadConn(), "OBJECT_NAME", eventinfo.getObjectkey());
			readConn.commit();
			writeConn.commit();
		} catch (Exception e) {
			DBTools.rollback(getWriteConn());
			DBTools.rollback(getReadConn());
			LoggerUtil.logger.error("doProcess error.", e);
		} finally{
			DBTools.close(getWriteConn());
		}
	}
	
	String shipNumber = "";
	
	private PurInWarehsBillInfo readAndTranslate(EventInfo eventinfo) throws SQLException {
		String querySql = "select * from T_COS_BUNKER_STOCKIN where fid='" + eventinfo.getObjectkey() + "'";
		ResultSet rs = getReadConn().createStatement().executeQuery(querySql);
		
		PurInWarehsBillInfo purInWarehsBillInfo = new PurInWarehsBillInfo();
		while (rs.next()) {
//			//事务类型
//			String str = rs.getString("TRANSACTION_TYPE");
			//燃油供应商
			String str = rs.getString("SUPPLIERS_NM");
			String supplierID = DPUtil.getSupplierID(getReadConn(), str, getWriteConn());
			purInWarehsBillInfo.setFSupplierID(supplierID);
			//业务日期
			Timestamp timestamp = rs.getTimestamp("BIZ_DATE");
			Date date = new Date(timestamp.getYear(), timestamp.getMonth(), timestamp.getDate());
			purInWarehsBillInfo.setFbizdate(date);
//			//库存组织
			str = rs.getString("STOCK_ORG");
			shipNumber = str;
//			purInWarehsBillInfo.setFStorageOrgUnitID(DPUtil.getStorageOrgUnitIDByfnumber(getReadConn(), str, getWriteConn()));
			//单据编号
//			str = rs.getString("FID");
			DateFormat df = new SimpleDateFormat("yyyyMMdd");
			purInWarehsBillInfo.setFnumber("CGRK-" + shipNumber + "-" + df.format(new java.util.Date()) + "-" + getSeqNo(date) );
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
		
		String queryEntrySql =  "select * from T_COS_BUNKER_STOCKIN_DETAIL where fid='" + eventinfo.getObjectkey() + "'";
		rs = getReadConn().createStatement().executeQuery(queryEntrySql);
		List<PurInWarehsEntryInfo> lspurEntryInfos = new ArrayList<PurInWarehsEntryInfo>();
		while (rs.next()) {
			PurInWarehsEntryInfo purInWarehsEntryInfo = new PurInWarehsEntryInfo();
			
			String str = rs.getString("CODE");
			purInWarehsEntryInfo.setFMaterialID(DPUtil.getMaterialFid(str, getWriteConn()));
			purInWarehsEntryInfo.setFQty(rs.getDouble("QUANTITY"));
			purInWarehsEntryInfo.setFBaseQty(rs.getString("BASE_QUANTITY"));
			purInWarehsEntryInfo.setFbaseUnitID(DPUtil.getMaterialBaseUtil(purInWarehsEntryInfo.getFMaterialID(), getWriteConn()));
			//仓库
			str = rs.getString("WAREHOUSE");
			purInWarehsEntryInfo.setFWarehouseID(DPUtil.getWarehouseFid(getReadConn(), str, getWriteConn()));
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
			insertLsPurInWarehsEntryInfo(writeConn, purInWarehsBillInfo, purInWarehsBillInfo.getLspurInWarehsEntryInfos());
	}

	private void insertPurInWarehsBillInfo(Connection writeConn,
			PurInWarehsBillInfo purInWarehsBillInfo) throws SQLException {
		String fid = DBTools.getUUid(writeConn, purInWarehsBillInfo.getBOStype());
		purInWarehsBillInfo.setFid(fid);
		String insertSql = "insert into T_IM_PurInWarehsBill(fid, FControlUnitID, FCreatorID, FCreateTime, FLastUpdateUserID, FLastUpdateTime, fnumber,"
				+ " FTransactionTypeID, fbizdate, FSupplierID, FStorageOrgUnitID, FBaseStatus, FPaymentTypeID, FCurrencyID, FExchangeRate, fdescription,"
				+ "fbiztypeid,fbilltypeid,fyear,fmonth,fday) "
				+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
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
		//
		pst.setString(17, purInWarehsBillInfo.getFbiztypeid());
		//
		pst.setString(18, purInWarehsBillInfo.getFbilltypeid());
		//fyear
		pst.setString(19, purInWarehsBillInfo.getFyear());
		//fmonth
		pst.setString(20, purInWarehsBillInfo.getFmonth());
		//fday
		pst.setString(21, purInWarehsBillInfo.getFday());
		
		pst.execute();
	}

	private void insertLsPurInWarehsEntryInfo(Connection writeConn,PurInWarehsBillInfo purInWarehsBillInfo,
			List<PurInWarehsEntryInfo> lspurInWarehsPurInWarehsEntryInfos) throws Exception {
		String insertSql = "insert into T_IM_PurInWarehsEntry(fid, FParentID, fseq, FMaterialID, FUnitID, FQty, FbaseUnitID, FBaseQty, FWarehouseID, FPrice, Famount, "
				+ "FTaxRate, FPurchaseOrgUnitID,FSTORAGEORGUNITID,FCOMPANYORGUNITID,FRECEIVESTORAGEORGUNITID) "
				+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement pst = writeConn.prepareStatement(insertSql);
		int seq = 1;
		for (PurInWarehsEntryInfo purInWarehsEntryInfo : lspurInWarehsPurInWarehsEntryInfos) {
			pst.setString(1, DBTools.getUUid(writeConn, purInWarehsEntryInfo.getBOStype()));
//			//FControlUnitID
//			pst.setString(2, purInWarehsEntryInfo.getFControlUnitID());
//			//FCreatorID
//			pst.setString(3, purInWarehsEntryInfo.getFCreatorID());
//			//FCreateTime
//			pst.setTimestamp(4, new Timestamp(Calendar.getInstance().getTimeInMillis()));
//			//FLastUpdateUserID
//			pst.setString(5, purInWarehsEntryInfo.getFLastUpdateUserID());
//			//FLastUpdateTime
//			pst.setTimestamp(6, new Timestamp(Calendar.getInstance().getTimeInMillis()));
			//FParentID
			pst.setString(2, purInWarehsBillInfo.getFid());
			//fseq
			pst.setInt(3, seq++);
			//FMaterialID
			pst.setString(4, purInWarehsEntryInfo.getFMaterialID());
			//FUnitID
			pst.setString(5, purInWarehsEntryInfo.getFUnitID());
			//Fqty
			pst.setDouble(6, purInWarehsEntryInfo.getFQty());
			//FbaseUnitID
			pst.setString(7, purInWarehsEntryInfo.getFbaseUnitID());
			//FBaseQty
			pst.setString(8, purInWarehsEntryInfo.getFBaseQty());
			//FWarehouseID
			pst.setString(9, purInWarehsEntryInfo.getFWarehouseID());
			//FPrice
			pst.setDouble(10, purInWarehsEntryInfo.getFPrice());
			//Famount
			pst.setDouble(11, purInWarehsEntryInfo.getFamount());
			//FTaxRate
			pst.setDouble(12, purInWarehsEntryInfo.getFTaxRate());
			//FPurchaseOrgUnitID
			pst.setString(13, purInWarehsEntryInfo.getFPurchaseOrgUnitID());
			//
			pst.setString(14,purInWarehsBillInfo.getFStorageOrgUnitID()); 
			//
			pst.setString(15,purInWarehsBillInfo.getFControlUnitID()); 
			//purInWarehsBillInfo
			pst.setString(16,purInWarehsBillInfo.getFStorageOrgUnitID()); 
			
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
	
	/**
	 * 获取顺序号
	 * @param date
	 * @return
	 */
	private int getSeqNo(java.util.Date date){
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		String strDate = df.format(date);
		int i = 1;
		if (seqNo.containsKey(strDate)) {
			i = seqNo.get(strDate);
		}
		seqNo.put(strDate, ++i);
		return 1;
	}
	
}
