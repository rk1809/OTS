package com.ots.common;
import java.util.Date;

public class ClientBean {
	private String clientId;
	private Date dateOfLevelUpgrade;
	private float totalOilQuantity;
	private float balanceAmount;
	private String level;
	UserBean userBean ;
	
	public Date getDateOfLevelUpgrade() {
		return dateOfLevelUpgrade;
	}

	public void setDateOfLevelUpgrade(Date dateOfLevelUpgrade) {
		this.dateOfLevelUpgrade = dateOfLevelUpgrade;
	}

	public float getTotalOilQuantity() {
		return totalOilQuantity;
	}

	public void setTotalOilQuantity(float totalOilQuantity) {
		this.totalOilQuantity = totalOilQuantity;
	}

	public float getBalanceAmount() {
		return balanceAmount;
	}

	public void setBalanceAmount(float balanceAmount) {
		this.balanceAmount = balanceAmount;
	}

	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public UserBean getUserBean() {
		return userBean;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}
	@Override
	public String toString() {
		return "ClientBean [clientId=" + clientId + ", dateOfLevelUpgrade=" + dateOfLevelUpgrade + ", totalOilQuantity="
				+ totalOilQuantity + ", balanceAmount=" + balanceAmount + ", level=" + level + ", userBean=" + userBean
				+ "]";
	}
}
