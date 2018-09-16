package com.ots.common;
import java.util.Date;
public class PaymentBean {
	private String paymentId;
	private String clientId;
	private Date dateAccepted;
	private String traderId;
	private float amount;
	private float balance;
	public String getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public Date getDateAccepted() {
		return dateAccepted;
	}
	public void setDateAccepted(Date dateAccepted) {
		this.dateAccepted = dateAccepted;
	}
	public String getTraderId() {
		return traderId;
	}
	public void setTraderId(String traderId) {
		this.traderId = traderId;
	}
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	public float getBalance() {
		return balance;
	}
	public void setBalance(float balance) {
		this.balance = balance;
	}
	@Override
	public String toString() {
		return "PaymentBean [paymentId=" + paymentId + ", clientId=" + clientId + ", dateAccepted=" + dateAccepted
				+ ", traderId=" + traderId + ", amount=" + amount + ", balance=" + balance + "]";
	}
}
