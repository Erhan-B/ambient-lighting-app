package gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import monitor.capture.CaptureScreen;

public class GUI extends Application{
	
	public static void main(String[] args) {
		CaptureScreen screen = new CaptureScreen();
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		GridPane root = new GridPane();
		
		GuiComp gui = new GuiComp(stage, root, 33, 16);
		Scene scene = new Scene(root, 500, 500);
		gui.updateLed(0, 0, 255, 255, 255);
		gui.updateLed(0, 1, 200, 0, 150);
		stage.setMaximized(true);
		stage.setScene(scene);
		stage.show();
	}
	
}
