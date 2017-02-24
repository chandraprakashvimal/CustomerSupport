package Utility;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.json.JSONObject;
import org.testng.Assert;

import com.Tapzo.CustomerSupport.AnswerResponse;
import com.Tapzo.CustomerSupport.MyConnection;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
//import org.json.simple.JSONObject;
import org.codehaus.jackson.map.ObjectMapper;

public class Utilities {
	static ObjectMapper objectMapper = new ObjectMapper();
	public static HttpClient client = HttpClientBuilder.create().build();
	public static String auth="eyJ1c2VyX25hbWUiOm51bGwsImlkIjo4MTAzOTksIm1vYmlsZSI6Ijk5ODA5MjM4MTkiLCJleHBpcmVzIjoxODAzMDE3OTY1NzM1fQ==./zutLg/53SCeH5GoH5d5xEzAdfqM6r/EjylCn3iyWVE=";

	public Integer createOrder() throws Exception {
		HttpPost request1;
		// private String question_id;
		int order_id;

		// HttpClient client;
		client = HttpClientBuilder.create().build();
		String url = "http://docker03.helpchat.in:13081/checkout/v5/initiate";
		request1 = new HttpPost(url);
		request1.setEntity(new StringEntity(
				"{\"orderType\":\"RECHARGE\",\"orderAttributes\":[{\"orderAttributeDefinition\":{\"attributeId\":1},\"attributeValue\":\"7795550000\"},{\"orderAttributeDefinition\":{\"attributeId\":2},\"attributeValue\":\"Tata Docomo GSM\"},{\"orderAttributeDefinition\":{\"attributeId\":3},\"attributeValue\":\"Karnataka\"},{\"orderAttributeDefinition\":{\"attributeId\":5},\"attributeValue\":\"prepaid\"},{\"orderAttributeDefinition\":{\"attributeId\":46},\"attributeValue\":\"Topup\"}],\"cartLineItems\":[{\"quantity\":1,\"productId\":5,\"price\":1.0,\"itemId\":0}],\"orderSource\":\"MOBILE_APP\"}"));
		//auth = "eyJ1c2VyX25hbWUiOm51bGwsImlkIjo4MTAzOTksIm1vYmlsZSI6Ijk5ODA5MjM4MTkiLCJleHBpcmVzIjoxODAzMDE3OTY1NzM1fQ==./zutLg/53SCeH5GoH5d5xEzAdfqM6r/EjylCn3iyWVE=";
		request1.addHeader("X-AKOSHA-AUTH", auth);
		request1.addHeader("Content-Type", "application/json");
		HttpResponse response1 = client.execute(request1);
		if (!response1.getStatusLine().toString().contains("200")) {
			throw new Exception("Order not created. Api is giving: " + response1.getStatusLine());
		}
		// CONVERT RESPONSE TO STRING
		String result = EntityUtils.toString(response1.getEntity(), "UTF-8");
		JSONObject obj = new JSONObject(result);
		// System.out.println(obj.getInt("totalPointsAvailable"));
		order_id = obj.getJSONObject("order").getInt("orderId");
		// JSONObject jsonObject = (JSONObject) result;
		request1.reset();
		return order_id;
	}

	public void changeOrderStatus(Statement s, int order_id, int order_status) throws SQLException {
		MyConnection.update(s, "update orders.orders set order_status=" + order_status + " where order_id=" + order_id);
	}

	public void validateAnswer(int question_id, int expected_answer_id, int order_id)
			throws ClientProtocolException, IOException, InterruptedException {

		String url = "http://docker03.helpchat.in:13081/customer-support/support/faq/answer";
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(new StringEntity("{\"faqId\":\"" + question_id + "\",\"orderId\":\"" + order_id + "\"}"));
		httpPost.addHeader("X-AKOSHA-AUTH", auth);
		httpPost.addHeader("Content-Type", "application/json");
		HttpResponse response = null;

		RequestConfig HTTP_CONNECT_CONFIG = RequestConfig.custom().setSocketTimeout(2 * 1000)
				.setConnectTimeout(2 * 1000).build();
		httpPost.setConfig(HTTP_CONNECT_CONFIG);
		response = client.execute(httpPost);

		AnswerResponse answer = objectMapper.readValue(response.getEntity().getContent(), AnswerResponse.class);
		int answer_id = Integer.parseInt(answer.getAnswer().getId());
		String answers = answer.getAnswer().getValue();
		httpPost.reset();
		Assert.assertEquals(answer_id, expected_answer_id,
				"Answer is wrong(Answer id=" + answer_id + ") and \n answer=" + answers);
	}

	/**
	 * 
	 * @param s
	 * @param order_id
	 * @param hour
	 * @throws Exception 
	 */
	public void changeOrderDate(Statement s, int order_id, int hour) throws Exception {
		ResultSet rs = MyConnection.execute(s, "select order_date from orders.orders where order_id=" + order_id);
		//rs.next();
		
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = new GregorianCalendar();
		Timestamp dt = rs.getTimestamp("order_date",calendar);
		//System.out.println( sdf.format(dt));
		dt= new Timestamp(dt.getTime()+ hour*1000*60*60);
		//System.out.println( sdf.format(dt));
		//calendar.add(Calendar.HOUR, hour);
		//System.out.println( sdf.format(calendar.getTime()));
		MyConnection.update(s, "update orders.orders set order_date=\'" + sdf.format(dt) + "\' where order_id=" + order_id);

		
	}
	public void changeOrderDate(Statement s, int order_id, String date) throws Exception {
		MyConnection.update(s, "update orders.orders set order_date=\'" + date + "\' where order_id=" + order_id);
	}
}
