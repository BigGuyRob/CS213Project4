package application;


import java.util.ArrayList;

import Pizzas.Order;
import Pizzas.Pizza;
import Pizzas.Size;
import Pizzas.StoreOrders;
import Pizzas.Topping;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * MainMenuConntroller class contains logic for interacting with MainMenuView.fxml and 
 * the other controller classes and their attributes
 * @author Robert Reid, Anthony Romanushko
 */
public class MainMenuController {
	@FXML private ToggleGroup tgPizzaType;
	@FXML private RadioButton rdbDeluxe;
	@FXML private RadioButton rdbHawaiian;
	@FXML private RadioButton rdbPepperoni;
	@FXML private Button btnNewOrder;
	@FXML private TextField txtOrderID;
	@FXML private TextArea outputArea;
	@FXML private Button btnCart;
	@FXML private Button btnStoreOrders;
	@FXML private ListView<String> lvOrders;
	@FXML private Button btnComplete;
	private Order focus;
	private int cartNum = 0;
	private StoreOrders storeOrders = new StoreOrders();
	
	/**
	 * Method to enable options on the Main Menu for creating a new order
	 * @param event- on any of the rdbPizza buttons click
	 */
	@FXML
	public void enableNewOrder(ActionEvent event) {
		endisableNewOrder(false);
	}
	
	/**
	 * Method to disable options on the Main Menu view for creating new order
	 * @param event - On rdbButtons none selected
	 */
	@FXML
	public void disableNewOrder(ActionEvent event) {
		endisableNewOrder(true);
	}
	
	/**
	 * Method for changing the status of New Order Button and Cart Button
	 * @param status - status to setDisable
	 */
	public void endisableNewOrder(boolean status) {
		btnNewOrder.setDisable(status);
		btnCart.setDisable(status);
	}
	
	/**
	 * Method for enabling or disabling radio buttons on the main menu
	 * @param status - status to setDisable rdbbuttons
	 */
	public void endisableNewPizzaRDB(boolean status) {
		rdbDeluxe.setDisable(status);
		rdbHawaiian.setDisable(status);
		rdbPepperoni.setDisable(status);
		rdbPepperoni.setSelected(true);
		rdbPepperoni.setSelected(false);
		btnStoreOrders.setDisable(status);
		btnComplete.setDisable(status);;
	}
	
