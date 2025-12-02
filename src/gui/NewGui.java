package gui;

import java.awt.GraphicsDevice;

import javafx.application.Platform;
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
	
	public NewGui() {
		root = new BorderPane();
		menuBar = new MenuBar();
		leftPane = new GridPane();
		rightPane = new GridPane();
		statusPane = new GridPane();
		ledPane = new Pane();
		
		initLayout();
		styleLayout();
	}
	
	private void initLayout() {
		initMenu();
		initLeft();
		initRight();
		initStatus();
		initLedPane();
		
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
	
	private void initMenu() {
		Menu fileMenu = new Menu("File");
		Menu helpMenu = new Menu("Help");
		
		menuBar.getMenus().addAll(fileMenu, helpMenu);
	}
	
	private void initLeft() {
		MenuButton monitorButton = new MenuButton("Select monitor");
		
		TextField sampleField = new TextField();
		Text sampleText = new Text("Sample size ");
		TextFormatter<Integer> sampleFormatter = new TextFormatter<>(new IntegerStringConverter());
		TextFormatter<Integer> sparcityFormatter = new TextFormatter<>(new IntegerStringConverter());
		
		sampleField.setTextFormatter(sampleFormatter);
		
		TextField sparcityField = new TextField();
		Text sparcityText = new Text("Sample sparcity ");
		
		sparcityField.setTextFormatter(sparcityFormatter);
		
		Platform.runLater(() -> {
			//Monitor selection
			int screenCounter = 0;
			GraphicsDevice[] screenList = capture.getScreens();
			for(GraphicsDevice d : screenList) {
				String screen = String.format("Screen %d: %dx%d", screenCounter, d.getDisplayMode().getWidth(), d.getDisplayMode().getHeight());
				MenuItem selectMonitor = new MenuItem(screen);
//				System.out.println(screen);
				monitorButton.getItems().add(selectMonitor);
				screenCounter++;
				
				selectMonitor.setOnAction(new EventHandler<ActionEvent>() {
					@Override public void handle(ActionEvent e) {
						capture.setScreen(d);
//						System.out.printf("Set screen to: %s\n", d);
						monitorButton.setText(screen);
					}
				});
			}
			
			//Sample size
			sampleField.setOnKeyPressed(event-> {
				if(event.getCode() == KeyCode.ENTER) {
					Integer sampleValue = sampleFormatter.getValue();
					if(sampleValue != null) {
						capture.setSample(Integer.parseInt(sampleField.getText()));
//						System.out.printf("Set sample to %d\n", sampleValue);
					}
				}
			});
			
			
			//Sparcity
			sparcityField.setOnKeyPressed(event -> {
				if(event.getCode() == KeyCode.ENTER) {
					Integer sparcityValue = sparcityFormatter.getValue();
					if(sparcityValue != null) {
						capture.setSparcity(Integer.parseInt(sparcityField.getText()));
//						System.out.printf("Set sparcity to %d\n", sparcityValue);
					}
				}
			});
		});

		leftPane.add(monitorButton, 0, 0, 2, 1);
		leftPane.add(sampleText, 0, 1);
		leftPane.add(sampleField, 1, 1);
		leftPane.add(sparcityText, 0, 2);
		leftPane.add(sparcityField, 1, 2);
	}
	
	private void initRight() {
		//TODO
	}
	
	private void initStatus() {
		Text statusText = new Text("Connection status: unknown\n1\n2\n3");
		statusPane.getChildren().addAll(statusText);
	}
	
	private void initLedPane() {
		//TODO
	}
	
	private void initCapture() {
		capture = new CaptureScreen();
	}
	
	public BorderPane getRoot() {
		return root;
	}
}
