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
		boolean debugMode = true;
//		GuiComp gui = new GuiComp(33, 16);
		CaptureScreen capture = new CaptureScreen(debugMode);
		VisualGUI view = new VisualGUI(capture, debugMode);
		LogicGUI logic = new LogicGUI(view, capture, false, debugMode);
		capture.setLogic(logic);
		
		Scene scene = new Scene(view.getRoot(), 500, 500);
		
		stage.setMaximized(true);
		stage.setScene(scene);
		stage.show();
	}
	
}
