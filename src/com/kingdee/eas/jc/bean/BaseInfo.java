package com.kingdee.eas.jc.bean;

public abstract class BaseInfo {

	/** ���� */
	String fid;
	/** ���Ƶ�Ԫ */
	String FControlUnitID;
	/** ������ */
	String FCreatorID;
	/** �޸��� */
	String FLastUpdateUserID;
	
	public String getFid() {
		return fid;
	}
	public void setFid(String fid) {
		this.fid = fid;
	}
	public String getFControlUnitID() {
		return FControlUnitID;
	}
	public void setFControlUnitID(String fControlUnitID) {
		FControlUnitID = fControlUnitID;
	}
	public String getFCreatorID() {
		return FCreatorID;
	}
	public void setFCreatorID(String fCreatorID) {
		FCreatorID = fCreatorID;
	}
	public String getFLastUpdateUserID() {
		return FLastUpdateUserID;
	}
	public void setFLastUpdateUserID(String fLastUpdateUserID) {
		FLastUpdateUserID = fLastUpdateUserID;
	}
	public abstract String getBOStype();
	
	public abstract String getTableName();
}