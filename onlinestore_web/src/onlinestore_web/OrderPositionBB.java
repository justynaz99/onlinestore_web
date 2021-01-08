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

import jsfproject.dao.OrderDAO;
import jsfproject.dao.OrderPositionDAO;
import jsfproject.dao.ProductDAO;
import jsfproject.entities.Order;
import jsfproject.entities.OrderPosition;
import jsfproject.entities.OrderStatus;
import jsfproject.entities.Product;
import jsfproject.entities.User;

import javax.servlet.http.HttpServletRequest;

@Named
@RequestScoped
public class OrderPositionBB implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final String PAGE_INDEX = "index?faces-redirect=true";
	private static final String PAGE_SHOPPING_CART = "shoppingCart?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;

	Order order = new Order();
	OrderPosition orderPosition = new OrderPosition();
	OrderStatus orderStatus = new OrderStatus(); 
	
	@Inject
	FacesContext context;

	@Inject
	ExternalContext extcontext;

	@Inject
	Flash flash;

	@EJB
	OrderPositionDAO orderPositionDAO;
	@EJB
	OrderDAO orderDAO;
	@EJB
	OrderDAO orderStatusDAO;

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
	
	public String addToCart(Product product) {
		
		HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
		session.setAttribute("order", order);
		order.setUser((User) session.getAttribute("user"));
		order.setOrderStatus(orderStatusDAO.find(1));
		orderDAO.create(order);
		
		orderPosition.setOrder((Order) session.getAttribute("order"));
		orderPosition.setPriceProduct(product.getPrice());
		orderPosition.setProduct(product);
		orderPosition.setQuantity(1);
		orderPositionDAO.create(orderPosition);
		return PAGE_SHOPPING_CART;
	}
	
	public void deletePosition(OrderPosition position) {
		orderPositionDAO.remove(position);
	}

}