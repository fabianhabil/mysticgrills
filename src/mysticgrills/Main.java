package mysticgrills;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mysticgrills.view.home.Register;
import mysticgrills.view.menuitem.ManageMenuItem;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setScene(new Scene(new ManageMenuItem(stage), 1366, 768));
		stage.show();
	}

}