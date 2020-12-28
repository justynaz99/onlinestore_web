package onlinestore_web;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import jsfproject.dao.UserDAO;
import jsfproject.entities.User;
import javax.servlet.http.HttpServletRequest;

@Named
@RequestScoped
public class StoreBB {

	@Inject
	FacesContext ctx;

	@EJB
	UserDAO userDAO;
	private static final String PAGE_INDEX = "index?faces-redirect=true";
	private static final String PAGE_LOGIN = "login?faces-redirect=true";
	private static final String PAGE_REGISTRATION = "registration?faces-redirect=true";
	private static final String PAGE_SHOPPING_CART = "shoppingCart?faces-redirect=true";
	private static final String PAGE_ORDERS = "orders?faces-redirect=true";
	private static final String PAGE_INDEXX = "/public/index";

	public String indexPage() {
		return PAGE_INDEX;
	}

	public String loginPage() {
		return PAGE_LOGIN;
	}
	
	public String registrationPage() {
		return PAGE_REGISTRATION;
	}
	
	public String shoppingCartPage() {
		return PAGE_SHOPPING_CART;
	}
	
	public String ordersPage() {
		return PAGE_ORDERS;
	}
	

}
