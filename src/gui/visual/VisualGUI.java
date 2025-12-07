package gui.visual;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.converter.IntegerStringConverter;
import monitor.capture.CaptureScreen;


public class VisualGUI {	
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
	public VisualGUI(CaptureScreen capture) {
		this.capture = capture;
		
		root = new BorderPane();
		menuBar = new MenuBar();
		leftPane = new GridPane();
		rightPane = new GridPane();
		statusPane = new GridPane();
		ledPane = new Pane();
		
		initLayout();
		styleLayout();
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

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~GETTERS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public BorderPane getRoot() {
		return root;
	}

	/**
	 * @return the capture
	 */
	public CaptureScreen getCapture() {
		return capture;
	}

	/**
	 * @param capture the capture to set
	 */
	public void setCapture(CaptureScreen capture) {
		this.capture = capture;
	}

	/**
	 * @return the menuBar
	 */
	public MenuBar getMenuBar() {
		return menuBar;
	}

	/**
	 * @param menuBar the menuBar to set
	 */
	public void setMenuBar(MenuBar menuBar) {
		this.menuBar = menuBar;
	}

	/**
	 * @return the leftPane
	 */
	public GridPane getLeftPane() {
		return leftPane;
	}

	/**
	 * @param leftPane the leftPane to set
	 */
	public void setLeftPane(GridPane leftPane) {
		this.leftPane = leftPane;
	}

	/**
	 * @return the rightPane
	 */
	public GridPane getRightPane() {
		return rightPane;
	}

	/**
	 * @param rightPane the rightPane to set
	 */
	public void setRightPane(GridPane rightPane) {
		this.rightPane = rightPane;
	}

	/**
	 * @return the statusPane
	 */
	public GridPane getStatusPane() {
		return statusPane;
	}

	/**
	 * @param statusPane the statusPane to set
	 */
	public void setStatusPane(GridPane statusPane) {
		this.statusPane = statusPane;
	}

	/**
	 * @return the ledPane
	 */
	public Pane getLedPane() {
		return ledPane;
	}

	/**
	 * @param ledPane the ledPane to set
	 */
	public void setLedPane(Pane ledPane) {
		this.ledPane = ledPane;
	}

	/**
	 * @return the monitorButton
	 */
	public MenuButton getMonitorButton() {
		return monitorButton;
	}

	/**
	 * @param monitorButton the monitorButton to set
	 */
	public void setMonitorButton(MenuButton monitorButton) {
		this.monitorButton = monitorButton;
	}

	/**
	 * @return the sampleField
	 */
	public TextField getSampleField() {
		return sampleField;
	}

	/**
	 * @param sampleField the sampleField to set
	 */
	public void setSampleField(TextField sampleField) {
		this.sampleField = sampleField;
	}

	/**
	 * @return the sparsityField
	 */
	public TextField getSparsityField() {
		return sparsityField;
	}

	/**
	 * @param sparsityField the sparsityField to set
	 */
	public void setSparsityField(TextField sparsityField) {
		this.sparsityField = sparsityField;
	}

	/**
	 * @return the sampleText
	 */
	public Text getSampleText() {
		return sampleText;
	}

	/**
	 * @param sampleText the sampleText to set
	 */
	public void setSampleText(Text sampleText) {
		this.sampleText = sampleText;
	}

	/**
	 * @return the sparsityText
	 */
	public Text getSparsityText() {
		return sparsityText;
	}

	/**
	 * @param sparsityText the sparsityText to set
	 */
	public void setSparsityText(Text sparsityText) {
		this.sparsityText = sparsityText;
	}

	/**
	 * @return the sampleFormatter
	 */
	public TextFormatter<Integer> getSampleFormatter() {
		return sampleFormatter;
	}

	/**
	 * @param sampleFormatter the sampleFormatter to set
	 */
	public void setSampleFormatter(TextFormatter<Integer> sampleFormatter) {
		this.sampleFormatter = sampleFormatter;
	}

	/**
	 * @return the sparsityFormatter
	 */
	public TextFormatter<Integer> getSparsityFormatter() {
		return sparsityFormatter;
	}

	/**
	 * @param sparsityFormatter the sparsityFormatter to set
	 */
	public void setSparsityFormatter(TextFormatter<Integer> sparsityFormatter) {
		this.sparsityFormatter = sparsityFormatter;
	}

	/**
	 * @param root the root to set
	 */
	public void setRoot(BorderPane root) {
		this.root = root;
	}
	
	
}
