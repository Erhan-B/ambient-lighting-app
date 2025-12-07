import gui.logic.LogicGUI;
import gui.visual.VisualGUI;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import monitor.capture.CaptureScreen;

public class Main extends Application{
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {		
//		GuiComp gui = new GuiComp(33, 16);
		CaptureScreen capture = new CaptureScreen();
		VisualGUI view = new VisualGUI(capture);
		LogicGUI logic = new LogicGUI(view, capture);
		
		Scene scene = new Scene(view.getRoot(), 500, 500);
//		gui.startCaptureThread();
		stage.setMaximized(true);
		stage.setScene(scene);
		stage.show();
	}
	
}