	/**
	 * Method for starting the Current Order View and viewing the 
	 * order currently in progress
	 * @param event - on Cart button click
	 */
	@FXML
	private void viewCart(ActionEvent event) {
		if(cartNum > 0) {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("CurrentOrderView.fxml"));
				BorderPane CurrentOrder = (BorderPane) loader.load();
				CurrentOrderController cartController = loader.getController();
				cartController.setMainController(this);
				Scene CartScene = new Scene(CurrentOrder,600,400);
				CartScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				Stage primaryStage = new Stage();
				primaryStage.setOnHidden(e-> cartController.shutdown());
				primaryStage.setTitle("Current Order");
				endisableNewOrder(true);
				endisableNewPizzaRDB(true);
				primaryStage.setScene(CartScene);
				primaryStage.show(); 
			} catch(Exception e) {
				e.printStackTrace();
			}
		}else {
			outputArea.setText("Cart is empty.");
		}
	}
	
	/**
	 * Method for completing the selected order and removing it from the store orders
	 * @param - on complete Button click 
	 */
	@FXML
	private void completeOrder(ActionEvent event) {
		if(lvOrders.getSelectionModel().getSelectedItem() !=null) {
			storeOrders.completeOrder(storeOrders.getOrder(lvOrders.getSelectionModel().getSelectedIndex()));
			updateLVOrders();
		}else {
			outputArea.setText("No order selected.");
		}
	}
	
	/**
	 * Method for starting the Store Orders view and viewing all store orders
	 * @param event - on View all orders button click
	 */
	@FXML
	private void viewStoreOrders(ActionEvent event) {
		if(storeOrders.getOrders().size() > 0) {
			try {
				txtOrderID.setDisable(true);
				FXMLLoader loader = new FXMLLoader(getClass().getResource("StoreOrdersView.fxml"));
				BorderPane StoreOrders = (BorderPane) loader.load();
				StoreOrdersController StoreordersController = loader.getController();
				StoreordersController.setMainMenuController(this);
				Scene StoreOrder = new Scene(StoreOrders,600,400);
				StoreOrder.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				Stage primaryStage = new Stage();
				primaryStage.setOnHidden(e-> StoreordersController.shutdown());
				primaryStage.setTitle("All Store Orders");
				endisableNewOrder(true);
				endisableNewPizzaRDB(true);
				primaryStage.setScene(StoreOrder);
				primaryStage.show(); 
			} catch(Exception e) {
				e.printStackTrace();
			}
		}else {
			outputArea.setText("There are no current store orders.");
		}
	}
	
	/**
	 * Method for creating a new order or adding to the current order
	 * @param event - On New Pizza button click
	 */
	@FXML
	private void newOrder(ActionEvent event) {
		String errorMessage = validateOrder();
		if(errorMessage.equals("")){
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("PizzaCustomizationView.fxml"));
				BorderPane PizzaCustomizer = (BorderPane) loader.load();
				PizzaCustomizationController pizzaController = loader.getController();
				pizzaController.setMainController(this);
				Scene PizzaScene = new Scene(PizzaCustomizer,600,400);
				PizzaScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				Stage primaryStage = new Stage();
				primaryStage.setOnHidden(e-> pizzaController.shutdown());
				primaryStage.setTitle("Pizza Customizer");
				endisableNewOrder(true);
				endisableNewPizzaRDB(true);
				outputArea.setText("");
				if(focus == null) {
					focus = new Order(txtOrderID.getText());
					cartNum = 0;
					txtOrderID.setDisable(true);
				}
				primaryStage.setScene(PizzaScene);
				primaryStage.show(); 
			} catch(Exception e) {
				e.printStackTrace();
			}
		}else {
			outputArea.setText(errorMessage);
		}
	}
	
	/**
	 * Method for validating that all fields on Main Menu have valid entries
	 * @return - error message for checking fields on the Main Menu for creating new order
	 */
	private String validateOrder() {
		int orderID = 0;
		String retMes = "";
		if(txtOrderID.getText().trim().length() == 0){
			retMes = "Please enter a phone number for an Order ID.";
		}else
		if(txtOrderID.getText().trim().matches("[0-9]+")) {
			if(txtOrderID.getText().trim().length() != 10) {
				retMes = "Invalid 10 digit phone number for order ID.";
			}
		}else {
			retMes = "Order ID phone number cannot have special characters.";
		}
		return retMes;
	}
	
	/**
	 * Method for taking the user entries and creating pizza object for customizer
	 * @return - String[] representation of pizza object to pass to customizer
	 */
	public String[] getUserData() {
		String orderID = txtOrderID.getText();
		String[] pass;
		if(rdbDeluxe.isSelected()) {
			pass = new String[] {orderID, "deluxe","small", "CHICKEN", "SAUSAGE", "PEPPERS", "ONION", "BACON"};
		}else if(rdbHawaiian.isSelected()) {
			pass = new String[] {orderID, "hawaiian", "small", "CHICKEN", "PINEAPPLE"};
		}else {
			pass = new String[] {orderID, "pepperoni","small", "PEPPERONI"};
		}
		return pass;
	}
	
	/**
	 * Method for returning order in progress
	 * @return - Order currently being worked on Main Menu
	 */
	public Order getOrder() {
		return this.focus;
	}
	
	/**
	 * Method for returning the store orders
	 * @return - current StoreOrders object
	 */
	public StoreOrders getStoreOrders() {
		return this.storeOrders;
	}
	
	/**
	 * Method for adding an order to store orders
	 * @param order - Order to add to store orders
	 */
	public void addtoStoreOrders(Order order) {
		storeOrders.addOrder(order);
		clearFocus();
	}
	
	/**
	 * Method for clearing the order that is currently in progress if cancelled or completed
	 */
	public void clearFocus() {
		focus = null;
		cartNum = 0;
		btnCart.setText("Cart");
		txtOrderID.setText("");
		endisableNewPizzaRDB(false);
		txtOrderID.setDisable(false);
		updateLVOrders();
	}
	
	/**
	 * Method for updating the number of pizzas in the cart
	 * @param num - Number or pizzas added or removed from the order
	 */
	public void updateCartNum(int num) {
		cartNum += num;
	}
	
	/**
	 * Method for updating the list of current orders in the store orders
	 */
	private void updateLVOrders() {
		ObservableList<String> lvElem = FXCollections.observableArrayList();
		if((storeOrders.getOrders()).size() != 0) {
			ArrayList<Order> Orders = storeOrders.getOrders();
			for(Order o : Orders) {
				lvElem.add(o.getOrderID());
			}
			lvOrders.setItems(lvElem);
		}else {
			lvOrders.setItems(lvElem);
		}
	}
	
	/**
	 * Method for refocusing on this stage whenever another view is closed which decides which options 
	 * to enable.
	 */
	public void reFocus() {
		outputArea.setText("");
		updateLVOrders();
		endisableNewPizzaRDB(false);
		if(cartNum == 0) {
			txtOrderID.setDisable(false);
			btnCart.setDisable(true);
		}else {
			txtOrderID.setDisable(true);
			btnCart.setText("Cart (" + cartNum + ")");
			btnCart.setDisable(false);
		}
	}
	
	/**
	 * Method for adding a pizza to current order
	 * @param pizza - Pizza to be added to order currently in progress
	 */
	public void addPizzatoOrder(Pizza pizza) {
		focus.addPizza(pizza);
	}
	
}
