package onlinestore_web;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Date;
import java.text.SimpleDateFormat;

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
import jsfproject.entities.User;
import javax.servlet.http.HttpServletRequest;

@Named
@RequestScoped
public class RegistrationBB implements Serializable {
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

	public String indexPage() {
		return PAGE_INDEX;
	}

	public String loginPage() {
		return PAGE_LOGIN;
	}

	public String registrationPage() {
		return PAGE_REGISTRATION;
	}

	public User getUser() {
		return user;
	}



	public String registration() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			user.setWhoAdded(user.getIdUser());
			user.setDateAdded(Date.valueOf(formatter.format(date)));
			user.setRole("user");
			userDAO.create(user);
			user.setWhoAdded(user.getIdUser());
			userDAO.merge(user);

		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage("login",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wyst¹pi³ b³¹d podczas rejestracji", null));
			return PAGE_STAY_AT_THE_SAME;
		}

		return PAGE_LOGIN;
	}

}