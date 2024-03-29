package mysticgrills.view.home;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import mysticgrills.GlobalState;
import mysticgrills.model.User;
import mysticgrills.utils.Dialog;
import mysticgrills.view.Login;
import mysticgrills.view.order.ViewOrder;

public class KitchenHome extends BorderPane {

	GlobalState gs = GlobalState.getInstance();

	VBox container;
	HBox buttonBox;
	Button viewOrder;
	Button logout;
	Dialog dg;
	Text notice;

	public void initialize() {
		container = new VBox(8);
		buttonBox = new HBox();
		viewOrder = new Button("View Order");
		logout = new Button("Logout");
		notice = new Text();
		dg = new Dialog();

		// Button Container
		buttonBox.getChildren().addAll(viewOrder);
		buttonBox.setAlignment(Pos.CENTER);

		// Container
		container.getChildren().addAll(notice, buttonBox, logout);
		container.setAlignment(Pos.CENTER);
	}

	public void fill() {
		User user = gs.getCurrentLoggedInUser();

		notice.setText("Hello " + user.getUserName() + "!\n\nWelcome To Mystic Grills!");
		notice.setFont(new Font(18));
		notice.setTextAlignment(TextAlignment.CENTER);
	}

	public void listenerFunction(Stage stage) {
		// Redirect to add order menu
		viewOrder.setOnMouseClicked(e -> {
			stage.setScene(new Scene(new ViewOrder(stage), 1366, 768));
		});

		// Logout
		logout.setOnMouseClicked(e -> {
			gs.removeUser();
			dg.informationDialog("Success", "Success", "Logout Success");
			stage.setScene(new Scene(new Login(stage), 1366, 768));
		});
	}

	public KitchenHome(Stage stage) {
		stage.setTitle("Kitchen");
		initialize();
		fill();
		listenerFunction(stage);
		setCenter(container);
	}

}
