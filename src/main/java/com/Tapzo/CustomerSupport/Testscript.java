package com.Tapzo.CustomerSupport;

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.testng.annotations.Test;

import Utility.Utilities;

public class Testscript extends Utilities {

	AnswerResponse answer;

	@Test(groups = "Recharge", enabled = false)
	public void PaymentPending() throws Exception {

		int order_id = createOrder();
		System.out.println(order_id);

		// My payment got deducted but recharge did not happen.
		int question_id = 7000;

		// Get db connection
		Statement s = MyConnection.getConnection("172.16.0.75", "3303", "appuser", "appuser");

		// get the order_details
		ResultSet rs = MyConnection.execute(s, "select * from orders.orders where order_id=" + order_id);

		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = new GregorianCalendar();
		Timestamp dt = rs.getTimestamp("order_date", calendar);
		String order_date = sdf.format(dt);
		String transaction_id = rs.getString("transaction_id");

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
			changeOrderDate(s, order_id, -3);
			validateAnswer(question_id, 67, order_id);
			changeOrderDate(s, order_id, order_date);
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
			// changeOrderDate(s, order_id, -1);
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
			changeOrderDate(s, order_id, order_date);
		}

		changeOrderStatus(s, order_id, 3);
		rs = MyConnection.execute(s, "select * from orders.orders where order_id=" + order_id);
		if (!rs.getString("order_status").equals("8")) {
			changeOrderStatus(s, order_id, 8);
			MyConnection.update(s, "delete from orders.merchant_transactions where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`merchant_transactions` (`order_id`, `amount`, `create_at`, `last_try_datetime`, `merchant_transaction_id`, `status`, `retry_count`, `status_text`, `gateway_status`, `enquiry_count`, `request_id`, `operator_ref_id`) VALUES ("
							+ order_id
							+ ", '8.00', '2017-02-28 13:02:34', '2017-02-28 13:02:34', '0', '2', '0', 'Response code:-1603, Invalid Operator', '3', '0', 'docker0328386917r0', '00')");

			validateAnswer(question_id, 46, order_id);

			MyConnection.update(s, "delete from orders.merchant_transactions where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`merchant_transactions` (`order_id`, `amount`, `create_at`, `last_try_datetime`, `merchant_transaction_id`, `status`, `retry_count`, `status_text`, `gateway_status`, `enquiry_count`, `request_id`, `operator_ref_id`) VALUES ("
							+ order_id
							+ ", '8.00', '2017-02-28 13:02:34', '2017-02-28 13:02:34', '0', '2', '0', 'Response code:-130, Technical Failure.', '5', '0', 'docker0328386917r0', '00')");

			validateAnswer(question_id, 14, order_id);

			MyConnection.update(s, "delete from orders.merchant_transactions where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`merchant_transactions` (`order_id`, `amount`, `create_at`, `last_try_datetime`, `merchant_transaction_id`, `status`, `retry_count`, `status_text`, `gateway_status`, `enquiry_count`, `request_id`, `operator_ref_id`) VALUES ("
							+ order_id
							+ ", '8.00', '2017-02-28 13:02:34', '2017-02-28 13:02:34', '0', '2', '0', 'Response code:-131, Invalid Mobile Number', '5', '0', 'docker0328386917r0', '00')");

			validateAnswer(question_id, 14, order_id);

			MyConnection.update(s, "delete from orders.merchant_transactions where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`merchant_transactions` (`order_id`, `amount`, `create_at`, `last_try_datetime`, `merchant_transaction_id`, `status`, `retry_count`, `status_text`, `gateway_status`, `enquiry_count`, `request_id`, `operator_ref_id`) VALUES ("
							+ order_id
							+ ", '8.00', '2017-02-28 13:02:34', '2017-02-28 13:02:34', '0', '2', '0', 'Response code:-1604, Invalid Amount', '2', '0', 'docker0328386917r0', '00')");

			validateAnswer(question_id, 46, order_id);

			MyConnection.update(s, "delete from orders.merchant_transactions where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`merchant_transactions` (`order_id`, `amount`, `create_at`, `last_try_datetime`, `merchant_transaction_id`, `status`, `retry_count`, `status_text`, `gateway_status`, `enquiry_count`, `request_id`, `operator_ref_id`) VALUES ("
							+ order_id
							+ ", '8.00', '2017-02-28 13:02:34', '2017-02-28 13:02:34', '0', '2', '0', 'Response code:-1614, Invalid Denomination', '2', '0', 'docker0328386917r0', '00')");

			validateAnswer(question_id, 46, order_id);

			// changeOrderDate(s, order_id, order_date);

			MyConnection.update(s, "delete from orders.merchant_transactions where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`merchant_transactions` (`order_id`, `amount`, `create_at`, `last_try_datetime`, `merchant_transaction_id`, `status`, `retry_count`, `status_text`, `gateway_status`, `enquiry_count`, `request_id`, `operator_ref_id`) VALUES ("
							+ order_id
							+ ", '8.00', '2017-02-28 13:02:34', '2017-02-28 13:02:34', '0', '2', '0', 'Response code:-1614, Invalid Denomination', '2', '0', 'docker0328386917r0', '00')");

			validateAnswer(question_id, 46, order_id);

			// changeOrderDate(s, order_id, order_date);

			MyConnection.update(s, "delete from orders.merchant_transactions where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`merchant_transactions` (`order_id`, `amount`, `create_at`, `last_try_datetime`, `merchant_transaction_id`, `status`, `retry_count`, `status_text`, `gateway_status`, `enquiry_count`, `request_id`, `operator_ref_id`) VALUES ("
							+ order_id
							+ ", '8.00', '2017-02-28 13:02:34', '2017-02-28 13:02:34', '0', '2', '2', 'Response code:-1612, Currently operator is down, please try after sometimes,Max retry reached:1 Normal  2 retries', '4', '0', 'docker0328386917r0', '00')");

			validateAnswer(question_id, 44, order_id);

			// changeOrderDate(s, order_id, order_date);

			MyConnection.update(s, "delete from orders.merchant_transactions where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`merchant_transactions` (`order_id`, `amount`, `create_at`, `last_try_datetime`, `merchant_transaction_id`, `status`, `retry_count`, `status_text`, `gateway_status`, `enquiry_count`, `request_id`, `operator_ref_id`) VALUES ("
							+ order_id
							+ ", '8.00', '2017-02-28 13:02:34', '2017-02-28 13:02:34', '0', '2', '3', 'Response code:-1612, Currently operator is down, please try after sometimes,Max retry reached:1 Normal  3 retries', '4', '0', 'docker0328386917r0', '00')");

			validateAnswer(question_id, 44, order_id);

			MyConnection.update(s, "delete from orders.merchant_transactions where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`merchant_transactions` (`order_id`, `amount`, `create_at`, `last_try_datetime`, `merchant_transaction_id`, `status`, `retry_count`, `status_text`, `gateway_status`, `enquiry_count`, `request_id`, `operator_ref_id`) VALUES ("
							+ order_id
							+ ", '8.00', '2017-02-28 13:02:34', '2017-02-28 13:02:34', '0', '2', '0', 'Response code:-101, Rollback Wallet Credit', '5', '0', 'docker0328386917r0', '00')");

			validateAnswer(question_id, 47, order_id);

			MyConnection.update(s, "delete from orders.merchant_transactions where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`merchant_transactions` (`order_id`, `amount`, `create_at`, `last_try_datetime`, `merchant_transaction_id`, `status`, `retry_count`, `status_text`, `gateway_status`, `enquiry_count`, `request_id`, `operator_ref_id`) VALUES ("
							+ order_id
							+ ", '8.00', '2017-02-28 13:02:34', '2017-02-28 13:02:34', '0', '2', '0', 'Response code:100, Transaction Pending', '5', '0', 'docker0328386917r0', '00')");

			validateAnswer(question_id, 14, order_id);

			MyConnection.update(s, "delete from orders.merchant_transactions where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`merchant_transactions` (`order_id`, `amount`, `create_at`, `last_try_datetime`, `merchant_transaction_id`, `status`, `retry_count`, `status_text`, `gateway_status`, `enquiry_count`, `request_id`, `operator_ref_id`) VALUES ("
							+ order_id
							+ ", '8.00', '2017-02-28 13:02:34', '2017-02-28 13:02:34', '0', '2', '2', 'Response code:-1611, Duplicate Transaction,Max retry reached:1 Normal  2 retries', '5', '0', 'docker0328386917r0', '00')");

			validateAnswer(question_id, 14, order_id);

			changeOrderDate(s, order_id, order_date);
		}

