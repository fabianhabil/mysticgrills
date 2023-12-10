package mysticgrills.model;

import java.sql.Date;

public class Receipt {

	private Integer receiptId;
	private Order receiptOrder;
	private Integer receiptPaymentAmount;
	private Date receiptPaymentDate;
	private String receiptPaymentType;

	public Receipt(Integer receiptId, Order receiptOrder, Integer receiptPaymentAmount, Date receiptPaymentDate,
			String receiptPaymentType) {
		this.receiptId = receiptId;
		this.receiptOrder = receiptOrder;
		this.receiptPaymentAmount = receiptPaymentAmount;
		this.receiptPaymentDate = receiptPaymentDate;
		this.receiptPaymentType = receiptPaymentType;
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

}
