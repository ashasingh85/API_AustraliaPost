package com.testcases;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class AustraliaAPI {
	
	@Test
	public static void calculateDeliveryCost() {
	// Use the get api to retrieve the service code
	RestAssured.baseURI="https://digitalapi.auspost.com.au/";
	Response getCodeResponse = given().header("AUTH-KEY","03526a7d-f686-4dea-9cbb-b9e8271f4d36").log().all().queryParam("length","22")
						.queryParam("width", "16").queryParam("height", "7.7").queryParam("weight", "1.5")
						.queryParam("from_postcode", "2000").queryParam("to_postcode", "2133")
	.when().get("postage/parcel/domestic/service.json")
	.then().assertThat().statusCode(200)
	.extract().response();
	int count = getCodeResponse.jsonPath().getInt("services.service.size()");
	
	for (int i=0;i<count;i++) {
		String serviceCode = getCodeResponse.jsonPath().get("services.service["+i+"].code");
		
		// Once the service code is fetched then get another api to retrieve the corresponding price
		Response getPriceResponse = given().header("AUTH-KEY","03526a7d-f686-4dea-9cbb-b9e8271f4d36").log().all().queryParam("length","22")
				.queryParam("width", "16").queryParam("height", "7.7").queryParam("weight", "1.5")
				.queryParam("from_postcode", "2000").queryParam("to_postcode", "2133").queryParam("service_code", serviceCode)
				.when().get("postage/parcel/domestic/calculate.json")
				.then().assertThat().statusCode(200)
				.extract().response();
		String price = getPriceResponse.jsonPath().get("postage_result.total_cost");
		System.out.println("The Service code is " + serviceCode + " and its corresponding price " + price );
			
		
	}
	

	
	}
	

}
