import gui.NewGui;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {		
//		GuiComp gui = new GuiComp(33, 16);
		
		NewGui gui = new NewGui();
		
		Scene scene = new Scene(gui.getRoot(), 500, 500);
//		gui.startCaptureThread();
		stage.setMaximized(true);
		stage.setScene(scene);
		stage.show();
	}
	
}