		changeOrderStatus(s, order_id, 3);
		rs = MyConnection.execute(s, "select * from orders.orders where order_id=" + order_id);
		if (!rs.getString("order_status").equals("8")) {
			changeOrderStatus(s, order_id, 8);
			MyConnection.update(s, "delete from orders.merchant_transactions where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`merchant_transactions` (`order_id`, `amount`, `create_at`, `last_try_datetime`, `merchant_transaction_id`, `status`, `retry_count`, `status_text`, `gateway_status`, `enquiry_count`, `request_id`, `operator_ref_id`) VALUES ("
							+ order_id
							+ ", '8.00', '2017-02-28 13:02:34', '2017-02-28 13:02:34', '0', '2', '0', 'Response code:-1603, Invalid Operator', '3', '0', 'docker0328386917r0', '00')");
			MyConnection.update(s, "delete from orders.refunds where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`refunds` ( `order_id`, `amount`, `refund_date`, `refund_status`, `ext_transaction_id`, `transaction_id`, `status_text`, `created_at`, `modified_date`, `refund_type`, `refund_transaction_id`, `checksum`, `order_date`) VALUES ( "
							+ order_id + ", '10.00', '2017-03-02 16:34:18', '7', '269545511', '" + transaction_id
							+ "', 'refund as Tapzo cash', '2017-03-02 16:34:17', '2017-03-02 16:34:18', '3', '4697-HC', 1, '2017-03-02 16:29:41')");
			validateAnswer(question_id, 50, order_id);
			changeOrderDate(s, order_id, order_date);
		}

