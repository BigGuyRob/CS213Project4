package application;


import java.util.ArrayList;

import javax.swing.event.ChangeListener;

import Pizzas.Order;
import Pizzas.Pizza;
import Pizzas.Topping;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * CurrentOrderController class contains logic for interacting with CurrentOrderView.fxml and 
 * MainMenuController and its attributes
 * @author Robert Reid, Anthony Romanushko
 *
 */
public class CurrentOrderController {
	private MainMenuController stage;
	private Order focus;
	private String orderID;
	private Pizza selectedPizza;
	@FXML private ListView<String>lvOrder;
	@FXML private Label lblOrderID;
	@FXML private Label lblsubtotal1;
	@FXML private Label lblsubtotal2;
	@FXML private Label lbltaxRate;
	@FXML private Label lbltotal;
	@FXML private Button btnCancelOrder;
	
	/**
	 * Method for accessing public methods of MainMenuController which called this scene
	 * Works as a constructor for CurrentOrderController
	 * @param mainMenuController - the main menu that called to open this scene
	 */
	public void setMainController(MainMenuController mainMenuController) {
		this.stage = mainMenuController;
		this.focus = stage.getOrder();
		orderID = focus.getOrderID();
		lblOrderID.setText(orderID);
		populate();
	}
	
	/**
	 * 
	 * @return Gets the selected pizza from the order being interacted with
	 */
	public Pizza getSelectedPizza() {
		return focus.getOrder().get(lvOrder.getSelectionModel().getSelectedIndex());
	}
	
	/**
	 * Closes currentOrderView
	 */
	private void closeScene() {
		Stage closestage = (Stage) lblOrderID.getScene().getWindow();
		closestage.close();
	}
	
	/**
	 * Method run on close of CurrentOrderView calls MainMenuController method to pass data
	 */
	@FXML
	public void shutdown() {
	   stage.reFocus();
	}
	
	/**
	 * Method populates fields on CurrentOrderView 
	 */
	public void populate() {
		String sales_tax = "6.625%";
		double total = 0;
		ArrayList<Pizza> Pizzas = focus.getOrder();
		ObservableList<String> lvElem = FXCollections.observableArrayList();
		for(Pizza p : Pizzas) {
			total += p.price();
			lvElem.add(p.toString());
		}
		String str = total+"";
		lblsubtotal1.setText(str);
		lblsubtotal2.setText(str);
		str = sales_tax;
		lbltaxRate.setText(str);
		str = String.format("%,.2f", focus.getPrice());
		lbltotal.setText(str);
		lvOrder.setItems(lvElem);
	}
	
	/**
	 * Method to get data from the fields on CurrentOrderView
	 * @return String[] Representation of pizza object
	 */
	public String[] getData() {
		int numToppings = selectedPizza.getToppings().size();
		String[] data = new String[numToppings + 3];
		int dataStart = 3;
		ArrayList<Topping> topping = selectedPizza.getToppings();
		data[0] = focus.getOrderID();
		data[1] = selectedPizza.getType();
		data[2] = selectedPizza.getSize().name();
		for(int x = dataStart; x < data.length; x++) {
			data[x] = topping.get(x - dataStart).name();
		}
		return data;
	}
	
	/**
	 * Method to cancel the current order, clears the cart and closes the scene to restart MainMenuController
	 * @param event - On Cancel Order button click
	 */
	@FXML private void cancelOrder(ActionEvent event) {
		try {
			stage.clearFocus();
			closeScene();
		}catch (NullPointerException e){
			//do nothing
		}
	}
	
	/**
	 * Method to place the current order. This closes the scene and adds the order to store orders
	 * @param event - On Place Order button click
	 */
	@FXML
	private void placeOrder(ActionEvent event) {
		try {
			stage.addtoStoreOrders(focus);
			closeScene();
		}catch (NullPointerException e) {
			//do nothing
		}
	}
	
	/**
	 * Method removes the selected pizza from the listview from the current order. If this makes
	 * the order have 0 pizzas the screen is closed the same as cancelling the order
	 * @param event - On Remove pizza button click 
	 */
	@FXML
	private void removePizza(ActionEvent event) {
		if(lvOrder.getSelectionModel().getSelectedItem() !=null) {
			ArrayList<Pizza> pizzas = focus.getOrder();
			Pizza temp = pizzas.get(lvOrder.getSelectionModel().getSelectedIndex());
			focus.removePizza(temp);
			stage.updateCartNum(-1);
			if(focus.getOrder().size() > 0) {
				populate();
			}else {
				stage.clearFocus();
				closeScene();
			}
		}
	}
}
