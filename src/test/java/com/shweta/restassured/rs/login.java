package com.shweta.restassured.rs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import net.minidev.json.JSONObject;

public class login {

	String host = "https://us-central1-qa01-tekarch-accmanager.cloudfunctions.net";
	String uri_login = "/login";
	String uri_getdata = "/getdata";
	String uri_adddata = "/addData";
	String uri_deletedata = "/deleteData";
	String url = null;
	String token;

	/*
	 * When writing automation for API, First set url, body or header, give it a
	 * method name(i.e. post, get etc)
	 * 
	 * Restassured will work on Given, When, Then conditions u have given url, then
	 * add header, add body means, for a given url, when header is xx, and body is
	 * yy , then u should get response 201
	 * 
	 * When & Then r not mandatory, but Given is.
	 */

	@BeforeSuite
	public void loginAPI() {
		url = host + uri_login;
		RestAssured.baseURI = url;
		Response res = RestAssured.given().contentType("application/json")
				.body("{\"username\": \"oshweta25@gmail.com\", \"password\": \"shweta123\"}").post();
		// System.out.println(res.asString());
		// System.out.println("Status code : " +res.statusCode());
		 res.prettyPrint();
		// System.out.println(res.jsonPath().getString("token").toString());
		// System.out.println(res.jsonPath().getString("token[0]"));
		token = res.andReturn().jsonPath().get("token[0]");

	}

	@BeforeTest
	public void settingDependencyBeforeMethod() {
		url = null;
	}

	@Test
	public void getData() {
		url = host + uri_getdata;
		Map<String, String> map = new HashMap();
		map.put("token", token);
		RestAssured.baseURI = url;
		Response res = RestAssured.given().contentType("application/json").headers(map).get();
		// System.out.println(res.asString());
		// System.out.println("Status code : " +res.statusCode());
		 res.prettyPrint();
		// System.out.println(res.jsonPath().getString("accountno").toString());
		// System.out.println(res.jsonPath().getString("accountno[0]"));

		List<Object> li = res.jsonPath().getList("accountno");
		for (Object s : li) {
			// System.out.println(s);
		}

	}

	@Test
	public void addData() {
		url = host + uri_adddata;

		Map<String, String> map = new HashMap();
		map.put("token", token);

		RestAssured.baseURI = url;

		JSONObject requestParams = new JSONObject();
		requestParams.put("accountno", "TA-ojhshw4");
		requestParams.put("departmentno", "2");
		requestParams.put("salary", "500000");
		requestParams.put("pincode", "200000");

		/*
		 * RequestSpecification request = RestAssured.given();
		 * request.header("Content-Type", "application/json"); // Add the Json to the
		 * body of the request request.body(requestParams.toJSONString()); // Post the
		 * request and check the response Response response = request.post();
		 */

		Response res = RestAssured.given().contentType("application/json").headers(map).body(requestParams).post(url);

		int statusCode = res.getStatusCode();
		/*
		 * System.out.println("The status code received: " + statusCode);
		 * System.out.println("Response body: " + res.body().asString());
		 * res.prettyPrint();
		 */
	}

	@Test
	public void deleteData() {
		// {"id": "TUkc77E1sBx0C1rYaIjd", "userid": "8FgZY2oyrdCqdtSIxvXR"}

		url = host + uri_deletedata;
		RestAssured.baseURI = url;
		// String emptyId = "76gtqL8TwOpI1fGTjGxO";

		JSONObject requestParams = new JSONObject();
		requestParams.put("id", "TUkc77E1sBx0C1rYaIjd");
		requestParams.put("userid", "8FgZY2oyrdCqdtSIxvXR");

		Map<String, String> map = new HashMap();
		map.put("token", token);
		Response res = RestAssured.given().contentType("application/json").headers(map).body(requestParams).delete(url);

		int statusCode = res.getStatusCode();
		System.out.println("The status code recieved: " + statusCode);
		System.out.println(res.asString());
		Assert.assertEquals(statusCode, 200);
		System.out.println("Response body: " + res.body().asString());
		String jsonString = res.asString();
		Assert.assertEquals(jsonString.contains("success"), true);
		res.prettyPrint();

	}

}
// Write 15 API test cases and automate it for API_test_Tekarch web site.
