package mysticgrills;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Main extends Application {

	Scene scene;
	GridPane gp;
	FlowPane fp;
	BorderPane bp;

	VBox registerContainer, registerForm;
	HBox genderContainer;
	Label nameLbl, passwordLbl, genderLbl, nationalityLbl, ageLbl, DOBLbl, Register;
	TextField nameField;
	PasswordField password;
	RadioButton rbMale, rbFemale;
	ComboBox<String> nationality;
	Spinner<Integer> age;
	DatePicker DOB;
	CheckBox terms;
	Button registerBtn;

	public void gridPaneTest() {
		gp = new GridPane();

		gp.add(new Label("Label 1"), 0, 0);
		gp.add(new Label("Label 2"), 1, 3);
		gp.add(new Label("Label 3"), 3, 1);

		scene = new Scene(gp);
	}

	public void flowPaneTest() {
		fp = new FlowPane();
		fp.getChildren().addAll(new Label("Label 1"), new Label("Label 2"));
		fp.setHgap(30);
		scene = new Scene(fp);
	}

	public void borderPaneTest() {
		bp = new BorderPane();

		nameLbl = new Label("Username");
		Register = new Label("Register");
		passwordLbl = new Label("Password");
		genderLbl = new Label("Gender");
		nationalityLbl = new Label("Nationality");
		ageLbl = new Label("Age");
		DOBLbl = new Label("DOB");

		registerContainer = new VBox();
		registerForm = new VBox();
		genderContainer = new HBox();

		nameField = new TextField();
		password = new PasswordField();
		rbMale = new RadioButton();
		rbFemale = new RadioButton();
		nationality = new ComboBox<>();
		age = new Spinner<>(1, 20, 10);
		DOB = new DatePicker();
		terms = new CheckBox("Agree to Terms & Agreements");
		registerBtn = new Button("test lagi LAGI DEH");
		
		scene = new Scene(bp, 500, 650);
		
		this.layoutingBP();

	}

	public void initialize() {
		this.borderPaneTest();
	}

	public void layoutingBP() {
		registerContainer.getChildren().addAll(Register, registerForm, registerBtn);
		registerForm.getChildren().addAll(nameLbl, nameField, passwordLbl, password, genderLbl, genderContainer,
				nationalityLbl, age, DOBLbl, DOB, terms);
		genderContainer.getChildren().addAll(rbMale, rbFemale);
		bp.setCenter(registerContainer);
		
		nationality.getItems().addAll("Indonesia", "Malaysia", "Singapore", "Hongkong");
		nationality.getSelectionModel().selectFirst();
		
		ToggleGroup genderGroup = new ToggleGroup();
		rbMale.setToggleGroup(genderGroup);
		rbFemale.setToggleGroup(genderGroup);
		
		DOB.setEditable(false);
		
		nameField.setMaxWidth(200);
		password.setMaxWidth(200);
		
		registerContainer.setAlignment(Pos.CENTER);
		registerForm.setMaxWidth(200);
		
		Register.setFont(Font.font("Arial", FontWeight.BOLD, 30));
	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		this.initialize();
		stage.setScene(scene);
		stage.setTitle("Mystic Grills");
		stage.show();
	}

}