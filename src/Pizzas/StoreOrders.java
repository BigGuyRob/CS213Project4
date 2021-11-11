package Pizzas;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class StoreOrders {
	private ArrayList<Order> orders = new ArrayList<Order>();
	
	public ArrayList<Order> getOrders(){
		return this.orders;
	}
	
	public void addOrder(Order e) {
		orders.add(e);
	}
	
	public void completeOrder(Order e) {
		orders.remove(e);
	}
	
	public Order getOrder(int index) {
		return orders.get(index);
	}
	
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
