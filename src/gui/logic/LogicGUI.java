package gui.logic;

//TODO save profile


import java.awt.GraphicsDevice;
import java.util.ArrayList;
import java.util.List;

import gui.visual.VisualGUI;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import monitor.capture.CaptureScreen;

public class LogicGUI {
	private boolean debugMode;
	private boolean editingMode;
	
	private VisualGUI view;
	private CaptureScreen capture;
	private Rectangle monitorRepresentation;
	private List<Rectangle> ledList;
	
	public LogicGUI(VisualGUI view, CaptureScreen capture, boolean useProfile, boolean debugMode) {
		this.debugMode = debugMode;
		this.view = view;
		this.capture = capture;

		ledList = new ArrayList<>();
		
		if(useProfile) {
			loadProfile();
		}
		else {
			initProfile();
		}
	}
	
	private void loadProfile() {
		
	}
	
	private void initProfile() {
		logicLeft();
		logicLed();
	}
	
	private void logicLeft() {
		//Monitor selection
		int screenCounter = 0;
		MenuButton monitorButton = view.getMonitorButton();
		Button editingButton = view.getEditingButton();
		TextField sampleField = view.getSampleField();
		TextField sparsityField = view.getSparsityField();
		
		Text sampleText = view.getSampleText();
		Text sparsityText = view.getSparsityText();
		
		TextFormatter<Integer> sampleFormatter = view.getSampleFormatter();
		TextFormatter<Integer> sparsityFormatter = view.getSparsityFormatter();
		
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
					logicLed();
					logicRight();
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
					//TODO resize samples instead of clearing
					logicLed();
					logicRight();
					sparsityField.setVisible(true);
					sparsityText.setVisible(true);
					if(debugMode) {
						System.out.printf("Set sample to %d\n", sampleValue);
					}
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
						if(debugMode) {
							System.out.printf("Set sparsity to %d\n", sparsityValue);
						}
						capture.CaptureThread();
						editingButton.setVisible(true);
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
		
		editingButton.setOnMouseClicked(event -> {
			editingMode = !editingMode;
			if(editingMode) {
				editingButton.setStyle("-fx-background-color: #4444aa");
				view.getLedPane().setStyle("-fx-background-color: #767879;-fx-border-color: red; -fx-border-width: 10px; -fx-border-style: solid");
			}
			else {
				editingButton.setStyle("-fx-background-color: #007bff");
				view.getLedPane().setStyle("-fx-background-color: #767879;-fx-border-color: #8e9091; -fx-border-width: 3px;");
			}
		});
	}
	
	private void logicRight() {
		GridPane rightPane = view.getRightPane();
		Label screenDim = new Label(String.format("Monitor size: %sx%s", capture.getScreenConfig().getScreenWidth(), capture.getScreenConfig().getScreenHeight()));
		rightPane.add(screenDim, 0, 0);
		
	}
	
