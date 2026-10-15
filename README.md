# Consumer Complaint Portal — Selenium & DevOps MVP

**Student:** Harshad Ahire · **Roll No.:** 23102C0056 · **Project:** Selenium Testing for a Consumer Complaint Portal

A small, runnable Spring Boot web application for submitting complaints, validating inputs, reviewing complaints, approving/rejecting them, and tracking status. The repository includes Selenium UI tests, Maven, Docker, Jenkins pipeline, Ansible provisioning, and project documentation.

## Quick start
Requirements: Java 17+, Maven 3.9+, Chrome/Chromium (for Selenium tests).
```bash
mvn spring-boot:run
```
Open http://localhost:8080. H2 is used for a simple local/demo database; data is in-memory by default. To run tests: `mvn test`. Selenium uses Selenium Manager to resolve the browser driver. To package: `mvn clean package`. Docker: `docker build -t consumer-complaint-portal:1.0 . && docker run --rm -p 8080:8080 consumer-complaint-portal:1.0`.

## MVP workflow
1. Consumer submits name, email, category, subject and description.
2. Server validates required fields and email format.
3. Consumer receives a complaint reference (CCP-xxxxxx) and can track it.
4. Reviewer lists complaints and approves or rejects a pending item with a note.
5. Status transitions: SUBMITTED → APPROVED / REJECTED.

Demo note: reviewer actions are intentionally open for a local academic demo. Do not expose this build to the public internet without authentication/authorization, CSRF hardening, privacy controls and production database configuration.

## Technology
Java 17, Spring Boot 3, Spring MVC, Thymeleaf, Spring Data JPA, H2, Maven, Selenium 4/JUnit 5, Jenkins, Docker, Ansible.

## Repository guide
- `src/main/java` — application, entity, repository, MVC controller
- `src/main/resources/templates` — consumer/reviewer pages
- `src/test/java` — Selenium browser tests
- `Jenkinsfile` — build, test, package, Docker build (registry publish/deploy requires credentials/agent setup)
- `Dockerfile`, `docker-compose.yml` — container run
- `ansible/` — repeatable host prerequisites and Docker deployment
- `docs/` — problem definition, SRS, architecture, backlog, test plan, evidence checklist

## Jenkins setup
Install Jenkins with JDK 17, Maven and Docker available on the agent; install the Pipeline and JUnit plugins. Create a Pipeline job pointing at this repo. Configure Docker access for the Jenkins agent. The pipeline runs Maven tests and builds the image; pushing to Docker Hub requires a credential named `dockerhub` containing username/password and setting `DOCKERHUB_USER`. Do not store credentials in source control.

## Ansible
Review `ansible/inventory.ini` and `ansible/deploy.yml`, configure SSH access and target host, then run `ansible-playbook -i ansible/inventory.ini ansible/deploy.yml`. The playbook installs Docker and starts the image. Target is Ubuntu/Debian with sudo access.

## 15-task MVP plan
See [docs/PROJECT_PLAN.md](docs/PROJECT_PLAN.md), [docs/SRS.md](docs/SRS.md), [docs/TEST_PLAN.md](docs/TEST_PLAN.md), and [docs/DEVOPS_WORKFLOW.md](docs/DEVOPS_WORKFLOW.md).

## Limitations / next steps
This is an academic MVP, not a production complaint-handling system. Add authentication/role-based access, secure document uploads and malware scanning, audit logs, email notifications, persistent managed DB, rate limiting, retention policy and privacy review before real use. Build/test/deployment evidence must be captured from your own environment; no deployment or CI success is claimed until you run it.
