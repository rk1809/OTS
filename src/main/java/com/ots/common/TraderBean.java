package com.ots.common;
public class TraderBean {
private String roleId;
private String traderId;
public String getRoleId() {
	return roleId;
}
public void setRoleId(String roleId) {
	this.roleId = roleId;
}
public String getTraderId() {
	return traderId;
}
public void setTraderId(String traderId) {
	this.traderId = traderId;
}
@Override
public String toString() {
	return "TraderBean [roleId=" + roleId + ", traderId=" + traderId + "]";
} 
}
