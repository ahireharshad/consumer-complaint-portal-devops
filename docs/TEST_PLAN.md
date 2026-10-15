# Selenium Test Plan and Test Cases

## Environment
Java 17+, Maven 3.9+, Chrome/Chromium. Start app with `mvn spring-boot:run`, then run in another terminal:
```bash
mvn -Dheadless=true -DbaseUrl=http://localhost:8080 test
```
The current CI workflow builds the JAR, starts it, and runs Selenium. Reports are in `target/surefire-reports/`.

## Test cases
| ID | Test | Expected result | Automation |
|---|---|---|---|
| UI-01 | Open home | Form and submit control visible | Implemented |
| UI-02 | Submit valid complaint | Confirmation and CCP reference shown | Implemented; currently failing in latest observed CI run, investigate before sign-off |
| UI-03 | Track new complaint | SUBMITTED status displayed | In same flow as UI-02 |
| UI-04 | Reviewer approves pending complaint | Status changes to APPROVED | Implemented |
| UI-05 | Track unknown reference | Not-found message displayed | Implemented |
| VAL-01 | Invalid email | Server rejects and shows validation error | Manual/automated follow-up needed |
| VAL-02 | Description under 10 chars | Server rejects and shows validation error | Manual/automated follow-up needed |
| DEC-01 | Repeat decision after final status | No second decision accepted | Add controller test/guard before sign-off |

## Failure screenshots
The current Selenium class does not automatically capture screenshots on failure. Add a JUnit extension or a test teardown that captures `TakesScreenshot` when a test fails and saves it under `target/screenshots`; archive that directory in CI. Do not claim this is complete until implemented and exercised.

## CI gate
The Maven test stage must run before Docker build/publish/deploy. A failed UI test must make the workflow fail. The latest observed GitHub Actions run built and started the app successfully but failed in `submitComplaintAndCaptureReference` with a WebDriver wait timeout. This is an open defect, not a passing test result.
