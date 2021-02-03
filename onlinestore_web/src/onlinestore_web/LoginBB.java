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
	private String name;
	private String role;
	private HttpSession session;

	@Inject
	FacesContext context;

	@Inject
	ExternalContext extcontext;

	@Inject
	Flash flash;

	@EJB
	UserDAO userDAO;

	public HttpSession getSession() {
		return session;
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}

	public User getUser() {
		return user;
	}

	public String getRole() {
		session = (HttpSession) context.getExternalContext().getSession(false);
		return (String) session.getAttribute("role");
	}

	public String getName() {
		session = (HttpSession) context.getExternalContext().getSession(false);
		User user = (User) session.getAttribute("user");
		if(user != null)
			return user.getFirstName();
		return " ";
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
	
	public void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().
                addMessage(null, new FacesMessage(severity, summary, detail));
    }

	public String login() {
		try {
			session = (HttpSession) context.getExternalContext().getSession(true);
			User user1 = userDAO.checkUser(user.getEmail(), user.getPassword());
			if (user1 != null) {
				user = user1;
				session.setAttribute("role", user.getRole());
				session.setAttribute("user", user);
				session.setAttribute("userID", user.getIdUser());
				role = (String) session.getAttribute("role");
				return PAGE_INDEX;
			}
			FacesContext.getCurrentInstance().addMessage("login",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Niepoprawny adres email lub has³o", null));
			FacesContext.getCurrentInstance().addMessage("login", new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Zarejestruj siê, jeœli nie posiadasz jeszcze konta", null));
		} catch (Exception e) {
			e.printStackTrace();
			return PAGE_STAY_AT_THE_SAME;
		}

		return PAGE_STAY_AT_THE_SAME;
	}

	public String logout() {
		session = (HttpSession) context.getExternalContext().getSession(false);
		if (session != null) {
			session.invalidate();
		}
		
		addMessage(FacesMessage.SEVERITY_INFO, "Sukces!", "Zosta³eœ wylogowany.");
		return PAGE_INDEX;
	}

	public List<User> getList() {
		return userDAO.listAllUsers();
	}

}