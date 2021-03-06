package com.ots.controller;

import java.util.Arrays;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Calendar;
import java.util.List;
import java.util.List;
import java.util.ResourceBundle;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndView;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.ots.common.ClientBean;
import com.ots.common.ClientBean;
import com.ots.common.LoginBean;
import com.ots.common.LoginBean;
import com.ots.common.OrderSummaryBean;
import com.ots.common.OrderSummaryBean;
import com.ots.common.PaymentBean;
import com.ots.common.PaymentBean;
import com.ots.common.PlaceOrderBean;
import com.ots.common.PlaceOrderBean;
import com.ots.common.ReportOilBean;
import com.ots.common.TraderBean;
import com.ots.common.TraderBean;
import com.ots.common.UserBean;
import com.ots.common.UserBean;
import com.ots.service.OrderServiceImpl;
import com.ots.service.OrderServiceImpl;
import com.ots.service.UserManagementServiceImpl;
import com.ots.service.UserManagementServiceImpl;

@Controller
public class HomeController {

	final static Logger logger = Logger.getLogger(HomeController.class);

	@Autowired
	private UserManagementServiceImpl userManagementServiceImpl;

	@Autowired
	private OrderServiceImpl orderSerivceImpl;

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public ModelAndView home(ModelMap model, HttpServletRequest request) {
		
		if (request.getSession().getAttribute("user") != null) {
			if (request.getSession().getAttribute("selectedClient") != null) {
				return loadOrderSummaries(model, request);
			} else {
				return new ModelAndView("searchUser");
			}
		} else {
			return new ModelAndView("loginInput");
		}
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ModelAndView login(ModelMap model, HttpServletRequest request, @ModelAttribute LoginBean loginBean) {
		logger.debug("loginBean= " + loginBean);
		model.addAttribute("userName", loginBean.getUserName());
		UserBean user = userManagementServiceImpl.validateAndFetchUserDetails(loginBean.getUserName(),
				loginBean.getPassword());
		logger.debug("userObject found " + user);

		ResourceBundle bundle = ResourceBundle.getBundle("config");
		request.getSession().setAttribute("oil_price", Integer.parseInt(bundle.getString("oil_price").trim()));

		if (user != null) {
			List<String> features = null;
			request.getSession().setAttribute("user", user);
			ClientBean clientBean = userManagementServiceImpl.getClientDetails(user.getId());

			if (clientBean != null) {
				request.getSession().setAttribute("selectedClient", clientBean);
				logger.debug("settting===?>" + request.getSession().getAttribute("selectedClient"));
				features = userManagementServiceImpl.getClientFeatureCodes(clientBean.getClientId());
				setFeaturesInSession(request, features);
				return loadOrderSummaries(model, request);

			} else {
				TraderBean traderBean = userManagementServiceImpl.getTraderDetails(user.getId());
				if (traderBean == null) {
					model.addAttribute("message",
							"User has not been properly set up yet. Please contact administrator");
					return new ModelAndView("loginInput");
				} else {
					features = userManagementServiceImpl.getTraderFeatureCodes(traderBean.getRoleId());
					setFeaturesInSession(request, features);
					return new ModelAndView("searchUser");
				}
			}
		} else {
			model.addAttribute("message", "Please check username/password");
			return new ModelAndView("loginInput");
		}

	}
	private void setFeaturesInSession(HttpServletRequest request, List<String> features) {
		if (features != null && features.size() != 0) {
			for (String feature : features) {
				request.getSession().setAttribute(feature, "true");
			}
		}
	}
	@RequestMapping(value = "/editProfile", method = RequestMethod.GET)
	public ModelAndView editProfile(ModelMap model, HttpServletRequest request) {
		UserBean userToBeEdited = (UserBean) request.getSession().getAttribute("user");
		userToBeEdited.setPassword(null);
		model.addAttribute("userToBeEdited", userToBeEdited);
		return new ModelAndView("createUser");
	}
	@RequestMapping(value = "/searchUser", method = RequestMethod.POST)
	public String searchUserResult(ModelMap model, @ModelAttribute UserBean searchUserBean) {
		logger.debug("searchUserBean= " + searchUserBean);

		List<UserBean> usersFromDb = userManagementServiceImpl.searchUser(searchUserBean);
		logger.debug("usersFromDb" + usersFromDb);
		model.addAttribute("usersFromDb", usersFromDb);
		return ("searchUserResult");
	}

	@RequestMapping(value = "/selectUser", method = RequestMethod.GET)
	public ModelAndView selectUser(ModelMap model, HttpServletRequest request,
			@RequestParam(required = false) String userId) {
		logger.debug("searchUserBean= " + userId);
		ClientBean bean = userManagementServiceImpl
				.getClientDetails(userManagementServiceImpl.getUserDetails(userId).getId());
		request.getSession().setAttribute("selectedClient", bean);
		model.addAttribute("userId", userId);
		return loadOrderSummaries(model, request);
	}

	@RequestMapping(value = "/insertOrUpdateUser", method = RequestMethod.POST)
	public ModelAndView insertOrUpdateUser(ModelMap model, HttpServletRequest request,
			@ModelAttribute UserBean userToBeInsertedOrUpdated) {
		logger.debug("userToBeInsertedOrUpdated= " + userToBeInsertedOrUpdated);

		model.addAttribute("userToBeEdited", userToBeInsertedOrUpdated);
		if (!userToBeInsertedOrUpdated.getPassword1().equals(userToBeInsertedOrUpdated.getPassword2())) {
			model.addAttribute("message", " Please make sure both the passwords match");
			return new ModelAndView("createUser");
		}

		userToBeInsertedOrUpdated.setPassword(userToBeInsertedOrUpdated.getPassword1());
		Boolean result = userManagementServiceImpl.insertUser(userToBeInsertedOrUpdated);
		logger.debug("result" + result);
		if (result == null || !result) {
			model.addAttribute("message", " Error occured while saving details. Please relogin");

			return new ModelAndView("createUser");
		} else {
			model.addAttribute("message", " User Created successfully.");
		}
		/* } */

		model.addAttribute("message", " User Added/Updated Successfully");
		return loadOrderSummaries(model, request);
	}

	@RequestMapping(value = "/cancelOrder", method = RequestMethod.GET)
	public ModelAndView cancelOrder(ModelMap model, @RequestParam(required = false) String orderIds,
			HttpServletRequest request) {
		logger.debug("searchUserBean= " + orderIds + " : ");

		ClientBean clientBean = (ClientBean) request.getSession().getAttribute("selectedClient");
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		try {
			int count = 0;
			for (String orderId : orderIds.split(",")) {
				if (orderId != null) {
					count++;
					userManagementServiceImpl.insertIntoCancel(user, clientBean, orderId);
					model.addAttribute("message", "Congratulations! Payment cancellation was successful");
				}
			}
			if (count == 0) {
				model.addAttribute("message", "Please select orders you want to Cancel");
				return new ModelAndView("orderSummary");

			}

			Float userAcountOilQty = clientBean.getTotalOilQuantity();
			Float userBalanceAmount = clientBean.getBalanceAmount();
			Float[] quants = orderSerivceImpl.getTotalAmountToBePaid(clientBean.getClientId(),
					Arrays.asList(orderIds.split(",")));

			userManagementServiceImpl.updateOilAndBalanceOfClient(clientBean.getClientId(),
					(userBalanceAmount + quants[1]), (userAcountOilQty + quants[0]));
		
			clientBean = userManagementServiceImpl.getClientDetails(clientBean.getUserBean().getId());
			request.getSession().setAttribute("selectedClient", clientBean);

		} catch (MySQLIntegrityConstraintViolationException mse) {
			mse.printStackTrace();
			model.addAttribute("message", "Exception occured while deleting, please logout and try again.");
		}

		return loadOrderSummaries(model, request);
	}
	@RequestMapping(value = "/payment", method = RequestMethod.GET)
	public String makePayment(ModelMap model, HttpServletRequest request,
			@RequestParam(required = false) String orderIds) {
		logger.debug("orderIds= " + orderIds);
		int count = 0;
		for (String string : orderIds.split(",")) {
			logger.debug("-->" + string);
			if (string.trim().length() != 0) {
				count++;
			}
		}
		if (count == 0) {
			model.addAttribute("message", "Please select orders you want to Cancel");
			return "orderSummary";

		}

		request.getSession().setAttribute("orderIds", orderIds);
		ClientBean clientBean = (ClientBean) request.getSession().getAttribute("selectedClient");
		Long orderCost = new Float(orderSerivceImpl.getTotalAmountToBePaid(Arrays.asList(orderIds.split(","))) * 100
				+ clientBean.getBalanceAmount() * 100).longValue();

		if (orderCost > 0L) {
			logger.debug("orders_total_Amout: " + orderCost);
			model.addAttribute("amountDue", orderCost);
			model.addAttribute("balAmount", clientBean.getBalanceAmount() + "$");
			return ("payment");
		} else {
			model.addAttribute("message", "There is no amount due for the selected transactions");
			return "orderSummary";
		}
	}

	@RequestMapping(value = "/paymentAccepted", method = RequestMethod.POST)
	public String paymentAccepted(ModelMap model, HttpServletRequest request) {

		// Check if user has appropriate role or not if user does not has
		// CANCEL_ORDER feature access, reject and log user out
		List<String> orderIds = Arrays.asList((String) request.getSession().getAttribute("orderIds"));
		PaymentBean paymentBean = new PaymentBean();
		String payId = UUID.randomUUID().toString();
		paymentBean.setPaymentId(payId);
		ClientBean clientBean = (ClientBean) request.getSession().getAttribute("selectedClient");
		UserBean user = (UserBean) request.getSession().getAttribute("user");

		paymentBean.setClientId(clientBean.getClientId());
		paymentBean.setTraderId(user.getId());
		paymentBean.setDateAccepted(Calendar.getInstance().getTime());
		paymentBean.setAmount((orderSerivceImpl.getTotalAmountToBePaid(orderIds) + clientBean.getBalanceAmount()));
		logger.debug(paymentBean);

		orderSerivceImpl.insertPaymentDetails(paymentBean);

		for (String orderId : orderIds) {
			OrderSummaryBean orderSummaryBean = new OrderSummaryBean();
			if (orderId.trim().length() != 0) {
				orderSummaryBean.setOrderId(orderId);
				orderSummaryBean.setPaymentId(payId);
				logger.debug("updating " + orderSummaryBean);
				orderSerivceImpl.updateOrderDetails(orderSummaryBean);
			}
		}

		userManagementServiceImpl.updateOilAndBalanceOfClient(clientBean.getClientId(), new Float(0.0),
				clientBean.getTotalOilQuantity());
		clientBean = userManagementServiceImpl.getClientDetails(clientBean.getUserBean().getId());
		request.getSession().setAttribute("selectedClient", clientBean);

		model.addAttribute("message", "Congratulations! Payment  was successful");

		logger.debug("Stripe token= " + request.getSession().getAttribute("stripeToken"));
		logger.debug("Stripe type= " + request.getSession().getAttribute("stripeTokenType"));
		return "redirect:/";
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(HttpServletRequest request, ModelMap map) {
		return "home";
	}


	@RequestMapping(value = "/topMenu", method = RequestMethod.GET)
	public ModelAndView logout(ModelMap model) {
		return new ModelAndView("heading");
	}


	@RequestMapping(value = "/loadOrders", method = RequestMethod.GET)
	public ModelAndView loadOrders(ModelMap model, HttpServletRequest request) {
		return loadOrderSummaries(model, request);
	}

	private ModelAndView loadOrderSummaries(ModelMap model, HttpServletRequest request) {
		ClientBean clientBean = (ClientBean) request.getSession().getAttribute("selectedClient");
		System.out.println(clientBean);

		float commission = userManagementServiceImpl.getCommission(clientBean);
		System.out.println("commission"+commission);
		request.getSession().setAttribute("commission", commission*100);
		request.getSession().removeAttribute("orderIds");
		List<OrderSummaryBean> orders = orderSerivceImpl.fetchAllOrders(clientBean.getClientId());
		logger.debug(orders);
		model.addAttribute("orders", orders);
		return new ModelAndView("orderSummary");
	}

	@RequestMapping(value = "/createUser", method = RequestMethod.GET)
	public String createUser(ModelMap model) {
		return "createUser";

	}

	@RequestMapping(value = "/createOrder", method = RequestMethod.POST)
	public String createOrder(ModelMap model) {
		return "placeorder";

	}

	@RequestMapping(value = "/placeOrder", method = RequestMethod.POST)
	public ModelAndView placeOrder(ModelMap model, HttpServletRequest request,
			@ModelAttribute PlaceOrderBean placeOrderBean) {
		logger.debug("order created= " + placeOrderBean);

		ClientBean clientBean = (ClientBean) request.getSession().getAttribute("selectedClient");
		OrderSummaryBean orderSummaryBean = new OrderSummaryBean();

		String ordId = UUID.randomUUID().toString();
		orderSummaryBean.setOrderId(ordId);
		orderSummaryBean.setDate(Calendar.getInstance().getTime());
		orderSummaryBean.setType(placeOrderBean.getType());
		orderSummaryBean.setQuantity(placeOrderBean.getQuantity());
		orderSummaryBean.setCommissionType(placeOrderBean.getCommissionType());
		float commission = userManagementServiceImpl.getCommission(clientBean);
		System.out.println("commission"+commission);
		orderSummaryBean
				.setAmount(placeOrderBean.getQuantity() * ((Integer) request.getSession().getAttribute("oil_price")));

		if (placeOrderBean.getCommissionType().equals("Oil")) {
			orderSummaryBean.setCommisisioninoil((float) orderSummaryBean.getQuantity() * (float) commission);
			orderSummaryBean.setCommissionindollar(new Float(0.0));
		} else {
			orderSummaryBean.setCommissionindollar(orderSummaryBean.getAmount() * (float) commission);
			orderSummaryBean.setCommisisioninoil(new Float(0.0));
		}

		orderSummaryBean.setQuantity((orderSummaryBean.getQuantity() - orderSummaryBean.getCommisisioninoil()));
		orderSummaryBean.setAmount((orderSummaryBean.getAmount() + orderSummaryBean.getCommissionindollar()));

		logger.debug("after applying commission" + orderSummaryBean);
		request.getSession().setAttribute("commission", 100*commission);
		orderSerivceImpl.createOrder(orderSummaryBean);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		try {
			orderSerivceImpl.insertPlacesRecord(user.getId(), clientBean.getClientId(), ordId);

			Float userAcountOilQty = clientBean.getTotalOilQuantity();
			if (placeOrderBean.getType().equals("buy")) {
				userAcountOilQty += orderSummaryBean.getQuantity();
			} else {
				userAcountOilQty -= orderSummaryBean.getQuantity();
			}
			userManagementServiceImpl.updateOilAndBalanceOfClient(clientBean.getClientId(),
					clientBean.getBalanceAmount(), userAcountOilQty);
			clientBean = userManagementServiceImpl.getClientDetails(clientBean.getUserBean().getId());
			request.getSession().setAttribute("selectedClient", clientBean);

		} catch (MySQLIntegrityConstraintViolationException mse) {
			model.addAttribute("message", "Exception occured while deleting, please logout and try again.");
		}
		return loadOrderSummaries(model, request);

	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(ModelMap modesetttingl, HttpServletRequest request, @ModelAttribute LoginBean loginBean) {
		return logUserOut(request);
	}

	@RequestMapping(value = "/viewReports", method = RequestMethod.GET)
	public String reports(ModelMap map, HttpServletRequest request) {

		map.addAttribute("chart1Json", getChartJson("Quantity", orderSerivceImpl.getReportOilQty("quantity")));
		map.addAttribute("chart2Json", getChartJson("Total Amount", orderSerivceImpl.getReportOilQty("total_amt")));
		map.addAttribute("chart3Json",
				getChartJson("Total Oil Commission", orderSerivceImpl.getReportOilQty("oil_adjusted_quantity")));
		map.addAttribute("chart4Json",
				getChartJson("Total Commission Fees", orderSerivceImpl.getReportOilQty("commission_fees")));
		return "reportsView";
	}

	public String getChartJson(String type, List<ReportOilBean> rbean) {
		String chart1Json;
		double paymentDone = 0;
		double paymentCancelledValue = 0;
		double paymentPendingValue = 0;
		for (ReportOilBean rb : rbean) {
			// done
			if (rb.getPayment_avl() && !rb.getIs_cancelled()) {
				paymentDone += rb.getSums();
			}
			// pending
			if (!rb.getPayment_avl() && !rb.getIs_cancelled()) {
				paymentPendingValue += rb.getSums();
			}
			// cancelled
			if (rb.getIs_cancelled()) {
				paymentCancelledValue += rb.getSums();
			}
		}

		chart1Json = getJson(paymentDone, paymentCancelledValue, paymentPendingValue, type);
		return chart1Json;
	}

	public String getJson(double paymentDone, double paymentCancelledValue, double paymentPendingValue, String type) {
		return "{	\"cols\": [{		\"id\": \"\",		\"label\": \"Payment Info\",		\"pattern\": \"\",		\"type\": \"string\"	}, {		\"id\": \"\",		\"label\": \""
				+ type
				+ "\",		\"pattern\": \"\",		\"type\": \"number\"	}],	\"rows\": [{		\"c\": [{			\"v\": \"Payment Done\",			\"f\": null		}, {			\"v\": "
				+ paymentDone
				+ ",			\"f\": null		}]	}, {		\"c\": [{			\"v\": \"Payment Pending\",				\"f\": null		}, {			\"v\": "
				+ paymentPendingValue
				+ ",			\"f\": null		}]	}, {		\"c\": [{			\"v\": \"Payment Cancelled\",			\"f\": null		}, {			\"v\": "
				+ paymentCancelledValue + ",			\"f\": null		}]	}]}";
	}

	private String logUserOut(HttpServletRequest request) {
		request.getSession().invalidate();
		logger.debug("loggingout");
		return "redirect:/";
	}

}
