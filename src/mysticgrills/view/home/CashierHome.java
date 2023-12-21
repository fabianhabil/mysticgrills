package mysticgrills.view.home;

import javafx.geometry.Insets;
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
import mysticgrills.view.cashier.ViewReceipt;
import mysticgrills.view.order.ViewOrder;

public class CashierHome extends BorderPane {
	GlobalState gs = GlobalState.getInstance();

	VBox container;
	HBox buttonBox;
	Button viewReceipt;
	Button viewOrder;
	Button logout;
	Dialog dg;
	Text notice;

	public void initialize() {
		container = new VBox(8);
		buttonBox = new HBox();
		viewReceipt = new Button("View Receipt");
		viewOrder = new Button("View Order");
		logout = new Button("Logout");
		notice = new Text();
		dg = new Dialog();

		// Button Container
		buttonBox.getChildren().addAll(viewReceipt, viewOrder);
		buttonBox.setAlignment(Pos.CENTER);
		HBox.setMargin(viewReceipt, new Insets(10));

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
		viewReceipt.setOnMouseClicked(e -> {
			stage.setScene(new Scene(new ViewReceipt(stage), 1366, 768));
		});

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

	public CashierHome(Stage stage) {
		stage.setTitle("Home");
		initialize();
		fill();
		listenerFunction(stage);
		setCenter(container);
	}

}
