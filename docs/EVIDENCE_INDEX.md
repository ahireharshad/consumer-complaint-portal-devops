# Evidence Index & Verification Log (Phase 1)

**Project Name:** Consumer Complaint Portal â€” Selenium Testing and DevOps  
**Student Name:** Harshad Ahire Â· **Roll Number:** 23102C0056  
**Evaluation Target:** Phase 1 Verification Baseline  
**Verification Date:** 2026-10-17  

### Status Definitions:
- **PASS** â€” Successfully executed, tested, and verified with direct evidence.
- **PARTIAL** â€” Work partially implemented or planned across multiple phases.
- **BLOCKED** â€” Blocker identified preventing execution.
- **NOT IMPLEMENTED** â€” Work has not begun.

---

## 1. Evidence Verification Matrix

| Requirement / Scope Item | Verification Command / Test | Actual Result Observed | Evidence Location | Status |
|---|---|---|---|---|
| **Java 17+ Environment Compatibility** | `java -version` | Java version `25.0.4.1` LTS 64-Bit Server VM detected | Host environment runtime log | **PASS** |
| **Maven 3.9+ Build System** | `mvn -version` | Apache Maven `3.10.0` running on Windows 11 | Host environment runtime log | **PASS** |
| **Spring Boot Application Compilation** | `mvn clean test-compile` | `BUILD SUCCESS` (Compiles 4 main + 1 test class) | Maven compile log | **PASS** |
| **Executable Fat JAR Packaging** | `mvn -DskipTests package` | Generated `target/consumer-complaint-portal-1.0.0.jar` (repackaged archive) | `target/consumer-complaint-portal-1.0.0.jar` | **PASS** |
| **Application Startup & Port Configuration** | `java "-Dserver.port=8088" -jar target/...jar` | Tomcat started on port 8088, Spring Boot context initialized in 5.19s | Runtime console logs | **PASS** |
| **Port 8080 Conflict Diagnosis** | `netstat -ano \| findstr :8080`, `curl http://localhost:8080/` | Port 8080 is bound to local Jenkins (`X-Jenkins: 2.580.1`); resolved by port binding | `docs/PROJECT_AUDIT.md` Section 1 | **PASS** |
| **HTTP Root Route Health (`GET /`)** | `curl.exe -i http://localhost:8088/` | HTTP 200 OK; semantic intake form rendered | `docs/PROJECT_AUDIT.md` Section 2 | **PASS** |
| **Complaint Submission & Reference Generation** | `ComplaintPortalSeleniumTest.test01` | Reference generated (`CCP-XXXXXX`), status verified as `SUBMITTED` | `target/surefire-reports/*.xml` | **PASS** |
| **Server-Side Input Validation Enforcement** | `ComplaintPortalSeleniumTest.test02` | Invalid inputs rejected; error alert displayed (`role='alert'`) | `target/surefire-reports/*.xml` | **PASS** |
| **Real-Time Status Tracking by Reference** | `ComplaintPortalSeleniumTest.test03` | Correct category, subject, and `SUBMITTED` status retrieved | `target/surefire-reports/*.xml` | **PASS** |
| **Reviewer Approval Workflow & Note Display** | `ComplaintPortalSeleniumTest.test04` | Status updated to `APPROVED`; note visible in tracking page | `target/surefire-reports/*.xml` | **PASS** |
| **Reviewer Rejection Workflow & Reason Display**| `ComplaintPortalSeleniumTest.test05` | Status updated to `REJECTED`; reason visible in tracking page | `target/surefire-reports/*.xml` | **PASS** |
| **Unknown Reference Not-Found Alert** | `ComplaintPortalSeleniumTest.test06` | Message displayed: `No complaint found for that reference.` | `target/surefire-reports/*.xml` | **PASS** |
| **Decision State Transition Lock** | `Complaint.java.decide()`, `ComplaintController.java` | Status check blocks mutating already decided complaints | `src/main/java/in/ac/consumerportal/Complaint.java` | **PASS** |
| **Selenium Failure Screenshot Mechanism** | `ScreenshotOnFailureExtension` | JUnit 5 TestWatcher implemented; captures to `target/screenshots/` | `src/test/java/.../ComplaintPortalSeleniumTest.java` | **PASS** |
| **Complete Selenium Test Suite Execution** | `mvn -Dheadless=true -DbaseUrl=http://localhost:8088 test` | Tests run: 6, Failures: 0, Errors: 0, Skipped: 0 (18.55s) | `target/surefire-reports/in.ac.consumerportal.ComplaintPortalSeleniumTest.txt` | **PASS** |
| **Git & GitHub Repository Configuration** | `git status`, `.gitignore`, PR template | Initialized main branch; created PR template & Git workflow docs | `.github/pull_request_template.md`, `docs/GIT_WORKFLOW.md` | **PASS** |
| **15-Task MVP Backlog & User Stories** | Inspection of project planning docs | 15 tasks, user stories, acceptance criteria, DoD completed | `docs/PROJECT_PLAN.md` | **PASS** |
| **Software Requirements Specification (SRS)** | Inspection of SRS document | SRS with Mermaid use cases, architecture, ER model, routes | `docs/SRS.md` | **PASS** |
| **Phase 2: Jenkins Declarative Pipeline Execution** | Pipeline execution | Jenkinsfile present; awaiting Phase 2 authorization | `Jenkinsfile` | **PENDING** |
| **Phase 2: Docker Container Build & Execution** | Container execution | Dockerfile present; awaiting Phase 2 authorization | `Dockerfile` | **PENDING** |
| **Phase 2: Ansible Playbook Execution** | Ansible execution | Playbook present; awaiting Phase 2 authorization | `ansible/deploy.yml` | **PENDING** |

