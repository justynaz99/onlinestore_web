package onlinestore_web;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import javax.servlet.http.HttpServletRequest;

import jsfproject.dao.OrderDAO;
import jsfproject.dao.UserDAO;
import jsfproject.entities.User;

@Named
@RequestScoped
public class StoreBB {

	private static final String PAGE_INDEX = "index?faces-redirect=true";
	private static final String PAGE_LOGIN = "login?faces-redirect=true";
	private static final String PAGE_REGISTRATION = "registration?faces-redirect=true";
	private static final String PAGE_SHOPPING_CART = "shoppingCart?faces-redirect=true";
	private static final String PAGE_ORDERS = "orders?faces-redirect=true";
	private static final String PAGE_USERS = "users?faces-redirect=true";
	private static final String PAGE_MY_ACCOUNT = "myAccount?faces-redirect=true";
	private static final String PAGE_CONFIRM_ORDER = "confirmOrder?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;

	private HttpSession session;

	@Inject
	FacesContext context;

	@Inject
	ExternalContext extcontext;

	@Inject
	Flash flash;

	@EJB
	UserDAO userDAO;
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

	public String shoppingCartPage() {
		return PAGE_SHOPPING_CART;
	}

	public String ordersPage() {
		return PAGE_ORDERS;
	}

	public String usersPage() {
		return PAGE_USERS;
	}

	public String myAccountPage() {
		return PAGE_MY_ACCOUNT;

	}

	public String checkSessionProduct() {
		try {
			session = (HttpSession) context.getExternalContext().getSession(false);
			if (session != null)
				return PAGE_STAY_AT_THE_SAME;
			else {
				FacesContext.getCurrentInstance().addMessage("sessionError",
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Sesja wygas³a!", null));
				return PAGE_INDEX;
		}} catch (Exception e) {
			return PAGE_INDEX;
		}
		
	}
	
	public String checkSession() {
		session = (HttpSession) context.getExternalContext().getSession(false);
		if (session != null)
			return PAGE_STAY_AT_THE_SAME;
		else {
			FacesContext.getCurrentInstance().addMessage("sessionError",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Sesja wygas³a! Zaloguj siê ponownie.", null));
			return PAGE_LOGIN;
		}
	}

	public void addMessage(FacesMessage.Severity severity, String summary, String detail) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
	}

	public String checkRole(String role, String role2) {
		session = (HttpSession) context.getExternalContext().getSession(false);
		try {
			User userFromSession = (User) session.getAttribute("user");
			String roleFromSession = userFromSession.getRole();
			if (roleFromSession.equals(role) || roleFromSession.equals(role2))
				return PAGE_STAY_AT_THE_SAME;
			else {
				addMessage(FacesMessage.SEVERITY_ERROR, "B³¹d!", "Nie posiadasz uprawnieñ do tej strony!");
				return PAGE_INDEX;
			}
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage("userError", new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Zaloguj siê aby uzyskaæ dostêp do tej strony!", null));
			return PAGE_LOGIN;
		}
	}

	public String checkCart() { //checks if there is something in cart to display in confirmOrder.xhtml
		session = (HttpSession) context.getExternalContext().getSession(false);
		User user = (User) session.getAttribute("user");
		try {
			if (orderDAO.cartExists(user))
				return PAGE_CONFIRM_ORDER;
			else {
				addMessage(FacesMessage.SEVERITY_ERROR, "B³¹d!", "Nie mo¿esz przejœæ do tej strony jeœli nic nie znajduje siê w koszyku.");
				return PAGE_INDEX;
				}
		} catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "B³¹d!", "Wyst¹pi³ nieoczekiwany b³¹d.!");
			return PAGE_INDEX;
		}
	}

}
