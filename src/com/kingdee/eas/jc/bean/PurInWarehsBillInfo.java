package com.kingdee.eas.jc.bean;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import com.kingdee.eas.jc.util.StringUtil;


/**
 * �ɹ���ⵥ��ͷ
 * @author fans.fan
 *
 */
public class PurInWarehsBillInfo extends BaseInfo{

	//���ݱ��
	String fnumber;
	//�������� �ֹ��ɹ����
	String FTransactionTypeID = "0F/edAELEADgABkkwKgSKbAI3Kc=";
	//ҵ������
	Date fbizdate;
	//��Ӧ��
	String FSupplierID;
	//�����֯ ԭ����ҵ��
	String FStorageOrgUnitID = "Fg/3htR2QAy9dbSisTDtxMznrtQ=";
	//����״̬ 1������
	String FBaseStatus = "1";
	//���ʽ �޹�
	String FPaymentTypeID = "2fa35444-5a23-43fb-99ee-6d4fa5f260da6BCA0AB5";
	//�ұ�
	String FCurrencyID;
	//����
	double FExchangeRate = 1d;
	//����
	String FDescription;
	//
	String fbiztypeid = "d8e80652-0106-1000-e000-04c5c0a812202407435C";
	
	//
	String fbilltypeid = "50957179-0105-1000-e000-015fc0a812fd463ED552";
	
	String fyear;
	String fmonth;
	String fday;
	/* �ӱ���Ϣ */
	List<PurInWarehsEntryInfo> lspurInWarehsEntryInfos;
	
	public String getFnumber() {
//		"CGRK-" + 
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
	public double getFExchangeRate() {
		return FExchangeRate;
	}
	public void setFExchangeRate(double fExchangeRate) {
		FExchangeRate = fExchangeRate;
	}

	public String getFDescription() {
		return FDescription;
	}
	public void setFDescription(String fDescription) {
		FDescription = fDescription;
	}
	
	public String getFbiztypeid() {
		return fbiztypeid;
	}
	public void setFbiztypeid(String fbiztypeid) {
		this.fbiztypeid = fbiztypeid;
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
