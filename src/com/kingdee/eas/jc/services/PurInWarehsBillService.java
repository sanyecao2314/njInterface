package com.kingdee.eas.jc.services;

import java.math.BigDecimal;
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
import com.kingdee.eas.jc.util.PropertiesUtil;
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
			purInWarehsBillInfo.setFnumber("CGRK-" + shipNumber + "-" + df.format(new java.util.Date()) + "-" + getSeqNo() );
//			//部门
//			String str = rs.getString("DEPARTMENT");
//			//单据状态
//			String str = rs.getString("BIZ_STATUS");
			//币种 CURRENCY_ID
			str = rs.getString("CURRENCY_ID");
			purInWarehsBillInfo.setFCurrencyID(DPUtil.getCurrencyFid(getReadConn(), str, getWriteConn()));
			//汇率
			purInWarehsBillInfo.setFExchangeRate(DPUtil.getExchange(purInWarehsBillInfo.getFCurrencyID(), getWriteConn()));
			
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
			// 计量单位
			String unitId = rs.getString("UOM");
			purInWarehsEntryInfo.setFUnitID(DPUtil.getFUnitID(unitId, getReadConn(), getWriteConn()));
			//
			purInWarehsEntryInfo.setFbaseUnitID(DPUtil.getMaterialBaseUtil(purInWarehsEntryInfo.getFMaterialID(), getWriteConn()));
			//仓库
			str = rs.getString("WAREHOUSE");
			purInWarehsEntryInfo.setFWarehouseID(DPUtil.getWarehouseFid(getReadConn(), str, getWriteConn()));
			// 传过来的是含税单价
			purInWarehsEntryInfo.setFTaxPrice(rs.getDouble("PRICE"));
			// 无税单价 = 含税单价/1.17
			BigDecimal price = new BigDecimal(purInWarehsEntryInfo.getFTaxPrice()/1.17);  
			purInWarehsEntryInfo.setFPrice(price.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue());
			// 金额也是含税金额(价税合计)
			purInWarehsEntryInfo.setFTaxAmount(rs.getDouble("AMOUNT"));
			//税额 = (加税合计 / 1.17) * 0.17 
			BigDecimal tax = new BigDecimal((purInWarehsEntryInfo.getFTaxAmount() / 1.17) * 0.17);  
			purInWarehsEntryInfo.setFTax(tax.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue());
			// 无税金额 = 加税合计  - 税额
			purInWarehsEntryInfo.setFamount(purInWarehsEntryInfo.getFTaxAmount() - purInWarehsEntryInfo.getFTax());
			//实际成本=无税金额*汇率   
			BigDecimal actualCost = new BigDecimal(purInWarehsEntryInfo.getFamount() * Double.valueOf(purInWarehsBillInfo.getFExchangeRate()));
			purInWarehsEntryInfo.setFActualCost(actualCost.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue());
			// 单位实际成本=实际成本/数量
			BigDecimal unitActualCost = new BigDecimal(purInWarehsEntryInfo.getFActualCost() / purInWarehsEntryInfo.getFQty());
			purInWarehsEntryInfo.setFUnitActualCost(unitActualCost.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue());
			//采购成本=无税金额*汇率   
			BigDecimal purchaseCost = new BigDecimal(purInWarehsEntryInfo.getFamount() * Double.valueOf(purInWarehsBillInfo.getFExchangeRate()));
			purInWarehsEntryInfo.setFPurchaseCost(purchaseCost.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue());
			//单位采购成本=采购成本/数量
			BigDecimal unitPurchaseCost = new BigDecimal(purInWarehsEntryInfo.getFPurchaseCost() / purInWarehsEntryInfo.getFQty());
			purInWarehsEntryInfo.setFUnitPurchaseCost(unitPurchaseCost.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue());
			
			
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
				+ "fbiztypeid,fbilltypeid,fyear,fmonth,fday,fisintax,fispriceintax,fadminorgunitid) "
				+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
		pst.setDouble(15, purInWarehsBillInfo.getFExchangeRate());
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
		//fday
		pst.setString(22, "1");
		//fday
		pst.setString(23, "1");
		
		pst.setString(24, DPUtil.getAdminOrgUnitId(shipNumber, getWriteConn()));
		pst.execute();
	}

	private void insertLsPurInWarehsEntryInfo(Connection writeConn,PurInWarehsBillInfo purInWarehsBillInfo,
			List<PurInWarehsEntryInfo> lspurInWarehsPurInWarehsEntryInfos) throws Exception {
		String insertSql = "insert into T_IM_PurInWarehsEntry(fid, FParentID, fseq, FMaterialID, FUnitID, FQty, FbaseUnitID, FBaseQty, FWarehouseID, FPrice, Famount, "
				+ "FTaxRate, FPurchaseOrgUnitID,FSTORAGEORGUNITID,"
				+ "FCOMPANYORGUNITID,FRECEIVESTORAGEORGUNITID,FTaxPrice,FTaxAmount,FTax,"
				+ "FUnitPurchaseCost,FPurchaseCost,FUnitActualCost,FActualCost,"
				+ "FLocalPrice,FLocalAmount,FLocalTax,FLocalTaxAmount,FActualPrice,FActualTaxPrice) "
				+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
			pst.setString(5, purInWarehsEntryInfo.getFbaseUnitID());
			//Fqty
			pst.setDouble(6, purInWarehsEntryInfo.getFQty());
			//FbaseUnitID
			pst.setString(7, purInWarehsEntryInfo.getFbaseUnitID());
			//FBaseQty
			pst.setDouble(8, purInWarehsEntryInfo.getFQty());
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
			
			pst.setDouble(17,purInWarehsEntryInfo.getFTaxPrice()); 
			pst.setDouble(18,purInWarehsEntryInfo.getFTaxAmount()); 
			pst.setDouble(19,purInWarehsEntryInfo.getFTax()); 
			
			// 单位采购成本
			pst.setDouble(20,purInWarehsEntryInfo.getFUnitPurchaseCost()); 
			// 采购成本
			pst.setDouble(21,purInWarehsEntryInfo.getFPurchaseCost()); 
			// 单位实际成本
			pst.setDouble(22,purInWarehsEntryInfo.getFUnitActualCost()); 
			// 实际成本
			pst.setDouble(23,purInWarehsEntryInfo.getFActualCost()); 
			
			//表体：“实际单价”＝“单价”	FLocalPrice
			BigDecimal localtax = new BigDecimal(purInWarehsEntryInfo.getFPrice() * Double.valueOf(purInWarehsBillInfo.getFExchangeRate()));
			pst.setDouble(24, localtax.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue());
			//表体：“实际含税单价”＝“含税单价”	FLocalAmount
			localtax = new BigDecimal(purInWarehsEntryInfo.getFTaxPrice() * Double.valueOf(purInWarehsBillInfo.getFExchangeRate()));
			pst.setDouble(25, localtax.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue());
			//表体：“税额本位币”＝税额*汇率	FLocalTax
			localtax = new BigDecimal(purInWarehsEntryInfo.getFTax() * Double.valueOf(purInWarehsBillInfo.getFExchangeRate()));
			pst.setDouble(26, localtax.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue());
			//表体：“价税合计本位币”未传递＝价税合计*汇率	FLocalTaxAmount
			BigDecimal localTaxAmount = new BigDecimal(purInWarehsEntryInfo.getFTaxAmount() * Double.valueOf(purInWarehsBillInfo.getFExchangeRate()));
			pst.setDouble(27, localTaxAmount.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue());
			//表体：“实际单价”＝“单价”	FLocalPrice
			pst.setDouble(28, purInWarehsEntryInfo.getFPrice());
			//表体：“实际含税单价”＝“含税单价”	FLocalAmount
			pst.setDouble(29, purInWarehsEntryInfo.getFTaxPrice());
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
	private int getSeqNo(){
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		String strDate = df.format(new java.util.Date());
		int i = 1;
		/*
		 * 
		if (seqNo.containsKey(strDate)) {
			i = seqNo.get(strDate);
		}
		seqNo.put(strDate, ++i);
		 */
		try {
			PropertiesUtil pu = new PropertiesUtil("resource/fnumber.propties");
			String strNo = pu.getValue(strDate);
			if (strNo == null && !"".equals(strNo)) {
				pu.setValue(strDate, i + "");
			} else {
				Integer no = Integer.parseInt(strNo);
				i = no + 1;
			}
		} catch (EASException e) {
			e.printStackTrace();
		}
		return i;
	}
	
}
