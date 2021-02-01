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
import jsfproject.entities.User;

@Named
@RequestScoped
public class UserBB implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final String PAGE_INDEX = "index?faces-redirect=true";
	private static final String PAGE_USER_EDIT = "userEdit?faces-redirect=true";
	private static final String PAGE_USER_ADD = "userAdd?faces-redirect=true";
	private static final String PAGE_USERS = "users?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;
	private User user = new User();
	private User loaded = null;
	private List<User> list = null;
	private HttpSession session;

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

	public List<User> getList() {
		return list = userDAO.listAllUsers();
	}

	public String editUser(User user) {
		session = (HttpSession) context.getExternalContext().getSession(false);
		session.setAttribute("userEdit", user);
		return PAGE_USER_EDIT;
	}

	public void onLoad() throws IOException {
		session = (HttpSession) context.getExternalContext().getSession(false);
		loaded = (User) session.getAttribute("userEdit");
		if (loaded != null) {
			user = loaded;
		} else {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "B³¹d", null));
		}
	}

	public String saveData() {
		if (loaded == null) {
			return PAGE_STAY_AT_THE_SAME;
		}
		try {
			if (user.getIdUser() == null) {
				userDAO.create(user);
			} else {
				userDAO.merge(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wyst¹pi³ b³¹d podczas zapisu", null));
			return PAGE_STAY_AT_THE_SAME;
		}
		return PAGE_USERS;
	}

	public String addUser() {
		return PAGE_USER_ADD;
	}

	public String saveAddUser() {
		try {
			userDAO.create(user);

		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wyst¹pi³ b³¹d podczas rejestracji", null));
			return PAGE_STAY_AT_THE_SAME;
		}
		return PAGE_USERS;
	}
	
	

}