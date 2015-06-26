package com.kingdee.eas.jc.bean;

/**
 * 采购入库单表体
 * @author fans.fan
 *
 */
public class PurInWarehsEntryInfo extends BaseInfo{

	String FParentID;
	int fseq;
	//物料编码
	String FMaterialID;
	//计量单位
	String FUnitID;
	//数量
	double FQty;
	//基本计量单位
	String FbaseUnitID;
	//基本数量
	String FBaseQty;
	//仓库
	String FWarehouseID;
	//单价
	double FPrice;
	//金额
	double Famount;
	//税率 17% 固定值.
	double FTaxRate = 17d;
	//采购组织
	String FPurchaseOrgUnitID;
	//仓库
	String warehouse;
	
	public String getFParentID() {
		return FParentID;
	}
	public void setFParentID(String fParentID) {
		FParentID = fParentID;
	}
	public String getFMaterialID() {
		return FMaterialID;
	}
	public void setFMaterialID(String fMaterialID) {
		FMaterialID = fMaterialID;
	}
	public String getFUnitID() {
		return FUnitID;
	}
	public void setFUnitID(String fUnitID) {
		FUnitID = fUnitID;
	}
	public double getFQty() {
		return FQty;
	}
	public void setFQty(double fQty) {
		FQty = fQty;
	}
	public String getFbaseUnitID() {
		return FbaseUnitID;
	}
	public void setFbaseUnitID(String fbaseUnitID) {
		FbaseUnitID = fbaseUnitID;
	}
	public String getFBaseQty() {
		return FBaseQty;
	}
	public void setFBaseQty(String fBaseQty) {
		FBaseQty = fBaseQty;
	}
	public String getFWarehouseID() {
		return FWarehouseID;
	}
	public void setFWarehouseID(String fWarehouseID) {
		FWarehouseID = fWarehouseID;
	}
	public double getFPrice() {
		return FPrice;
	}
	public void setFPrice(double fPrice) {
		FPrice = fPrice;
	}
	public double getFamount() {
		return Famount;
	}
	public void setFamount(double famount) {
		Famount = famount;
	}
	public double getFTaxRate() {
		return FTaxRate;
	}
	public void setFTaxRate(double fTaxRate) {
		FTaxRate = fTaxRate;
	}
	public String getFPurchaseOrgUnitID() {
		return FPurchaseOrgUnitID;
	}
	public void setFPurchaseOrgUnitID(String fPurchaseOrgUnitID) {
		FPurchaseOrgUnitID = fPurchaseOrgUnitID;
	}
	public int getFseq() {
		return fseq;
	}
	public void setFseq(int fseq) {
		this.fseq = fseq;
	}
	public String getWarehouse() {
		return warehouse;
	}
	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}
	@Override
	public String getBOStype() {
		return "8E088616";
	}
	@Override
	public String getTableName() {
		return "T_IM_PurInWarehsEntry";
	}
	
}
