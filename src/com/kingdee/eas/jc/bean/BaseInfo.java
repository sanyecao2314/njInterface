package com.kingdee.eas.jc.bean;

public abstract class BaseInfo {

	/** 主键 */
	String fid;
	/** 控制单元 */
	String FControlUnitID = "I6iJIAEXEADgAHydChkaBsznrtQ=";
	/** 创建人 */
	String FCreatorID = "sO/+CwEhEADgBRL8rBAAfRO33n8=";
	/** 修改人 */
	String FLastUpdateUserID = "sO/+CwEhEADgBRL8rBAAfRO33n8=";
	
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