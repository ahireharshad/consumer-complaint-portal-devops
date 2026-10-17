# Selenium Test Plan & Execution Report

**Project Name:** Consumer Complaint Portal â€” Selenium Testing and DevOps
**Student Name:** Harshad Ahire Â· **Roll Number:** 23102C0056
**Test Suite Class:** `in.ac.consumerportal.ComplaintPortalSeleniumTest`
**Execution Environment:** Java 25.0.4.1 LTS, Maven 3.10.0, Google Chrome 155.0.8059.80 (Headless), Selenium WebDriver 4.25.0, JUnit 5.10.5

---

## 1. Test Execution Architecture

The test suite exercises end-to-end user journeys against a running Spring Boot instance using headless Chrome:

```
  â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”          HTTP Requests
  â”‚   Selenium Test Suite   â”‚â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”
  â”‚  (ComplaintPortalTest)  â”‚                                 â–¼
  â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”¬â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜                     â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”
              â”‚                                   â”‚  Spring Boot Web App  â”‚
              â”‚ If Failure Occurs                 â”‚  (Port 8088 / 8080)   â”‚
              â–¼                                   â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜
  â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”                                 â–²
  â”‚   Screenshot Extension  â”‚â”€â”€â–¶ Saves PNG to                 â”‚ JPA Queries
  â”‚ (ScreenshotOnFailure)   â”‚    target/screenshots/          â–¼
  â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜                     â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”
                                                  â”‚   H2 In-Memory DB     â”‚
                                                  â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜
```

### Execution Command:
```bash
# Package and launch app
java -Dserver.port=8088 -jar target/consumer-complaint-portal-1.0.0.jar

# Run headless tests in second terminal
mvn -Dheadless=true -DbaseUrl=http://localhost:8088 test
```

---

## 2. Test Case Matrix & Verified Results

| Test ID | Test Name / Journey | Objective / Verification Criteria | Status | Actual Execution Time |
|---|---|---|---|---|
| **UI-01** | `test01_successfulComplaintSubmission...` | Fills valid form; submits; asserts `CCP-XXXXXX` generated; tracks and asserts status `SUBMITTED`. | **PASS** | ~2.1 s |
| **UI-02** | `test02_missingOrInvalidFormInputs...` | Submits malformed email and short description; asserts server-side validation error banner `[role='alert']`. | **PASS** | ~0.6 s |
| **UI-03** | `test03_trackComplaintByGenerated...` | Submits complaint; visits `/track?reference=...`; asserts subject, category, and status match database. | **PASS** | ~1.4 s |
| **UI-04** | `test04_reviewerCanApprovePending...` | Submits fresh complaint; opens `/review`; clicks `Approve` with note; verifies status `APPROVED` and note displayed in `/track`. | **PASS** | ~2.5 s |
| **UI-05** | `test05_reviewerCanRejectPending...` | Submits fresh complaint; opens `/review`; clicks `Reject` with reason; verifies status `REJECTED` and note displayed in `/track`. | **PASS** | ~2.4 s |
| **UI-06** | `test06_trackUnknownReference...` | Navigates to `/track?reference=CCP-999999NOTFOUND`; asserts alert message `No complaint found for that reference.`. | **PASS** | ~0.4 s |

**Overall Result:** **6 Tests Run, 0 Failures, 0 Errors, 0 Skipped.** (Total elapsed: 10.05 seconds).

---

## 3. Failure Screenshot Mechanism

The test class implements `ScreenshotOnFailureExtension` using JUnit 5's `TestWatcher`:
- When any assertion fails, the extension casts `WebDriver` to `TakesScreenshot`.
- A full-DOM PNG file is written to: `target/screenshots/{methodName}_{timestamp}.png`.
- Output log reports: `[SELENIUM] Test failed! Captured screenshot saved to: target/screenshots/...`.
- If tests pass cleanly (as in the current baseline), no failure screenshots are generated.

---

## 4. Surefire XML Report Location

Test reports are formatted according to the standard JUnit XML format for CI/CD integration:
* **Report Directory:** `target/surefire-reports/`
* **XML File:** `TEST-in.ac.consumerportal.ComplaintPortalSeleniumTest.xml`
* **Text Summary:** `in.ac.consumerportal.ComplaintPortalSeleniumTest.txt`
