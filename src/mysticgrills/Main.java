package mysticgrills;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mysticgrills.view.Register;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setScene(new Scene(new Register(stage), 1366, 768));
		stage.show();
	}

}