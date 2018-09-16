package com.ots.common;
public class PlaceOrderBean {
	private String commissionType;
	private float quantity;
	private String type;
	public String getCommissionType() {
		return commissionType;
	}
	public void setCommissionType(String commissionType) {
		this.commissionType = commissionType;
	}
	public float getQuantity() {
		return quantity;
	}
	public void setQuantity(float quantity) {
		this.quantity = quantity;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "PlaceOrderBean [commissionType=" + commissionType + ", quantity=" + quantity + ", type=" + type + "]";
	}		
}
