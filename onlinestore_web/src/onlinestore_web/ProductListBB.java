package onlinestore_web;

import java.io.Serializable;
import java.util.HashMap;
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
	private Integer page;
	private String stringPage;
	private int lastPage;
	List<Product> list = null;
	
	@Inject
	FacesContext context;

	@Inject
	ExternalContext extcontext;

	@Inject
	Flash flash;

	@EJB
	ProductDAO productDAO;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public Product getProduct() {
		return product;
	}
	
	public String deleteProduct(Product product) {
		productDAO.remove(product);
		return PAGE_STAY_AT_THE_SAME;
	}
	
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
	

//	public List<Product> getFullList(){
//		return productDAO.listAllProducts();
//	}

	
	public List<Product> getList() {
		Map<String,Object> searchParams = new HashMap<String, Object>();
		
		if (name != null && name.length() > 0){
			searchParams.put("name", name);
		}
		
		list = productDAO.getList(searchParams);
		return list;
	}
	
	public String search() {
		return PAGE_STAY_AT_THE_SAME;
	}
	
	public String allProducts() {
		setName("");
		return PAGE_STAY_AT_THE_SAME;
	}
	

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
	


	

}