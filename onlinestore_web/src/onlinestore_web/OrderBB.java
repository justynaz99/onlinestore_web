package onlinestore_web;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import jsfproject.dao.OrderDAO;
import jsfproject.dao.OrderPositionDAO;
import jsfproject.dao.OrderStatusDAO;
import jsfproject.entities.Order;
import jsfproject.entities.OrderPosition;
import jsfproject.entities.OrderStatus;
import jsfproject.entities.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@Named
@RequestScoped
public class OrderBB implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String PAGE_INDEX = "index?faces-redirect=true";
	private static final String PAGE_LOGIN = "login?faces-redirect=true";
	private static final String PAGE_REGISTRATION = "registration?faces-redirect=true";
	private static final String PAGE_SHOPPING_CART = "shoppingCart?faces-redirect=true";
	private static final String PAGE_ORDERS = "orders?faces-redirect=true";
	private static final String PAGE_CONFIRM_ORDER = "confirmOrder?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;

	private List<Order> orders;
	private HttpSession session;
	private Order cart;
	
	@Inject
	FacesContext context;

	@Inject
	ExternalContext extcontext;

	@Inject
	Flash flash;

	@EJB
	OrderDAO orderDAO;
	@EJB
	OrderPositionDAO orderPositionDAO;
	@EJB
	OrderStatusDAO orderStatusDAO;
	

	public String indexPage() {
		return PAGE_INDEX;
	}

	public String loginPage() {
		return PAGE_LOGIN;
	}

	public String registrationPage() {
		return PAGE_REGISTRATION;
	}

	
	public List<Order> getOrders() {
		List<Order> orders = orderDAO.listAllOrders();
		return orders;
	}
	
	public boolean cartExists() { //checks if there is already an order with status cart for user saved in session
		session = (HttpSession) context.getExternalContext().getSession(false);
		User user = (User) session.getAttribute("user");
		if (orderDAO.cartExists(user))
			return true;
		return false;
	}
	
	public Order getCart() { //returns order with status cart for user saved in session
		session = (HttpSession) context.getExternalContext().getSession(false);
		User user = (User) session.getAttribute("user");
		return orderDAO.getCart(user);
	}
	
	public String orderItemsFromCart() { //changes order status from cart to ordered
		session = (HttpSession) context.getExternalContext().getSession(false);
		if(session != null)
			return PAGE_CONFIRM_ORDER;
		return PAGE_LOGIN;
		
	}


}