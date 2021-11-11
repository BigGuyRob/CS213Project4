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
	
	public void setMainController(MainMenuController mainMenuController) {
		this.stage = mainMenuController;
		this.focus = stage.getOrder();
		orderID = focus.getOrderID();
		lblOrderID.setText(orderID);
		populate();
	}
	
	public Pizza getSelectedPizza() {
		return focus.getOrder().get(lvOrder.getSelectionModel().getSelectedIndex());
	}
	
	private void closeScene() {
		Stage closestage = (Stage) lblOrderID.getScene().getWindow();
		closestage.close();
	}
	
	@FXML
	public void shutdown() {
	   stage.reFocus();
	}
	
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
	
	@FXML private void cancelOrder(ActionEvent event) {
		try {
			stage.clearFocus();
			closeScene();
		}catch (NullPointerException e){
			//do nothing
		}
	}
	@FXML
	private void placeOrder(ActionEvent event) {
		try {
			stage.addtoStoreOrders(focus);
			closeScene();
		}catch (NullPointerException e) {
			//do nothing
		}
	}
	
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
