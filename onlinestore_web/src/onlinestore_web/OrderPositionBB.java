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
import javax.servlet.http.HttpSession;

import jsfproject.dao.OrderPositionDAO;
import jsfproject.dao.ProductDAO;
import jsfproject.entities.OrderPosition;
import jsfproject.entities.Product;
import javax.servlet.http.HttpServletRequest;

@Named
@RequestScoped
public class OrderPositionBB implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final String PAGE_INDEX = "index?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;

	
	@Inject
	FacesContext context;

	@Inject
	ExternalContext extcontext;

	@Inject
	Flash flash;

	@EJB
	OrderPositionDAO orderPositionDAO;

	public String indexPage() {
		return PAGE_INDEX;
	}


	public List<OrderPosition> getList(){
		return orderPositionDAO.listAllPositions();
	}
	
	public boolean checkIfEmpty() {
		return getList().isEmpty();
	}

	public boolean checkIfNotEmpty() {
		return !getList().isEmpty();
	}

}