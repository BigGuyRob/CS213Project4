package application;

import java.util.ArrayList;

import Pizzas.Order;
import Pizzas.Pizza;
import Pizzas.PizzaMaker;
import Pizzas.Size;
import Pizzas.Topping;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * PizzaCustomization Controller class contains logic for interacting with CurrentOrderView.fxml and 
 * MainMenuController and its attributes
 * @author Robert Reid, Anthony Romanushko
 *
 */
public class PizzaCustomizationController {
	private String[] data;
	private MainMenuController stage;
	private CurrentOrderController passer;
	private String pizzaType;
	@FXML private Label lblOrderID;
	@FXML private CheckBox cbPEP;
	@FXML private CheckBox cbEXC;
	@FXML private CheckBox cbBAC;
	@FXML private CheckBox cbJAL;
	@FXML private CheckBox cbONI;
	@FXML private CheckBox cbCHC;
	@FXML private CheckBox cbSAG;
	@FXML private CheckBox cbPPR;
	@FXML private CheckBox cbBRO;
	@FXML private CheckBox cbRIC;
	@FXML private CheckBox cbPIN;
	@FXML private ComboBox<String> cbSize;
	@FXML private Label subTotal;
	
	// images of different toppings/pizza
	@FXML private StackPane toppingPane;
	@FXML private ImageView basePizza;
	@FXML private ImageView pepperoni;
	@FXML private ImageView extraCheese;
	@FXML private ImageView bacon;
	@FXML private ImageView jalapeno;
	@FXML private ImageView onion;
	@FXML private ImageView chicken;
	@FXML private ImageView sausage;
	@FXML private ImageView peppers;
	@FXML private ImageView brocolli;
	@FXML private ImageView ricotta;
	@FXML private ImageView pineapple;
	private int amtToppings = 0;
	private ArrayList<Topping> toppings = new ArrayList<Topping>();
	private PizzaMaker pizzaMaker = new PizzaMaker();
	private Pizza valuePizza;
	private Pizza tempPizza;
	
	private double smallScale = .6;
	private double medScale = .8;
	private double largeScale = 1;
	
	/**
	 * Method to initialize values works as constructor for PizzaCustomizationController
	 * @param stage - stage that created the instance of PizzaCustomizationController
	 */
	public void setMainController(MainMenuController stage) {
		this.stage = stage;
		this.data = (String[]) stage.getUserData();
		lblOrderID.setText(data[0]);
		this.pizzaType = data[1];
		populate();
		displaySubtotal();
	}
	
	/**
	 * Method for setting the currentOrderController run on initialization of new order
	 * @param controller - CurrentOrder controller passes data to this
	 */
	public void setCurrentOrderController(CurrentOrderController controller) {
		tempPizza = passer.getSelectedPizza();
		this.passer = controller;
		this.data = (String[]) passer.getData();
		lblOrderID.setText(data[0]);
		this.pizzaType = data[1];
		populate();
		displaySubtotal();
	}
	
	/**
	 * Method for calculating and displaying subtotal
	 */
	public void displaySubtotal() {
		valuePizza = PizzaMaker.createPizza(pizzaType, parsers.parseSize(cbSize.getSelectionModel().getSelectedItem()), toppings);
		String o = String.format("%,.2f", valuePizza.price());
		subTotal.setText("$" + o);
	}
	
	/**
	 * Method for adding a pizza to order
	 * @param event - on Add to Order button click
	 */
	@FXML
	public void addToOrder(ActionEvent event) {
		stage.addPizzatoOrder(valuePizza);
		if(passer != null) {
			stage.getOrder().removePizza(tempPizza);
			passer.populate();
		}else {
			stage.updateCartNum(1);
		}
		closeScene();
	}
	
	/**
	 * Method for updating the topping array for the current pizza and changing image
	 * @param event - On topping checkbox check changed
	 */
	@FXML 
	public void updateToppings(ActionEvent event) {
		CheckBox source = (CheckBox)event.getSource();
		if(!source.isSelected()) {
			if(amtToppings > 0) {
				amtToppings -=1;
				toppings.remove(parsers.parseTopping(source.getText().toUpperCase()));
				modLayer(parsers.parseTopping(source.getText().toUpperCase()), false);
			}
		}else {
			if(amtToppings >= Constants.MAX_TOPPING) {
				source.setSelected(false);
			}else {
				toppings.add(parsers.parseTopping(source.getText().toUpperCase()));
				modLayer(parsers.parseTopping(source.getText().toUpperCase()), true);
				amtToppings +=1;
			}
		}
		displaySubtotal();
	}
	
