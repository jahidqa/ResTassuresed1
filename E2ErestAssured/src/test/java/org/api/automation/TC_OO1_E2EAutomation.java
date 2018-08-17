package org.api.automation;

import org.api.library.CreatePost;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;


public class TC_OO1_E2EAutomation {
	
	//extent report
	public ExtentReports extentrepot;
	public ExtentTest extenttest;
	
	
	@Test
	public void tcase1()
	{
		extentrepot= new ExtentReports("./Report/report.html",true);
		extentrepot.addSystemInfo("Author Name","Testing World");
		extenttest=extentrepot.startTest("E2E RestAssured Testing");
	
		
		int input=28;
		//Step1-Created new resource
		
		CreatePost cpost=new CreatePost();
		cpost.setTitle("myTitle14");
		cpost.setId(input);
		cpost.setAuthor("myauthor14");
		
		ValidatableResponse response=given()
		.contentType(ContentType.JSON)
		.body(cpost)
		
		.when()
		.post("http://localhost:3000/posts")
		.then()
		.contentType(ContentType.JSON);
		
		int responseId=response.extract().path("id");
		int actualStatusCode=response.extract().response().getStatusCode();

		Assert.assertEquals(actualStatusCode, 201);
		
		//Step 2-Validate resource created 
		 ValidatableResponse response1= when()
		.get("http://localhost:3000/posts/"+responseId)
		.then()
		.contentType(ContentType.JSON);
		
		 String actTitle=response1.extract().path("title");
		 String actauthor=response1.extract().path("author");
		 
		 Assert.assertEquals(actTitle, "myTitle14");
		 Assert.assertEquals(actauthor, "myauthor14");
		 
		 //Step 3- update Resource
		 CreatePost cpost1=new CreatePost();
		 cpost1.setId(input);
		 cpost1.setAuthor("Updated Author");
		 cpost1.setTitle("Updated title");
		 
		
		 ValidatableResponse response2=given()
		 .contentType(ContentType.JSON)
		 .body(cpost1)
		 .when()
		 .put("http://localhost:3000/posts/"+input)
		 .then()
		 .contentType(ContentType.JSON);
		 
		 //Step 3.1 validate updates
		 ValidatableResponse response4=when()
				 .get("http://localhost:3000/posts/"+responseId)
				 .then()
				 .contentType(ContentType.JSON);
		 
		 String actTitle1=response4.extract().path("title");
		 String actauthor1=response4.extract().path("author");
		 
		 Assert.assertEquals(actTitle1, "Updated title");
		 Assert.assertEquals(actauthor1, "Updated Author");
		 
		 //Step-4
		  when()
		 .delete("http://localhost:3000/posts"+input);
		 
		 //Step-4.1 validate Delete
		  ValidatableResponse response5= when()
				  .get("http://localhost:3000/posts/"+responseId)
				  .then()
				  .contentType(ContentType.JSON);
		  
		  Assert.assertNotEquals(response5.extract().response().statusCode(), 201);
		  
			extentrepot.endTest(extenttest);
			extentrepot.flush();
	}
}
