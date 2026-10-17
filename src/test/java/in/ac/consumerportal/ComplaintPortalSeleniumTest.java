package in.ac.consumerportal;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(ComplaintPortalSeleniumTest.ScreenshotOnFailureExtension.class)
public class ComplaintPortalSeleniumTest {
    static WebDriver driver;
    static String baseUrl;
    static ConfigurableApplicationContext appServer;

    public static class ScreenshotOnFailureExtension implements TestWatcher {
        @Override
        public void testFailed(ExtensionContext context, Throwable cause) {
            if (driver != null && driver instanceof TakesScreenshot) {
                try {
                    File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                    Path screenshotDir = Paths.get("target", "screenshots");
                    Files.createDirectories(screenshotDir);
                    String methodName = context.getRequiredTestMethod().getName();
                    String fileName = methodName + "_" + System.currentTimeMillis() + ".png";
                    Path target = screenshotDir.resolve(fileName);
                    Files.copy(src.toPath(), target, StandardCopyOption.REPLACE_EXISTING);
                    System.err.println("[SELENIUM] Test failed! Captured screenshot saved to: " + target.toAbsolutePath());
                } catch (Exception ex) {
                    System.err.println("[SELENIUM] Unable to capture failure screenshot: " + ex.getMessage());
                }
            }
        }
    }

    private static int extractPort(String urlString, int defaultPort) {
        try {
            URI uri = URI.create(urlString);
            int port = uri.getPort();
            return port != -1 ? port : defaultPort;
        } catch (Exception e) {
            return defaultPort;
        }
    }

    private static boolean isServerReachable(String urlString) {
        try {
            URL url = URI.create(urlString).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(1000);
            conn.setReadTimeout(1000);
            conn.setRequestMethod("GET");
            int responseCode = conn.getResponseCode();
            return responseCode >= 200 && responseCode < 400;
        } catch (Exception e) {
            return false;
        }
    }

    private static void waitForServerReachable(String urlString, int timeoutSeconds) {
        long deadline = System.currentTimeMillis() + (timeoutSeconds * 1000L);
        while (System.currentTimeMillis() < deadline) {
            if (isServerReachable(urlString)) {
                return;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Interrupted waiting for application at " + urlString, e);
            }
        }
        throw new IllegalStateException("Application at " + urlString + " did not become reachable within " + timeoutSeconds + " seconds.");
    }

    @BeforeAll
    static void setup() {
        baseUrl = System.getProperty("baseUrl", "http://localhost:8088");
        int port = extractPort(baseUrl, 8088);

        // 1. Ensure application is genuinely reachable before opening Chrome
        if (!isServerReachable(baseUrl)) {
            System.out.println("[TEST-LIFECYCLE] Application not running at " + baseUrl + ". Starting Spring Boot on port " + port + "...");
            appServer = SpringApplication.run(ComplaintPortalApplication.class,
                    "--server.port=" + port,
                    "--spring.datasource.url=jdbc:h2:mem:complaints;DB_CLOSE_DELAY=-1");
            waitForServerReachable(baseUrl, 35);
            System.out.println("[TEST-LIFECYCLE] Application is genuinely reachable at " + baseUrl);
        } else {
            System.out.println("[TEST-LIFECYCLE] Application is already running at " + baseUrl);
        }

        // 2. Open Chrome browser after server is verified reachable
        ChromeOptions options = new ChromeOptions();
        if (Boolean.parseBoolean(System.getProperty("headless", "true"))) {
            options.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage", "--disable-gpu");
        }
        options.addArguments("--window-size=1440,1000");
        driver = new ChromeDriver(options);
    }

    @AfterAll
    static void teardown() {
        try {
            if (driver != null) {
                driver.quit();
            }
        } finally {
            if (appServer != null && appServer.isActive()) {
                System.out.println("[TEST-LIFECYCLE] Shutting down Spring Boot application...");
                appServer.close();
                System.out.println("[TEST-LIFECYCLE] Spring Boot application stopped.");
            }
        }
    }

    private String createComplaint(String name, String email, String category, String subject, String description) {
        driver.get(baseUrl + "/");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(d -> d.findElement(By.id("complaint-form")).isDisplayed());

        driver.findElement(By.id("name")).clear();
        driver.findElement(By.id("name")).sendKeys(name);

        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("email")).sendKeys(email);

