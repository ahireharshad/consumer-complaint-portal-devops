# Project plan: Consumer Complaint Portal

**Student:** Harshad Ahire · **Roll number:** 23102C0056  
**Project:** Selenium Testing for a Consumer Complaint Portal

## 1. Problem statement
Consumers need a consistent way to submit a product/service complaint, provide enough information for review, receive a reference, and understand the decision. The MVP standardizes submission, basic validation, status lookup and reviewer decisions.

## 2. Stakeholders
- Consumer/complainant: enters complaint and tracks reference.
- Reviewer/support officer: reviews and approves/rejects a submission.
- Operator/administrator: builds, deploys and monitors the service.
- Developer/tester: maintains source, tests, CI and release.
- Evaluator: checks requirements and evidence.

## 3. Goals and measurable success criteria
- Valid complaint submission creates a reference.
- Required fields, email syntax and minimum description length are validated server-side.
- A valid reference displays the current status.
- Reviewer can decide APPROVED or REJECTED and record a note.
- Selenium covers home page, submission/tracking, reviewer decision and unknown reference.
- CI fails on failed tests and archives JUnit reports.
- Container serves HTTP on port 8080 and deployment automation checks health.

## 4. Constraints and exclusions
Java 17, Spring Boot 3, Maven, Thymeleaf, JPA and H2; local academic demo; no real document upload, authentication, durable database, notifications or audit trail. In-memory H2 data resets on restart. Reviewer endpoints are intentionally unauthenticated and must not be exposed to the internet. Docker/Ansible/Jenkins execution and screenshots must be verified in the student's own environment.

## 5. Fifteen-task backlog and acceptance
| ID | Task | Acceptance criteria | Current evidence/status |
|---|---|---|---|
| T01 | Problem definition and scope | Problem, stakeholders, objectives, constraints, metrics recorded | Documented |
| T02 | Agile/DevOps planning | Stories, 15-task plan, DoD and workflow diagram | Documented; real board not created |
| T03 | Requirements and architecture | SRS, use cases, architecture, data model, endpoints, local setup | Documented/source present; local setup must be run |
| T04 | GitHub initialization | README, ignore rules, folder layout, issue templates, branch policy | Files present; only main branch confirmed |
| T05 | First feature branch workflow | Feature branch, PR, review, merge evidence | Not demonstrated; no PR found |
| T06 | Functional MVP and collaboration | Submission, validation, reviewer decision, tracking, conflict resolution, tag | Core source present; conflict/tag evidence outstanding |
| T07 | Jenkins CI | Jenkins job, trigger, build log and archived JAR | Jenkinsfile present; Jenkins job not verified |
| T08 | Pipeline/deployment | Pipeline as code, deployment target, configurable setting, live URL | Partial: Jenkinsfile; live deployment not verified |
| T09 | Selenium local testing | 3–5 journeys, assertions, reports, failure screenshot mechanism | Selenium tests present; CI has a failing test; screenshot capture not automated |
| T10 | Continuous testing | Test gate, failure evidence, defect fix and green rerun | Incomplete: latest observed workflow failed during UI test |
| T11 | Docker lifecycle | Dockerfile, build/run/log/stop/restart/remove evidence | Files/commands documented; runtime evidence absent |
| T12 | Jenkins-Docker CD | Versioned image, registry publish, automatic deploy | Partial: versioned image build/optional push; publish/deploy not verified |
| T13 | Configuration management | Ansible inventory/playbook and first run log | Files present; playbook execution not verified |
| T14 | Provisioning/reliability | Clean node, idempotency, health check and rollback | Partial: health-check task exists; run/idempotency/rollback evidence absent |
| T15 | Release/evidence | Release tag, report, evidence checklist and updated backlog | Checklist present; release tag/screenshots/report outstanding |

## 6. User stories
- As a consumer, I can submit a complaint with contact details and description.
- As a consumer, I receive a reference and can track the status.
- As a reviewer, I can approve/reject a pending complaint with a note.
- As an operator, I can build/test/package/deploy repeatably.

## 7. Definition of Done
- Acceptance criteria met and input validation occurs server-side.
- Changes use a named feature branch, meaningful commits and review before merge.
- Maven/Selenium tests pass and JUnit reports are retained.
- Docker image is versioned; credentials are not committed.
- Deployment health check succeeds and rollback is documented.
- Evidence is captured from actual executions and secrets/consumer data are redacted.

## 8. Sprint/Kanban plan
Suggested academic plan: Sprint 1 (T01–T04); Sprint 2 (T05–T06); Sprint 3 (T07–T10); Sprint 4 (T11–T15). Track each item as Backlog → Ready → In Progress → Review/Test → Done. A task is not Done solely because a configuration file exists; its execution evidence is required.

## 9. Branching and release policy
Use `main` for stable releases and `feature/<short-name>` for work. Open PRs against main, link the issue, describe acceptance criteria, require tests/review, then merge. Use `v1.0.0` only after verification. Create conflict-resolution evidence honestly by making divergent edits on two branches, merging one, then resolving the second branch.
