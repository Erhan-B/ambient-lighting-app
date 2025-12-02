package gui;

import java.util.HashMap;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import monitor.Pixel;
import monitor.capture.CaptureScreen;


public class GuiComp {
	private CaptureScreen screen;
	private boolean isRunning;
	
	private StackPane root;
	private GridPane mainView;
	private GridPane editView;
	private GridPane leds;
	
	private Hyperlink docsLink = new Hyperlink("Documentation");
	
	private int numLedW;
	private int numLedH;
	
	private int ledPixelsW;
	private int ledPixelsH;
	
	private Rectangle[][] ledArray;
	private HashMap<Integer,Pixel> pixelList;
 	
	/**
	 * Parameterized constructor
	 * @param mainView Gridpane mainView instance
	 * @param ledWidth The number of horizontal leds on each side
	 * @param ledHeight The number of vertical leds on each side
	 */
	public GuiComp(int numLedW, int numLedH) {
		screen = new CaptureScreen();
		
		mainView = new GridPane();
		editView = new GridPane();
		root = new StackPane();
		
		this.numLedW = numLedW;
		this.numLedH = numLedH;
		
		ledArray = new Rectangle[numLedW][numLedH];
		
		leds = new GridPane();
		pixelList = new HashMap<>();
		
		initProgram();
		setLayout();

		root.getChildren().addAll(mainView,editView);
		editView.setVisible(false);
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
//~~~~~~~~~~~~~~Main view~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		ledPixelsW = 20;
		ledPixelsH = 20;
		leds.hgapProperty().bind(mainView.widthProperty().multiply(0.005));
		leds.vgapProperty().bind(mainView.heightProperty().multiply(0.005));
		leds.setStyle("-fx-border-color: black; -fx-border-width: 2px; -fx-padding: 10px; -fx-background-color: #7ea5aa");
		mainView.setStyle("-fx-background-color: #1e1e1e;");
		
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
		
		mainMenu();
		mainView.add(leds, 0, 1);
		
//~~~~~~~~~~~~~~Edit view~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		editView.setStyle("-fx-background-color: #1e1e1e;");
		editMenu();
		
		
	}
	
	private void mainMenu() {
		MenuBar menuBar = new MenuBar();
		
		final Menu menu1 = new Menu("File");
		final Menu menu2 = new Menu("Options");
		final Menu menu3 = new Menu("Edit");
		final Menu menu4 = new Menu("Help");
		
		//'File' menu items
		MenuItem menu1item1 = new MenuItem("Save");
		MenuItem menu1item2 = new MenuItem("Save as");
		MenuItem menu1item3 = new MenuItem("Load preset");
		
		//'Options' menu items
		MenuItem menu2item1 = new MenuItem("Restart connection");
		
		//'Edit' menu items
		MenuItem menu3item1 = new MenuItem("Edit led layout");
		
		//'Help' menu item
		CustomMenuItem menu4item1 = new CustomMenuItem(docsLink);
		
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
		
		menu1item1.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				System.out.println("Opening database connection...");
			}
		});
		
		menu3item1.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				System.out.println("Editing led layout");
				mainView.setVisible(false);
				editView.setVisible(true);
			}
		});
		
		menu4item1.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				
			}
		});
		
		
		menu1.getItems().addAll(menu1item1,menu1item2,menu1item3);
		menu2.getItems().addAll(menu2item1);
		menu3.getItems().addAll(menu3item1);
		menu4.getItems().addAll(menu4item1);
		
		menuBar.getMenus().addAll(menu1,menu2,menu3,menu4);
		
		mainView.add(menuBar, 0, 0);
	}
	
	private void editMenu() {
		ToolBar toolbar = new ToolBar();
		
		Button doneBtn = new Button("Done");
		Button addLEDBtn = new Button("Add LED");
		Button removeBtn = new Button("Remove LED");
		
		addLEDBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				//TODO pixel coordinate logic
				pixelList.put(pixelList.size()+1, new Pixel(3,3));
			}
		});
		
		toolbar.getItems().addAll(doneBtn,addLEDBtn,removeBtn);
		editView.add(toolbar, 0, 0);
	}
	
	public void updateLed(int coordX, int coordY, int color) {
		if(coordX > numLedW || coordY > numLedH) {
			System.err.println("Index is out of bounds for the array");
			return;
		}
		int red = color >> 16 & 0xFF;
		int green = color >> 8 & 0xFF;
		int blue = color & 0xFF;
		
		Rectangle updated = ledArray[coordX][coordY];
		if(updated != null && checkRGB(red,green,blue)) {
			updated.setFill(Color.rgb(red, green, blue));
		}
	}
	
	private void editLed() {
		
	}
	
	public void startCaptureThread() {
		isRunning = true;
		
		Thread t = new Thread(()-> {
			while(isRunning) {
				int color = screen.readPixels();
				
				javafx.application.Platform.runLater(()-> {
					updateLed(0,0,color);
				});
				
				try {
					Thread.sleep(16);
				}catch(InterruptedException e) {}
			}
		});
		t.start();
	}
	
	private boolean checkRGB(int red, int green, int blue) {
		return(red <= 255 && red >=0 && green <= 255 && green >=0 && blue <= 255 && blue >= 0);
	}
	
	public StackPane getRoot() {
		return root;
	}
}
