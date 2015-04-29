package com.kingdee.eas.jc.bean;

import java.sql.Date;


/**
 * 采购入库单表头
 * @author fans.fan
 *
 */
public class PurInWarehsBillInfo extends BaseInfo{

	//单据编号
	String fnumber;
	//事务类型
	String FTransactionTypeID;
	//业务日期
	Date fbizdate;
	//供应商
	String FSupplierID;
	//库存组织
	String FStorageOrgUnitID;
	//单据状态
	String FBaseStatus;
	//付款方式
	String FPaymentTypeID;
	//币别
	String FCurrencyID;
	//汇率
	String FExchangeRate;
	//航次
	String Voyage;
	
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
	public String getFSupplierID() {
		return FSupplierID;
	}
	public void setFSupplierID(String fSupplierID) {
		FSupplierID = fSupplierID;
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
	public String getFPaymentTypeID() {
		return FPaymentTypeID;
	}
	public void setFPaymentTypeID(String fPaymentTypeID) {
		FPaymentTypeID = fPaymentTypeID;
	}
	public String getFCurrencyID() {
		return FCurrencyID;
	}
	public void setFCurrencyID(String fCurrencyID) {
		FCurrencyID = fCurrencyID;
	}
	public String getFExchangeRate() {
		return FExchangeRate;
	}
	public void setFExchangeRate(String fExchangeRate) {
		FExchangeRate = fExchangeRate;
	}
	public String getVoyage() {
		return Voyage;
	}
	public void setVoyage(String voyage) {
		Voyage = voyage;
	}
	@Override
	public String getBOStype() {
		return "783061E3";
	}
	@Override
	public String getTableName() {
		return "T_IM_PurInWarehsBill";
	}
	
}
