package onlinestore_web;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
import jsfproject.entities.Product;
import jsfproject.entities.User;

@Named
@RequestScoped
public class ProductListBB implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String PAGE_STAY_AT_THE_SAME = null;
	private static final String PAGE_PRODUCT = "product?faces-redirect=true";
	private static final String PAGE_PRODUCT_EDIT = "productEdit?faces-redirect=true";

	private Product product = new Product();
	private String name;
	private HttpSession session;
	private boolean inStock;
	
	// pagination
	private Integer page;
	private String stringPage;
	private Integer lastPage;
	private String stringLastPage;
	private List<String> pages;
	
	// sorting
	private String selectedItem = "1";

	@Inject
	FacesContext context;

	@Inject
	ExternalContext extcontext;

	@Inject
	Flash flash;

	@EJB
	ProductDAO productDAO;

	public Product getProduct() {
		return product;
	}

	// searching

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// sorting

	public String getSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(String selectedItem) {
		this.selectedItem = selectedItem;
	}

	// pagination

	public Integer getPage() {
		page = productDAO.getPage();
		return page;
	}

	public String getStringPage() {
		stringPage = page.toString();
		return stringPage;
	}

	public int getLastPage() {
		lastPage = productDAO.getLastPage();
		return lastPage;
	}

	public String getStringLastPage() {
		Integer lastPage = (Integer) productDAO.getLastPage();
		stringLastPage = lastPage.toString();
		return stringLastPage;
	}
	
	//
	
	public boolean getInStock(Product product) {
		if (product != null) {
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

	public List<Product> getList() {
		List<Product> list = null;
		Map<String, Object> searchParams = new HashMap<String, Object>();

		if (name != null && name.length() > 0) {
			searchParams.put("name", name);
		}

		if (selectedItem != null) {
			searchParams.put("selectedItem", selectedItem);
		}

		list = productDAO.getList(searchParams);
		return list;
	}


	public void addMessage(FacesMessage.Severity severity, String summary, String detail) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
	}

	public String showMore(Product product) {
		if (session == null) {
			session = (HttpSession) context.getExternalContext().getSession(true);
		} else {
			session = (HttpSession) context.getExternalContext().getSession(false);
		}
		session.setAttribute("product", product);
		return PAGE_PRODUCT;
	}

	// methods for seller

	public String addProduct() {

		try {
			if (productDAO.findByName(product.getName()).size() == 0) {
				productDAO.create(product);
				addMessage(FacesMessage.SEVERITY_INFO, "Sukces!", "Produkt zosta� dodany.");
			} else {
				addMessage(FacesMessage.SEVERITY_ERROR, "B�ad!", "Istnieje ju� produkt o takiej nazwie.");
			}

		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wyst�pi� b��d podczas dodawania rekordu", null));
			return PAGE_STAY_AT_THE_SAME;
		}

		return PAGE_STAY_AT_THE_SAME;
	}

	public String deleteProduct(Product product) {
		productDAO.remove(product);
		return PAGE_STAY_AT_THE_SAME;
	}

	// searching

	public String search() {
		return PAGE_STAY_AT_THE_SAME;
	}

	public String allProducts() {
		setName("");
		return PAGE_STAY_AT_THE_SAME;
	}

	// sorting

	public String sort() {
		return PAGE_STAY_AT_THE_SAME;
	}

	// pagination

	public String nextPage() {
		if (productDAO.getPage() != productDAO.getLastPage()) {
			productDAO.setOffset(productDAO.getOffset() + productDAO.getQuantity());
			productDAO.setPage(productDAO.getPage() + 1);
		}
		return PAGE_STAY_AT_THE_SAME;
	}

	public String prevPage() {
		if (productDAO.getPage() != 1) {
			productDAO.setOffset(productDAO.getOffset() - productDAO.getQuantity());
			productDAO.setPage(productDAO.getPage() - 1);
		}
		return PAGE_STAY_AT_THE_SAME;
	}

	public String firstPage() {
		productDAO.setOffset(productDAO.getOffset() - (productDAO.getQuantity() * productDAO.getPage()));
		productDAO.setPage(productDAO.getPage() - (productDAO.getPage()) + 1);
		return PAGE_STAY_AT_THE_SAME;
	}

	public String lastPage() {
		productDAO.setOffset(productDAO.getOffset()
				+ (productDAO.getQuantity() * (productDAO.getLastPage() - productDAO.getPage())));
		productDAO.setPage(productDAO.getPage() + (productDAO.getLastPage() - productDAO.getPage()));
		return PAGE_STAY_AT_THE_SAME;
	}

	public String editProduct(Product product) {
		session = (HttpSession) context.getExternalContext().getSession(false);
		session.setAttribute("productEdit", product);
		return PAGE_PRODUCT_EDIT;
	}

}