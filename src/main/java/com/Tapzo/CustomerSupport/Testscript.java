package com.Tapzo.CustomerSupport;

import java.sql.ResultSet;
import java.sql.Statement;
import org.testng.annotations.Test;

import Utility.Utilities;

public class Testscript extends Utilities {
	
	AnswerResponse answer;
	
	@Test(groups = "Recharge")
	public void RechargePending() throws Exception {

		int order_id = createOrder();

		// My payment got deducted but recharge did not happen.
		int question_id = 7000;

		// Get db connection
		Statement s = MyConnection.getConnection("172.16.0.75", "3303", "appuser", "appuser");

		// get the order_details
		ResultSet rs = MyConnection.execute(s, "select * from orders.orders where order_id=" + order_id);

		// rs.next();

		if (!rs.getString("order_status").equals("3")) {
			changeOrderStatus(s, order_id, 3);
			validateAnswer(question_id, 59, order_id);
		}

		rs = MyConnection.execute(s, "select * from orders.orders where order_id=" + order_id);
		if (!rs.getString("order_status").equals("6")) {
			changeOrderStatus(s, order_id, 6);
			validateAnswer(question_id, 59, order_id);
		}

		rs = MyConnection.execute(s, "select * from orders.orders where order_id=" + order_id);
		if (!rs.getString("order_status").equals("4")) {
			changeOrderStatus(s, order_id, 4);
			// changeOrderDate(s, order_id, -1);
			validateAnswer(question_id, 64, order_id);
		}

		changeOrderStatus(s, order_id, 3);
		rs = MyConnection.execute(s, "select * from orders.orders where order_id=" + order_id);
		if (!rs.getString("order_status").equals("4")) {
			changeOrderStatus(s, order_id, 4);
			changeOrderDate(s, order_id, -3);
			validateAnswer(question_id, 67, order_id);
		}

		changeOrderStatus(s, order_id, 3);
		rs = MyConnection.execute(s, "select * from orders.orders where order_id=" + order_id);
		if (!rs.getString("order_status").equals("4")) {
			changeOrderStatus(s, order_id, 4);
			MyConnection.update(s,
					"INSERT INTO `orders`.`merchant_transactions` ( `order_id`, `amount`, `create_at`, `last_try_datetime`, `merchant_transaction_id`, `status`, `retry_count`, `status_text`, `enquiry_count`, `request_id`, `operator_ref_id`) VALUES ("
							+ order_id
							+ " , '10.00', '2017-02-16 13:37:45', '2017-02-16 13:37:45', '170668692', '1', '0', 'Response code:1, Transaction Successful', '0', 'docker0328386130r0', '3260486320')");
			validateAnswer(question_id, 31, order_id);
		}

		rs = MyConnection.execute(s, "select * from orders.orders where order_id=" + order_id);
		if (!rs.getString("order_status").equals("7")) {
			changeOrderStatus(s, order_id, 7);
			changeOrderDate(s, order_id, -1);
			try {
				rs = MyConnection.execute(s, "select * from orders.merchant_transactions where order_id=" + order_id);
			} catch (Exception e) {
				MyConnection.update(s,
						"INSERT INTO `orders`.`merchant_transactions` ( `order_id`, `amount`, `create_at`, `last_try_datetime`, `merchant_transaction_id`, `status`, `retry_count`, `status_text`, `enquiry_count`, `request_id`, `operator_ref_id`) VALUES ("
								+ order_id
								+ " , '10.00', '2017-02-16 13:37:45', '2017-02-16 13:37:45', '170668692', '1', '0', 'Response code:1, Transaction Successful', '0', 'docker0328386130r0', '3260486320')");
			}
			validateAnswer(question_id, 66, order_id);
		}
		changeOrderStatus(s, order_id, 3);
		rs = MyConnection.execute(s, "select * from orders.orders where order_id=" + order_id);
		if (!rs.getString("order_status").equals("7")) {
			changeOrderStatus(s, order_id, 7);
			changeOrderDate(s, order_id, -3);
			try {
				rs = MyConnection.execute(s, "select * from orders.merchant_transactions where order_id=" + order_id);
			} catch (Exception e) {
				MyConnection.update(s,
						"INSERT INTO `orders`.`merchant_transactions` ( `order_id`, `amount`, `create_at`, `last_try_datetime`, `merchant_transaction_id`, `status`, `retry_count`, `status_text`, `enquiry_count`, `request_id`, `operator_ref_id`) VALUES ("
								+ order_id
								+ " , '10.00', '2017-02-16 13:37:45', '2017-02-16 13:37:45', '170668692', '1', '0', 'Response code:1, Transaction Successful', '0', 'docker0328386130r0', '3260486320')");
			}
			validateAnswer(question_id, 49, order_id);
		}
	}

}
