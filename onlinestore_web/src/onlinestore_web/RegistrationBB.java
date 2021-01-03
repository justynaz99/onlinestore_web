package onlinestore_web;

import java.io.IOException;
import java.io.Serializable;

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
	private User loaded = null;

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

//	public void onLoad() throws IOException {
//		
//		loaded = (User) flash.get("user");
//
//		if (loaded != null) {
//			user = loaded;
//		} else {
//			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "B��dne u�ycie systemu", null));
//		}
//		
//
//	}

	public String registration() {

//		if (loaded == null) {
//			return PAGE_STAY_AT_THE_SAME;
//		}

		try {
			userDAO.create(user);

		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wyst�pi� b��d podczas zapisu podczas zapisu", null));
			return PAGE_STAY_AT_THE_SAME;
		}

		return PAGE_INDEX;
	}

}