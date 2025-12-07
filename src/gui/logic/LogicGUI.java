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
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import monitor.capture.CaptureScreen;

public class LogicGUI {
	private VisualGUI view;
	private CaptureScreen capture;
	
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
}
