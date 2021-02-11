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

import jsfproject.dao.ProductDAO;
import jsfproject.dao.UserDAO;
import jsfproject.entities.User;
import jsfproject.entities.Product;

@Named
@RequestScoped
public class ProductEditBB implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final String PAGE_PRODUCT_EDIT = "productEdit?faces-redirect=true";
	private static final String PAGE_INDEX = "index?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;
	private Product product = new Product();
	private Product loaded = null;
	private HttpSession session;

	@Inject
	FacesContext context;

	@Inject
	ExternalContext extcontext;

	@Inject
	Flash flash;

	@EJB
	UserDAO userDAO;	
	@EJB
	ProductDAO productDAO;

	public Product getProduct() {
		session = (HttpSession) context.getExternalContext().getSession(false);
		loaded = (Product) session.getAttribute("productEdit");
		if (loaded != null) {
			product = loaded;
			return product;
		}
		else return null;
	}
	
	public void addMessage(FacesMessage.Severity severity, String summary, String detail) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
	}

	public void onLoad() throws IOException {
		session = (HttpSession) context.getExternalContext().getSession(false);
		loaded = (Product) session.getAttribute("productEdit");
		if (loaded != null) {
			product = loaded;
		} else {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "B³¹d!", "Nie wybrano produktu."));
		}
	}

	public String saveData() {
//		session = (HttpSession) context.getExternalContext().getSession(false);
//		try {
//			if (product == null) {			
//				productDAO.create(product);
//			} else {
//				productDAO.merge(product);
//			}
//			addMessage(FacesMessage.SEVERITY_INFO, "Sukces!", "Dane zosta³y poprawnie edytowane. ");
//		} catch (Exception e) {
//			e.printStackTrace();
//			addMessage(FacesMessage.SEVERITY_ERROR, "B³¹d!", "Wyst¹pi³ b³¹d podczas edycji danych. ");
//			return PAGE_STAY_AT_THE_SAME;
//		}
//		session.removeAttribute("productEdit");		
		addMessage(FacesMessage.SEVERITY_INFO, "Sukces!", "Produkt zosta³ poprawnie edytowany.");
		return PAGE_INDEX;
	}
	
	

}