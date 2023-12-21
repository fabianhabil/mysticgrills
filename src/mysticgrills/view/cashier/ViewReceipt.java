package mysticgrills.view.cashier;

import java.sql.Date;
import java.util.ArrayList;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.control.TableView;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Callback;
import mysticgrills.controller.ReceiptController;
import mysticgrills.model.Receipt;
import mysticgrills.utils.Dialog;
import mysticgrills.view.home.CashierHome;

public class ViewReceipt extends BorderPane {
	VBox container, titleContainer, buttonContainer;
	HBox buttonBox;

	ScrollPane containerTable;
	TableView<Receipt> tableReceipt;
	ArrayList<Receipt> receipts;
	Button viewReceiptButton, backButton;
	Text title;

	Dialog dg;
	Receipt selectedReceipt;
	private ReceiptController receiptController = ReceiptController.getInstance();

	public void initialize() {
		container = new VBox(8);
		buttonContainer = new VBox(4);
		titleContainer = new VBox();

		buttonBox = new HBox(8);

		containerTable = new ScrollPane();
		tableReceipt = new TableView<Receipt>();

		viewReceiptButton = new Button("View Receipt Detail");
		backButton = new Button("Back");

		title = new Text("View Receipt");

		dg = new Dialog();
	}

	public void style() {
		title.setFont(new Font(24));
		title.setTextAlignment(TextAlignment.CENTER);

		titleContainer.getChildren().add(title);
		titleContainer.setAlignment(Pos.CENTER);

		tableReceipt.setMaxHeight(600);
		tableReceipt.setPadding(new Insets(10, 10, 10, 10));

		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.setVisible(false);

		buttonBox.getChildren().setAll(viewReceiptButton);
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.setVisible(false);

		containerTable.setContent(tableReceipt);
		containerTable.setHbarPolicy(ScrollBarPolicy.NEVER);
		containerTable.setMinWidth(700);
		containerTable.setFitToWidth(true);

		buttonContainer.getChildren().setAll(buttonBox, backButton);
		buttonContainer.setAlignment(Pos.CENTER);

		container.getChildren().setAll(titleContainer, containerTable, buttonContainer);
		container.setPadding(new Insets(16));

		setTable();
	}

	public void setTable() {
		TableColumn<Receipt, Integer> idColumn = new TableColumn<Receipt, Integer>("ID");
		TableColumn<Receipt, Integer> orderIdColumn = new TableColumn<Receipt, Integer>("Order Id");
		TableColumn<Receipt, Date> dateColumn = new TableColumn<Receipt, Date>("Date");
		TableColumn<Receipt, String> typeColumn = new TableColumn<Receipt, String>("Payment Type");

		tableReceipt.getColumns().addAll(idColumn, orderIdColumn, dateColumn, typeColumn);

		idColumn.setCellValueFactory(new PropertyValueFactory("receiptId"));

		// Accessing value in nested objects, we need to make custom for the
		// PropertyValueFactory
		orderIdColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Receipt, Integer>, ObservableValue<Integer>>() {
					@Override
					public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Receipt, Integer> param) {
						return new SimpleObjectProperty<>(param.getValue().getReceiptOrder().getOrderId());
					}
				});

		dateColumn.setCellValueFactory(new PropertyValueFactory("receiptPaymentDate"));
		typeColumn.setCellValueFactory(new PropertyValueFactory("receiptPaymentType"));

		refreshTable();
	}

	public void refreshTable() {
		receipts = receiptController.getAllReceipts();
		tableReceipt.getItems().clear();
		tableReceipt.getItems().addAll(receipts);

		tableReceipt.setOnMouseClicked(tableMouseEvent());
	}

	// to handle if user press a row in table
	private EventHandler<MouseEvent> tableMouseEvent() {
		return new EventHandler<MouseEvent>() {
			// get order data from pressed table row
			@Override
			public void handle(MouseEvent event) {
				TableSelectionModel<Receipt> tableSelectionModel = tableReceipt.getSelectionModel();
				tableSelectionModel.setSelectionMode(SelectionMode.SINGLE);

				// get data from selected model
				selectedReceipt = tableSelectionModel.getSelectedItem();
				buttonBox.setVisible(true);
			}
		};
	}

	public void eventListener(Stage stage) {
		backButton.setOnMouseClicked(e -> {
			stage.setScene(new Scene(new CashierHome(stage), 1366, 768));
		});

		viewReceiptButton.setOnMouseClicked(e -> {
			if (selectedReceipt == null) {
				dg.informationDialog("Error", "Error Message", "Please select receipt first!");
			}

			else {
				stage.setScene(new Scene(
						new ViewDetailedReceipt(stage, selectedReceipt.getReceiptOrder().getOrderId(), selectedReceipt),
						1366, 768));
			}
		});

	}

	public ViewReceipt(Stage stage) {
		stage.setTitle("View All Receipt");
		initialize();
		style();
		setCenter(container);
		eventListener(stage);
	}

}
