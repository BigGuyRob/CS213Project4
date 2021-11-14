package application;

import java.util.ArrayList;

import Pizzas.Order;
import Pizzas.StoreOrders;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;

/**
 * StoreOrdersController class contains logic for interacting with StoreOrdersView.fxml and 
 * MainMenuController and its attributes
 * @author Robert Reid, Anthony Romanushko
 *
 */
public class StoreOrdersController {
	private MainMenuController stage;
	private StoreOrders storeOrders;
	@FXML private ListView<String> lvOrders;
	
	/**
	 * Method to set MainMenuController to access its attributes and populate fields
	 * @param controller - MainMenuController that called to start this controller
	 */
	public void setMainMenuController(MainMenuController controller) {
		this.stage = controller;
		this.storeOrders = stage.getStoreOrders();
		populate();
	}

	/**
	 * Method run on shutdown of StoreOrdersView
	 */
	@FXML
	public void shutdown() {
		stage.reFocus();
	}
	
	/**
	 * Method to populate list view with store orders
	 */
	private void populate() {
		updateLVOrders();
	}
	
	/**
	 * Method to updateLVOrders with storeorders array
	 */
	private void updateLVOrders() {
		ObservableList<String> lvElem = FXCollections.observableArrayList();
		if((storeOrders.getOrders()).size() != 0) {
			ArrayList<Order> Orders = storeOrders.getOrders();
			for(Order o : Orders) {
				lvElem.add(o.toString());
			}
			lvOrders.setItems(lvElem);
		}else {
			lvOrders.setItems(lvElem);
		}
	}
	
	/**
	 * Method to cancel an order and remove it from StoreOrders
	 * @param event - on cancel order button click
	 */
	@FXML
	private void cancelOrder(ActionEvent event) {
		if(lvOrders.getSelectionModel().getSelectedItem() !=null) {
			storeOrders.completeOrder(storeOrders.getOrder(lvOrders.getSelectionModel().getSelectedIndex()));
			updateLVOrders();
		}
	}
	
	/**
	 * Method to export store orders to text document
	 */
	@FXML
	private void export() {
		Alert a = new Alert(AlertType.INFORMATION);
		a.setContentText(storeOrders.export());
		a.show();
	}
}
