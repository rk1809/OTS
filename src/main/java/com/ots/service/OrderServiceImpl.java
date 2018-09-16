package com.ots.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.ots.common.OrderSummaryBean;
import com.ots.common.PaymentBean;
import com.ots.common.ReportOilBean;
import com.ots.dao.OrderDaoImpl;
import com.ots.dao.PaymentDaoImpl;
import com.ots.dao.PlacesDaoImpl;


@Service
public class OrderServiceImpl {

	@Autowired
	private OrderDaoImpl orderDaoImpl;

	@Autowired
	private PaymentDaoImpl paymentDaoImpl;
	
	@Autowired
	private PlacesDaoImpl placesDaoImpl;

	public List<OrderSummaryBean> fetchAllOrders(String clientId) {
		return orderDaoImpl.getOrders(clientId);
	}


	public float getTotalAmountToBePaid(List<String> orderIds) {
		float val = orderDaoImpl.getTotalAmountByOrderIds(orderIds);
		System.out.println("===="+val);
		
		return val;
	}
	
	public Float[]  getTotalAmountToBePaid(String clientId, List<String> orderIds) {
		Float[] quantityAndAmount= new Float[2];
		List<OrderSummaryBean> orders = orderDaoImpl.getOrders(clientId);
		quantityAndAmount[0]=new Float(0);
		quantityAndAmount[1]=new Float(0);
		
		for (OrderSummaryBean orderSummaryBean : orders) {
			if(orderIds.contains(orderSummaryBean.getOrderId().trim()))
			{
				if (orderSummaryBean.getType().equals("buy")) {
					quantityAndAmount[0] -= orderSummaryBean.getQuantity();
				} else {
					quantityAndAmount[0]+= orderSummaryBean.getQuantity();
				}
				if(orderSummaryBean.getPaymentId()!=null && orderSummaryBean.getPaymentId().trim().length()>0)
				{
					if (orderSummaryBean.getType().equals("buy")) {
						quantityAndAmount[1]+= orderSummaryBean.getAmount();
					} else {
						quantityAndAmount[1]-= orderSummaryBean.getAmount();
					}
				}
			}
		}
		return quantityAndAmount;
	}
	
	public Boolean insertPaymentDetails(PaymentBean paymentBean)
	{
		return paymentDaoImpl.insertPaymentDetails(paymentBean);
	}

	public Boolean updateOrderDetails(OrderSummaryBean orderSummaryBean)
	{
		return orderDaoImpl.updateOrderDetails(orderSummaryBean);
	}

	public Boolean createOrder(OrderSummaryBean orderSummaryBean)
	{
	return orderDaoImpl.createOrder(orderSummaryBean);
	}
	
	public Boolean insertPlacesRecord(String userID,String  clientID,String  orderID) throws MySQLIntegrityConstraintViolationException
	{
		return placesDaoImpl.insertPlacesRecord(userID, clientID, orderID);
	}
	
	public List<ReportOilBean> getReportOilQty(String type) {
		return orderDaoImpl.getReportDataForType(type);
	}
}
