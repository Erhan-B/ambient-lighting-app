package gui;

import java.awt.GraphicsDevice;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.converter.IntegerStringConverter;
import monitor.capture.CaptureScreen;


public class GUI {	
	private CaptureScreen capture;
	
	//GUI components
	private BorderPane root;
	private MenuBar menuBar;
	private GridPane leftPane;
	private GridPane rightPane;
	private GridPane statusPane;
	private Pane ledPane;
	
	private MenuButton monitorButton;
	private TextField sampleField;
	private TextField sparsityField;
	private Text sampleText;
	private Text sparsityText;
	private TextFormatter<Integer> sampleFormatter;
	private TextFormatter<Integer> sparsityFormatter;
	
	/**
	 * Default constructor for GUI
	 */
	public GUI() {
		root = new BorderPane();
		menuBar = new MenuBar();
		leftPane = new GridPane();
		rightPane = new GridPane();
		statusPane = new GridPane();
		ledPane = new Pane();
		
		initLayout();
		styleLayout();
		//Delay update to ensure no null values
		Platform.runLater(() -> {
			updateLayout();
		});
	}
	
	/**
	 * Initialises the various GUI components
	 */
	private void initLayout() {
		initMenu();
		initLeft();
		initRight();
		initStatus();
		
		root.setTop(menuBar);
		root.setLeft(leftPane);
		root.setCenter(ledPane);
		root.setRight(rightPane);
		root.setBottom(statusPane);
		
		initCapture();
	}
	
	/**
	 * Sets GUI style and preferences 
	 */
	private void styleLayout() {
		root.setStyle("-fx-background-color: #474848");
		menuBar.setStyle("-fx-background-color: #8e9091;");
		leftPane.setStyle("-fx-background-color: #a5a8a9; -fx-border-color: #8e9091; -fx-border-width: 3px;");
		rightPane.setStyle("-fx-background-color: #a5a8a9; -fx-border-color: #8e9091; -fx-border-width: 3px;");
		ledPane.setStyle("-fx-background-color: #767879;-fx-border-color: #8e9091; -fx-border-width: 3px;");
		statusPane.setStyle("-fx-background-color: #8e9091; -fx-border-color: #8e9091; -fx-border-width: 3px;");

		
		leftPane.setPrefWidth(200);
		leftPane.setMinWidth(40);
		
		rightPane.setPrefWidth(200);
		rightPane.setMinWidth(40);
		
		statusPane.setPrefHeight(50);
		statusPane.setMinHeight(10);
		statusPane.setMaxHeight(50);
	}
	
	/**
	 * Updates visual information
	 */
	private void updateLayout() {
		updateLeft();
		updateRight();
		updateLedPane();
		updateStatus();
	}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~INIT~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	private void initMenu() {
		Menu fileMenu = new Menu("File");
		Menu helpMenu = new Menu("Help");
		
		menuBar.getMenus().addAll(fileMenu, helpMenu);
	}
	
	private void initLeft() {
		monitorButton = new MenuButton("Select monitor");
		
		sampleFormatter = new TextFormatter<>(new IntegerStringConverter());
		sparsityFormatter = new TextFormatter<>(new IntegerStringConverter());
		
		sampleField = new TextField();
		sparsityField = new TextField();
		sampleText = new Text("Sample size");
		sparsityText = new Text("Sample sparsity");
		
		sampleField.setTextFormatter(sampleFormatter);
		sparsityField.setTextFormatter(sparsityFormatter);
		
		sampleField.setVisible(false);
		sampleText.setVisible(false);
		sparsityField.setVisible(false);
		sparsityText.setVisible(false);

		leftPane.add(monitorButton, 0, 0, 2, 1);
		leftPane.add(sampleText, 0, 1);
		leftPane.add(sampleField, 1, 1);
		leftPane.add(sparsityText, 0, 2);
		leftPane.add(sparsityField, 1, 2);
	}
	
	private void initRight() {
		//TODO
	}
	
	private void initLedPane() {
		//TODO
	}
	
	private void initStatus() {
		Text statusText = new Text("Connection status: unknown\n1\n2\n3");
		statusPane.getChildren().addAll(statusText);
	}
	
	private void initCapture() {
		capture = new CaptureScreen();
	}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~UPDATE~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	private void updateLeft() {
		//Monitor selection
		int screenCounter = 0;
		GraphicsDevice[] screenList = capture.getScreens();
		for(GraphicsDevice d : screenList) {
			String screenText = String.format("Screen %d: %dx%d\n", screenCounter, d.getDisplayMode().getWidth(), d.getDisplayMode().getHeight());
			MenuItem selectMonitor = new MenuItem(screenText);
			monitorButton.getItems().add(selectMonitor);
			screenCounter++;
			
			selectMonitor.setOnAction(new EventHandler<ActionEvent>() {
				@Override public void handle(ActionEvent e) {
					capture.setScreen(d);
					monitorButton.setText(screenText);
					capture.initRead();
					updateLedPane();
					sampleField.setVisible(true);
					sampleText.setVisible(true);
				}
			});
		}

		//Sample size
		sampleField.setOnKeyPressed(event-> {
			if(event.getCode() == KeyCode.ENTER) {
				Integer sampleValue = sampleFormatter.getValue();
				if(sampleValue != null) {
					capture.setSample(Integer.parseInt(sampleField.getText()));
					if(!capture.sampleSparsityCheck()) {
						sampleField.clear();
						//TODO log
					}
					sparsityField.setVisible(true);
					sparsityText.setVisible(true);
					System.out.printf("Set sample to %d\n", sampleValue);
				}
			}
		});

		//Sparsity size
		sparsityField.setOnKeyPressed(event -> {
			if(event.getCode() == KeyCode.ENTER) {
				Integer sparsityValue = sparsityFormatter.getValue();
				if(sparsityValue != null) {
					capture.setSparsity(Integer.parseInt(sparsityField.getText()));
					if(!capture.sampleSparsityCheck()) {
						sparsityField.clear();
						//TODO log
					}
					else {
						System.out.printf("Set sparsity to %d\n", sparsityValue);
					}
				}
				if(!capture.sampleSparsityCheck()) {
					sampleField.clear();
					sparsityField.clear();
					sparsityField.setVisible(false);
					//TODO log
				}
			}
		});
	}
	
	private void updateRight() {
		//TODO
	}
	
	private void updateLedPane() {
		if(capture == null || capture.getScreenConfig() == null) {
			return;
		}
		if(ledPane.getChildren() != null) {
			ledPane.getChildren().clear();
		}
		int extraPadding = 40;
		double xRatio = (ledPane.getWidth() - extraPadding) / capture.getScreenConfig().screenWidth();
		double yRatio = (ledPane.getHeight() - extraPadding)/ capture.getScreenConfig().screenHeight();
		
		double ratio = Math.min(xRatio, yRatio);
		
		Rectangle monitorRep = new Rectangle(capture.getScreenConfig().screenWidth() * ratio, capture.getScreenConfig().screenHeight()*ratio);
		
		int xPadding = (int) ((ledPane.getWidth() - extraPadding) - capture.getScreenConfig().screenWidth()*ratio)/2;
		int yPadding = (int) ((ledPane.getHeight() - extraPadding) - capture.getScreenConfig().screenHeight()*ratio)/2;
		
		ledPane.getChildren().add(monitorRep);
		monitorRep.relocate(xPadding + extraPadding/2, yPadding + extraPadding/2);
	}
	
	private void updateStatus() {
		//TODO
	}

	public BorderPane getRoot() {
		return root;
	}
}
