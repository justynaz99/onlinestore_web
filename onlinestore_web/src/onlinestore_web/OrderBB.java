package onlinestore_web;

import java.io.Serializable;
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
import jsfproject.dao.OrderDAO;
import jsfproject.dao.OrderPositionDAO;
import jsfproject.dao.OrderStatusDAO;
import jsfproject.dao.ProductDAO;
import jsfproject.entities.Order;
import jsfproject.entities.OrderPosition;
import jsfproject.entities.OrderStatus;
import jsfproject.entities.User;

@Named
@RequestScoped
public class OrderBB implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String PAGE_INDEX = "index?faces-redirect=true";
	private static final String PAGE_LOGIN = "login?faces-redirect=true";
	private static final String PAGE_REGISTRATION = "registration?faces-redirect=true";
	private static final String PAGE_CONFIRM_ORDER = "confirmOrder?faces-redirect=true";
	private static final String PAGE_PAYMENT = "payment?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;

	private List<Order> orders;
	private HttpSession session;
	private Order cart;

	@Inject
	FacesContext context;

	@Inject
	ExternalContext extcontext;

	@Inject
	Flash flash;

	@EJB
	OrderDAO orderDAO;
	@EJB
	OrderPositionDAO orderPositionDAO;
	@EJB
	OrderStatusDAO orderStatusDAO;
	@EJB
	ProductDAO productDAO;

	public String indexPage() {
		return PAGE_INDEX;
	}

	public String loginPage() {
		return PAGE_LOGIN;
	}

	public String registrationPage() {
		return PAGE_REGISTRATION;
	}
	
	public String pageConfirmOrder() {
		return PAGE_CONFIRM_ORDER;
	}

	public List<Order> getOrders() {
		List<Order> orders = orderDAO.listAllOrders();
		return orders;
	}

	public boolean cartExists() { // checks if there is already an order with status cart for user saved in
									// session
		session = (HttpSession) context.getExternalContext().getSession(false);
		User user = (User) session.getAttribute("user");
		try {
			if (orderDAO.cartExists(user))
				return true;
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public Order getCart() { //returns order with status cart for user saved in session
		session = (HttpSession) context.getExternalContext().getSession(false);
		User user = (User) session.getAttribute("user");
		try {			
			return orderDAO.getCart(user);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void addMessage(FacesMessage.Severity severity, String summary, String detail) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
	}

	public String orderItemsFromCart() { // changes order status from cart to ordered
		try {
			session = (HttpSession) context.getExternalContext().getSession(false);
			User user = (User) session.getAttribute("user");
			Order order = orderDAO.getCart(user);
			//change quantity for every product
			List<OrderPosition> list = orderPositionDAO.listPositionsFromThisOrder(order);
			for (OrderPosition op : list) {
				op.getProduct().setAvailableQuantity(op.getProduct().getAvailableQuantity() - 1);
				productDAO.merge(op.getProduct());
				
			}
			OrderStatus status = orderStatusDAO.find(2);
			order.setOrderStatus(status);
			orderDAO.merge(order);
			return PAGE_PAYMENT;
		} catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "B³¹d!",
					"Wyst¹pi³ b³¹d podczas zapisu rekordu.");
			return PAGE_STAY_AT_THE_SAME;
		}
	}

}