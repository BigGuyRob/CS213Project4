package Pizzas;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * StoreOrders class contains logic for interacting with StoreOrders object and its attributes which are orders
 * @author Robert Reid, Anthony Romanushko
 *
 */
public class StoreOrders {
	private ArrayList<Order> orders = new ArrayList<Order>();
	
	/**
	 * Method for getting the orders the store currently has
	 * @return ArrayList<Order> of the store orders
	 */
	public ArrayList<Order> getOrders(){
		return this.orders;
	}
	
	/**
	 * Method for adding an order to the store orders
	 * @param e - Order to be added to store orders
	 */
	public void addOrder(Order e) {
		orders.add(e);
	}
	
	/**
	 * Method for removing an order from the store orders
	 * @param e - Order to be removed from store orders
	 */
	public void completeOrder(Order e) {
		orders.remove(e);
	}
	
	/**
	 * 
	 * @param index - Index of order to be returned
	 * @return - Order at Index in store orders
	 */
	public Order getOrder(int index) {
		return orders.get(index);
	}
	
	/**
	 * Method for exporting all current store orders to text file
	 * @return Return message for exported text file as string
	 */
	public String export() {
		String path = "../CS213Project4/src/ExportedFiles/";
        File outputFile = new File(path+"store_orders.txt");
        try 
        {
        FileWriter output = new FileWriter(outputFile);
        for(Order o: orders) {output.write(o.toString()+"\n");}
        output.close();
        return "Orders exported Successfully.";
        }catch(IOException e) {
        	return "Something went wrong.";
        }
	}
	
}
