package com.kingdee.eas.jc.bean;

/**
 * ���ϳ������
 * @author fans.fan
 *
 */
public class MaterialReqBillEntryInfo extends BaseInfo{

	String fparentId;
	int fseq;
	//���ϱ���
	String FMaterialID;
	//������λ
	String FUnitID;
	//����
	double FQty;
	//����������λ
	String FAssistUnitID;
	//��������
	double FAssistQty;
	//�ֿ�
	String FWarehouseID;
	public String getFparentId() {
		return fparentId;
	}
	public void setFparentId(String fparentId) {
		this.fparentId = fparentId;
	}
	public int getFseq() {
		return fseq;
	}
	public void setFseq(int fseq) {
		this.fseq = fseq;
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
	public String getFAssistUnitID() {
		return FAssistUnitID;
	}
	public void setFAssistUnitID(String fAssistUnitID) {
		FAssistUnitID = fAssistUnitID;
	}
	public double getFAssistQty() {
		return FAssistQty;
	}
	public void setFAssistQty(double fAssistQty) {
		FAssistQty = fAssistQty;
	}
	public String getFWarehouseID() {
		return FWarehouseID;
	}
	public void setFWarehouseID(String fWarehouseID) {
		FWarehouseID = fWarehouseID;
	}
	@Override
	public String getBOStype() {
		return "11774BB4";
	}
	@Override
	public String getTableName() {
		return "T_IM_MaterialReqBillEntry";
	}

}
