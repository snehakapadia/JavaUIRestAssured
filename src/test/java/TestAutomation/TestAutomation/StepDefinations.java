package TestAutomation.TestAutomation;

import static org.junit.Assert.assertEquals;
import static org.hamcrest.Matchers.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

public class StepDefinations {
	
	Properties config = new Properties();
	Properties xpath = new Properties();;
	WebDriver driver = null;
	HashMap<String, String> gHash = new HashMap<String, String>();
	
	
	@Before
	public void setup()
	{
		String configFileName = "ConfigFiles/config.properties";
		
		try {
			config.load(new FileInputStream(configFileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		configFileName = "ConfigFiles/xpath.properties";
		 
		try {
			xpath.load(new FileInputStream(configFileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@When("User closes the browser")
	public void closeBrowser()
	{
		driver.close();
	}
	
	@Given("User navigates to {string} webpage")
	public void hitURL(String appName)
	{
		switch (config.getProperty("Browser").toLowerCase())
		{
		case "chrome":
			WebDriverManager.chromedriver().setup();
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--headless"); 
			options.addArguments("enable-automation"); 
			options.addArguments("--no-sandbox"); 
			options.addArguments("--disable-infobars");
			options.addArguments("--disable-dev-shm-usage");
			options.addArguments("--disable-browser-side-navigation"); 
			options.addArguments("--disable-gpu"); 
			driver = new ChromeDriver(options);
			break;
		case "firefox":
			WebDriverManager.firefoxdriver().setup();
			FirefoxOptions optionsf = new FirefoxOptions();
			optionsf.addArguments("--headless"); 
			optionsf.addArguments("enable-automation"); 
			optionsf.addArguments("--no-sandbox"); 
			optionsf.addArguments("--disable-infobars");
			optionsf.addArguments("--disable-dev-shm-usage");
			optionsf.addArguments("--disable-browser-side-navigation"); 
			optionsf.addArguments("--disable-gpu"); 
			driver = new FirefoxDriver(optionsf);
		}
		
		driver.get(config.getProperty(appName));
		driver.manage().window().maximize();
	}
	
	@When("User clicks on {string} button")
	public void clickOnButton(String buttonName)
	{
		String xpathP = xpath.getProperty(buttonName).replace("<buttonName>", buttonName);
		WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(30));
		w.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathP)));
		w.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath(xpathP))));
		driver.findElement(By.xpath(xpathP)).click();
	}
	
	@Then("User {string} see {string}")
	public void visibleOrNot(String canOrCannot, String objectName)
	{
		if(canOrCannot.equalsIgnoreCase("can"))
		{
			if(driver.findElement(By.xpath(xpath.getProperty(objectName))).isDisplayed())
			{
				assertEquals(driver.findElement(By.xpath(xpath.getProperty(objectName))).isDisplayed(), true);
			}
			else
			{
				assertEquals(driver.findElement(By.xpath(xpath.getProperty(objectName))).isDisplayed(), false);
			}
		}
		else if(canOrCannot.equalsIgnoreCase("cannot"))
		{
			if(!driver.findElement(By.xpath(xpath.getProperty(xpath.getProperty(objectName)))).isDisplayed())
			{
				assertEquals(driver.findElement(By.xpath(xpath.getProperty(objectName))).isDisplayed(), true);
			}
			else
			{
				assertEquals(driver.findElement(By.xpath(xpath.getProperty(objectName))).isDisplayed(), false);
			}
		}
	}
	
	@Then("User verifies the basic search results")
	public void verifySearchResult()
	{
		List<WebElement> elements = driver.findElements(By.xpath(xpath.getProperty("List_Results")));
		String serachedValue = gHash.get("SerachValue");
		for(WebElement element: elements)
		{
			String resultHeading = element.findElement(By.xpath(xpath.getProperty("Result_Heading"))).getText();
			String resultBody = element.findElement(By.xpath(xpath.getProperty("Result_Body"))).getText();
			if(resultHeading.contains(serachedValue.split(" ")[0]) || resultHeading.contains(serachedValue.split(" ")[1]))
			{
				assertEquals(true, true);
			}
			else
			{
				assertEquals(true, false);
			}
			if(resultBody.contains(serachedValue.split(" ")[0]) || resultBody.contains(serachedValue.split(" ")[1]))
			{
				assertEquals(true, true);
			}
			else
			{
				assertEquals(true, false);
			}
		}
	}
	
