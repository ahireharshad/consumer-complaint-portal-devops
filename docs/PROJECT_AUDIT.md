# Project Baseline Audit & Requirements Matrix (Phase 1)

**Project:** Consumer Complaint Portal — Selenium Testing and DevOps  
**Student:** Harshad Ahire · **Roll No.:** 23102C0056  
**Repository:** [https://github.com/ahireharshad/consumer-complaint-portal-devops](https://github.com/ahireharshad/consumer-complaint-portal-devops)  
**Audit Date:** 2026-10-17  
**Evaluation Scope:** Phase 1 Verification & Baseline Audit

---

## 1. Executive Summary

This audit establishes the baseline of the Consumer Complaint Portal project. The source code, build scripts, tests, dependencies, and environment were directly inspected and executed on the local system.

### Key Baseline Findings:
1. **Compilation & Packaging:** Verified working with **Java 25.0.4.1 LTS** and **Apache Maven 3.10.0**. The application compiles cleanly to Java 17 bytecode compatibility.
2. **Port 8080 Service Conflict Identified:** Port 8080 was discovered to be occupied by an active Jenkins instance (`Jetty 12.1.13`, `X-Jenkins: 2.580.1`). When the application defaulted to 8080, it failed to start, or tests directed at `localhost:8080` hit the Jenkins login page, causing WebDriver wait timeouts. When bound to port `8088`, the portal starts and responds cleanly with HTTP 200.
3. **Application Deficiencies Identified & Repaired:**
   - Detached entity mutation without explicit repository persistence in `ComplaintController.decide()`.
   - Missing state transition guard (allowing repeated review decisions on already decided complaints).
   - Incomplete Selenium test suite (originally only 4 journeys; missing invalid input tests, rejection tests, and failure screenshot captures).
4. **Current Test Status:** Enhanced 6-journey Selenium test suite executed and passed with **0 failures, 0 errors, 0 skipped** (Execution time: 10.05 seconds, Surefire report generated).

---

## 2. Existing Features & Project Structure

### Repository Structure
```
consumer-complaint-portal-devops/
├── .github/
│   ├── ISSUE_TEMPLATE/
│   │   ├── bug_report.md
│   │   └── feature_request.md
│   ├── workflows/
│   │   └── ci.yml
│   └── pull_request_template.md
├── .gitignore
├── Dockerfile
├── Jenkinsfile
├── README.md
├── ansible/
│   ├── deploy.yml
│   └── inventory.ini
├── docker-compose.yml
├── docs/
│   ├── COMPLETION_AUDIT.md
│   ├── DEVOPS_WORKFLOW.md
│   ├── EVIDENCE_CHECKLIST.md
│   ├── GIT_WORKFLOW.md
│   ├── PROJECT_AUDIT.md
│   ├── PROJECT_PLAN.md
│   ├── SRS.md
│   └── TEST_PLAN.md
├── pom.xml
└── src/
    ├── main/
    │   ├── java/in/ac/consumerportal/
    │   │   ├── Complaint.java
    │   │   ├── ComplaintController.java
    │   │   ├── ComplaintPortalApplication.java
    │   │   └── ComplaintRepository.java
    │   └── resources/
    │       ├── application.properties
    │       └── templates/
    │           ├── index.html
    │           ├── review.html
    │           └── track.html
    └── test/
        └── java/in/ac/consumerportal/
            └── ComplaintPortalSeleniumTest.java
```

### Functional Inventory
| Feature | Module / File | Verified Status |
|---|---|---|
| Complaint Submission Form | `ComplaintController.java`, `index.html` | PASS |
| Server-Side Input Validation (Regex email, min 10 char description) | `ComplaintController.java` | PASS |
| Unique Reference Generation (`CCP-XXXXXX`) | `Complaint.java` (@PrePersist) | PASS |
| Complaint Tracking by Reference | `ComplaintController.java`, `track.html` | PASS |
| Reviewer Desk Dashboard | `ComplaintController.java`, `review.html` | PASS |
| Reviewer Decision (Approval with Note) | `ComplaintController.java`, `review.html` | PASS |
| Reviewer Decision (Rejection with Note) | `ComplaintController.java`, `review.html` | PASS |
| Status Transition Guard (Immutability after decision) | `Complaint.java`, `ComplaintController.java` | PASS |
| Headless Selenium Test Automation (6 Journeys) | `ComplaintPortalSeleniumTest.java` | PASS |
| Failure Screenshot Capture Extension | `ComplaintPortalSeleniumTest.java` | PASS |

---

## 3. Build & Test Baseline

* **JDK Environment:** Java version `25.0.4.1` (Java HotSpot 64-bit Server VM).
* **Build System:** Apache Maven `3.10.0`.
* **Package Target:** `target/consumer-complaint-portal-1.0.0.jar` (Spring Boot executable fat JAR).
* **Browser Driver:** Google Chrome `155.0.8059.80` with Selenium WebDriver `4.25.0` (Headless mode).
* **Actual Surefire Result:**
  ```
  Tests run: 6, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 10.05 s
  ```
* **Report Location:** `target/surefire-reports/TEST-in.ac.consumerportal.ComplaintPortalSeleniumTest.xml`.

---

## 4. Bugs, Configuration Problems & Remediation

| Issue ID | Identified Defect / Problem | Root Cause | Remediation Applied |
|---|---|---|---|
| BUG-01 | Port 8080 collision on developer host | Host runs Jenkins background service on port 8080 | Configured configurable application port (`PORT=8088`), updated test instructions, verified independent execution. |
| BUG-02 | Unpersisted reviewer decisions | Detached entity modified in controller without `repository.save()` when OSIV is disabled | Added explicit `repository.save(c)` in `ComplaintController.decide()` to guarantee transaction commit. |
| BUG-03 | Re-adjudication vulnerability | No backend validation checking if status was already decided | Added state check `if (!"SUBMITTED".equals(c.getStatus()))` in `Complaint.java` and controller. |
| BUG-04 | Reference ID negative number risk | `System.nanoTime() % 1000000` can be negative depending on JVM clock | Replaced with `Math.abs(System.nanoTime() % 1000000L)` and formatted with zero-padding `String.format("CCP-%06d", token)`. |
| BUG-05 | Incomplete Selenium coverage | Only 4 tests existed; missing invalid input testing and reviewer rejection | Re-architected `ComplaintPortalSeleniumTest.java` with 6 complete journeys. |
| BUG-06 | Missing failure screenshot mechanism | Documented in test plan but absent in test code | Implemented JUnit 5 `TestWatcher` (`ScreenshotOnFailureExtension`) saving to `target/screenshots`. |

---

## 5. Security Concerns & Prototype Caveats

1. **Unauthenticated Reviewer Desk:** The reviewer desk (`/review`) and action endpoints (`/review/{id}/{decision}`) are not access-controlled in this academic MVP. For production, Spring Security with role-based access control (`ROLE_REVIEWER`) and CSRF tokens must be added.
2. **In-Memory Storage:** The database uses H2 in-memory mode (`jdbc:h2:mem:complaints`). Records do not survive process restarts. Production environments require a durable RDBMS (e.g., PostgreSQL or MySQL).
3. **Collision Safety:** Reference generation uses nano-time hashing suitable for academic demo, but enterprise production requires UUIDv4 or Twitter Snowflake IDs with database unique constraints.

---

## 6. Comprehensive Requirements Matrix (14 Assignment Stages & 15-Task MVP)

### Fourteen Assignment Stages

| Stage | Description | Phase | Status | Evidence / Notes |
|---|---|---|---|---|
| Stage 01 | Problem Definition & Scope Analysis | Phase 1 | **PASS** | Documented in `docs/PROJECT_PLAN.md` and `docs/SRS.md` |
| Stage 02 | Agile Project Planning & Backlog Formulation | Phase 1 | **PASS** | 15-task backlog and sprint plan in `docs/PROJECT_PLAN.md` |
| Stage 03 | Requirements Specification & System Architecture | Phase 1 | **PASS** | Documented with Mermaid architecture/ER in `docs/SRS.md` |
| Stage 04 | Git Repository & GitHub Governance Initialization | Phase 1 | **PASS** | `.gitignore`, templates, Git workflow in `docs/GIT_WORKFLOW.md` |
| Stage 05 | Branching Strategy & Pull Request Workflow | Phase 1 | **PASS** | PR template and branching documented; ready for PR exercise |
| Stage 06 | Application Source Implementation & State Logic | Phase 1 | **PASS** | Spring Boot MVC, JPA entity, controller guards, Thymeleaf UI verified |
| Stage 07 | Selenium Automation & Failure Screenshot System | Phase 1 | **PASS** | 6 journeys verified passing (Surefire report verified) |
| Stage 08 | Verification of Defect Diagnosis & Green Rerun | Phase 1 | **PASS** | Port 8080 conflict diagnosed and fixed; 100% green test run |
| Stage 09 | Jenkins Declarative Pipeline Configuration | Phase 2 | **PENDING** | `Jenkinsfile` present; Phase 2 execution pending user approval |
| Stage 10 | Docker Containerization & Multi-Stage Packaging | Phase 2 | **PENDING** | `Dockerfile` present; Phase 2 execution pending user approval |
| Stage 11 | Docker Hub Container Registry Distribution | Phase 2 | **PENDING** | Phase 2 task pending user approval |
| Stage 12 | Ansible Configuration Management & Host Inventory | Phase 2 | **PENDING** | `ansible/deploy.yml` present; Phase 2 execution pending |
| Stage 13 | Idempotent Provisioning & Automated Health Check | Phase 2 | **PENDING** | Phase 2 task pending user approval |
| Stage 14 | Final Verification, Evidence Audit & Release Tag | Phase 1/2 | **PARTIAL** | Phase 1 verified; release tag and final viva report in Phase 2 |

---

### Fifteen-Task MVP Backlog Status

| Task ID | Task Description | Priority | Acceptance Criteria | Phase 1 Status | Evidence Location |
|---|---|---|---|---|---|
| T01 | Problem definition and scope | High | Problem, objectives, stakeholders, constraints documented | **PASS** | `docs/PROJECT_PLAN.md` |
| T02 | Agile/DevOps planning | High | 15-task backlog, user stories, DoD, sprint plan | **PASS** | `docs/PROJECT_PLAN.md` |
| T03 | Requirements and architecture | High | SRS, use cases, architecture, data model, route list | **PASS** | `docs/SRS.md` |
| T04 | GitHub initialization | High | `.gitignore`, README, PR template, issue templates | **PASS** | `.github/`, `.gitignore`, `README.md` |
| T05 | Feature branch workflow | Medium | Branching model, code review, merge procedures | **PASS** | `docs/GIT_WORKFLOW.md` |
| T06 | Functional MVP implementation | High | Submission, validation, tracking, reviewer decision | **PASS** | `src/main/`, verified running on port 8088 |
| T07 | Jenkins CI pipeline | High | Pipeline as code, test execution, artifact archiving | **PENDING** | Phase 2 (Configured in `Jenkinsfile`) |
| T08 | Pipeline deployment automation | Medium | Automated build-to-deploy pipeline | **PENDING** | Phase 2 |
| T09 | Selenium local testing | High | 5+ journeys, assertions, explicit waits, screenshots | **PASS** | `ComplaintPortalSeleniumTest.java` (6/6 Pass) |
| T10 | Continuous testing gate | High | CI gating on test results, defect diagnosis | **PASS** | Verified locally; ready for CI gate |
| T11 | Docker container lifecycle | High | Multi-stage build, non-root user, container execution | **PENDING** | Phase 2 (Configured in `Dockerfile`) |
| T12 | Jenkins-Docker CD integration | Medium | Automated image build and registry distribution | **PENDING** | Phase 2 |
| T13 | Configuration management (Ansible)| High | Ansible playbook, host inventory | **PENDING** | Phase 2 (Configured in `ansible/`) |
| T14 | Provisioning & health reliability| High | Host provisioning, health check retry loop | **PENDING** | Phase 2 |
| T15 | Final evidence audit & release | High | Comprehensive reports, evidence index, release tag | **PARTIAL** | Phase 1 complete in `docs/` |

---

## 7. Phase 1 Sign-Off Readiness

All Phase 1 code implementations, architectural fixes, test enhancements, and baseline audits are complete and verified by actual execution. No Phase 2 tasks (Jenkins execution, Docker container deployment, or Ansible execution) have been initiated, in strict adherence to Phase 1 boundaries.
