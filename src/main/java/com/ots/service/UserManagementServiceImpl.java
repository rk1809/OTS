package com.ots.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.ots.common.ClientBean;
import com.ots.common.TraderBean;
import com.ots.common.UserBean;
import com.ots.dao.CancelDaoImpl;
import com.ots.dao.ClientDaoImpl;
import com.ots.dao.FeatureDaoImpl;
import com.ots.dao.TraderDaoImpl;
import com.ots.dao.UserDaoImpl;

@Service
public class UserManagementServiceImpl {

	@Autowired
	private UserDaoImpl userDao;

	@Autowired
	private ClientDaoImpl clientDao;

	@Autowired
	private TraderDaoImpl traderDao;

	@Autowired
	private FeatureDaoImpl featureDao;

	@Autowired
	private CancelDaoImpl cancelDao;
	


	public UserBean getUserDetails(final String email) {

		return userDao.getUserDetails(email);
	}

	public UserBean validateAndFetchUserDetails(String userName, String password) {
		UserBean user = userDao.getUserDetails(userName, password);
		if (user != null) {
			user.setPassword(null);
		}
		return user;
	}
	public ClientBean getClientDetails(String id) {
		ClientBean cbean = clientDao.getClientDetails(id);
		if (cbean != null) {
			cbean.setUserBean(userDao.getUserDetailsById(id));
		}
		return cbean;
	}

	public TraderBean getTraderDetails(String traderID) {
		TraderBean tbean = traderDao.getTraderDetails(traderID);
		return tbean;

	}
	public List<String> getClientFeatureCodes(String clientID) {
		List<String> listOfFeatures = featureDao.getClientFeatureCodes(clientID);
		return listOfFeatures;
	}

	public List<String> getTraderFeatureCodes(String roleID) {
		List<String> listOfFeatures = featureDao.getTraderFeatureCodes(roleID);

		return listOfFeatures;
	}

	public Float getCommission(ClientBean client)
	{
		if(client.getLevel().equalsIgnoreCase("Silver"))
		{
			return userDao.getCommissionDetailsById("level2_comm", client.getUserBean().getCompanyId());	
		}
		else
		{
			return userDao.getCommissionDetailsById("level1_comm", client.getUserBean().getCompanyId());
		}
	}
	public Boolean insertUser(UserBean userBean) {

		try {
			userDao.insertUserDetails(userBean);
		} catch (MySQLIntegrityConstraintViolationException mse) {
			return null;
		}
		UserBean bean = userDao.getUserDetails(userBean.getEmailId());
		System.out.println(bean);
		bean.setUserType(userBean.getUserType());
		if (bean != null) {
			switch (userBean.getUserType()) {
			case CLIENT:
				return clientDao.insertClientDetails(bean);
			case TRADER:
			case ADMIN:
				return traderDao.insertTraderDetails(bean);
			}
		}
		return null;
	}

	public List<UserBean> searchUser(final UserBean userBean) {
		return userDao.searchUser(userBean);
	}

	public boolean insertIntoCancel(UserBean ub, ClientBean cb, String orderID)
			throws MySQLIntegrityConstraintViolationException {
		return cancelDao.insertIntoCancel(ub.getId(), cb.getClientId(), orderID);
	}
	
	
	public Boolean updateOilAndBalanceOfClient(final String clientId,final float balanceAmount, final float totalOilQuantity ) {
		
		System.out.println("User Account to be updated "+ clientId+" : "+balanceAmount+ " : "+totalOilQuantity);
	return clientDao.updateOilAndBalanceOfClient(clientId, balanceAmount, totalOilQuantity);
	}
	

}