	@Then("User verifies the advanced search results")
	public void verifyAdvancedSearchResult()
	{
		List<WebElement> elements = driver.findElements(By.xpath(xpath.getProperty("List_Results")));
		String serachedValue = gHash.get("SerachValue");
		String searchedNotValue = gHash.get("SerachValueNot");
		for(WebElement element: elements)
		{
			String resultHeading = element.findElement(By.xpath(xpath.getProperty("Result_Heading"))).getText();
			String resultBody = element.findElement(By.xpath(xpath.getProperty("Result_Body"))).getText();
			if((resultHeading.toLowerCase().contains(serachedValue.split(" ")[0]) || resultHeading.toLowerCase().contains(serachedValue.split(" ")[1])) && (!resultHeading.contains(searchedNotValue)))
			{
				assertEquals(true, true);
			}
			else
			{
				assertEquals(true, false);
			}
			if((resultBody.contains(serachedValue.split(" ")[0]) || resultBody.contains(serachedValue.split(" ")[1])) && (!resultHeading.contains(searchedNotValue)))
			{
				assertEquals(true, true);
			}
			else
			{
				assertEquals(true, false);
			}
		}
	}
	
	@When("User enters {string} as {string}")
	public void enterData(String fieldName, String value)
	{
		if(fieldName.equals("Password"))
		{
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(30));
		w.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath.getProperty(fieldName))));
		w.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath(xpath.getProperty(fieldName)))));
		driver.findElement(By.xpath(xpath.getProperty(fieldName))).sendKeys(value);
		driver.findElement(By.xpath(xpath.getProperty(fieldName))).sendKeys(Keys.TAB);
		if(fieldName.equals("Not_These_Words"))
			gHash.put("SerachValueNot", value);
		else	
			gHash.put("SerachValue", value);
	}
	
	@When("User gets the pet by the id")
	public void getAllInventories()
	{
		ValidatableResponse res =
				RestAssured.given()
			          .headers(
			              "api_key", "special-key",
			              "accept", "application/json")
			          .when()
			          .get(config.getProperty("BackendUrl") + "/pet/" + gHash.get("petId"))
			          .then()
	                  .statusCode(200);
	}
	
	@Then("User deletes the pet")
	public void deletePet()
	{
		ValidatableResponse res =
				RestAssured.given()
			          .headers(
			              "api_key", "special-key",
			              "accept", "application/json")
			          .when()
			          .delete(config.getProperty("BackendUrl") + "/pet/" + gHash.get("petId"))
			          .then()
	                  .statusCode(200);
	}
	
	@When("User places an order")
	public void placeOrder()
	{
		JSONParser jsonParser = new JSONParser();
        String body = null;
        Object obj = null;
        try (FileReader reader = new FileReader("JsonFiles/store.json"))
        {
            obj = jsonParser.parse(reader);
            body = obj.toString();
          
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        JSONObject jsonObject = ((JSONObject) obj);
        jsonObject.remove("id");
        jsonObject.remove("petId");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String dateTimeStamp = sdf1.format(timestamp);
        jsonObject.put("id",dateTimeStamp);
        jsonObject.put("petId",gHash.get("petId"));
        gHash.put("storeId", dateTimeStamp);
        
		ValidatableResponse res =
				RestAssured.given()
//					.log()
//					.all()
			          .headers(
			              "api_key", "special-key",
			              "accept", "application/json")
			          .contentType(ContentType.JSON)
			          .and()
			          .body(jsonObject)
			          .when()
			          .post(config.getProperty("BackendUrl") + "/store/order")
			          .then()
	                  .statusCode(200);
	}
	
	@When("User adds a user")
	public void userAdd()
	{
		JSONParser jsonParser = new JSONParser();
        String body = null;
        Object obj = null;
        try (FileReader reader = new FileReader("JsonFiles/user.json"))
        {
            obj = jsonParser.parse(reader);
            body = obj.toString();
          
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        JSONObject jsonObject = ((JSONObject) obj);
        jsonObject.remove("id");
        jsonObject.remove("username");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String dateTimeStamp = sdf1.format(timestamp);
        jsonObject.put("id",dateTimeStamp);
        jsonObject.put("username",dateTimeStamp);
        gHash.put("userId", dateTimeStamp);
        gHash.put("username", dateTimeStamp);
        System.out.println("User: " + dateTimeStamp);
        
		ValidatableResponse res =
				RestAssured.given()
//					.log()
//					.all()
			          .headers(
			              "api_key", "special-key",
			              "accept", "application/json")
			          .contentType(ContentType.JSON)
			          .and()
			          .body(jsonObject)
			          .when()
			          .post(config.getProperty("BackendUrl") + "/user")
			          .then()
	                  .statusCode(200);
		System.out.println(res.extract().body().asString());
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Then("User validates if the user is {string}")
	public void validateUser(String addOrUpdate)
	{
		System.out.println("Get User: " + gHash.get("username"));
		ValidatableResponse res =
				RestAssured.given()
			          .headers(
			              "api_key", "special-key",
			              "accept", "application/json")
			          .when()
			          .get(config.getProperty("BackendUrl") + "/user/" + gHash.get("username"))
			          .then()
	                  .statusCode(200);
		System.out.println(res.extract().response().getBody().jsonPath().getString("id"));
		assertEquals(res.extract().response().getBody().jsonPath().getString("id"), gHash.get("userId"));
		if(addOrUpdate.equalsIgnoreCase("updated")) {
			assertEquals(res.extract().response().getBody().jsonPath().getString("firstName"), "testName");
		}
	}
	
	@When("User logins in to the system")
	public void userLogin()
	{
		ValidatableResponse res =
		RestAssured.given()
	          .headers(
	              "api_key", "special-key",
	              "accept", "application/json")
	          .queryParam("username", gHash.get("username"))
	          .queryParam("password", "Password@123")
	          .when()
	          .get(config.getProperty("BackendUrl") + "/user/login")
	          .then()
              .statusCode(200)
              .body("message",containsString("logged in user session"));
	}
	
	@When("User logs out from the system")
	public void userLogout()
	{
		ValidatableResponse res =
				RestAssured.given()
			          .headers(
			              "api_key", "special-key",
			              "accept", "application/json")
			          .when()
			          .get(config.getProperty("BackendUrl") + "/user/logout")
			          .then()
		              .statusCode(200)
		              .body("message",containsString("ok"));
	}
	
	@Then("User gets the order by id and validates the order")
	public void validateOrderById()
	{
		ValidatableResponse res =
				RestAssured.given()
			          .headers(
			              "api_key", "special-key",
			              "accept", "application/json")
			          .when()
			          .get(config.getProperty("BackendUrl") + "/store/order/" + gHash.get("storeId"))
			          .then()
	                  .statusCode(200);
	}
	
	@When("User adds a pet to the store")
	public void addPet()
	{
        JSONParser jsonParser = new JSONParser();
        String body = null;
        Object obj = null;
        try (FileReader reader = new FileReader("JsonFiles/pet.json"))
        {
            obj = jsonParser.parse(reader);
            body = obj.toString();
          
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        JSONObject jsonObject = ((JSONObject) obj);
        jsonObject.remove("id");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String dateTimeStamp = sdf1.format(timestamp);
        jsonObject.put("id",dateTimeStamp);
        gHash.put("petId", dateTimeStamp);
        
		ValidatableResponse res =
				RestAssured.given()
//					.log()
//					.all()
			          .headers(
			              "api_key", "special-key",
			              "accept", "application/json")
			          .contentType(ContentType.JSON)
			          .and()
			          .body(jsonObject)
			          .when()
			          .post(config.getProperty("BackendUrl") + "/pet")
			          .then()
	                  .statusCode(200);
		System.out.println(res.extract().body().asString());
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@When("User updates the user data")
	public void userUpdate()
	{
		JSONParser jsonParser = new JSONParser();
        String body = null;
        Object obj = null;
        try (FileReader reader = new FileReader("JsonFiles/user.json"))
        {
            obj = jsonParser.parse(reader);
            body = obj.toString();
          
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        JSONObject jsonObject = ((JSONObject) obj);
        jsonObject.remove("username");
        jsonObject.remove("id");
        jsonObject.remove("firstName");
        jsonObject.put("id",gHash.get("userId"));
        jsonObject.put("username",gHash.get("username"));
        jsonObject.put("firstName","testName");
        
		ValidatableResponse res =
				RestAssured.given()
//					.log()
//					.all()
			          .headers(
			              "api_key", "special-key",
			              "accept", "application/json")
			          .contentType(ContentType.JSON)
			          .and()
			          .body(jsonObject)
			          .when()
			          .put(config.getProperty("BackendUrl") + "/user/" + gHash.get("username"))
			          .then()
	                  .statusCode(200);
		System.out.println(res.extract().body().asString());
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