        new Select(driver.findElement(By.id("category"))).selectByVisibleText(category);

        driver.findElement(By.id("subject")).clear();
        driver.findElement(By.id("subject")).sendKeys(subject);

        driver.findElement(By.id("description")).clear();
        driver.findElement(By.id("description")).sendKeys(description);

        driver.findElement(By.id("submit-complaint")).click();

        wait.until(d -> !d.findElements(By.id("submitted-reference")).isEmpty()
                || !d.findElements(By.cssSelector("[role='alert']")).isEmpty());

        if (driver.findElements(By.id("submitted-reference")).isEmpty()) {
            fail("Complaint submission failed. Page message: "
                    + driver.findElement(By.cssSelector("[role='alert']")).getText());
        }

        return driver.findElement(By.id("submitted-reference")).getText().trim();
    }

    @Test
    @Order(1)
    @DisplayName("Journey 1: Successful complaint submission and reference generation")
    void test01_successfulComplaintSubmissionAndCaptureReference() {
        driver.get(baseUrl + "/");
        assertTrue(driver.getTitle().contains("Consumer Complaint Portal"), "Home page title must match");
        assertTrue(driver.findElement(By.id("complaint-form")).isDisplayed(), "Complaint form must be visible");

        String email = "consumer-" + System.currentTimeMillis() + "@example.com";
        String reference = createComplaint("Test Consumer One", email, "Billing", "Overcharged invoice", "The billing department charged an extra recurring fee.");

        assertNotNull(reference, "Reference must not be null");
        assertTrue(reference.startsWith("CCP-"), "Reference format must start with CCP-, got: " + reference);

        // Verify tracking immediately shows SUBMITTED
        driver.get(baseUrl + "/track?reference=" + reference);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(d -> !d.findElements(By.id("complaint-status")).isEmpty());
        assertEquals("SUBMITTED", driver.findElement(By.id("complaint-status")).getText().trim());
    }

    @Test
    @Order(2)
    @DisplayName("Journey 2: Missing or invalid form inputs rejected with validation error")
    void test02_missingOrInvalidFormInputsValidation() {
        driver.get(baseUrl + "/");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(d -> d.findElement(By.id("complaint-form")).isDisplayed());

        // Disable HTML5 form validation to test server-side validation enforcement
        ((JavascriptExecutor) driver).executeScript("arguments[0].noValidate = true;", driver.findElement(By.id("complaint-form")));

        driver.findElement(By.id("name")).clear();
        driver.findElement(By.id("name")).sendKeys("Invalid User");

        // Invalid email format (missing domain/tld)
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("email")).sendKeys("not-an-email");

        new Select(driver.findElement(By.id("category"))).selectByVisibleText("Service");

        driver.findElement(By.id("subject")).clear();
        driver.findElement(By.id("subject")).sendKeys("Bad Service");

        // Short description under 10 chars
        driver.findElement(By.id("description")).clear();
        driver.findElement(By.id("description")).sendKeys("short");

        driver.findElement(By.id("submit-complaint")).click();

        wait.until(d -> !d.findElements(By.cssSelector("[role='alert']")).isEmpty());
        WebElement alert = driver.findElement(By.cssSelector("[role='alert']"));
        assertTrue(alert.isDisplayed(), "Validation error alert must be displayed");
        assertTrue(alert.getText().contains("description of at least 10 characters")
                || alert.getText().contains("valid email"), "Error message must explain validation rules");
    }

    @Test
    @Order(3)
    @DisplayName("Journey 3: Track complaint by generated reference")
    void test03_trackComplaintByGeneratedReference() {
        String uniqueSubject = "Track Test Subject " + System.currentTimeMillis();
        String ref = createComplaint("Tracking User", "track-" + System.currentTimeMillis() + "@test.org", "Delivery", uniqueSubject, "Package was delayed by over two weeks with no update.");

        driver.get(baseUrl + "/track?reference=" + ref);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(d -> !d.findElements(By.id("complaint-status")).isEmpty());

        assertEquals("SUBMITTED", driver.findElement(By.id("complaint-status")).getText().trim());
        assertTrue(driver.getPageSource().contains(uniqueSubject), "Tracking page must display complaint subject");
        assertTrue(driver.getPageSource().contains("Delivery"), "Tracking page must display category");
    }

    @Test
    @Order(4)
    @DisplayName("Journey 4: Reviewer approval and status verification")
    void test04_reviewerCanApprovePendingComplaint() {
        String uniqueSubject = "Approval Test Subject " + System.currentTimeMillis();
        String ref = createComplaint("Approval User", "approval-" + System.currentTimeMillis() + "@test.org", "Product quality", uniqueSubject, "Product arrived with a broken power switch.");

        driver.get(baseUrl + "/review");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(d -> d.getPageSource().contains(ref));

        // Locate the row for this specific complaint
        WebElement row = driver.findElement(By.id("complaint-row-" + ref));
        WebElement noteInput = row.findElement(By.cssSelector(".approve-form input[name='note']"));
        noteInput.clear();
        noteInput.sendKeys("Claim verified by officer - approved");

        WebElement approveBtn = row.findElement(By.cssSelector(".approve-btn"));
        approveBtn.click();

        wait.ignoring(StaleElementReferenceException.class)
                .until(d -> {
                    try {
                        List<WebElement> rows = d.findElements(By.id("complaint-row-" + ref));
                        if (rows.isEmpty()) return false;
                        List<WebElement> cells = rows.get(0).findElements(By.cssSelector(".status-cell"));
                        return !cells.isEmpty() && "APPROVED".equals(cells.get(0).getText().trim());
                    } catch (StaleElementReferenceException ex) {
                        return false;
                    }
                });

        // Verify status change and note in tracking page
        driver.get(baseUrl + "/track?reference=" + ref);
        wait.until(d -> !d.findElements(By.id("complaint-status")).isEmpty());
        assertEquals("APPROVED", driver.findElement(By.id("complaint-status")).getText().trim());
        assertEquals("Claim verified by officer - approved", driver.findElement(By.id("reviewer-note")).getText().trim());
    }

    @Test
    @Order(5)
    @DisplayName("Journey 5: Reviewer rejection and status verification")
    void test05_reviewerCanRejectPendingComplaint() {
        String uniqueSubject = "Rejection Test Subject " + System.currentTimeMillis();
        String ref = createComplaint("Rejection User", "reject-" + System.currentTimeMillis() + "@test.org", "Other", uniqueSubject, "Customer requested warranty past expiration date.");

        driver.get(baseUrl + "/review");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(d -> d.getPageSource().contains(ref));

        WebElement row = driver.findElement(By.id("complaint-row-" + ref));
        WebElement noteInput = row.findElement(By.cssSelector(".reject-form input[name='note']"));
        noteInput.clear();
        noteInput.sendKeys("Warranty period expired 6 months ago");

        WebElement rejectBtn = row.findElement(By.cssSelector(".reject-btn"));
        rejectBtn.click();

        wait.ignoring(StaleElementReferenceException.class)
                .until(d -> {
                    try {
                        List<WebElement> rows = d.findElements(By.id("complaint-row-" + ref));
                        if (rows.isEmpty()) return false;
                        List<WebElement> cells = rows.get(0).findElements(By.cssSelector(".status-cell"));
                        return !cells.isEmpty() && "REJECTED".equals(cells.get(0).getText().trim());
                    } catch (StaleElementReferenceException ex) {
                        return false;
                    }
                });

        // Verify status change and rejection reason in tracking page
        driver.get(baseUrl + "/track?reference=" + ref);
        wait.until(d -> !d.findElements(By.id("complaint-status")).isEmpty());
        assertEquals("REJECTED", driver.findElement(By.id("complaint-status")).getText().trim());
        assertEquals("Warranty period expired 6 months ago", driver.findElement(By.id("reviewer-note")).getText().trim());
    }

    @Test
    @Order(6)
    @DisplayName("Journey 6: Tracking unknown reference shows not found message")
    void test06_trackUnknownReferenceShowsNotFound() {
        driver.get(baseUrl + "/track?reference=CCP-999999NOTFOUND");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(d -> !d.findElements(By.id("not-found-message")).isEmpty() || d.getPageSource().contains("No complaint found"));
        assertTrue(driver.getPageSource().contains("No complaint found for that reference."), "Not found message must be displayed for invalid reference");
    }
}
