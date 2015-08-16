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
import com.kingdee.eas.jc.bean.MaterialReqBillEntryInfo;
import com.kingdee.eas.jc.bean.MaterialReqBillInfo;
import com.kingdee.eas.jc.exception.EASException;
import com.kingdee.eas.jc.util.DBReadUtil;
import com.kingdee.eas.jc.util.DBTools;
import com.kingdee.eas.jc.util.DBWriteUtil;
import com.kingdee.eas.jc.util.DPUtil;
import com.kingdee.eas.jc.util.LoggerUtil;
import com.kingdee.eas.jc.util.PropertiesUtil;
import com.kingdee.eas.jc.util.StringUtil;

/**
 * 材料出库处理
 * @author fans.fan
 *
 */
public class MaterialReqBillService {
	
	
	public void doProcess(EventInfo eventinfo){
		try {
			MaterialReqBillInfo materialReqBillInfo = readAndTranslate(eventinfo);
			doInsert(materialReqBillInfo);
			
			DPUtil.updateMiddleTable(getReadConn(), "OBJECT_NAME", eventinfo.getObjectkey());
			DPUtil.updateMiddleTable(getReadConn(), "OBJECT_NAME", eventinfo.getObjectkey());
			readConn.commit();
			writeConn.commit();
		} catch (Exception e) {
			DBTools.rollback(getWriteConn());
			DBTools.rollback(getReadConn());
			LoggerUtil.logger.error("doProcess error.", e);
		}finally{
			DBTools.close(getWriteConn());
		}
	}
	
	String shipNumber = "";

