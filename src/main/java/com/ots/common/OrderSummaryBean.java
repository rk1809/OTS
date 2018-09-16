package com.ots.common;
import java.util.Date;
public class OrderSummaryBean {
	
	private String orderId;
	private Date date;
	private String type;
	private float quantity;
	private int status;
	private float amount;
	private float commissionindollar;
	private float commisisioninoil;
	private String paymentId;
	private boolean isCancelled;
	private String commissionType;
	
	@Override
	public String toString() {
		return "OrderSummaryBean [orderId=" + orderId + ", date=" + date + ", type=" + type + ", quantity=" + quantity
				+ ", status=" + status + ", amount=" + amount + ", commissionindollar=" + commissionindollar
				+ ", commisisioninoil=" + commisisioninoil + ", paymentId=" + paymentId + ", isCancelled=" + isCancelled
				+ ", commissionType=" + commissionType  + "]";
	}
	public boolean isCancelled() {
		return isCancelled;
	}
	public void setCancelled(boolean isCancelled) {
		this.isCancelled = isCancelled;
	}
	public String getCommissionType() {
		return commissionType;
	}
	public void setCommissionType(String commissionType) {
		this.commissionType = commissionType;
	}
	public String getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}

	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public float getQuantity() {
		return quantity;
	}
	
	public void setQuantity(float quantity) {
		this.quantity = quantity;
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public float getAmount() {
		return amount;
	}
	
	public void setAmount(float amount) {
		this.amount = amount;
	}
	
	public float getCommissionindollar() {
		return commissionindollar;
	}
	
	public void setCommissionindollar(float commissionindollar) {
		this.commissionindollar = commissionindollar;
	}
	
	public float getCommisisioninoil() {
		return commisisioninoil;
	}
	public void setCommisisioninoil(float commisisioninoil) {
		this.commisisioninoil = commisisioninoil;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
}