	private void logicLed() {
		Pane ledPane = view.getLedPane();
		if(capture == null || capture.getScreenConfig() == null) {
			return;
		}
		if(ledPane.getChildren() != null) {
			ledPane.getChildren().clear();
		}
		
		if(capture.getScreenConfig().getNumLeds() >= 0) {
			capture.getScreenConfig().setNumLeds(0);
		}
		
		int extraPadding = 40;
		double xRatio = (ledPane.getWidth() - extraPadding) / capture.getScreenConfig().getScreenWidth();
		double yRatio = (ledPane.getHeight() - extraPadding)/ capture.getScreenConfig().getScreenHeight();
		
		double ratio = Math.min(xRatio, yRatio);
		
		monitorRepresentation = new Rectangle(capture.getScreenConfig().getScreenWidth() * ratio, capture.getScreenConfig().getScreenHeight()*ratio);
		
		int monitorPaddingX = (int) (((ledPane.getWidth() - extraPadding) - capture.getScreenConfig().getScreenWidth()*ratio)/2);
		int monitorPaddingY = (int) (((ledPane.getHeight() - extraPadding) - capture.getScreenConfig().getScreenHeight()*ratio)/2);
		
		ledPane.getChildren().add(monitorRepresentation);
		monitorRepresentation.relocate(monitorPaddingX + extraPadding/2, monitorPaddingY + extraPadding/2);
		
		ledPane.setOnMouseClicked(event -> {
			//Nothing to do here
			if(!editingMode) {
				return;
			}
			double clickX = event.getX();
			double clickY = event.getY();
			
			double monitorX = monitorRepresentation.getLayoutX();
			double monitorY = monitorRepresentation.getLayoutY();
			
			int fullScreenX = (int) ((clickX - monitorX) / ratio);
			int fullScreenY = (int) ((clickY - monitorY) / ratio);
			
			fullScreenX = Math.max(0, Math.min(fullScreenX, capture.getScreenConfig().getScreenWidth() - 1));
			fullScreenY = Math.max(0, Math.min(fullScreenY, capture.getScreenConfig().getScreenHeight() - 1));
			if(validLedClick(clickX, clickY)) {
				capture.addSample(fullScreenX, fullScreenY);
				addLEDVisual(fullScreenX, fullScreenY, ledPane, ratio, monitorX, monitorY);
			}
			
			if(debugMode) {
				System.out.printf("Led pane clicked at(%s,%s)\n", (int) event.getX(), (int) event.getY());
			}			
		});
	}
	
	private boolean validLedClick(double x, double y) {
		double rectX = monitorRepresentation.getLayoutX();
		double rectY = monitorRepresentation.getLayoutY();
		double rectWidth = monitorRepresentation.getWidth();
		double rectHeight = monitorRepresentation.getHeight();
		if(x <= rectX + rectWidth && y <= rectY + rectHeight &&
				x >= rectX && y >= rectY) {
			return true;
		}
		return false;
	}
	
	private void addLEDVisual(int fullScreenX, int fullScreenY, Pane ledPane, double ratio, double monitorX, double monitorY) {
	    int currLedNum = capture.getScreenConfig().getNumLeds();
	    capture.getScreenConfig().setNumLeds(++currLedNum);

	    double ledSize = capture.getScreenConfig().getSampleSize();
	    Rectangle ledRectangle = new Rectangle(ledSize, ledSize);
	    ledRectangle.setFill(Color.GREY);

	    double paneX = fullScreenX*ratio + monitorX - ledSize/2;
	    double paneY = fullScreenY*ratio + monitorY - ledSize/2;

	    //Clamp led
	    double minX = monitorX;
	    double minY = monitorY;
	    double maxX = monitorX + monitorRepresentation.getWidth() - ledSize;
	    double maxY = monitorY + monitorRepresentation.getHeight() - ledSize;

	    paneX = Math.max(minX, Math.min(paneX, maxX));
	    paneY = Math.max(minY, Math.min(paneY, maxY));

	    ledRectangle.relocate(paneX, paneY);

	    //Add label
	    Label numLabel = new Label(String.valueOf(currLedNum));
	    numLabel.relocate(paneX + ledSize / 2, paneY + ledSize / 2);
	    numLabel.setTextFill(Color.WHITE);
	    Platform.runLater(()-> {
	    	ledPane.getChildren().addAll(ledRectangle, numLabel);
		    ledList.add(ledRectangle);
	    });
	    
	}
	
	public void updateLed(int index, int color) {
		double red = ((color >> 16) & 0xFF) / 255.0;
		double green = ((color >> 8) & 0xFF) / 255.0;
		double blue = (color & 0xFF) / 255.0;
		if(debugMode) {
			System.out.printf("index:%d R:%d G:%d B:%d\n", index, red, green, blue);			
		}

		Color rgb = new Color(red, green, blue, 1.0);
		Platform.runLater(() -> {
			ledList.get(index).setFill(rgb);
		});
	}
	
	private void logicStatus() {
		GridPane statusPane = view.getStatusPane();
		
		//TODO
	}
}
