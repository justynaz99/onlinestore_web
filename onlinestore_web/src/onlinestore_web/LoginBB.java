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

import jsfproject.dao.UserDAO;
import jsfproject.entities.Order;
import jsfproject.entities.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@Named
@RequestScoped
public class LoginBB implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final String PAGE_INDEX = "index?faces-redirect=true";
	private static final String PAGE_LOGIN = "login?faces-redirect=true";
	private static final String PAGE_REGISTRATION = "registration?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;

	private User user = new User();
	private String role;

	@Inject
	FacesContext context;

	@Inject
	ExternalContext extcontext;

	@Inject
	Flash flash;

	@EJB
	UserDAO userDAO;

	private static HttpSession session;

	public User getUser() {
		return user;
	}

	public static HttpSession getSession() {
		return session;
	}

	public String indexPage() {
		return PAGE_INDEX;
	}

	public String loginPage() {
		return PAGE_LOGIN;
	}

	public String registrationPage() {
		return PAGE_REGISTRATION;
	}

	public String login() {
		session = (HttpSession) context.getExternalContext().getSession(true);
		User user1 = userDAO.checkUser(user.getUsername(), user.getPassword());
		if (user1 != null) {
			user = user1;

			session.setAttribute("role", user.getRole());
			session.setAttribute("user", user);
			session.setAttribute("userID", user.getIdUser());
			role = (String) session.getAttribute("role");
			return PAGE_INDEX;
		}

		return PAGE_STAY_AT_THE_SAME;
	}

	public String logout() {

		if (session != null) {
			session.invalidate();
			session.setAttribute("role", null);
			session.setAttribute("user", null);
			session.setAttribute("userID", null);
		}

		return PAGE_INDEX;
	}
	
	public String getRole() {
		return (String) session.getAttribute("role");
	}

	public boolean checkIfUser() {
		if (session != null) {
			if (session.getAttribute("role") != null)
				return session.getAttribute("role").equals("user");

		}
		return false;
	}

	public boolean checkIfSeller() {

		if (session != null && session.getAttribute("role") != null) {
			return session.getAttribute("role").equals("seller");
		} else
			return false;
	}

	public boolean checkIfNotSeller() {

		if (session != null && session.getAttribute("role") != null) {
			return !session.getAttribute("role").equals("seller");
		} else
			return true;
	}

	public boolean checkIfAdmin() {

		if (session != null && session.getAttribute("role") != null) {
			return session.getAttribute("role").equals("admin");
		} else
			return false;
	}

	public boolean checkIfGuest() {
		if (session == null || session.getAttribute("role") == null) {
			return true;
		} else
			return false;
	}

	public List<User> getList() {
		return userDAO.listAllUsers();
	}

	public boolean checkIfUserEmpty() {
		return getList().isEmpty();
	}

	public boolean checkIfUserNotEmpty() {
		return !getList().isEmpty();
	}

}