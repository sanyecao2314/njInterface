package com.kingdee.eas.jc.bean;

/**
 * �ɹ���ⵥ����
 * @author fans.fan
 *
 */
public class PurInWarehsEntryInfo extends BaseInfo{

	String FParentID;
	int fseq;
	//���ϱ���
	String FMaterialID;
	//������λ
	String FUnitID;
	//����
	double FQty;
	//����������λ
	String FbaseUnitID;
	//��������
	String FBaseQty;
	//�ֿ�
	String FWarehouseID;
	//����
	double FPrice;
	//���
	double Famount;
	//˰�� 17% �̶�ֵ.
	double FTaxRate = 17d;
	//�ɹ���֯
	String FPurchaseOrgUnitID;
	//�ֿ�
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
