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

public class StoreOrdersController {
	private MainMenuController stage;
	private StoreOrders storeOrders;
	@FXML private ListView<String> lvOrders;
	
	public void setMainMenuController(MainMenuController controller) {
		this.stage = controller;
		this.storeOrders = stage.getStoreOrders();
		populate();
	}

	@FXML
	public void shutdown() {
		stage.reFocus();
	}
	
	
	private void populate() {
		updateLVOrders();
	}
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
	
	@FXML
	private void cancelOrder(ActionEvent event) {
		if(lvOrders.getSelectionModel().getSelectedItem() !=null) {
			storeOrders.completeOrder(storeOrders.getOrder(lvOrders.getSelectionModel().getSelectedIndex()));
			updateLVOrders();
		}
	}
	
	@FXML
	private void export() {
		Alert a = new Alert(AlertType.INFORMATION);
		a.setContentText(storeOrders.export());
	}
}
