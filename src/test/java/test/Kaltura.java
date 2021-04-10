package test;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Kaltura {

	// Constant variables
	public static String userName;
	public static String password;

	@BeforeClass
	public void setup() {
		RestAssured.baseURI = "https://api.frs1.ott.kaltura.com";
		RestAssured.basePath = "/api_v3/service/ottuser/action";
	}

	@Test(enabled = true, priority = 1, description = "Create a user in BE")
	public void createUser_BE() {
		
		//Get random number for create user in BE
		String userNum = String.valueOf(getRandomNumber(50, 999));

		//Send the response
		Response response = given().contentType("application/json").accept("application/json")
				.body("{" + "\"apiVersion\": \"6.0.0\"," + "\"partnerId\": 3197," + "\"user\": {"
						+ "\"objectType\": \"KalturaOTTUser\"," + "\"username\": \"QATest__hagai_" + userNum + "\","
						+ "\"firstName\": \"QATesthagai_" + userNum + "\"," + "\"lastName\": \"QATesthagai_" + userNum
						+ "\"," + "\"email\": \"hagai_" + userNum + "@mailinator.com\","
						+ "\"address\": \"ott_user_lWkiwzTJJGYI fake address\","
						+ "\"city\": \"ott_user_lWkiwzTJJGYI fake city\"," + "\"countryId\": 5,"
						+ "\"externalId\": \"31640041221907_" + userNum + "\"" + "},"
						+ "\"password\": \"password_SlLVWDLl\"" + "}")
				.when().post("/register").then().statusCode(200).extract().response();

		//Set Global variable for using in next test
		userName = response.jsonPath().getString("result.username");
		password = "password_SlLVWDLl";

		// Print header
		System.out.println("Print header");

		// Reader header of a give name. In this line we will get
		// Header named Content-Type
		String contentType = response.header("Content-Type");
		System.out.println("Content-Type value: " + contentType);

		// Reader header of a give name. In this line we will get
		// Header named Server
		String serverType = response.header("Server");
		System.out.println("Server value: " + serverType);

		// Reader header of a give name. In this line we will get
		// Header named Content-Encoding
		String acceptLanguage = response.header("Content-Encoding");
		System.out.println("Content-Encoding: " + acceptLanguage);

		String DateHeader = response.header("Date");
		System.out.println("Date: " + DateHeader);

		// Print the response
		System.out.println("========================");
		System.out.println("Response: " + response.asPrettyString());

		String headerName = response.getHeader("Date");
		System.out.println("Print header date: " + headerName);
		// Verify date is not empty
		Assert.assertTrue(!headerName.isEmpty());

		// Assert response header
		Assert.assertTrue(response.getStatusLine().equals("HTTP/1.1 200 OK"));

		// Verify id field is NOT empty
		String id = response.jsonPath().getString("result.id");
		Assert.assertTrue(!id.isEmpty());

		// Verify id field is numeric
		try {
			Assert.assertTrue(isNumeric(id));
		} catch (NumberFormatException nfe) {
			System.out.println(id + " is a number");
		}

		// Get the country ID variable
		String countryId = response.jsonPath().getString("result.country.id");

		// Verify Country id field is NOT empty
		Assert.assertTrue(!countryId.isEmpty());

		// Verify Country id field is numeric
		try {
			Assert.assertTrue(isNumeric(countryId));
		} catch (NumberFormatException nfe) {
			System.out.println(id + " is NOT a number");
		}

	}

	@Test(enabled = true, priority = 2, description = "Login the BE with the user created before")
	public void login_BE() throws ParseException {
		RestAssured.baseURI = "https://api.frs1.ott.kaltura.com";
		RestAssured.basePath = "/api_v3/service/ottuser/action";

		Map<String, Object> userDetails = new HashMap<String, Object>();

		userDetails.put("apiVersion", "6.0.0");
		userDetails.put("partnerId", 3197);
		userDetails.put("username", userName);
		userDetails.put("password", "password_SlLVWDLl");
		// userDetails.put("extraParams", extraDetails);

		Response response = given().contentType("application/json").accept("application/json").body(userDetails).when()
				.post("/login").then().statusCode(200).extract().response();

		// Print header
		System.out.println("Print header");

		// Reader header of a give name. In this line we will get
		// Header named Content-Type
		String contentType = response.header("Content-Type");
		System.out.println("Content-Type value: " + contentType);

		// Reader header of a give name. In this line we will get
		// Header named Server
		String serverType = response.header("Server");
		System.out.println("Server value: " + serverType);

		// Reader header of a give name. In this line we will get
		// Header named Content-Encoding
		String acceptLanguage = response.header("Content-Encoding");
		System.out.println("Content-Encoding: " + acceptLanguage);

		String DateHeader = response.header("Date");
		System.out.println("Date: " + DateHeader);

		// Print the response
		System.out.println("========================");
		System.out.println("Response: " + response.asPrettyString());

		String headerName = response.getHeader("Date");
		System.out.println("Print header date: " + headerName);
		// Verify date is not empty
		Assert.assertTrue(!headerName.isEmpty());

		// Assert response header
		Assert.assertTrue(response.getStatusLine().equals("HTTP/1.1 200 OK"));

		// Verify id field is NOT empty and exist
		int lastLogin = response.jsonPath().getInt("result.user.lastLoginDate");
		Assert.assertTrue(!Integer.toString(lastLogin).isEmpty());

		String dateAsText = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(lastLogin * 1000L));
		System.out.println("Last Login Date: " + dateAsText);

		// Verify id field is NOT empty
		String loginSession = response.jsonPath().getString("result.loginSession");
		Assert.assertTrue(!loginSession.isEmpty());
	}

	@Test(enabled = true, priority = 3, description = "Try to create already exist user")
	public void createExistUser_BE() throws ParseException {
		String userNum = "40";

		Response response = given().contentType("application/json").accept("application/json")
				.body("{" + "\"apiVersion\": \"6.0.0\"," + "\"partnerId\": 3197," + "\"user\": {"
						+ "\"objectType\": \"KalturaOTTUser\"," + "\"username\": \"QATest__hagai_" + userNum + "\","
						+ "\"firstName\": \"QATesthagai_" + userNum + "\"," + "\"lastName\": \"QATesthagai_" + userNum
						+ "\"," + "\"email\": \"hagai_" + userNum + "@mailinator.com\","
						+ "\"address\": \"ott_user_lWkiwzTJJGYI fake address\","
						+ "\"city\": \"ott_user_lWkiwzTJJGYI fake city\"," + "\"countryId\": 5,"
						+ "\"externalId\": \"31640041221907_" + userNum + "\"" + "},"
						+ "\"password\": \"password_SlLVWDLl\"" + "}")
				.when().post("/register").then().statusCode(200).extract().response();

		// Print header
		System.out.println("Print header");

		// Reader header of a give name. In this line we will get
		// Header named Content-Type
		String contentType = response.header("Content-Type");
		System.out.println("Content-Type value: " + contentType);

		// Reader header of a give name. In this line we will get
		// Header named Server
		String serverType = response.header("Server");
		System.out.println("Server value: " + serverType);

		// Reader header of a give name. In this line we will get
		// Header named Content-Encoding
		String acceptLanguage = response.header("Content-Encoding");
		System.out.println("Content-Encoding: " + acceptLanguage);

		String DateHeader = response.header("Date");
		System.out.println("Date: " + DateHeader);

		// Print the response
		System.out.println("========================");
		System.out.println("Response: " + response.asPrettyString());

		
		String errCode = response.jsonPath().getString("result.error.code");
		String errMsg = response.jsonPath().getString("result.error.message");
		String errObjectType = response.jsonPath().getString("result.error.objectType");
		
		System.out.println("========================");
		System.out.println("Error Code: " + errCode);
		System.out.println("Error Msg: " + errMsg);
		System.out.println("Error Object Type: " + errObjectType);
		System.out.println("========================");

		
       

	}

	public static boolean isNumeric(String strNum) {
		if (strNum == null) {
			return false;
		}
		try {
			double d = Double.parseDouble(strNum);
			System.out.println("succeed to convert to double " + d);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	public int getRandomNumber(int min, int max) {
		return (int) ((Math.random() * (max - min)) + min);
	}

}
