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
import jsfproject.dao.ProductDAO;
import jsfproject.entities.Product;


@Named
@RequestScoped
public class ProductListBB implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private static final String PAGE_STAY_AT_THE_SAME = null;
	private Product product = new Product();
	private String name;
	//pagination
	private Integer page;
	private String stringPage;
	private int lastPage;
	//sorting
	private Map<String, String> availableItems;
	private String selectedItem = "1";

	@Inject
	FacesContext context;

	@Inject
	ExternalContext extcontext;

	@Inject
	Flash flash;

	@EJB
	ProductDAO productDAO;
	
//	public ProductListBB() {
//		availableItems = new HashMap<String, String>();
//		availableItems.put("1", "Po cenie rosn¹co");
//		availableItems.put("2", "Po cenie malej¹co");
//		availableItems.put("3", "Alfabetycznie");
//	}
	
//	public Map<String, String> getAvailableItems() {
//		return availableItems;
//	}
	
	public Product getProduct() {
		return product;
	}
	
	//searching
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	//sorting
	
	public String getSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(String selectedItem) {
		this.selectedItem = selectedItem;
	}
	
	//pagination
	
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
	
	
	
	public List<Product> getList() {
		List<Product> list = null;
		Map<String,Object> searchParams = new HashMap<String, Object>();
		
		if (name != null && name.length() > 0){
			searchParams.put("name", name);
		}
		
		if(selectedItem != null) {
			searchParams.put("selectedItem", selectedItem);
		}
			
		list = productDAO.getList(searchParams);
		return list;
	}
	
	//methods for seller
	
	public String addProduct() {

		try {
			productDAO.create(product);

		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wyst¹pi³ b³¹d podczas dodawania rekordu", null));
			return PAGE_STAY_AT_THE_SAME;
		}

		return PAGE_STAY_AT_THE_SAME;
	}
	
	public String deleteProduct(Product product) {
		productDAO.remove(product);
		return PAGE_STAY_AT_THE_SAME;
	}
	
	//searching
	
	public String search() {
		return PAGE_STAY_AT_THE_SAME;
	}

	public String allProducts() {
		setName("");
		return PAGE_STAY_AT_THE_SAME;
	}
	
	//sorting
	
	public String sort() {
		return PAGE_STAY_AT_THE_SAME;
	}
	
	//pagination

	public String nextPage() {
		if(productDAO.getPage() != productDAO.getLastPage()) {
			productDAO.setOffset(productDAO.getOffset() + productDAO.getQuantity()); 
			productDAO.setPage(productDAO.getPage() + 1);
		}
		return PAGE_STAY_AT_THE_SAME;
	}
	
	public String prevPage() {
		if(productDAO.getPage() != 1) {
			productDAO.setOffset(productDAO.getOffset() - productDAO.getQuantity()); 
			productDAO.setPage(productDAO.getPage() - 1);
		}
		return PAGE_STAY_AT_THE_SAME;
	}
	
	

	
	
	
	
	


	

}