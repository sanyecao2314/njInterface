package com.kingdee.eas.jc.bean;

/**
 * 领料出库单表头
 * @author fans.fan
 *
 */
public class MaterialReqBillInfo extends BaseInfo{

	//单据编号
	String fnumber;
	//事务类型
	String FTransactionTypeID;
	//业务日期
	String fbizData;
	//库存组织
	String FStorageOrgUnitID;
	//单据状态
	String FBaseStatus;
	//成本中心
	String FCostCenterOrgUnitID;
	//业务类型
	String FBizTypeID;
	//航次
	String voyage;
	
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
	public String getFbizData() {
		return fbizData;
	}
	public void setFbizData(String fbizData) {
		this.fbizData = fbizData;
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
	public String getVoyage() {
		return voyage;
	}
	public void setVoyage(String voyage) {
		this.voyage = voyage;
	}
	
	public String getBOStype(){
		return "500AB75E";
	}
	
	public String getTableName(){
		return "T_IM_MaterialReqBill";
	}
	
}
