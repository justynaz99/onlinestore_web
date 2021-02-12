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

import jsfproject.dao.OrderDAO;
import jsfproject.dao.OrderPositionDAO;
import jsfproject.dao.OrderStatusDAO;
import jsfproject.dao.ProductDAO;
import jsfproject.entities.Order;
import jsfproject.entities.OrderPosition;
import jsfproject.entities.OrderStatus;
import jsfproject.entities.Product;
import jsfproject.entities.User;

@Named
@RequestScoped
public class ProductBB implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String PAGE_STAY_AT_THE_SAME = null;
	private static final String PAGE_PRODUCT = "product?faces-redirect=true";

	private Product product = new Product();
	private String name;
	private HttpSession session;
	private Product loaded;

	@Inject
	FacesContext context;

	@Inject
	ExternalContext extcontext;

	@Inject
	Flash flash;

	@EJB
	ProductDAO productDAO;
	@EJB
	OrderDAO orderDAO;
	@EJB
	OrderPositionDAO orderPositionDAO;
	@EJB
	OrderStatusDAO orderStatusDAO;

	public Product getProduct() {
		return product;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().
                addMessage(null, new FacesMessage(severity, summary, detail));
    }

	public void onLoad() {
		session = (HttpSession) context.getExternalContext().getSession(false);
		loaded = (Product) session.getAttribute("product");
		if (loaded != null) {
			product = loaded;
		} else {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "B³¹d", null));
		}
	}

	public boolean getInStock(Product product) {
		session = (HttpSession) context.getExternalContext().getSession(false);
		loaded = (Product) session.getAttribute("product");
		if (loaded != null) {
			product = loaded;
			int quantity = productDAO.find(product.getIdProduct()).getAvailableQuantity();
			if (quantity > 0)
				return true;
			else if (quantity == 0)
				return false;
			else
				return false;
		} else {
			return false;
		}

	}
}