	private MaterialReqBillInfo readAndTranslate(EventInfo eventinfo) throws SQLException {
		String querySql = "select * from T_COS_BUNKER_CONSUME where fid='" + eventinfo.getObjectkey() + "'";
		ResultSet rs = getReadConn().createStatement().executeQuery(querySql);

		MaterialReqBillInfo materReqBillInfo = new MaterialReqBillInfo();
		while (rs.next()) {
			String str = rs.getString("FID");
			Timestamp timestamp = rs.getTimestamp("BIZ_DATE");
			Date date = new Date(timestamp.getYear(), timestamp.getMonth(), timestamp.getDate());
			materReqBillInfo.setFbizdate(date);
			str = rs.getString("STOCK_ORG");
			shipNumber = str;
//			materReqBillInfo.setFStorageOrgUnitID(DPUtil.getStorageOrgUnitIDByfnumber(getReadConn(), str, getWriteConn()));
			DateFormat df = new SimpleDateFormat("yyyyMMdd");
			materReqBillInfo.setFnumber("LLCK-" + shipNumber + "-" + df.format(new java.util.Date()) + "-" + getSeqNo(date) );
			materReqBillInfo.setFDescription(rs.getString("VOY_NO"));
			str = rs.getString("COST_CENTER");
			materReqBillInfo.setFCostCenterOrgUnitID(DPUtil.getCostCenterFid(getReadConn(), str, getWriteConn()));
		}

		String queryEntrySql = "select * from T_COS_BUNKER_CONSUME_DETAIL where fid='" + eventinfo.getObjectkey() + "'";
		rs = getReadConn().createStatement().executeQuery(queryEntrySql);
		List<MaterialReqBillEntryInfo> lspurEntryInfos = new ArrayList<MaterialReqBillEntryInfo>();
		while (rs.next()) {
			try {
				MaterialReqBillEntryInfo materialReqBillEntryInfo = new MaterialReqBillEntryInfo();
				
				String str = rs.getString("CODE");
				materialReqBillEntryInfo.setFMaterialID(DPUtil.getMaterialFid(str, getWriteConn()));
				materialReqBillEntryInfo.setFQty(rs.getDouble("QUANTITY"));
				materialReqBillEntryInfo.setFAssistQty(rs.getDouble("BASE_QUANTITY"));
				materialReqBillEntryInfo.setFWarehouseID(DPUtil.getWarehouseFidByfnumber(getReadConn(), shipNumber, getWriteConn()));
				materialReqBillEntryInfo.setFAssistUnitID(DPUtil.getMaterialBaseUtil(materialReqBillEntryInfo.getFMaterialID(), getWriteConn()));
				lspurEntryInfos.add(materialReqBillEntryInfo);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}

		materReqBillInfo.setLsmaterBillEntryInfos(lspurEntryInfos);

		return materReqBillInfo;
	}

	private void doInsert(MaterialReqBillInfo materialReqBillInfo) throws Exception {
		Connection writeConn = null;
			writeConn = getWriteConn();
			insertMaterialReqBillInfo(writeConn, materialReqBillInfo);
			insertLsMaterialReqBillEntryInfo(writeConn, materialReqBillInfo, materialReqBillInfo.getLsmaterBillEntryInfos());
			
	}

	private void insertMaterialReqBillInfo(Connection writeConn,
			MaterialReqBillInfo materialReqBillInfo) throws SQLException {
		String fid = DBTools.getUUid(writeConn, materialReqBillInfo.getBOStype());
		materialReqBillInfo.setFid(fid);
		String insertSql = "insert into T_IM_MaterialReqBill(fid, FControlUnitID, FCreatorID, FCreateTime, FLastUpdateUserID, FLastUpdateTime, fnumber,"
				+ " FTransactionTypeID, fbizdate, FStorageOrgUnitID, FBaseStatus, FCostCenterOrgUnitID, FBizTypeID, fdescription,fbilltypeid,fyear,fmonth,fday) "
				+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
		pst.setString(14, materialReqBillInfo.getFDescription());
		//
		pst.setString(15, materialReqBillInfo.getFbilltypeid());
		//
		pst.setString(16, materialReqBillInfo.getFyear());
		//
		pst.setString(17, materialReqBillInfo.getFmonth());
		//
		pst.setString(18, materialReqBillInfo.getFday());
		pst.execute();
	}

	private void insertLsMaterialReqBillEntryInfo(Connection writeConn, MaterialReqBillInfo materialReqBillInfo,
			List<MaterialReqBillEntryInfo> lsmaterBillEntryInfos) throws Exception {
		String insertSql = "insert into T_IM_MaterialReqBillEntry(fid, FParentID, fseq, FMaterialID, FUnitID, FQty, FAssistUnitID, FAssistQty, FWarehouseID,"
				+ "fstorageorgunitid,fcompanyorgunitid,FBaseUnitID) " 
				+" values(?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement pst = writeConn.prepareStatement(insertSql);
		int seq = 1;
		for (MaterialReqBillEntryInfo materialReqBillEntryInfo : lsmaterBillEntryInfos) {
			pst.setString(1, DBTools.getUUid(writeConn, materialReqBillEntryInfo.getBOStype()));
//			//FControlUnitID
//			pst.setString(2, materialReqBillEntryInfo.getFControlUnitID());
//			//FCreatorID
//			pst.setString(3, materialReqBillEntryInfo.getFCreatorID());
//			//FCreateTime
//			pst.setTimestamp(4, new Timestamp(Calendar.getInstance().getTimeInMillis()));
//			//FLastUpdateUserID
//			pst.setString(5, materialReqBillEntryInfo.getFLastUpdateUserID());
//			//FLastUpdateTime
//			pst.setTimestamp(6, new Timestamp(Calendar.getInstance().getTimeInMillis()));
			//FParentID
			pst.setString(2, materialReqBillInfo.getFid());
			//fseq
			pst.setInt(3, seq++);
			//FMaterialID
			pst.setString(4, materialReqBillEntryInfo.getFMaterialID());
			//FUnitID
			pst.setString(5, materialReqBillEntryInfo.getFUnitID());
			//Fqty
			pst.setDouble(6, materialReqBillEntryInfo.getFQty());
			//FAssistUnitID
			pst.setString(7, materialReqBillEntryInfo.getFUnitID());
			//FAssistQty;
			pst.setDouble(8, materialReqBillEntryInfo.getFAssistQty());
			//FWarehouseID
			pst.setString(9, materialReqBillEntryInfo.getFWarehouseID());
			
			pst.setString(10, materialReqBillInfo.getFStorageOrgUnitID());
			
			pst.setString(11, materialReqBillInfo.getFControlUnitID());
			
			pst.setString(12, materialReqBillEntryInfo.getFAssistUnitID());
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
			LoggerUtil.logger.error("getWriteConn error.", e);
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
	
	private static Map<String, Integer> seqNo = new HashMap<String, Integer>();
	
	/**
	 * 获取顺序号
	 * @param date
	 * @return
	 */
	private int getSeqNo(java.util.Date date){
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		String strDate = df.format(date);
		int i = 1;
		/*
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
		return 1;
	}
	
	
}
