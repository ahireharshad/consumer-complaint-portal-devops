# Consumer Complaint Portal — Selenium & DevOps MVP

**Student:** Harshad Ahire · **Roll No.:** 23102C0056 · **Project:** Selenium Testing for a Consumer Complaint Portal

A runnable Spring Boot web application for complaint submission, validation, reviewer decisions and status tracking. Includes Selenium UI tests, Maven, Jenkins, GitHub Actions, Docker, Ansible and project documentation.

## Quick start
Requirements: Java 17+, Maven 3.9+, Chrome/Chromium for Selenium.
```bash
mvn spring-boot:run
```
Open http://localhost:8080. H2 is in-memory for this demo, so data resets when the process stops. In a second terminal, with the application still running:
```bash
mvn -Dheadless=true -DbaseUrl=http://localhost:8080 test
```
To package: `mvn -DskipTests clean package`. Docker: `docker build -t consumer-complaint-portal:1.0 . && docker run --rm -p 8080:8080 consumer-complaint-portal:1.0`.

## MVP workflow
1. Consumer submits name, email, category, subject and description.
2. Server validates required fields and email format.
3. Consumer receives a complaint reference (CCP-xxxxxx) and can track it.
4. Reviewer lists complaints and approves or rejects a pending item with a note.
5. Status transitions: SUBMITTED → APPROVED / REJECTED.

**Security warning:** reviewer actions are intentionally open for a local academic demo. Do not expose this build publicly without authentication/authorization, CSRF protection, privacy controls and production database configuration.

## Technology
Java 17, Spring Boot 3, Spring MVC, Thymeleaf, Spring Data JPA, H2, Maven, Selenium 4/JUnit 5, Jenkins, GitHub Actions, Docker, Ansible.

## Repository guide
- `src/main/java` — application, entity, repository, MVC controller
- `src/main/resources/templates` — consumer/reviewer pages
- `src/test/java` — Selenium browser tests
- `Jenkinsfile`, `.github/workflows/ci.yml` — CI and Selenium pipeline
- `Dockerfile`, `docker-compose.yml` — container build/run
- `ansible/` — host configuration and container deployment
- `docs/` — project plan, SRS, test plan, DevOps lifecycle, evidence checklist

## Jenkins setup
Install JDK 17, Maven, Chrome/Chromium and Docker on the agent; install Jenkins Pipeline and JUnit plugins. Create a Pipeline job pointing to this repository. The agent needs Docker access. For optional Docker Hub publishing, create Jenkins username/password credential ID `dockerhub` and configure `DOCKERHUB_USER`. Never store credentials in source control.

## Ansible
Install Ansible and the collection with `ansible-galaxy collection install community.docker`. Review `ansible/inventory.ini`, replace the example IP with your own authorized Ubuntu/Debian host, and configure SSH/sudo before running `ansible-playbook -i ansible/inventory.ini ansible/deploy.yml`.

## Project documentation
See [Project plan](docs/PROJECT_PLAN.md), [SRS](docs/SRS.md), [Selenium test plan](docs/TEST_PLAN.md), [DevOps workflow](docs/DEVOPS_WORKFLOW.md), and [evidence checklist](docs/EVIDENCE_CHECKLIST.md).

## Limitations
This is an academic MVP, not a production complaint-handling system. It does not implement authentication, actual document upload, durable storage, audit logging or notifications. Build/test/deployment evidence must be captured from your own environment; no successful deployment or CI run is claimed until it actually runs.
