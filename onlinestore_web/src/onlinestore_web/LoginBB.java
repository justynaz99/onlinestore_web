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
	


	@Inject
	FacesContext context;

	@Inject
	ExternalContext extcontext;

	@Inject
	Flash flash;
	
	@EJB
	UserDAO userDAO;
	
	
	
	public User getUser() {
		return user;
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
		
		User user1 = userDAO.checkUser(user.getUsername(), user.getPassword());
		if (user1 != null) {
			user = user1;
			HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
			session.setAttribute("role", user.getRole());
			session.setAttribute("user", user);
			session.setAttribute("userID", user.getIdUser());
			return PAGE_INDEX;
		}
			
		
		return PAGE_STAY_AT_THE_SAME;
	}
	
	public String logout() {
		
		HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
		session.setAttribute("role", "guest");
		
		
		return PAGE_INDEX;
	}
	
	public boolean checkIfUser() {
		HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
		if (session.getAttribute("role") != null) {
			return session.getAttribute("role").equals("user");
		}
		else return false;
	}
	public boolean checkIfGuest() {
		HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
		if (session.getAttribute("role") != null) {
			return session.getAttribute("role").equals("guest");
		}
		else return false;
	}
	
	public boolean checkIfSeller() {
		HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
		if (session.getAttribute("role") != null) {
			return session.getAttribute("role").equals("seller");
		}
		else return false;
	}
	
	public boolean checkIfAdmin() {
		HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
		if (session.getAttribute("role") != null) {
			return session.getAttribute("role").equals("admin");
		}
		else return false;
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