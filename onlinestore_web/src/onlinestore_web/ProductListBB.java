package onlinestore_web;

import java.io.IOException;
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

	private static final String PAGE_INDEX = "index?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;

	private Product product = new Product();
	private String name;
	
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
	
	public String indexPage() {
		return PAGE_INDEX;
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
	

	public List<Product> getList(){
		
		if (name != null && name.length() > 0){
			productDAO.listAllProducts();
		}
		return productDAO.listAllProducts();
	}
	
//	public List<Product> getList(){
//		List<Product> list = null;
//		Map<String,Object> searchParams = new HashMap<String, Object>();
//		
//		if (name != null && name.length() > 0){
//			searchParams.put("name", name);
//		}
//		
//		list = productDAO.getList(searchParams);
//		return list;
//	}
	
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
	
	public boolean checkIfNotFirstPage() {
		if (productDAO.getPage() != 1)
				return true;
		return false;
	}
	
	public boolean checkIfNotLastPage() {
		if (productDAO.getLastPage() != productDAO.getPage())
			return true;
		return false;
	}

	


	

}