---

## 2. Directory Index of Reports & Artifacts

- **Compiled Application Archive:** [target/consumer-complaint-portal-1.0.0.jar](file:///c:/Users/ADMIN/Downloads/consumer-complaint-portal-devops-main/consumer-complaint-portal-devops-main/target/consumer-complaint-portal-1.0.0.jar)
- **Surefire XML Test Report:** [target/surefire-reports/TEST-in.ac.consumerportal.ComplaintPortalSeleniumTest.xml](file:///c:/Users/ADMIN/Downloads/consumer-complaint-portal-devops-main/consumer-complaint-portal-devops-main/target/surefire-reports/TEST-in.ac.consumerportal.ComplaintPortalSeleniumTest.xml)
- **Surefire Text Summary:** [target/surefire-reports/in.ac.consumerportal.ComplaintPortalSeleniumTest.txt](file:///c:/Users/ADMIN/Downloads/consumer-complaint-portal-devops-main/consumer-complaint-portal-devops-main/target/surefire-reports/in.ac.consumerportal.ComplaintPortalSeleniumTest.txt)
- **Screenshots Directory:** `target/screenshots/` (Activated on failure)
- **Pull Request Template:** [.github/pull_request_template.md](file:///c:/Users/ADMIN/Downloads/consumer-complaint-portal-devops-main/consumer-complaint-portal-devops-main/.github/pull_request_template.md)
- **Project Audit Document:** [docs/PROJECT_AUDIT.md](file:///c:/Users/ADMIN/Downloads/consumer-complaint-portal-devops-main/consumer-complaint-portal-devops-main/docs/PROJECT_AUDIT.md)
- **Project Plan Document:** [docs/PROJECT_PLAN.md](file:///c:/Users/ADMIN/Downloads/consumer-complaint-portal-devops-main/consumer-complaint-portal-devops-main/docs/PROJECT_PLAN.md)
- **SRS Document:** [docs/SRS.md](file:///c:/Users/ADMIN/Downloads/consumer-complaint-portal-devops-main/consumer-complaint-portal-devops-main/docs/SRS.md)
- **Test Plan Document:** [docs/TEST_PLAN.md](file:///c:/Users/ADMIN/Downloads/consumer-complaint-portal-devops-main/consumer-complaint-portal-devops-main/docs/TEST_PLAN.md)
- **Git Workflow Document:** [docs/GIT_WORKFLOW.md](file:///c:/Users/ADMIN/Downloads/consumer-complaint-portal-devops-main/consumer-complaint-portal-devops-main/docs/GIT_WORKFLOW.md)

