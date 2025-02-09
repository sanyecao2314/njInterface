package com.kingdee.eas.jc.bean;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
	//库存组织  原油事业部
	String FStorageOrgUnitID = "Fg/3htR2QAy9dbSisTDtxMznrtQ=";
	//单据状态 保存
	String FBaseStatus = "1";
	//成本中心
	String FCostCenterOrgUnitID;
	//业务类型 普通领料
	String FBizTypeID = "0rSFjAEeEADgAAyMwKgSQiQHQ1w=";
	//航次
	String FDescription;
	//
	String fbilltypeid = "50957179-0105-1000-e000-0163c0a812fd463ED552";
	
	String fyear;
	String fmonth;
	String fday;
	
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

	public String getFbilltypeid() {
		return fbilltypeid;
	}
	public void setFbilltypeid(String fbilltypeid) {
		this.fbilltypeid = fbilltypeid;
	}
	
	public String getFyear() {
		DateFormat df  = new SimpleDateFormat("yyyy");
		return df.format(fbizdate);
	}
	public void setFyear(String fyear) {
		this.fyear = fyear;
	}
	public String getFmonth() {
		DateFormat df  = new SimpleDateFormat("yyyyMM");
		return df.format(fbizdate);
	}
	public void setFmonth(String fmonth) {
		this.fmonth = fmonth;
	}
	public String getFday() {
		DateFormat df  = new SimpleDateFormat("yyyyMMdd");
		return df.format(fbizdate);
	}
	public void setFday(String fday) {
		this.fday = fday;
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
