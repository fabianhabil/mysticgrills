package mysticgrills.model;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import mysticgrills.DatabaseConnection;

public class Receipt {

	private Integer receiptId;
	private Order receiptOrder;
	private Integer receiptPaymentAmount;
	private Date receiptPaymentDate;
	private String receiptPaymentType;

	private DatabaseConnection db = DatabaseConnection.getInstance();

	public Receipt(Integer receiptId, Order receiptOrder, Integer receiptPaymentAmount, Date receiptPaymentDate,
			String receiptPaymentType) {
		this.receiptId = receiptId;
		this.receiptOrder = receiptOrder;
		this.receiptPaymentAmount = receiptPaymentAmount;
		this.receiptPaymentDate = receiptPaymentDate;
		this.receiptPaymentType = receiptPaymentType;
	}

	public Receipt() {

	}

	public Integer getReceiptId() {
		return receiptId;
	}

	public void setReceiptId(Integer receiptId) {
		this.receiptId = receiptId;
	}

	public Order getReceiptOrder() {
		return receiptOrder;
	}

	public void setReceiptOrder(Order receiptOrder) {
		this.receiptOrder = receiptOrder;
	}

	public Integer getReceiptPaymentAmount() {
		return receiptPaymentAmount;
	}

	public void setReceiptPaymentAmount(Integer receiptPaymentAmount) {
		this.receiptPaymentAmount = receiptPaymentAmount;
	}

	public Date getReceiptPaymentDate() {
		return receiptPaymentDate;
	}

	public void setReceiptPaymentDate(Date receiptPaymentDate) {
		this.receiptPaymentDate = receiptPaymentDate;
	}

	public String getReceiptPaymentType() {
		return receiptPaymentType;
	}

	public void setReceiptPaymentType(String receiptPaymentType) {
		this.receiptPaymentType = receiptPaymentType;
	}

	public Boolean createReceipt(Order order, String receiptPaymentType, Double receiptPaymentAmount,
			Date receiptPaymentDate) {
		String query = String.format(
				"INSERT INTO `receipts`(`receiptOrder`, `receiptPaymentAmount`, `receiptPaymentType`, `receiptPaymentDate`) VALUES ('%s','%s','%s','%s')",
				order.getOrderId(), receiptPaymentAmount, receiptPaymentType, receiptPaymentDate);
		return db.execute(query);
	}

	public Receipt getReceiptById(Integer receiptId) {
		String query = "SELECT * FROM `receipts` WHERE `receiptId` = " + receiptId;
		ResultSet rs = db.selectData(query);

		Receipt receipt = null;

		try {
			while (rs.next()) {
				Integer receiptIdDb = rs.getInt("receiptId");
				Integer receiptOrder = rs.getInt("receiptOrder");
				Integer receiptPaymentAmount = rs.getInt("receiptPaymentAmount");
				Date receiptPaymentDate = rs.getDate("receiptPaymentDate");
				String receiptPaymentType = rs.getString("receiptPaymentType");

				Order order = new Order();
				order.setOrderId(receiptOrder);

				receipt = new Receipt(receiptIdDb, order, receiptPaymentAmount, receiptPaymentDate, receiptPaymentType);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return receipt;
	}

	public ArrayList<Receipt> getAllReceipts() {
		String query = "SELECT * FROM `receipts`";
		ResultSet rs = db.selectData(query);

		ArrayList<Receipt> receipts = new ArrayList<Receipt>();

		try {
			while (rs.next()) {
				Integer receiptIdDb = rs.getInt("receiptId");
				Integer receiptOrder = rs.getInt("receiptOrder");
				Integer receiptPaymentAmount = rs.getInt("receiptPaymentAmount");
				Date receiptPaymentDate = rs.getDate("receiptPaymentDate");
				String receiptPaymentType = rs.getString("receiptPaymentType");

				Order order = new Order();
				order.setOrderId(receiptOrder);

				receipts.add(
						new Receipt(receiptIdDb, order, receiptPaymentAmount, receiptPaymentDate, receiptPaymentType));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return receipts;
	}

}
