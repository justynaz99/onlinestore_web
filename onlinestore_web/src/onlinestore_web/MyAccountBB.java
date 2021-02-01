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

	private User user = new User();
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
		user = (User) session.getAttribute("user");
		return user;
	}

	public void addMessage(FacesMessage.Severity severity, String summary, String detail) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
	}

	public void saveData() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if (user.getIdUser() != null) {
				user.setWhoModificated(user.getIdUser());
				user.setDateModification(Date.valueOf(formatter.format(date)));
				userDAO.merge(user);
				addMessage(FacesMessage.SEVERITY_INFO, "Sukces!", "Dane zosta³y poprawnie edytowane. ");
			}
		} catch (Exception e) {
			e.printStackTrace();
			addMessage(FacesMessage.SEVERITY_ERROR, "Wyst¹pi³ b³¹d podczas edycji.",
					"Wyst¹pi³ b³¹d podczas zapisu rekordu.");

		}
	}

}