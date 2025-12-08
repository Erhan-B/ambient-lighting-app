package gui.logic;

import java.awt.GraphicsDevice;

import gui.visual.VisualGUI;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import monitor.capture.CaptureScreen;

public class LogicGUI {
	private VisualGUI view;
	private CaptureScreen capture;
	
	private Rectangle monitorRepresentation;
	private Rectangle ledRectangle;
	
	public LogicGUI(VisualGUI view, CaptureScreen capture) {
		this.view = view;
		this.capture = capture;
		
		logicLeft();
		logicLed();
	}
	private void logicLeft() {
		//Monitor selection
		int screenCounter = 0;
		MenuButton monitorButton = view.getMonitorButton();
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
					logicLed();
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
	
	private void logicLed() {
		Pane ledPane = view.getLedPane();
		if(capture == null || capture.getScreenConfig() == null) {
			return;
		}
		if(ledPane.getChildren() != null) {
			ledPane.getChildren().clear();
		}
		
		ledPane.setOnMouseClicked(event -> {
			System.out.printf("Led pane clicked at(%s,%s)\n", (int) event.getX(), (int) event.getY());
			if(capture.getScreenConfig().getSampleSize() <= 0) {
				System.out.println(capture.getScreenConfig().getSampleSize());
				return;
			}
			
			if(validLedClick(event.getX(), event.getY())) {
				System.out.println("Click is within monitor representation bounds");
				ledRectangle = new Rectangle(capture.getScreenConfig().getSampleSize(), capture.getScreenConfig().getSampleSize());
				ledRectangle.setFill(Color.GREY);
				ledRectangle.relocate(event.getX() - capture.getScreenConfig().getSampleSize()/2, event.getY() - capture.getScreenConfig().getSampleSize()/2);
				ledPane.getChildren().add(ledRectangle);
			}
		});
		
		int extraPadding = 40;
		double xRatio = (ledPane.getWidth() - extraPadding) / capture.getScreenConfig().getScreenWidth();
		double yRatio = (ledPane.getHeight() - extraPadding)/ capture.getScreenConfig().getScreenHeight();
		
		double ratio = Math.min(xRatio, yRatio);
		
		monitorRepresentation = new Rectangle(capture.getScreenConfig().getScreenWidth() * ratio, capture.getScreenConfig().getScreenHeight()*ratio);
		
		int monitorPaddingX = (int) (((ledPane.getWidth() - extraPadding) - capture.getScreenConfig().getScreenWidth()*ratio)/2);
		int monitorPaddingY = (int) (((ledPane.getHeight() - extraPadding) - capture.getScreenConfig().getScreenHeight()*ratio)/2);
		
		ledPane.getChildren().add(monitorRepresentation);
		monitorRepresentation.relocate(monitorPaddingX + extraPadding/2, monitorPaddingY + extraPadding/2);
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
	
	private void clampLED() {
		
	}
	
	private void updateStatus() {
		//TODO
	}
}
