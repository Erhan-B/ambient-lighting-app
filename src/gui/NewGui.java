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
import javafx.scene.text.Text;
import javafx.util.converter.IntegerStringConverter;
import monitor.capture.CaptureScreen;


public class NewGui {
	private BorderPane root;
	private MenuBar menuBar;
	private GridPane leftPane;
	private GridPane rightPane;
	private GridPane statusPane;
	private Pane ledPane;
	
	private CaptureScreen capture;
	
	//Gui components
	private MenuButton monitorButton;
	private TextField sampleField;
	private TextField sparsityField;
	private TextFormatter<Integer> sampleFormatter;
	private TextFormatter<Integer> sparsityFormatter;
	
	public NewGui() {
		root = new BorderPane();
		menuBar = new MenuBar();
		leftPane = new GridPane();
		rightPane = new GridPane();
		statusPane = new GridPane();
		ledPane = new Pane();
		
		initLayout();
		styleLayout();
		Platform.runLater(() -> {
			updateLayout();
		});
	}
	
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
		Text sampleText = new Text("Sample size");
		Text sparsityText = new Text("Sample sparsity");
		
		sampleField.setTextFormatter(sampleFormatter);
		sparsityField.setTextFormatter(sparsityFormatter);
		sparsityField.setVisible(false);

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
					initLedPane();
					capture.initRead();
				}
			});
		}

		//Sample size
		sampleField.setOnKeyPressed(event-> {
			if(event.getCode() == KeyCode.ENTER) {
				Integer sampleValue = sampleFormatter.getValue();
				if(sampleValue != null) {
					if(!capture.setSample(Integer.parseInt(sampleField.getText()))) {
						sampleField.clear();
						//TODO log
					}
					sparsityField.setVisible(true);
					System.out.printf("Set sample to %d\n", sampleValue);
				}
			}
		});
		
		
		//Sparsity size
		sparsityField.setOnKeyPressed(event -> {
			if(event.getCode() == KeyCode.ENTER) {
				Integer sparsityValue = sparsityFormatter.getValue();
				if(sparsityValue != null) {
					if(!capture.setSparsity(Integer.parseInt(sparsityField.getText()))) {
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
		System.out.printf("ledPane: %d capture: %d\n", (int) ledPane.getWidth(), (int) capture.getScreenConfig().screenWidth());
		double xRatio = ledPane.getWidth() / capture.getScreenConfig().screenWidth();
		System.out.println(xRatio);
	}
	
	private void updateStatus() {
		//TODO
	}

	public BorderPane getRoot() {
		return root;
	}
}
