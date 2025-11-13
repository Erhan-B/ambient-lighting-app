package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Popup;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;


public class GuiComp {
	private Stage stage;
	private GridPane root;
	private GridPane leds;
	private MenuBar menuBar;
	
	private Hyperlink docsLink = new Hyperlink("Documentation");
	
	private int numLedW;
	private int numLedH;
	
	private int ledPixelsW;
	private int ledPixelsH;
	
	private Rectangle[][] ledArray;
 	
	/**
	 * Parameterized constructor
	 * @param root Gridpane root instance
	 * @param ledWidth The number of horizontal leds on each side
	 * @param ledHeight The number of vertical leds on each side
	 */
	public GuiComp(Stage stage, GridPane root, int numLedW, int numLedH) {
		this.stage = stage;
		this.root = root;
		this.numLedW = numLedW;
		this.numLedH = numLedH;
		
		ledArray = new Rectangle[numLedW][numLedH];
		
		leds = new GridPane();
		menuBar = new MenuBar();
		initProgram();
		setLayout();
		setMenu();
	}
	
	private void initProgram() {
		Label label = new Label("Init program");
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Initialization");
		alert.setHeaderText(null);
		alert.showAndWait();
	}
	
	/**
	 * Set layout properties
	 */
	private void setLayout() {
		ledPixelsW = 20;
		ledPixelsH = 20;
		leds.hgapProperty().bind(root.widthProperty().multiply(0.005));
		leds.vgapProperty().bind(root.heightProperty().multiply(0.005));
		leds.setStyle("-fx-border-color: black; -fx-border-width: 2px; -fx-padding: 10px; -fx-background-color: #7ea5aa");
		for(int i = 0; i < 33; i++) {
			Rectangle topLed = new Rectangle(ledPixelsW, ledPixelsH);
			Rectangle bottomLed = new Rectangle(ledPixelsW, ledPixelsH);
			topLed.setFill(Color.BLACK);
			bottomLed.setFill(Color.BLACK);
			ledArray[i][0] = topLed;
			ledArray[i][numLedH - 1] = bottomLed;
			leds.add(topLed, i, 0);
			leds.add(bottomLed, i, numLedH - 1);
		}
		for(int i = 1; i <= 15; i++) {
			Rectangle leftLed = new Rectangle(ledPixelsW, ledPixelsH);
			Rectangle rightLed = new Rectangle(ledPixelsW, ledPixelsH);
			
			ledArray[0][i] = leftLed;
			ledArray[numLedW -1][i] = rightLed;
			leds.add(leftLed, 0, i);
			leds.add(rightLed, numLedW -1, i);
		}

		root.add(leds, 2, 1);
	}
	
	private void setMenu() {
		final Menu menu1 = new Menu("File");
		final Menu menu2 = new Menu("Options");
		final Menu menu3 = new Menu("Help");
		
		//'File' menu items
		MenuItem menu1item1 = new MenuItem("Save");
		MenuItem menu1item2 = new MenuItem("Save as");
		MenuItem menu1item3 = new MenuItem("Load preset");
		
		//'Options' menu items
		MenuItem menu2item1 = new MenuItem("Restart connection");
		
		//'Help' menu item
		CustomMenuItem menu3item1 = new CustomMenuItem(docsLink);
		
		docsLink.setOnAction(event -> {
			new Thread(()-> {
				try {
					//TODO replace with appropriate help page
					java.awt.Desktop.getDesktop().browse(new java.net.URI("https://www.oracle.com"));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}).start();
		});
		
		menu3item1.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				
			}
		});
		
		menu1item1.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				System.out.println("Opening database connection...");
			}
		});
		
		menu1.getItems().addAll(menu1item1,menu1item2,menu1item3);
		menu2.getItems().addAll(menu2item1);
		menu3.getItems().addAll(menu3item1);
		
		menuBar.getMenus().addAll(menu1,menu2,menu3);
		
		root.add(menuBar, 0, 0);
	}
	
	public void updateLed(int coordX, int coordY, int red, int green, int blue) {
		if(coordX > numLedW && coordY > numLedH) {
			System.err.println("Index is out of bounds for the array");
			return;
		}
		Rectangle updated = ledArray[coordX][coordY];
		if(updated != null && checkRGB(red,green,blue)) {
			updated.setFill(Color.rgb(red, green, blue));
		}
	}
	
	private boolean checkRGB(int red, int green, int blue) {
		return(red <= 255 && red >=0 && green <= 255 && green >=0 && blue <= 255 && blue >= 0);
	}
}