	/**
	 * Method for updating the subtotal and image when the size of pizza is changed
	 * @param event - On combobox selection changed
	 */
	@FXML 
	public void updateSize(ActionEvent event) {
		displaySubtotal();
		setImageSize();
	}
	
	/**
	 * Method for updating the image size
	 */
	public void setImageSize()
	{
	    double newScale = 0;
	    switch(cbSize.getSelectionModel().getSelectedItem())
        {
            case "small" : newScale = smallScale; break;
            case "medium" : newScale = medScale; break;
            case "large" : newScale = largeScale; break;
        }
	    toppingPane.setScaleX(newScale);
	    toppingPane.setScaleY(newScale);
	}
	
	/**
	 * Method for closing the PizzaCustomizationController and view
	 */
	public void closeScene() {
		Stage closestage = (Stage) lblOrderID.getScene().getWindow();
		closestage.close();
	}
	
	/**
	 * Method run on shutdown refocuses on main menu
	 */
	@FXML
	public void shutdown() {
	   stage.reFocus();
	}
	
	/**
	 * Method for updating the image of the pizza shown
	 * @param topping - topping that was checked
	 * @param setTo - boolean to display image
	 */
	public void modLayer(Topping topping, boolean setTo)
	{
	    switch(topping)
	    {
    	    case PEPPERONI : pepperoni.setVisible(setTo); break;
    	    case EXTRACHEESE : extraCheese.setVisible(setTo); break;
    	    case BACON : bacon.setVisible(setTo); break;
    	    case JALAPENO : jalapeno.setVisible(setTo); break;
    	    case ONION : onion.setVisible(setTo); break;
    	    case CHICKEN : chicken.setVisible(setTo); break;
    	    case SAUSAGE : sausage.setVisible(setTo); break;
    	    case PEPPERS : peppers.setVisible(setTo); break;
    	    case BROCCOLI : brocolli.setVisible(setTo); break;
    	    case RICOTTA : ricotta.setVisible(setTo); break;
    	    case PINEAPPLE : pineapple.setVisible(setTo); break;
	    }
	}
	
	/**
	 * Method for populating fields on PizzaCustomizationView
	 */
	public void populate() {
		ObservableList<String> sizes = FXCollections.observableArrayList("small", "medium", "large");
		cbSize.setItems(sizes);
		cbSize.setValue("small");
		setImageSize();
		if(pizzaType.equals(Constants.deluxe)) {
			cbCHC.setSelected(true);
			cbSAG.setSelected(true);
			cbPPR.setSelected(true);
			cbONI.setSelected(true);
			cbBAC.setSelected(true);
			modLayer(Topping.CHICKEN,true);
            modLayer(Topping.SAUSAGE,true);
            modLayer(Topping.PEPPERS,true);
            modLayer(Topping.ONION,true);
            modLayer(Topping.BACON,true);
			loadToppings();
			amtToppings = Constants.deluxeIncludedToppings;
		}else if(pizzaType.equals(Constants.hawaiian)) {
			cbCHC.setSelected(true);
			cbPIN.setSelected(true);
			modLayer(Topping.CHICKEN,true);
			modLayer(Topping.PINEAPPLE,true);
			loadToppings();
			amtToppings = Constants.hawaiianIncludedToppings;
		}else {
			cbPEP.setSelected(true);
			modLayer(Topping.PEPPERONI,true);
			loadToppings();
			amtToppings = Constants.pepperoniIncludedToppings;
		}
	}
	
	/**
	 * Method for loading the preset topping array
	 */
	public void loadToppings() {
		int dataStart = 3;
		for(int x = dataStart; x < data.length; x++) {
			toppings.add(parsers.parseTopping(data[x].toUpperCase()));
		}
	}
	
}
