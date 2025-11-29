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
		
		GuiComp gui = new GuiComp(stage, 33, 16);
		Scene scene = new Scene(gui.getRoot(), 500, 500);
		gui.startCaptureThread();
		stage.setMaximized(true);
		stage.setScene(scene);
		stage.show();
	}
	
}
