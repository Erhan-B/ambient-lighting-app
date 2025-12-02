package gui;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class NewGui {
	private BorderPane root;
	private MenuBar menuBar;
	private GridPane leftPane;
	private GridPane rightPane;
	private GridPane statusPane;
	private Pane ledPane;
	
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
		root.setBottom(statusPane);;
	}
	
	private void styleLayout() {
		root.setStyle("-fx-background-color: #474848");
		menuBar.setStyle("-fx-background-color: #8e9091;");
		leftPane.setStyle("-fx-background-color: #a5a8a9; -fx-border-color: #8e9091; -fx-border-width: 3px;");
		rightPane.setStyle("-fx-background-color: #a5a8a9; -fx-border-color: #8e9091; -fx-border-width: 3px;");
		ledPane.setStyle("-fx-background-color: #767879;-fx-border-color: #8e9091; -fx-border-width: 3px;");
		statusPane.setStyle("-fx-background-color: #8e9091; -fx-border-color: #8e9091; -fx-border-width: 3px;");

		
		leftPane.setPrefWidth(100);
		leftPane.setMinHeight(40);
		
		rightPane.setPrefWidth(100);
		rightPane.setMinHeight(40);
		
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
//		Text testText = new Text("Just here to show the box");
//		leftPane.getChildren().add(testText);
//		leftPane.setPrefSize(200, 200);
	}
	
	private void initRight() {
//		Text testText = new Text("Just here to show the box");
//		rightPane.getChildren().add(testText);
	}
	
	private void initStatus() {
		Text statusText = new Text("Connection status: unknown\n1\n2\n3");
		statusPane.getChildren().addAll(statusText);
	}
	
	private void initLedPane() {

	}
	
	public BorderPane getRoot() {
		return root;
	}
}
