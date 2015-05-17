package com.kingdee.eas.jc.bean;

import java.sql.Date;
import java.util.List;

/**
 * ���ϳ��ⵥ��ͷ
 * @author fans.fan
 *
 */
public class MaterialReqBillInfo extends BaseInfo{

	//���ݱ��
	String fnumber;
	//��������
	String FTransactionTypeID;
	//ҵ������
	Date fbizdate;
	//�����֯
	String FStorageOrgUnitID;
	//����״̬
	String FBaseStatus;
	//�ɱ�����
	String FCostCenterOrgUnitID;
	//ҵ������
	String FBizTypeID;
	//����
	String voyage;
	/* �ӱ���Ϣ */
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
	public List<MaterialReqBillEntryInfo> getLsmaterBillEntryInfos() {
		return lsmaterBillEntryInfos;
	}
	public void setLsmaterBillEntryInfos(
			List<MaterialReqBillEntryInfo> lsmaterBillEntryInfos) {
		this.lsmaterBillEntryInfos = lsmaterBillEntryInfos;
	}
	
	
}
