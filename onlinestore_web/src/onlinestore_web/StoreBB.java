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
	private static final String PAGE_LOGIN = "login?faces-redirect=true";
	private static final String PAGE_INDEX = "index?faces-redirect=true";
	private static final String PAGE_INDEX_EDIT = "/public/index";


	public String loginPage() {
		return PAGE_LOGIN;
	}
	
	public String indexPage() {
		return PAGE_INDEX;
	}

}