		changeOrderStatus(s, order_id, 3);
		rs = MyConnection.execute(s, "select * from orders.orders where order_id=" + order_id);
		if (!rs.getString("order_status").equals("8")) {
			changeOrderStatus(s, order_id, 8);
			MyConnection.update(s, "delete from orders.merchant_transactions where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`merchant_transactions` (`order_id`, `amount`, `create_at`, `last_try_datetime`, `merchant_transaction_id`, `status`, `retry_count`, `status_text`, `gateway_status`, `enquiry_count`, `request_id`, `operator_ref_id`) VALUES ("
							+ order_id
							+ ", '8.00', '2017-02-28 13:02:34', '2017-02-28 13:02:34', '0', '2', '0', 'Response code:-1603, Invalid Operator', '3', '0', 'docker0328386917r0', '00')");
			MyConnection.update(s, "delete from orders.refunds where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`refunds` ( `order_id`, `amount`, `refund_date`, `refund_status`, `ext_transaction_id`, `transaction_id`, `status_text`, `created_at`, `modified_date`, `refund_type`, `refund_transaction_id`, `checksum`, `order_date`) VALUES ( "
							+ order_id + ", '10.00', '2017-03-02 16:34:18', '4', '269545511', '" + transaction_id
							+ "', 'refund as Tapzo cash', '2017-03-02 16:34:17', '2017-03-02 16:34:18', '3', '4697-HC', 1, '2017-03-02 16:29:41')");
			validateAnswer(question_id, 46, order_id);
			changeOrderDate(s, order_id, order_date);
		}

	}

	@Test(groups = "Recharge", enabled = false)
	public void RechargePending() throws Exception {

		int order_id = createOrder();
		System.out.println(order_id);

		// My status shows successful but I did not receive the recharge
		// benefit.
		int question_id = 7002;

		// Get db connection
		Statement s = MyConnection.getConnection("172.16.0.75", "3303", "appuser", "appuser");

		// get the order_details
		ResultSet rs = MyConnection.execute(s, "select * from orders.orders where order_id=" + order_id);
		// Timestamp order_date=rs.getTimestamp("order_date");
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = new GregorianCalendar();
		Timestamp dt = rs.getTimestamp("order_date", calendar);
		String order_date = sdf.format(dt);
		String transaction_id = rs.getString("transaction_id");

		if (!rs.getString("order_status").equals("3")) {
			changeOrderStatus(s, order_id, 3);
			validateAnswer(question_id, 65, order_id);
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
			changeOrderDate(s, order_id, -3);
			validateAnswer(question_id, 67, order_id);
			changeOrderDate(s, order_id, order_date);
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
			// changeOrderDate(s, order_id, -1);
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
			changeOrderDate(s, order_id, order_date);
		}

		changeOrderStatus(s, order_id, 3);
		rs = MyConnection.execute(s, "select * from orders.orders where order_id=" + order_id);
		if (!rs.getString("order_status").equals("8")) {
			changeOrderStatus(s, order_id, 8);
			MyConnection.update(s, "delete from orders.merchant_transactions where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`merchant_transactions` (`order_id`, `amount`, `create_at`, `last_try_datetime`, `merchant_transaction_id`, `status`, `retry_count`, `status_text`, `gateway_status`, `enquiry_count`, `request_id`, `operator_ref_id`) VALUES ("
							+ order_id
							+ ", '8.00', '2017-02-28 13:02:34', '2017-02-28 13:02:34', '0', '2', '0', 'Response code:-1603, Invalid Operator', '3', '0', 'docker0328386917r0', '00')");

			validateAnswer(question_id, 46, order_id);

			MyConnection.update(s, "delete from orders.merchant_transactions where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`merchant_transactions` (`order_id`, `amount`, `create_at`, `last_try_datetime`, `merchant_transaction_id`, `status`, `retry_count`, `status_text`, `gateway_status`, `enquiry_count`, `request_id`, `operator_ref_id`) VALUES ("
							+ order_id
							+ ", '8.00', '2017-02-28 13:02:34', '2017-02-28 13:02:34', '0', '2', '0', 'Response code:-130, Technical Failure.', '5', '0', 'docker0328386917r0', '00')");

			validateAnswer(question_id, 14, order_id);

			MyConnection.update(s, "delete from orders.merchant_transactions where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`merchant_transactions` (`order_id`, `amount`, `create_at`, `last_try_datetime`, `merchant_transaction_id`, `status`, `retry_count`, `status_text`, `gateway_status`, `enquiry_count`, `request_id`, `operator_ref_id`) VALUES ("
							+ order_id
							+ ", '8.00', '2017-02-28 13:02:34', '2017-02-28 13:02:34', '0', '2', '0', 'Response code:-131, Invalid Mobile Number', '5', '0', 'docker0328386917r0', '00')");

			validateAnswer(question_id, 14, order_id);

			MyConnection.update(s, "delete from orders.merchant_transactions where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`merchant_transactions` (`order_id`, `amount`, `create_at`, `last_try_datetime`, `merchant_transaction_id`, `status`, `retry_count`, `status_text`, `gateway_status`, `enquiry_count`, `request_id`, `operator_ref_id`) VALUES ("
							+ order_id
							+ ", '8.00', '2017-02-28 13:02:34', '2017-02-28 13:02:34', '0', '2', '0', 'Response code:-1604, Invalid Amount', '2', '0', 'docker0328386917r0', '00')");

			validateAnswer(question_id, 46, order_id);

			MyConnection.update(s, "delete from orders.merchant_transactions where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`merchant_transactions` (`order_id`, `amount`, `create_at`, `last_try_datetime`, `merchant_transaction_id`, `status`, `retry_count`, `status_text`, `gateway_status`, `enquiry_count`, `request_id`, `operator_ref_id`) VALUES ("
							+ order_id
							+ ", '8.00', '2017-02-28 13:02:34', '2017-02-28 13:02:34', '0', '2', '0', 'Response code:-1614, Invalid Denomination', '2', '0', 'docker0328386917r0', '00')");

			validateAnswer(question_id, 46, order_id);

			// changeOrderDate(s, order_id, order_date);

			MyConnection.update(s, "delete from orders.merchant_transactions where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`merchant_transactions` (`order_id`, `amount`, `create_at`, `last_try_datetime`, `merchant_transaction_id`, `status`, `retry_count`, `status_text`, `gateway_status`, `enquiry_count`, `request_id`, `operator_ref_id`) VALUES ("
							+ order_id
							+ ", '8.00', '2017-02-28 13:02:34', '2017-02-28 13:02:34', '0', '2', '0', 'Response code:-1614, Invalid Denomination', '2', '0', 'docker0328386917r0', '00')");

			validateAnswer(question_id, 46, order_id);

			// changeOrderDate(s, order_id, order_date);

			MyConnection.update(s, "delete from orders.merchant_transactions where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`merchant_transactions` (`order_id`, `amount`, `create_at`, `last_try_datetime`, `merchant_transaction_id`, `status`, `retry_count`, `status_text`, `gateway_status`, `enquiry_count`, `request_id`, `operator_ref_id`) VALUES ("
							+ order_id
							+ ", '8.00', '2017-02-28 13:02:34', '2017-02-28 13:02:34', '0', '2', '2', 'Response code:-1612, Currently operator is down, please try after sometimes,Max retry reached:1 Normal  2 retries', '4', '0', 'docker0328386917r0', '00')");

			validateAnswer(question_id, 44, order_id);

			// changeOrderDate(s, order_id, order_date);

			MyConnection.update(s, "delete from orders.merchant_transactions where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`merchant_transactions` (`order_id`, `amount`, `create_at`, `last_try_datetime`, `merchant_transaction_id`, `status`, `retry_count`, `status_text`, `gateway_status`, `enquiry_count`, `request_id`, `operator_ref_id`) VALUES ("
							+ order_id
							+ ", '8.00', '2017-02-28 13:02:34', '2017-02-28 13:02:34', '0', '2', '3', 'Response code:-1612, Currently operator is down, please try after sometimes,Max retry reached:1 Normal  3 retries', '4', '0', 'docker0328386917r0', '00')");

			validateAnswer(question_id, 44, order_id);

			MyConnection.update(s, "delete from orders.merchant_transactions where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`merchant_transactions` (`order_id`, `amount`, `create_at`, `last_try_datetime`, `merchant_transaction_id`, `status`, `retry_count`, `status_text`, `gateway_status`, `enquiry_count`, `request_id`, `operator_ref_id`) VALUES ("
							+ order_id
							+ ", '8.00', '2017-02-28 13:02:34', '2017-02-28 13:02:34', '0', '2', '0', 'Response code:-101, Rollback Wallet Credit', '5', '0', 'docker0328386917r0', '00')");

			validateAnswer(question_id, 47, order_id);

			MyConnection.update(s, "delete from orders.merchant_transactions where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`merchant_transactions` (`order_id`, `amount`, `create_at`, `last_try_datetime`, `merchant_transaction_id`, `status`, `retry_count`, `status_text`, `gateway_status`, `enquiry_count`, `request_id`, `operator_ref_id`) VALUES ("
							+ order_id
							+ ", '8.00', '2017-02-28 13:02:34', '2017-02-28 13:02:34', '0', '2', '0', 'Response code:100, Transaction Pending', '5', '0', 'docker0328386917r0', '00')");

			validateAnswer(question_id, 14, order_id);

			MyConnection.update(s, "delete from orders.merchant_transactions where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`merchant_transactions` (`order_id`, `amount`, `create_at`, `last_try_datetime`, `merchant_transaction_id`, `status`, `retry_count`, `status_text`, `gateway_status`, `enquiry_count`, `request_id`, `operator_ref_id`) VALUES ("
							+ order_id
							+ ", '8.00', '2017-02-28 13:02:34', '2017-02-28 13:02:34', '0', '2', '2', 'Response code:-1611, Duplicate Transaction,Max retry reached:1 Normal  2 retries', '5', '0', 'docker0328386917r0', '00')");

			validateAnswer(question_id, 14, order_id);

			changeOrderDate(s, order_id, order_date);
		}

		changeOrderStatus(s, order_id, 3);
		rs = MyConnection.execute(s, "select * from orders.orders where order_id=" + order_id);
		if (!rs.getString("order_status").equals("8")) {
			changeOrderStatus(s, order_id, 8);
			MyConnection.update(s, "delete from orders.merchant_transactions where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`merchant_transactions` (`order_id`, `amount`, `create_at`, `last_try_datetime`, `merchant_transaction_id`, `status`, `retry_count`, `status_text`, `gateway_status`, `enquiry_count`, `request_id`, `operator_ref_id`) VALUES ("
							+ order_id
							+ ", '8.00', '2017-02-28 13:02:34', '2017-02-28 13:02:34', '0', '2', '0', 'Response code:-1603, Invalid Operator', '3', '0', 'docker0328386917r0', '00')");
			MyConnection.update(s, "delete from orders.refunds where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`refunds` ( `order_id`, `amount`, `refund_date`, `refund_status`, `ext_transaction_id`, `transaction_id`, `status_text`, `created_at`, `modified_date`, `refund_type`, `refund_transaction_id`, `checksum`, `order_date`) VALUES ( "
							+ order_id + ", '10.00', '2017-03-02 16:34:18', '7', '269545511', '" + transaction_id
							+ "', 'refund as Tapzo cash', '2017-03-02 16:34:17', '2017-03-02 16:34:18', '3', '4697-HC', 1, '2017-03-02 16:29:41')");
			validateAnswer(question_id, 50, order_id);
			changeOrderDate(s, order_id, order_date);
		}

		changeOrderStatus(s, order_id, 3);
		rs = MyConnection.execute(s, "select * from orders.orders where order_id=" + order_id);
		if (!rs.getString("order_status").equals("8")) {
			changeOrderStatus(s, order_id, 8);
			MyConnection.update(s, "delete from orders.merchant_transactions where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`merchant_transactions` (`order_id`, `amount`, `create_at`, `last_try_datetime`, `merchant_transaction_id`, `status`, `retry_count`, `status_text`, `gateway_status`, `enquiry_count`, `request_id`, `operator_ref_id`) VALUES ("
							+ order_id
							+ ", '8.00', '2017-02-28 13:02:34', '2017-02-28 13:02:34', '0', '2', '0', 'Response code:-1603, Invalid Operator', '3', '0', 'docker0328386917r0', '00')");
			MyConnection.update(s, "delete from orders.refunds where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`refunds` ( `order_id`, `amount`, `refund_date`, `refund_status`, `ext_transaction_id`, `transaction_id`, `status_text`, `created_at`, `modified_date`, `refund_type`, `refund_transaction_id`, `checksum`, `order_date`) VALUES ( "
							+ order_id + ", '10.00', '2017-03-02 16:34:18', '4', '269545511', '" + transaction_id
							+ "', 'refund as Tapzo cash', '2017-03-02 16:34:17', '2017-03-02 16:34:18', '3', '4697-HC', 1, '2017-03-02 16:29:41')");
			validateAnswer(question_id, 46, order_id);
			changeOrderDate(s, order_id, order_date);
		}

	}

	@Test(groups = "Recharge", enabled = false)
	public void CancelRecharge() throws Exception {

		int order_id = createOrder();
		System.out.println(order_id);

		// I want to cancel my recharge?
		int question_id = 7007;

		// Get db connection
		Statement s = MyConnection.getConnection("172.16.0.75", "3303", "appuser", "appuser");

		// get the order_details
		ResultSet rs = MyConnection.execute(s, "select * from orders.orders where order_id=" + order_id);
		// Timestamp order_date=rs.getTimestamp("order_date");
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = new GregorianCalendar();
		Timestamp dt = rs.getTimestamp("order_date", calendar);
		String order_date = sdf.format(dt);
		String transaction_id = rs.getString("transaction_id");

		if (!rs.getString("order_status").equals("3")) {
			changeOrderStatus(s, order_id, 3);
			validateAnswer(question_id, 7023, order_id);
		}

		rs = MyConnection.execute(s, "select * from orders.orders where order_id=" + order_id);
		if (!rs.getString("order_status").equals("6")) {
			changeOrderStatus(s, order_id, 6);
			validateAnswer(question_id, 7022, order_id);
		}

		rs = MyConnection.execute(s, "select * from orders.orders where order_id=" + order_id);
		if (!rs.getString("order_status").equals("4")) {
			changeOrderStatus(s, order_id, 4);
			// changeOrderDate(s, order_id, -1);
			validateAnswer(question_id, 7023, order_id);
			// changeOrderDate(s, order_id, -3);
			// validateAnswer(question_id, 67, order_id);
			changeOrderDate(s, order_id, order_date);
		}

		changeOrderStatus(s, order_id, 3);
		rs = MyConnection.execute(s, "select * from orders.orders where order_id=" + order_id);
		if (!rs.getString("order_status").equals("4")) {
			changeOrderStatus(s, order_id, 4);
			MyConnection.update(s,
					"INSERT INTO `orders`.`merchant_transactions` ( `order_id`, `amount`, `create_at`, `last_try_datetime`, `merchant_transaction_id`, `status`, `retry_count`, `status_text`, `enquiry_count`, `request_id`, `operator_ref_id`) VALUES ("
							+ order_id
							+ " , '10.00', '2017-02-16 13:37:45', '2017-02-16 13:37:45', '170668692', '1', '0', 'Response code:1, Transaction Successful', '0', 'docker0328386130r0', '3260486320')");
			validateAnswer(question_id, 7023, order_id);
		}

		rs = MyConnection.execute(s, "select * from orders.orders where order_id=" + order_id);
		if (!rs.getString("order_status").equals("7")) {
			changeOrderStatus(s, order_id, 7);
			// changeOrderDate(s, order_id, -1);
			try {
				rs = MyConnection.execute(s, "select * from orders.merchant_transactions where order_id=" + order_id);
			} catch (Exception e) {
				MyConnection.update(s,
						"INSERT INTO `orders`.`merchant_transactions` ( `order_id`, `amount`, `create_at`, `last_try_datetime`, `merchant_transaction_id`, `status`, `retry_count`, `status_text`, `enquiry_count`, `request_id`, `operator_ref_id`) VALUES ("
								+ order_id
								+ " , '10.00', '2017-02-16 13:37:45', '2017-02-16 13:37:45', '170668692', '1', '0', 'Response code:1, Transaction Successful', '0', 'docker0328386130r0', '3260486320')");
			}
			validateAnswer(question_id, 7020, order_id);
		}

		changeOrderStatus(s, order_id, 3);
		rs = MyConnection.execute(s, "select * from orders.orders where order_id=" + order_id);
		if (!rs.getString("order_status").equals("8")) {
			changeOrderStatus(s, order_id, 8);
			MyConnection.update(s, "delete from orders.merchant_transactions where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`merchant_transactions` (`order_id`, `amount`, `create_at`, `last_try_datetime`, `merchant_transaction_id`, `status`, `retry_count`, `status_text`, `gateway_status`, `enquiry_count`, `request_id`, `operator_ref_id`) VALUES ("
							+ order_id
							+ ", '8.00', '2017-02-28 13:02:34', '2017-02-28 13:02:34', '0', '2', '0', 'Response code:-1603, Invalid Operator', '3', '0', 'docker0328386917r0', '00')");

			validateAnswer(question_id, 7021, order_id);

			MyConnection.update(s, "delete from orders.merchant_transactions where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`merchant_transactions` (`order_id`, `amount`, `create_at`, `last_try_datetime`, `merchant_transaction_id`, `status`, `retry_count`, `status_text`, `gateway_status`, `enquiry_count`, `request_id`, `operator_ref_id`) VALUES ("
							+ order_id
							+ ", '8.00', '2017-02-28 13:02:34', '2017-02-28 13:02:34', '0', '2', '0', 'Response code:-130, Technical Failure.', '5', '0', 'docker0328386917r0', '00')");

			validateAnswer(question_id, 7021, order_id);

			MyConnection.update(s, "delete from orders.merchant_transactions where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`merchant_transactions` (`order_id`, `amount`, `create_at`, `last_try_datetime`, `merchant_transaction_id`, `status`, `retry_count`, `status_text`, `gateway_status`, `enquiry_count`, `request_id`, `operator_ref_id`) VALUES ("
							+ order_id
							+ ", '8.00', '2017-02-28 13:02:34', '2017-02-28 13:02:34', '0', '2', '0', 'Response code:-131, Invalid Mobile Number', '5', '0', 'docker0328386917r0', '00')");

			validateAnswer(question_id, 7021, order_id);

			MyConnection.update(s, "delete from orders.merchant_transactions where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`merchant_transactions` (`order_id`, `amount`, `create_at`, `last_try_datetime`, `merchant_transaction_id`, `status`, `retry_count`, `status_text`, `gateway_status`, `enquiry_count`, `request_id`, `operator_ref_id`) VALUES ("
							+ order_id
							+ ", '8.00', '2017-02-28 13:02:34', '2017-02-28 13:02:34', '0', '2', '0', 'Response code:-1604, Invalid Amount', '2', '0', 'docker0328386917r0', '00')");

			validateAnswer(question_id, 7021, order_id);

			MyConnection.update(s, "delete from orders.merchant_transactions where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`merchant_transactions` (`order_id`, `amount`, `create_at`, `last_try_datetime`, `merchant_transaction_id`, `status`, `retry_count`, `status_text`, `gateway_status`, `enquiry_count`, `request_id`, `operator_ref_id`) VALUES ("
							+ order_id
							+ ", '8.00', '2017-02-28 13:02:34', '2017-02-28 13:02:34', '0', '2', '0', 'Response code:-1614, Invalid Denomination', '2', '0', 'docker0328386917r0', '00')");

			validateAnswer(question_id, 7021, order_id);

			// changeOrderDate(s, order_id, order_date);

			MyConnection.update(s, "delete from orders.merchant_transactions where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`merchant_transactions` (`order_id`, `amount`, `create_at`, `last_try_datetime`, `merchant_transaction_id`, `status`, `retry_count`, `status_text`, `gateway_status`, `enquiry_count`, `request_id`, `operator_ref_id`) VALUES ("
							+ order_id
							+ ", '8.00', '2017-02-28 13:02:34', '2017-02-28 13:02:34', '0', '2', '0', 'Response code:-1614, Invalid Denomination', '2', '0', 'docker0328386917r0', '00')");

			validateAnswer(question_id, 7021, order_id);

			// changeOrderDate(s, order_id, order_date);

			MyConnection.update(s, "delete from orders.merchant_transactions where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`merchant_transactions` (`order_id`, `amount`, `create_at`, `last_try_datetime`, `merchant_transaction_id`, `status`, `retry_count`, `status_text`, `gateway_status`, `enquiry_count`, `request_id`, `operator_ref_id`) VALUES ("
							+ order_id
							+ ", '8.00', '2017-02-28 13:02:34', '2017-02-28 13:02:34', '0', '2', '2', 'Response code:-1612, Currently operator is down, please try after sometimes,Max retry reached:1 Normal  2 retries', '4', '0', 'docker0328386917r0', '00')");

			validateAnswer(question_id, 7021, order_id);

			// changeOrderDate(s, order_id, order_date);

			MyConnection.update(s, "delete from orders.merchant_transactions where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`merchant_transactions` (`order_id`, `amount`, `create_at`, `last_try_datetime`, `merchant_transaction_id`, `status`, `retry_count`, `status_text`, `gateway_status`, `enquiry_count`, `request_id`, `operator_ref_id`) VALUES ("
							+ order_id
							+ ", '8.00', '2017-02-28 13:02:34', '2017-02-28 13:02:34', '0', '2', '3', 'Response code:-1612, Currently operator is down, please try after sometimes,Max retry reached:1 Normal  3 retries', '4', '0', 'docker0328386917r0', '00')");

			validateAnswer(question_id, 7021, order_id);

			MyConnection.update(s, "delete from orders.merchant_transactions where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`merchant_transactions` (`order_id`, `amount`, `create_at`, `last_try_datetime`, `merchant_transaction_id`, `status`, `retry_count`, `status_text`, `gateway_status`, `enquiry_count`, `request_id`, `operator_ref_id`) VALUES ("
							+ order_id
							+ ", '8.00', '2017-02-28 13:02:34', '2017-02-28 13:02:34', '0', '2', '0', 'Response code:-101, Rollback Wallet Credit', '5', '0', 'docker0328386917r0', '00')");

			validateAnswer(question_id, 7021, order_id);

			MyConnection.update(s, "delete from orders.merchant_transactions where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`merchant_transactions` (`order_id`, `amount`, `create_at`, `last_try_datetime`, `merchant_transaction_id`, `status`, `retry_count`, `status_text`, `gateway_status`, `enquiry_count`, `request_id`, `operator_ref_id`) VALUES ("
							+ order_id
							+ ", '8.00', '2017-02-28 13:02:34', '2017-02-28 13:02:34', '0', '2', '0', 'Response code:100, Transaction Pending', '5', '0', 'docker0328386917r0', '00')");

			validateAnswer(question_id, 7021, order_id);

			MyConnection.update(s, "delete from orders.merchant_transactions where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`merchant_transactions` (`order_id`, `amount`, `create_at`, `last_try_datetime`, `merchant_transaction_id`, `status`, `retry_count`, `status_text`, `gateway_status`, `enquiry_count`, `request_id`, `operator_ref_id`) VALUES ("
							+ order_id
							+ ", '8.00', '2017-02-28 13:02:34', '2017-02-28 13:02:34', '0', '2', '2', 'Response code:-1611, Duplicate Transaction,Max retry reached:1 Normal  2 retries', '5', '0', 'docker0328386917r0', '00')");

			validateAnswer(question_id, 7021, order_id);

			changeOrderDate(s, order_id, order_date);
		}

		changeOrderStatus(s, order_id, 3);
		rs = MyConnection.execute(s, "select * from orders.orders where order_id=" + order_id);
		if (!rs.getString("order_status").equals("8")) {
			changeOrderStatus(s, order_id, 8);
			MyConnection.update(s, "delete from orders.merchant_transactions where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`merchant_transactions` (`order_id`, `amount`, `create_at`, `last_try_datetime`, `merchant_transaction_id`, `status`, `retry_count`, `status_text`, `gateway_status`, `enquiry_count`, `request_id`, `operator_ref_id`) VALUES ("
							+ order_id
							+ ", '8.00', '2017-02-28 13:02:34', '2017-02-28 13:02:34', '0', '2', '0', 'Response code:-1603, Invalid Operator', '3', '0', 'docker0328386917r0', '00')");
			MyConnection.update(s, "delete from orders.refunds where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`refunds` ( `order_id`, `amount`, `refund_date`, `refund_status`, `ext_transaction_id`, `transaction_id`, `status_text`, `created_at`, `modified_date`, `refund_type`, `refund_transaction_id`, `checksum`, `order_date`) VALUES ( "
							+ order_id + ", '10.00', '2017-03-02 16:34:18', '7', '269545511', '" + transaction_id
							+ "', 'refund as Tapzo cash', '2017-03-02 16:34:17', '2017-03-02 16:34:18', '3', '4697-HC', 1, '2017-03-02 16:29:41')");
			validateAnswer(question_id, 7021, order_id);
			changeOrderDate(s, order_id, order_date);
		}

		changeOrderStatus(s, order_id, 3);
		rs = MyConnection.execute(s, "select * from orders.orders where order_id=" + order_id);
		if (!rs.getString("order_status").equals("8")) {
			changeOrderStatus(s, order_id, 8);
			MyConnection.update(s, "delete from orders.merchant_transactions where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`merchant_transactions` (`order_id`, `amount`, `create_at`, `last_try_datetime`, `merchant_transaction_id`, `status`, `retry_count`, `status_text`, `gateway_status`, `enquiry_count`, `request_id`, `operator_ref_id`) VALUES ("
							+ order_id
							+ ", '8.00', '2017-02-28 13:02:34', '2017-02-28 13:02:34', '0', '2', '0', 'Response code:-1603, Invalid Operator', '3', '0', 'docker0328386917r0', '00')");
			MyConnection.update(s, "delete from orders.refunds where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`refunds` ( `order_id`, `amount`, `refund_date`, `refund_status`, `ext_transaction_id`, `transaction_id`, `status_text`, `created_at`, `modified_date`, `refund_type`, `refund_transaction_id`, `checksum`, `order_date`) VALUES ( "
							+ order_id + ", '10.00', '2017-03-02 16:34:18', '4', '269545511', '" + transaction_id
							+ "', 'refund as Tapzo cash', '2017-03-02 16:34:17', '2017-03-02 16:34:18', '3', '4697-HC', 1, '2017-03-02 16:29:41')");
			validateAnswer(question_id, 7021, order_id);
			changeOrderDate(s, order_id, order_date);
		}

	}

	@Test(groups = "Recharge", enabled = false)
	public void TransactionPending() throws Exception {

		int order_id = createOrder();
		System.out.println(order_id);

		// Why is my transaction marked as pending?
		int question_id = 7003;

		// Get db connection
		Statement s = MyConnection.getConnection("172.16.0.75", "3303", "appuser", "appuser");

		// get the order_details
		ResultSet rs = MyConnection.execute(s, "select * from orders.orders where order_id=" + order_id);
		// Timestamp order_date=rs.getTimestamp("order_date");
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = new GregorianCalendar();
		Timestamp dt = rs.getTimestamp("order_date", calendar);
		String order_date = sdf.format(dt);
		String transaction_id = rs.getString("transaction_id");

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
			changeOrderDate(s, order_id, -3);
			validateAnswer(question_id, 67, order_id);
			changeOrderDate(s, order_id, order_date);
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
			// changeOrderDate(s, order_id, -1);
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
			changeOrderDate(s, order_id, order_date);
		}

		changeOrderStatus(s, order_id, 3);
		rs = MyConnection.execute(s, "select * from orders.orders where order_id=" + order_id);
		if (!rs.getString("order_status").equals("8")) {
			changeOrderStatus(s, order_id, 8);
			MyConnection.update(s, "delete from orders.merchant_transactions where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`merchant_transactions` (`order_id`, `amount`, `create_at`, `last_try_datetime`, `merchant_transaction_id`, `status`, `retry_count`, `status_text`, `gateway_status`, `enquiry_count`, `request_id`, `operator_ref_id`) VALUES ("
							+ order_id
							+ ", '8.00', '2017-02-28 13:02:34', '2017-02-28 13:02:34', '0', '2', '0', 'Response code:-1603, Invalid Operator', '3', '0', 'docker0328386917r0', '00')");

			validateAnswer(question_id, 46, order_id);

			MyConnection.update(s, "delete from orders.merchant_transactions where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`merchant_transactions` (`order_id`, `amount`, `create_at`, `last_try_datetime`, `merchant_transaction_id`, `status`, `retry_count`, `status_text`, `gateway_status`, `enquiry_count`, `request_id`, `operator_ref_id`) VALUES ("
							+ order_id
							+ ", '8.00', '2017-02-28 13:02:34', '2017-02-28 13:02:34', '0', '2', '0', 'Response code:-130, Technical Failure.', '5', '0', 'docker0328386917r0', '00')");

			validateAnswer(question_id, 14, order_id);

			MyConnection.update(s, "delete from orders.merchant_transactions where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`merchant_transactions` (`order_id`, `amount`, `create_at`, `last_try_datetime`, `merchant_transaction_id`, `status`, `retry_count`, `status_text`, `gateway_status`, `enquiry_count`, `request_id`, `operator_ref_id`) VALUES ("
							+ order_id
							+ ", '8.00', '2017-02-28 13:02:34', '2017-02-28 13:02:34', '0', '2', '0', 'Response code:-131, Invalid Mobile Number', '5', '0', 'docker0328386917r0', '00')");

			validateAnswer(question_id, 14, order_id);

			MyConnection.update(s, "delete from orders.merchant_transactions where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`merchant_transactions` (`order_id`, `amount`, `create_at`, `last_try_datetime`, `merchant_transaction_id`, `status`, `retry_count`, `status_text`, `gateway_status`, `enquiry_count`, `request_id`, `operator_ref_id`) VALUES ("
							+ order_id
							+ ", '8.00', '2017-02-28 13:02:34', '2017-02-28 13:02:34', '0', '2', '0', 'Response code:-1604, Invalid Amount', '2', '0', 'docker0328386917r0', '00')");

			validateAnswer(question_id, 46, order_id);

			MyConnection.update(s, "delete from orders.merchant_transactions where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`merchant_transactions` (`order_id`, `amount`, `create_at`, `last_try_datetime`, `merchant_transaction_id`, `status`, `retry_count`, `status_text`, `gateway_status`, `enquiry_count`, `request_id`, `operator_ref_id`) VALUES ("
							+ order_id
							+ ", '8.00', '2017-02-28 13:02:34', '2017-02-28 13:02:34', '0', '2', '0', 'Response code:-1614, Invalid Denomination', '2', '0', 'docker0328386917r0', '00')");

			validateAnswer(question_id, 46, order_id);

			// changeOrderDate(s, order_id, order_date);

			MyConnection.update(s, "delete from orders.merchant_transactions where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`merchant_transactions` (`order_id`, `amount`, `create_at`, `last_try_datetime`, `merchant_transaction_id`, `status`, `retry_count`, `status_text`, `gateway_status`, `enquiry_count`, `request_id`, `operator_ref_id`) VALUES ("
							+ order_id
							+ ", '8.00', '2017-02-28 13:02:34', '2017-02-28 13:02:34', '0', '2', '0', 'Response code:-1614, Invalid Denomination', '2', '0', 'docker0328386917r0', '00')");

			validateAnswer(question_id, 46, order_id);

			// changeOrderDate(s, order_id, order_date);

			MyConnection.update(s, "delete from orders.merchant_transactions where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`merchant_transactions` (`order_id`, `amount`, `create_at`, `last_try_datetime`, `merchant_transaction_id`, `status`, `retry_count`, `status_text`, `gateway_status`, `enquiry_count`, `request_id`, `operator_ref_id`) VALUES ("
							+ order_id
							+ ", '8.00', '2017-02-28 13:02:34', '2017-02-28 13:02:34', '0', '2', '2', 'Response code:-1612, Currently operator is down, please try after sometimes,Max retry reached:1 Normal  2 retries', '4', '0', 'docker0328386917r0', '00')");

			validateAnswer(question_id, 44, order_id);

			// changeOrderDate(s, order_id, order_date);

			MyConnection.update(s, "delete from orders.merchant_transactions where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`merchant_transactions` (`order_id`, `amount`, `create_at`, `last_try_datetime`, `merchant_transaction_id`, `status`, `retry_count`, `status_text`, `gateway_status`, `enquiry_count`, `request_id`, `operator_ref_id`) VALUES ("
							+ order_id
							+ ", '8.00', '2017-02-28 13:02:34', '2017-02-28 13:02:34', '0', '2', '3', 'Response code:-1612, Currently operator is down, please try after sometimes,Max retry reached:1 Normal  3 retries', '4', '0', 'docker0328386917r0', '00')");

			validateAnswer(question_id, 44, order_id);

			MyConnection.update(s, "delete from orders.merchant_transactions where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`merchant_transactions` (`order_id`, `amount`, `create_at`, `last_try_datetime`, `merchant_transaction_id`, `status`, `retry_count`, `status_text`, `gateway_status`, `enquiry_count`, `request_id`, `operator_ref_id`) VALUES ("
							+ order_id
							+ ", '8.00', '2017-02-28 13:02:34', '2017-02-28 13:02:34', '0', '2', '0', 'Response code:-101, Rollback Wallet Credit', '5', '0', 'docker0328386917r0', '00')");

			validateAnswer(question_id, 47, order_id);

			MyConnection.update(s, "delete from orders.merchant_transactions where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`merchant_transactions` (`order_id`, `amount`, `create_at`, `last_try_datetime`, `merchant_transaction_id`, `status`, `retry_count`, `status_text`, `gateway_status`, `enquiry_count`, `request_id`, `operator_ref_id`) VALUES ("
							+ order_id
							+ ", '8.00', '2017-02-28 13:02:34', '2017-02-28 13:02:34', '0', '2', '0', 'Response code:100, Transaction Pending', '5', '0', 'docker0328386917r0', '00')");

			validateAnswer(question_id, 14, order_id);

			MyConnection.update(s, "delete from orders.merchant_transactions where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`merchant_transactions` (`order_id`, `amount`, `create_at`, `last_try_datetime`, `merchant_transaction_id`, `status`, `retry_count`, `status_text`, `gateway_status`, `enquiry_count`, `request_id`, `operator_ref_id`) VALUES ("
							+ order_id
							+ ", '8.00', '2017-02-28 13:02:34', '2017-02-28 13:02:34', '0', '2', '2', 'Response code:-1611, Duplicate Transaction,Max retry reached:1 Normal  2 retries', '5', '0', 'docker0328386917r0', '00')");

			validateAnswer(question_id, 14, order_id);

			changeOrderDate(s, order_id, order_date);
		}

		changeOrderStatus(s, order_id, 3);
		rs = MyConnection.execute(s, "select * from orders.orders where order_id=" + order_id);
		if (!rs.getString("order_status").equals("8")) {
			changeOrderStatus(s, order_id, 8);
			MyConnection.update(s, "delete from orders.merchant_transactions where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`merchant_transactions` (`order_id`, `amount`, `create_at`, `last_try_datetime`, `merchant_transaction_id`, `status`, `retry_count`, `status_text`, `gateway_status`, `enquiry_count`, `request_id`, `operator_ref_id`) VALUES ("
							+ order_id
							+ ", '8.00', '2017-02-28 13:02:34', '2017-02-28 13:02:34', '0', '2', '0', 'Response code:-1603, Invalid Operator', '3', '0', 'docker0328386917r0', '00')");
			MyConnection.update(s, "delete from orders.refunds where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`refunds` ( `order_id`, `amount`, `refund_date`, `refund_status`, `ext_transaction_id`, `transaction_id`, `status_text`, `created_at`, `modified_date`, `refund_type`, `refund_transaction_id`, `checksum`, `order_date`) VALUES ( "
							+ order_id + ", '10.00', '2017-03-02 16:34:18', '7', '269545511', '" + transaction_id
							+ "', 'refund as Tapzo cash', '2017-03-02 16:34:17', '2017-03-02 16:34:18', '3', '4697-HC', 1, '2017-03-02 16:29:41')");
			validateAnswer(question_id, 50, order_id);
			changeOrderDate(s, order_id, order_date);
		}

		changeOrderStatus(s, order_id, 3);
		rs = MyConnection.execute(s, "select * from orders.orders where order_id=" + order_id);
		if (!rs.getString("order_status").equals("8")) {
			changeOrderStatus(s, order_id, 8);
			MyConnection.update(s, "delete from orders.merchant_transactions where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`merchant_transactions` (`order_id`, `amount`, `create_at`, `last_try_datetime`, `merchant_transaction_id`, `status`, `retry_count`, `status_text`, `gateway_status`, `enquiry_count`, `request_id`, `operator_ref_id`) VALUES ("
							+ order_id
							+ ", '8.00', '2017-02-28 13:02:34', '2017-02-28 13:02:34', '0', '2', '0', 'Response code:-1603, Invalid Operator', '3', '0', 'docker0328386917r0', '00')");
			MyConnection.update(s, "delete from orders.refunds where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`refunds` ( `order_id`, `amount`, `refund_date`, `refund_status`, `ext_transaction_id`, `transaction_id`, `status_text`, `created_at`, `modified_date`, `refund_type`, `refund_transaction_id`, `checksum`, `order_date`) VALUES ( "
							+ order_id + ", '10.00', '2017-03-02 16:34:18', '4', '269545511', '" + transaction_id
							+ "', 'refund as Tapzo cash', '2017-03-02 16:34:17', '2017-03-02 16:34:18', '3', '4697-HC', 1, '2017-03-02 16:29:41')");
			validateAnswer(question_id, 46, order_id);
			changeOrderDate(s, order_id, order_date);
		}

	}

	@Test(groups = "Recharge")
	public void CheckRefund() throws Exception {
		int order_id = createOrder();
		System.out.println(order_id);

		// I did not get a refund
		int question_id = 7001;

		// Get db connection
		Statement s = MyConnection.getConnection("172.16.0.75", "3303", "appuser", "appuser");

		// get the order_details
		ResultSet rs = MyConnection.execute(s, "select * from orders.orders where order_id=" + order_id);
		// Timestamp order_date=rs.getTimestamp("order_date");
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = new GregorianCalendar();
		Timestamp dt = rs.getTimestamp("order_date", calendar);
		String order_date = sdf.format(dt);
		String transaction_id = rs.getString("transaction_id");

		if (!rs.getString("order_status").equals("3")) {
			changeOrderStatus(s, order_id, 3);
			validateAnswer(question_id, 63, order_id);
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
			changeOrderDate(s, order_id, order_date);
		}
		
		changeOrderStatus(s, order_id, 3);
		rs = MyConnection.execute(s, "select * from orders.orders where order_id=" + order_id);
		if (!rs.getString("order_status").equals("4")) {
			changeOrderStatus(s, order_id, 4);

			MyConnection.update(s, "delete from orders.merchant_transactions where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`merchant_transactions` (`order_id`, `amount`, `create_at`, `last_try_datetime`, `merchant_transaction_id`, `status`, `retry_count`, `status_text`, `gateway_status`, `enquiry_count`, `request_id`, `operator_ref_id`) VALUES ("
							+ order_id
							+ ", '8.00', '2017-02-28 13:02:34', '2017-02-28 13:02:34', '0', '2', '0', 'Response code:100, Transaction Pending', '5', '0', 'docker0328386917r0', '00')");

			validateAnswer(question_id, 64, order_id);
		}

		rs = MyConnection.execute(s, "select * from orders.orders where order_id=" + order_id);
		if (!rs.getString("order_status").equals("7")) {
			changeOrderStatus(s, order_id, 7);
			// changeOrderDate(s, order_id, -1);
			try {
				rs = MyConnection.execute(s, "select * from orders.merchant_transactions where order_id=" + order_id);
			} catch (Exception e) {
				MyConnection.update(s,
						"INSERT INTO `orders`.`merchant_transactions` ( `order_id`, `amount`, `create_at`, `last_try_datetime`, `merchant_transaction_id`, `status`, `retry_count`, `status_text`, `enquiry_count`, `request_id`, `operator_ref_id`) VALUES ("
								+ order_id
								+ " , '10.00', '2017-02-16 13:37:45', '2017-02-16 13:37:45', '170668692', '1', '0', 'Response code:1, Transaction Successful', '0', 'docker0328386130r0', '3260486320')");
			}
			validateAnswer(question_id, 53, order_id);
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
			validateAnswer(question_id, 53, order_id);
			changeOrderDate(s, order_id, order_date);
		}
		changeOrderStatus(s, order_id, 3);
		rs = MyConnection.execute(s, "select * from orders.orders where order_id=" + order_id);
		if (!rs.getString("order_status").equals("8")) {
			changeOrderStatus(s, order_id, 8);
			MyConnection.update(s, "delete from orders.merchant_transactions where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`merchant_transactions` (`order_id`, `amount`, `create_at`, `last_try_datetime`, `merchant_transaction_id`, `status`, `retry_count`, `status_text`, `gateway_status`, `enquiry_count`, `request_id`, `operator_ref_id`) VALUES ("
							+ order_id
							+ ", '8.00', '2017-02-28 13:02:34', '2017-02-28 13:02:34', '0', '2', '0', 'Response code:-1603, Invalid Operator', '3', '0', 'docker0328386917r0', '00')");
			MyConnection.update(s, "delete from orders.refunds where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`refunds` ( `order_id`, `amount`, `refund_date`, `refund_status`, `ext_transaction_id`, `transaction_id`, `status_text`, `created_at`, `modified_date`, `refund_type`, `refund_transaction_id`, `checksum`, `order_date`) VALUES ( "
							+ order_id + ", '10.00', '2017-03-02 16:34:18', '7', '269545511', '" + transaction_id
							+ "', 'refund as Tapzo cash', '2017-03-02 16:34:17', '2017-03-02 16:34:18', '3', '4697-HC', 1, '2017-03-02 16:29:41')");
			//validateAnswer(question_id, 50, order_id);
			changeOrderDate(s, order_id, order_date);
		}

		changeOrderStatus(s, order_id, 3);
		rs = MyConnection.execute(s, "select * from orders.orders where order_id=" + order_id);
		if (!rs.getString("order_status").equals("8")) {
			changeOrderStatus(s, order_id, 8);
			MyConnection.update(s, "delete from orders.merchant_transactions where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`merchant_transactions` (`order_id`, `amount`, `create_at`, `last_try_datetime`, `merchant_transaction_id`, `status`, `retry_count`, `status_text`, `gateway_status`, `enquiry_count`, `request_id`, `operator_ref_id`) VALUES ("
							+ order_id
							+ ", '8.00', '2017-02-28 13:02:34', '2017-02-28 13:02:34', '0', '2', '0', 'Response code:-1603, Invalid Operator', '3', '0', 'docker0328386917r0', '00')");
			MyConnection.update(s, "delete from orders.refunds where order_id=" + order_id);
			MyConnection.update(s,
					"INSERT INTO `orders`.`refunds` ( `order_id`, `amount`, `refund_date`, `refund_status`, `ext_transaction_id`, `transaction_id`, `status_text`, `created_at`, `modified_date`, `refund_type`, `refund_transaction_id`, `checksum`, `order_date`) VALUES ( "
							+ order_id + ", '10.00', '2017-03-02 16:34:18', '4', '269545511', '" + transaction_id
							+ "', 'refund as Tapzo cash', '2017-03-02 16:34:17', '2017-03-02 16:34:18', '3', '4697-HC', 1, '2017-03-02 16:29:41')");
			validateAnswer(question_id, 46, order_id);
			changeOrderDate(s, order_id, order_date);
		}
		
	}

}
