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
import jsfproject.entities.Order;
import jsfproject.entities.OrderPosition;
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
	private static final String PAGE_STAY_AT_THE_SAME = null;

	Order order = new Order();
	private List<Order> orders;

	@Inject
	FacesContext context;

	@Inject
	ExternalContext extcontext;

	@Inject
	Flash flash;

	@EJB
	OrderDAO orderDAO;

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
	
	public boolean checkIfEmpty() {
		return getOrders().isEmpty();
	}

	public boolean checkIfNotEmpty() {
		return !getOrders().isEmpty();
	}

}