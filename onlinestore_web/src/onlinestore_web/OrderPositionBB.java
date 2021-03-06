package onlinestore_web;

import java.io.Console;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import jsfproject.dao.OrderStatusDAO;
import jsfproject.dao.ProductDAO;
import jsfproject.entities.Order;
import jsfproject.entities.OrderPosition;
import jsfproject.entities.OrderStatus;
import jsfproject.entities.Product;
import jsfproject.entities.User;

@Named
@RequestScoped
public class OrderPositionBB implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final String PAGE_INDEX = "index?faces-redirect=true";
	private static final String PAGE_LOGIN = "login?faces-redirect=true";
	private static final String PAGE_SHOPPING_CART = "shoppingCart?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;

	private HttpSession session;
	private List<OrderPosition> positions;
	private BigDecimal value = new BigDecimal(0);

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
	OrderStatusDAO orderStatusDAO;
	@EJB
	ProductDAO productDAO;
	
	public String getValue(Order order) {
		List<OrderPosition> opList = orderPositionDAO.listPositionsFromThisOrder(order);

		for (OrderPosition temp : opList) {
			value = value.add(temp.getPriceProduct());
		}
		return value.toString();
	}
	
	public List<OrderPosition> getPositions(Order order) { // lists all positions from given order
		List<OrderPosition> opList = orderPositionDAO.listPositionsFromThisOrder(order);
		return opList;
	}

	public List<OrderPosition> getPositions() { // lists all positions from all orders
		return orderPositionDAO.listAllPositions();
	}
	
	public String indexPage() {
		return PAGE_INDEX;
	}
	
	public void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().
                addMessage(null, new FacesMessage(severity, summary, detail));
    }

	public String addToCart(Product product) { // creating cart and first position or if cart already exists adding
												// position to cart
		session = (HttpSession) context.getExternalContext().getSession(false);
		User user = (User) session.getAttribute("user");
		
		if (!orderDAO.cartExists(user)) {
			Order order = new Order();
			Date date = new Date(System.currentTimeMillis());
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

			OrderStatus orderStatus = orderStatusDAO.find(1);
			order.setUser((User) session.getAttribute("user"));
			order.setOrderStatus(orderStatus);
			order.setDate(Date.valueOf(formatter.format(date)));
			orderDAO.create(order);

			OrderPosition orderPosition = new OrderPosition();
			orderPosition.setOrder(order);
			orderPosition.setPriceProduct(product.getPrice());
			orderPosition.setProduct(product);
			orderPosition.setQuantity(1);
			orderPositionDAO.create(orderPosition);
			addMessage(FacesMessage.SEVERITY_INFO, "Sukces!", "Produkt zosta� dodany do koszyka");
			
			if(session != null)
				return PAGE_STAY_AT_THE_SAME;
			else return PAGE_LOGIN;
			
		} else { // if order with status cart already exists for user saved in session
			OrderPosition orderPosition = new OrderPosition();
			orderPosition.setOrder(orderDAO.getCart((User) session.getAttribute("user")));
			orderPosition.setProduct(productDAO.find(product.getIdProduct()));
			orderPosition.setPriceProduct(product.getPrice());
			orderPosition.setQuantity(1);
			orderPositionDAO.create(orderPosition);
			addMessage(FacesMessage.SEVERITY_INFO, "Sukces!", "Produkt zosta� dodany do koszyka");
			
			if(session != null)
				return PAGE_STAY_AT_THE_SAME;
			else return PAGE_LOGIN;
		}
	}

	public void deletePosition(OrderPosition position) {
		session = (HttpSession) context.getExternalContext().getSession(false);
		User user = (User) session.getAttribute("user");
		Order cart = orderDAO.getCart(user);
		List<OrderPosition> list = orderPositionDAO.listPositionsFromThisOrder(cart);
		if (list.size() > 1) {
			orderPositionDAO.remove(position);
		}
		else if (list.size() == 1) {
			orderPositionDAO.remove(position);
			orderDAO.remove(cart);
		}
		
	}

}