package onlinestore_web;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Date;
import java.text.SimpleDateFormat;
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
public class UserEditBB implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final String PAGE_INDEX = "index?faces-redirect=true";
	private static final String PAGE_USER_EDIT = "userEdit?faces-redirect=true";
	private static final String PAGE_USER_ADD = "userAdd?faces-redirect=true";
	private static final String PAGE_USERS = "users?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;
	private User user = new User();
	private User loaded = null;
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
		session = (HttpSession) context.getExternalContext().getSession(false);
		loaded = (User) session.getAttribute("userEdit");
		if (loaded != null) {
			user = loaded;
			return user;
		}
		else return null;
	}
	
	public void addMessage(FacesMessage.Severity severity, String summary, String detail) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
	}

	public void onLoad() throws IOException {
		session = (HttpSession) context.getExternalContext().getSession(false);
		loaded = (User) session.getAttribute("userEdit");
		if (loaded != null) {
			user = loaded;
			//session.removeAttribute("userEdit");
		} else {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "B³¹d!", "Nie wybrano u¿ytkownika."));
		}
	}

	public String saveData() {
		session = (HttpSession) context.getExternalContext().getSession(false);
		User admin = (User) session.getAttribute("user");
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if (user.getIdUser() == null) {			
				userDAO.create(user);
			} else {
				user.setDateModification(Date.valueOf(formatter.format(date)));
				user.setWhoModificated(admin.getIdUser());
				userDAO.merge(user);
			}
			addMessage(FacesMessage.SEVERITY_INFO, "Sukces!", "Dane zosta³y poprawnie edytowane. ");
		} catch (Exception e) {
			e.printStackTrace();
			addMessage(FacesMessage.SEVERITY_ERROR, "B³¹d!", "Wyst¹pi³ b³¹d podczas edycji danych. ");
			return PAGE_STAY_AT_THE_SAME;
		}
		session.removeAttribute("userEdit");		
		return PAGE_USERS;
	}


}