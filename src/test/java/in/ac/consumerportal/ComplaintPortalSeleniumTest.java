package in.ac.consumerportal;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.*;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ComplaintPortalSeleniumTest {
 static WebDriver driver;
 static String baseUrl;
 @BeforeAll static void setup(){
  baseUrl=System.getProperty("baseUrl","http://localhost:8080");
  ChromeOptions options=new ChromeOptions();
  if(Boolean.parseBoolean(System.getProperty("headless","true"))){options.addArguments("--headless=new","--no-sandbox","--disable-dev-shm-usage");}
  options.addArguments("--window-size=1440,1000");
  driver=new ChromeDriver(options);
 }
 @AfterAll static void teardown(){if(driver!=null)driver.quit();}
 @Test @Order(1) void homeShowsComplaintForm(){
  driver.get(baseUrl+"/");
  assertTrue(driver.getTitle().contains("Consumer Complaint Portal"));
  assertTrue(driver.findElement(By.id("complaint-form")).isDisplayed());
  assertTrue(driver.findElement(By.id("submit-complaint")).isEnabled());
 }
 @Test @Order(2) void submitComplaintAndCaptureReference(){
  driver.get(baseUrl+"/");
  driver.findElement(By.id("name")).sendKeys("Selenium Test User");
  driver.findElement(By.id("email")).sendKeys("selenium@example.com");
  driver.findElement(By.id("category")).sendKeys("Billing");
  driver.findElement(By.id("subject")).sendKeys("Incorrect bill amount");
  driver.findElement(By.id("description")).sendKeys("The amount charged does not match the invoice.");
  driver.findElement(By.id("submit-complaint")).click();
  new WebDriverWait(driver,Duration.ofSeconds(5)).until(d->d.findElements(By.id("submitted-reference")).size()==1);
  String reference=driver.findElement(By.id("submitted-reference")).getText();
  assertTrue(reference.startsWith("CCP-"),"Reference should be generated");
  driver.get(baseUrl+"/track?reference="+reference);
  String status=new WebDriverWait(driver,Duration.ofSeconds(5)).until(d->d.findElement(By.id("complaint-status")).getText());
  assertEquals("SUBMITTED",status);
 }
 @Test @Order(3) void reviewerCanApprovePendingComplaint(){
  driver.get(baseUrl+"/review");
  assertTrue(driver.getPageSource().contains("Selenium Test User"));
  driver.findElement(By.cssSelector("form[action$='/APPROVED'] button")).click();
  driver.get(baseUrl+"/review");
  assertTrue(driver.getPageSource().contains("APPROVED"));
 }
 @Test @Order(4) void trackUnknownReferenceShowsNotFound(){
  driver.get(baseUrl+"/track?reference=CCP-NOTFOUND");
  assertTrue(driver.getPageSource().contains("No complaint found"));
 }
}
