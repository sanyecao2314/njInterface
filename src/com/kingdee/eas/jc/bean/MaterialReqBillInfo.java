package com.kingdee.eas.jc.bean;

import java.sql.Date;
import java.util.List;

/**
 * 领料出库单表头
 * @author fans.fan
 *
 */
public class MaterialReqBillInfo extends BaseInfo{

	//单据编号
	String fnumber;
	//事务类型  普通领料出库
	String FTransactionTypeID = "1aa51711-0f60-455a-a3c2-43b86155098dB008DCA7";
	//业务日期
	Date fbizdate;
	//库存组织
	String FStorageOrgUnitID;
	//单据状态 保存
	String FBaseStatus = "1";
	//成本中心
	String FCostCenterOrgUnitID;
	//业务类型 普通领料
	String FBizTypeID = "0rSFjAEeEADgAAyMwKgSQiQHQ1w=";
	//航次
	String FDescription;
	
	/* 子表信息 */
	List<MaterialReqBillEntryInfo> lsmaterBillEntryInfos;
	
	public String getFnumber() {
		return fnumber;
	}
	public void setFnumber(String fnumber) {
		this.fnumber = fnumber;
	}
	public String getFTransactionTypeID() {
		return FTransactionTypeID;
	}
	public void setFTransactionTypeID(String fTransactionTypeID) {
		FTransactionTypeID = fTransactionTypeID;
	}
	public Date getFbizdate() {
		return fbizdate;
	}
	public void setFbizdate(Date fbizdate) {
		this.fbizdate = fbizdate;
	}
	public String getFStorageOrgUnitID() {
		return FStorageOrgUnitID;
	}
	public void setFStorageOrgUnitID(String fStorageOrgUnitID) {
		FStorageOrgUnitID = fStorageOrgUnitID;
	}
	public String getFBaseStatus() {
		return FBaseStatus;
	}
	public void setFBaseStatus(String fBaseStatus) {
		FBaseStatus = fBaseStatus;
	}
	public String getFCostCenterOrgUnitID() {
		return FCostCenterOrgUnitID;
	}
	public void setFCostCenterOrgUnitID(String fCostCenterOrgUnitID) {
		FCostCenterOrgUnitID = fCostCenterOrgUnitID;
	}
	public String getFBizTypeID() {
		return FBizTypeID;
	}
	public void setFBizTypeID(String fBizTypeID) {
		FBizTypeID = fBizTypeID;
	}
	public String getFDescription() {
		return FDescription;
	}
	public void setFDescription(String fDescription) {
		FDescription = fDescription;
	}
	public String getBOStype(){
		return "500AB75E";
	}
	
	public String getTableName(){
		return "T_IM_MaterialReqBill";
	}
	public List<MaterialReqBillEntryInfo> getLsmaterBillEntryInfos() {
		return lsmaterBillEntryInfos;
	}
	public void setLsmaterBillEntryInfos(
			List<MaterialReqBillEntryInfo> lsmaterBillEntryInfos) {
		this.lsmaterBillEntryInfos = lsmaterBillEntryInfos;
	}
	
	
}
