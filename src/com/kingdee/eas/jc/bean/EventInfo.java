package com.kingdee.eas.jc.bean;

/**
 * 事件表实体
 * @author fans
 *
 */
public class EventInfo {
	private String eventid;
	private String objectkey;
	private String objectname;
	private String objectfunction;
	private String eventpriority;
	private String eventstatus;
	private String connectorID;
	public String getEventid() {
		return eventid;
	}
	public void setEventid(String eventid) {
		this.eventid = eventid;
	}
	public String getObjectkey() {
		return objectkey;
	}
	public void setObjectkey(String objectkey) {
		this.objectkey = objectkey;
	}
	public String getObjectname() {
		return objectname;
	}
	public void setObjectname(String objectname) {
		this.objectname = objectname;
	}
	public String getObjectfunction() {
		return objectfunction;
	}
	public void setObjectfunction(String objectfunction) {
		this.objectfunction = objectfunction;
	}
	public String getEventpriority() {
		return eventpriority;
	}
	public void setEventpriority(String eventpriority) {
		this.eventpriority = eventpriority;
	}
	public String getEventstatus() {
		return eventstatus;
	}
	public void setEventstatus(String eventstatus) {
		this.eventstatus = eventstatus;
	}
	public String getConnectorID() {
		return connectorID;
	}
	public void setConnectorID(String connectorID) {
		this.connectorID = connectorID;
	}

	
}
