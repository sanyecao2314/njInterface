package com.kingdee.eas.jc.bean;

import java.sql.Date;


/**
 * �ɹ���ⵥ��ͷ
 * @author fans.fan
 *
 */
public class PurInWarehsBillInfo extends BaseInfo{

	//���ݱ��
	String fnumber;
	//��������
	String FTransactionTypeID;
	//ҵ������
	Date fbizdate;
	//��Ӧ��
	String FSupplierID;
	//�����֯
	String FStorageOrgUnitID;
	//����״̬
	String FBaseStatus;
	//���ʽ
	String FPaymentTypeID;
	//�ұ�
	String FCurrencyID;
	//����
	String FExchangeRate;
	//����
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
