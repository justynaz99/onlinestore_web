package onlinestore_web;

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

@Named
@RequestScoped
public class MyAccountBB implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final String PAGE_STAY_AT_THE_SAME = null;

	private User userDataEdit = new User();
	private User userPassEdit = new User();
	private HttpSession session;

	@Inject
	FacesContext context;

	@Inject
	ExternalContext extcontext;

	@Inject
	Flash flash;

	@EJB
	UserDAO userDAO;

	public User getUserDataEdit() {
		session = (HttpSession) context.getExternalContext().getSession(false);
		userDataEdit = (User) session.getAttribute("user");
		return userDataEdit;
	}
	
	public User getUserPassEdit() {
		session = (HttpSession) context.getExternalContext().getSession(false);
		userPassEdit = (User) session.getAttribute("user");
		return userPassEdit;
	}

	public void addMessage(FacesMessage.Severity severity, String summary, String detail) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
	}

	public void saveData() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if (userDataEdit.getIdUser() != null) {
				userDataEdit.setWhoModificated(userDataEdit.getIdUser());
				userDataEdit.setDateModification(Date.valueOf(formatter.format(date)));
				userDAO.merge(userDataEdit);
				addMessage(FacesMessage.SEVERITY_INFO, "Sukces! Dane zosta³y poprawnie edytowane.", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			addMessage(FacesMessage.SEVERITY_ERROR, "B³¹d!",
					"Wyst¹pi³ b³¹d podczas edycji.");

		}
	}
	
	public void savePassword() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if (userPassEdit.getIdUser() != null) {
				userPassEdit.setWhoModificated(userPassEdit.getIdUser());
				userPassEdit.setDateModification(Date.valueOf(formatter.format(date)));
				userDAO.merge(userPassEdit);
				addMessage(FacesMessage.SEVERITY_INFO, "Sukces! Dane zosta³y poprawnie edytowane.", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			addMessage(FacesMessage.SEVERITY_ERROR, "B³¹d!",
					"Wyst¹pi³ b³¹d podczas edycji.");

		}
	}
}