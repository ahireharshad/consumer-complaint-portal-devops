# Project definition, backlog and 15-task plan

## Problem statement
Consumers often cannot tell where to submit a service/product complaint, what information is needed, or whether it has been reviewed. A compact portal should standardize complaint capture, validate essential data, provide a reference and expose the current decision/status.

## Stakeholders
- Consumer/complainant: submits complaint and tracks progress.
- Reviewer/support officer: examines details and records approve/reject plus note.
- Portal administrator/operations: deploys, monitors and maintains the service.
- Project evaluator: reviews requirements, test evidence, Git and DevOps workflow.

## Objectives and measurable acceptance targets
- A valid complaint can be submitted and assigned a reference.
- Invalid email, missing required fields or description under 10 characters is rejected.
- A submitted reference can be tracked and displays current status.
- Reviewer can approve/reject a pending complaint and add a note.
- Selenium smoke suite covers form display, submission/reference, reviewer action and unknown reference.
- CI runs Maven tests and publishes JUnit reports; failed build/test must prevent subsequent stages.
- Container responds on port 8080 and Ansible rerun converges without unnecessary changes.

## Constraints and exclusions
Academic MVP; one application; Java 17/Spring Boot/Maven/H2; local in-memory data resets on restart; no authentication or actual document upload; reviewer page is demo-only. Production use requires access control, privacy/security review, durable DB, audit trail and upload scanning.

## 15-task backlog
| ID | User story / task | Acceptance criteria | Evidence |
|---|---|---|---|
| T01 | Define problem and scope | Problem, stakeholders, constraints, metrics documented | This plan |
| T02 | Agile plan and workflow | Stories, 15 tasks, DoD and lifecycle documented | Plan/workflow |
| T03 | Requirements and architecture | SRS, use cases, data model and local stack documented | SRS |
| T04 | Initialize repository | README, ignore rules, folder layout and branch policy | Git history |
| T05 | Build submission workflow | Valid form saves complaint and reference | App + commit |
| T06 | Complete reviewer/status flow | Track, approve/reject and notes work | Browser demo |
| T07 | Configure CI | Jenkinsfile runs Maven and archives artifacts | Jenkins run |
| T08 | Package/deploy | Docker image runs on port 8080 | Container screenshot |
| T09 | Selenium design/execution | Four UI journeys and assertions available | Surefire report |
| T10 | CI test gate | Maven test stage fails build on failing tests | Failed/success run |
| T11 | Container lifecycle | Build/run/log/stop/remove documented | Docker log |
| T12 | Continuous delivery | Versioned image and optional registry push | Registry screenshot |
| T13 | Configuration management | Ansible playbook configures Docker and container | Playbook output |
| T14 | Provision/reliability | Health check and repeat-run idempotency assessed | Play recap |
| T15 | Release/evidence | Smoke tests, limitations, release tag and evidence index | Release checklist |

## User stories
- As a consumer, I want to submit a complaint with contact details and description so the support team can understand the issue.
- As a consumer, I want a unique reference so I can check progress later.
- As a reviewer, I want to approve or reject a pending complaint and record a note.
- As an operator, I want repeatable builds and container deployment so releases are consistent.

## Definition of Done
- Acceptance criteria verified; input validated server-side.
- Code committed with meaningful message and reviewed before merge.
- Automated tests pass; test report is archived.
- Image is versioned; no credentials committed.
- Deployment health check passes; rollback procedure and limitations documented.
- Screenshots/logs are genuine outputs from the student's environment.

## Branch policy
Use `main` for release-ready code and `feature/<short-name>` for work. Open PRs against main, include acceptance criteria, require tests/review, squash merge, and tag releases `v1.0.0`. Do not fabricate PR/review/conflict evidence; demonstrate these operations in GitHub.
