package Pizzas;
import java.util.ArrayList;

import application.Constants;

public class Order {
	private String phoneNumber;
	private ArrayList<Pizza> order = new ArrayList<Pizza>();
	private double totalPrice;
	
	public Order(String PHONENUMBER) {
		this.phoneNumber = PHONENUMBER;
		this.totalPrice = 0;
	}
	
	private double calculatePrice() {
		double price = 0;
		for(Pizza p : order) {
			price += p.price();
		}
		price += price * Constants.SALES_TAX;
		String o = String.format("%,.2f", price);
		price = Double.parseDouble(o);
		return price;
	}
	
	public void addPizza(Pizza pizza) {
		order.add(pizza);
	}
	
	public void removePizza(Pizza pizza) {
		order.remove(pizza);
	}
	
	public double getPrice() {
		return calculatePrice();
	}
	
	@Override
	public String toString() {
		String str = phoneNumber + ":\n";
		for(Pizza p : order) {
			str += p.toString() + ":\n";
		}
		str += "Price: " +this.getPrice();
		return str;
	}
	
	public int getSize() {
		return order.size();
	}
	
	public String getOrderID() {
		return this.phoneNumber;
	}
	
	public ArrayList<Pizza> getOrder() {
		return this.order;
	}
}
