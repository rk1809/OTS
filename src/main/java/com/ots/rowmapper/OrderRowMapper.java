package com.ots.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ots.common.OrderSummaryBean;
import com.ots.common.UserBean;


public class OrderRowMapper implements RowMapper<OrderSummaryBean> {

	public OrderSummaryBean mapRow(ResultSet rs, int rowNum) throws SQLException {

		OrderSummaryBean user = new OrderSummaryBean();
		user.setType(rs.getString("type"));
		user.setCommissionindollar(rs.getFloat("commission_fees"));
		user.setCommisisioninoil(rs.getFloat("oil_adjusted_quantity"));
		user.setQuantity(rs.getFloat("quantity"));
		user.setCommissionType(rs.getString("commission_type"));
		user.setAmount(rs.getFloat("total_amt"));
		user.setDate(rs.getDate("date_placed"));
		user.setPaymentId(rs.getString("payment_id"));
		user.setCancelled((rs.getInt("is_not_cancelled")!=1));
		user.setOrderId(rs.getString("id"));
		return user;
	}
}
