package com.kingdee.eas.jc.bean;

import java.sql.Date;
import java.util.List;


/**
 * 采购入库单表头
 * @author fans.fan
 *
 */
public class PurInWarehsBillInfo extends BaseInfo{

	//单据编号
	String fnumber;
	//事务类型 手工采购入库
	String FTransactionTypeID = "0F/edAELEADgABkkwKgSKbAI3Kc=";
	//业务日期
	Date fbizdate;
	//供应商
	String FSupplierID;
	//库存组织
	String FStorageOrgUnitID;
	//单据状态 1代表保存
	String FBaseStatus = "1";
	//付款方式 赊购
	String FPaymentTypeID = "2fa35444-5a23-43fb-99ee-6d4fa5f260da6BCA0AB5";
	//币别
	String FCurrencyID;
	//汇率
	String FExchangeRate;
	//航次
	String FDescription;
	/* 子表信息 */
	List<PurInWarehsEntryInfo> lspurInWarehsEntryInfos;
	
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

	public String getFDescription() {
		return FDescription;
	}
	public void setFDescription(String fDescription) {
		FDescription = fDescription;
	}
	@Override
	public String getBOStype() {
		return "783061E3";
	}
	@Override
	public String getTableName() {
		return "T_IM_PurInWarehsBill";
	}
	public List<PurInWarehsEntryInfo> getLspurInWarehsEntryInfos() {
		return lspurInWarehsEntryInfos;
	}
	public void setLspurInWarehsEntryInfos(
			List<PurInWarehsEntryInfo> lspurInWarehsEntryInfos) {
		this.lspurInWarehsEntryInfos = lspurInWarehsEntryInfos;
	}
	
}
