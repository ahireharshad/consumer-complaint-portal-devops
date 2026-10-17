# Project Plan & 15-Task Agile Backlog

**Project Name:** Consumer Complaint Portal â€” Selenium Testing and DevOps
**Student Name:** Harshad Ahire Â· **Roll Number:** 23102C0056
**GitHub Repository:** [https://github.com/ahireharshad/consumer-complaint-portal-devops](https://github.com/ahireharshad/consumer-complaint-portal-devops)
**Academic Target:** College DevOps & QA Engineering Phase 1 / Phase 2 Assessment

---

## 1. Problem Statement & Existing Pain Points

### The Problem
Consumers purchasing products or consuming digital/offline services frequently experience billing inaccuracies, delivery delays, and defective products. Traditional customer redressal channels suffer from:
1. **Opaque Communication:** Complaints submitted via generic email or paper forms lack verifiable tracking numbers.
2. **Inconsistent Data Capture:** Incomplete submissions (missing categories, malformed email addresses, uninformative one-word descriptions) overwhelm support queues.
3. **Deployment & Regression Risks:** Software updates frequently break critical consumer-facing web forms because automated browser testing is omitted from release pipelines.
4. **Environment Drift:** Manual server deployments cause inconsistencies between developer laptops and production servers.

### The Solution
The **Consumer Complaint Portal** standardizes grievance intake through server-validated semantic forms, assigns collision-resistant tracking reference codes (`CCP-XXXXXX`), provides a real-time status lookup engine, and offers an administrative triage desk with state transition safeguards. Release confidence is ensured through automated Selenium 4 test suites integrated into CI/CD pipelines and infrastructure-as-code deployment playbooks.

---

## 2. Project Objectives & Measurable Success Criteria

* **Objective 1 (Intake Standardization):** 100% of complaints must validate required fields, enforce RFC email formatting, and require at least 10 characters of descriptive detail.
* **Objective 2 (Deterministic Tracking):** Every accepted complaint receives an immediate reference token (`CCP-XXXXXX`) that maps deterministically to its database record.
* **Objective 3 (Audited Adjudication):** Support reviewers can make binary determinations (`APPROVED` or `REJECTED`) with an attached explanatory note; once decided, status transitions are locked.
* **Objective 4 (Automated Quality Gate):** A 6-journey Selenium E2E test suite running headless Chrome must execute in under 20 seconds with 0 failures before any build artifact is approved.
* **Objective 5 (Continuous Delivery):** Containerized multi-stage packaging and automated configuration management must verify HTTP 200 health upon deployment.

---

## 3. Stakeholders & Target Users

| Stakeholder Role | Responsibilities / Goals | Key Workflows |
|---|---|---|
| **Consumer / Complainant** | Submits grievances, captures reference ID, tracks real-time progress | `GET /`, `POST /complaints`, `GET /track?reference=...` |
| **Reviewer / Support Officer** | Reviews incoming complaints, evaluates claims, records decisions | `GET /review`, `POST /review/{id}/{decision}` |
| **QA / Automation Engineer** | Writes, maintains, and executes automated browser test suites | Headless Selenium WebDriver, JUnit 5 assertions, Surefire XML reports |
| **DevOps / SRE Engineer** | Maintains build toolchains, CI/CD pipelines, Dockerfiles, and Ansible playbooks | Jenkinsfile, GitHub Actions, Docker multi-stage builds, Ansible playbooks |
| **Academic Evaluator** | Audits documentation, verifies test logs, checks Git history and viva answers | Reviews `docs/`, validates execution reports, grades project |

---

## 4. Scope, Exclusions & Constraints

### In Scope (Phase 1 & Phase 2)
* Complaint submission form with client and server-side validation.
* Unique tracking reference generation and status tracking page.
* Reviewer dashboard for approval/rejection with reviewer notes.
* Spring Boot 3.3.5 MVC backend with Spring Data JPA and in-memory H2 database.
* Selenium 4 E2E browser automation covering all core user journeys with failure screenshot capability.
* Declarative CI/CD pipeline definitions (Jenkinsfile, GitHub Actions `ci.yml`).
* Multi-stage Docker containerization and Ansible deployment automation.

### Excluded from Scope (Academic Prototype)
* User authentication and role-based access control (RBAC). Reviewer endpoints are public for local demonstration.
* Durable disk-backed SQL storage (H2 in-memory resets on restart).
* Third-party document or photo file uploads.
* External email/SMS notification dispatchers.
* Payment gateway integrations.

### Technical Constraints
* **Runtime:** Java 17+ (Verified on Java 25 LTS).
* **Framework:** Spring Boot 3.3.5, Spring MVC, Spring Data JPA.
* **Browser Automation:** Selenium WebDriver 4.25.0, Headless Chrome, JUnit 5 Jupiter.
* **Port Allocation:** Configurable via `${PORT:8080}`; default to 8088 when local host port 8080 is occupied by Jenkins.

---

## 5. Fifteen-Task Backlog & Status Matrix

```mermaid
kanban
  Todo
    [T07] Jenkins CI Pipeline
    [T08] Pipeline Deployment
    [T11] Docker Containerization
    [T12] Jenkins-Docker CD
    [T13] Ansible Provisioning
    [T14] Reliability & Health Checks
  In Progress
    [T15] Final Evidence Checklist
  Done
    [T01] Problem Definition & Scope
    [T02] Agile Planning & Backlog
    [T03] SRS & Architecture
    [T04] GitHub Initialization
    [T05] Git Workflow & PR Procedure
    [T06] Functional MVP Implementation
    [T09] Selenium E2E Test Suite
    [T10] Continuous Testing Gate
```

| Task ID | Task Description | Priority | Acceptance Criteria | Phase | Status | Verification Evidence |
|---|---|---|---|---|---|---|
| **T01** | Problem definition and scope | High | Problem, objectives, stakeholders, constraints, metrics documented | Phase 1 | **PASS** | `docs/PROJECT_PLAN.md`, `docs/SRS.md` |
| **T02** | Agile/DevOps planning | High | 15-task backlog, user stories, DoD, sprint plan | Phase 1 | **PASS** | `docs/PROJECT_PLAN.md` |
| **T03** | Requirements and architecture | High | SRS, use case diagram, architecture, ER model, route list | Phase 1 | **PASS** | `docs/SRS.md` |
| **T04** | GitHub initialization | High | `.gitignore`, README, issue templates, PR template | Phase 1 | **PASS** | `.github/`, `.gitignore`, `README.md` |
| **T05** | Git workflow and collaboration | Medium | Documented branch strategy, PR process, merge conflict resolution | Phase 1 | **PASS** | `docs/GIT_WORKFLOW.md` |
| **T06** | Functional MVP implementation | High | Submission, validation, tracking, reviewer decisions, state lock | Phase 1 | **PASS** | Verified running on port 8088; verified routes |
| **T07** | Jenkins CI pipeline | High | Pipeline as code, test execution, JUnit report archiving | Phase 2 | **PENDING** | `Jenkinsfile` present |
| **T08** | Pipeline deployment automation | Medium | Automated build, package, test, container rollout | Phase 2 | **PENDING** | Pipeline configs present |
| **T09** | Selenium local test automation | High | 5+ journeys, explicit waits, assertions, failure screenshot | Phase 1 | **PASS** | `ComplaintPortalSeleniumTest.java` (6/6 Pass) |
| **T10** | Continuous testing gate | High | Automated test execution, defect diagnosis, green rerun | Phase 1 | **PASS** | Surefire XML report; port 8080 conflict resolved |
| **T11** | Docker container lifecycle | High | Multi-stage build, non-root user (10001), RAM tuning | Phase 2 | **PENDING** | `Dockerfile` present |
| **T12** | Jenkins-Docker CD integration | Medium | Automated image build and registry distribution | Phase 2 | **PENDING** | `Jenkinsfile` stage present |
| **T13** | Configuration management (Ansible) | High | Ansible playbook, host inventory | Phase 2 | **PENDING** | `ansible/deploy.yml` present |
| **T14** | Provisioning & health reliability | High | Remote host provisioning, retry health check loop | Phase 2 | **PENDING** | `ansible/deploy.yml` present |
| **T15** | Release evidence & viva prep | High | Evidence checklist, completion report, semantic release tag | Phase 1/2 | **PARTIAL** | Phase 1 verified in `docs/PHASE1_COMPLETION_REPORT.md` |

---

## 6. User Stories & Acceptance Criteria

### User Story 1 (Consumer Complaint Submission)
* **As a** consumer,
* **I want to** fill out a structured complaint form with my contact details and issue description,
* **So that** my grievance is officially registered and assigned a unique reference number.
* **Acceptance Criteria:**
  - Mandatory fields: Name, Email, Category, Subject, Description.
  - Server rejects empty fields, malformed email strings, and descriptions shorter than 10 characters.
  - On success, displays `Complaint submitted successfully` and presents reference `#submitted-reference`.

### User Story 2 (Consumer Complaint Tracking)
* **As a** consumer,
* **I want to** look up my complaint using its reference code (`CCP-XXXXXX`),
* **So that** I can track its current status and read reviewer comments.
* **Acceptance Criteria:**
  - Searching a valid reference displays Category, Subject, Status, Submission Date, and Reviewer Note.
  - Searching an invalid reference displays a semantic `[role='alert']` message: `No complaint found for that reference.`.

### User Story 3 (Support Reviewer Adjudication)
* **As a** customer support reviewer,
* **I want to** view all submitted complaints and approve or reject them with an explanatory note,
* **So that** issues are resolved transparently.
* **Acceptance Criteria:**
  - Reviewer desk lists all submitted complaints in a tabular format.
  - Reviewer can enter a note and click `Approve` or `Reject`.
  - Once decided, the status updates in the database, and the buttons are disabled to prevent duplicate state changes.

### User Story 4 (QA Engineer Automated Testing)
* **As a** QA engineer,
* **I want** automated Selenium browser tests to exercise all core journeys,
* **So that** functional regressions are caught automatically before deployment.
* **Acceptance Criteria:**
  - Tests run in headless Chrome mode.
  - Uses explicit `WebDriverWait` primitives.
  - Automatically captures failure screenshots to `target/screenshots/` if any assertion fails.

---

## 7. Sprint & Agile Delivery Plan

* **Sprint 1 (Weeks 1â€“2) â€” Foundation & Planning:** Tasks T01 to T04. Problem definition, SRS, Git initialization, issue templates.
* **Sprint 2 (Weeks 3â€“4) â€” MVP Implementation & Testing:** Tasks T05, T06, T09, T10. Form validation, reference generation, reviewer workflow, Selenium automation suite, defect resolution.
* **Sprint 3 (Weeks 5â€“6) â€” CI/CD Pipeline & Packaging:** Tasks T07, T08, T11, T12. Jenkinsfile configuration, GitHub Actions workflow, Docker multi-stage build, container registry integration.
* **Sprint 4 (Weeks 7â€“8) â€” Provisioning & Final Sign-Off:** Tasks T13, T14, T15. Ansible playbook execution, health check verification, evidence checklist completion, release tagging (`v1.0.0`).

---

## 8. Definition of Done (DoD)

A backlog item or phase is considered **Done** only when:
1. **Source Code:** Code is written, peer-reviewed, and follows idiomatic patterns.
2. **Server-Side Validation:** All inputs are sanitized and validated on the backend.
3. **Automated Tests:** Selenium E2E and unit tests execute cleanly (zero failures, zero errors).
4. **Build Verification:** `mvn clean package` succeeds and produces the executable fat JAR.
5. **No Committed Secrets:** Credentials, tokens, private keys, and binaries are excluded via `.gitignore`.
6. **Documentation:** Architecture, SRS, and test reports are up-to-date and reflect actual system behavior.
7. **Honest Status:** Requirements are verified with actual execution evidence before being marked complete.
