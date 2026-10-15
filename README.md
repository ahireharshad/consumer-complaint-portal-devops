# Consumer Complaint Portal — Selenium & DevOps MVP

**Student:** Harshad Ahire · **Roll No.:** 23102C0056 · **Project:** Selenium Testing for a Consumer Complaint Portal

A Spring Boot academic MVP for complaint submission, validation, reviewer decisions and status tracking. Includes Selenium tests, Maven, Jenkins, GitHub Actions, Docker, Ansible and Markdown documentation.

## Quick start
Requirements: Java 17+, Maven 3.9+, Chrome/Chromium for Selenium.
```bash
mvn spring-boot:run
```
Open http://localhost:8080. H2 is in-memory, so data resets when the process stops. In a second terminal:
```bash
mvn -Dheadless=true -DbaseUrl=http://localhost:8080 test
```
Package: `mvn -DskipTests clean package`. Docker: `docker build -t consumer-complaint-portal:1.0 . && docker run --rm -p 8080:8080 consumer-complaint-portal:1.0`.

## MVP
Consumer submits name/email/category/subject/description; server validates data and returns a reference; consumer tracks status; reviewer can approve/reject and add a note.

**Security warning:** reviewer actions are unauthenticated for local academic demonstration only. Do not expose publicly. Production requires authentication/authorization, CSRF protection, durable storage, privacy/security review and audit logging.

## Stack and repository map
- Java 17, Spring Boot 3, Spring MVC, Thymeleaf, Spring Data JPA, H2
- Maven, Selenium 4/JUnit 5, Jenkins, GitHub Actions, Docker, Ansible
- `src/main/java` — application/domain/controller/repository
- `src/main/resources/templates` — UI pages
- `src/test/java` — Selenium browser tests
- `Jenkinsfile`, `.github/workflows/ci.yml` — pipeline definitions
- `Dockerfile`, `docker-compose.yml` — container build/run
- `ansible/` — host configuration and deployment playbook

## Markdown documentation index
- [15-task project plan, stakeholders, acceptance criteria, DoD and sprint plan](docs/PROJECT_PLAN.md)
- [SRS, Mermaid use cases, architecture and data model](docs/SRS.md)
- [Selenium test plan and current test status](docs/TEST_PLAN.md)
- [DevOps lifecycle and operations runbook](docs/DEVOPS_WORKFLOW.md)
- [Completion audit with task-by-task gaps](docs/COMPLETION_AUDIT.md)
- [Submission evidence checklist](docs/EVIDENCE_CHECKLIST.md)

## Jenkins
Install JDK 17, Maven, Chrome/Chromium, Docker and Jenkins Pipeline/JUnit plugins. Create a Pipeline job from this repository; the agent needs Docker access. Optional Docker Hub publishing requires Jenkins username/password credential ID `dockerhub` and securely configured `DOCKERHUB_USER`.

## Ansible
Install Ansible and `community.docker`; replace the example IP in `ansible/inventory.ini` with an authorized target and configure SSH/sudo. Review the playbook topology before execution: the current image build runs on the controller while container creation runs on the target, so the image must be made available to the target (for example, through a registry or explicit image transfer).

## Verification status
**Not yet fully verified.** The latest observed GitHub Actions run before the most recent Selenium-test change failed with a WebDriver wait timeout in the complaint submission journey. The latest change triggered a new CI run; check [GitHub Actions](https://github.com/ahireharshad/consumer-complaint-portal-devops/actions) for its result. Do not claim a green pipeline, deployed URL, Docker Hub push, Jenkins execution, Ansible idempotency, PR review, conflict resolution or release tag without actual evidence.

## Limitations
No actual document upload, authentication, durable database, notifications or audit trail. The reviewer page is demo-only; reference ID generation is not production-grade. See [Completion Audit](docs/COMPLETION_AUDIT.md) for remaining tasks.
