package mysticgrills.controller;

import java.sql.Date;
import java.util.ArrayList;

import mysticgrills.model.Order;
import mysticgrills.model.Receipt;

public class ReceiptController {
	private static ReceiptController rc;

	Receipt receipt;

	private ReceiptController() {
		receipt = new Receipt();
	}

	public static ReceiptController getInstance() {
		if (rc == null) {
			rc = new ReceiptController();
		}

		return rc;
	}

	public Boolean createReceipt(Order order, String receiptPaymentType, Double receiptPaymentAmount,
			Date receiptPaymentDate) {
		return receipt.createReceipt(order, receiptPaymentType, receiptPaymentAmount, receiptPaymentDate);
	}

	public Receipt getReceiptById(Integer receiptId) {
		return receipt.getReceiptById(receiptId);
	}

	public ArrayList<Receipt> getAllReceipts() {
		return receipt.getAllReceipts();
	}
}
