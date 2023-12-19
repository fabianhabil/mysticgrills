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
import mysticgrills.view.menuitem.ManageMenuItem;
import mysticgrills.view.user.ManageUser;

public class AdminHome extends BorderPane {
	GlobalState gs = GlobalState.getInstance();

	VBox container;
	HBox buttonBox;
	Button manageUser;
	Button manageMenu;
	Button logout;
	Dialog dg;
	Text notice;

	public void initialize() {
		container = new VBox(8);
		buttonBox = new HBox();
		manageUser = new Button("Manage User");
		manageMenu = new Button("Manage Menu");
		logout = new Button("Logout");
		dg = new Dialog();
		notice = new Text();

		// Button Container
		buttonBox.getChildren().addAll(manageUser, manageMenu);
		buttonBox.setAlignment(Pos.CENTER);
		HBox.setMargin(manageUser, new Insets(10));

		// Container
		container.getChildren().addAll(notice, buttonBox, logout);
		container.setAlignment(Pos.CENTER);
	}

	public void fill() {
		User user = gs.getCurrentLoggedInUser();

		notice.setText("Hello " + user.getUserName() + "!\n Welcome To Mystic Grills!");
		notice.setFont(new Font(18));
		notice.setTextAlignment(TextAlignment.CENTER);
	}

	public void listenerFunction(Stage stage) {
		manageUser.setOnMouseClicked(e -> {
			stage.setScene(new Scene(new ManageUser(stage), 1366, 768));
		});

		manageMenu.setOnMouseClicked(e -> {
			stage.setScene(new Scene(new ManageMenuItem(stage), 1366, 768));
		});

		logout.setOnMouseClicked(e -> {
			gs.removeUser();
			dg.informationDialog("Success", "Success", "Logout Success");
			stage.setScene(new Scene(new Login(stage), 1366, 768));
		});
	}

	public AdminHome(Stage stage) {
		initialize();
		listenerFunction(stage);
		fill();
		setCenter(container);
	}

}
